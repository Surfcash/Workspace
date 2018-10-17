package com.surf;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static com.surf.MainApp.*;

class Player {
    private PImage sprite;
    PVector pos;
    private PVector vel;
    private PApplet p;
    private int SIZEX, SIZEY, SIZEX_HALF, SIZEY_HALF, jumpDelay;
    private float GRAVITY, ACCEL, MAXVEL, JUMPVEL, FRICTION;
    private boolean surfaceLeft, surfaceUp, surfaceRight, surfaceDown = false;
    boolean dead = false;

    Player(int x, int y, PApplet parent) {
        p = parent;
        this.pos = new PVector(x, y);
        this.vel = new PVector(0, 0);
        this.SIZEX = TILESIZE;
        this.SIZEY = TILESIZE;
        this.SIZEX_HALF = SIZEX / 2;
        this.SIZEY_HALF = SIZEY / 2;
        this.GRAVITY = 2;
        this.ACCEL = 4;
        this.MAXVEL = 6;
        this.JUMPVEL = 25;
        this.FRICTION = 2;
        this.sprite = spriteManager.getSprite("e_player");

        this.jumpDelay = 0;
        loadSprite();
    }

    void update() {
        detectSurfaces();

        addInputs();
        addFriction();
        addGravity();
        addVelocity();

        constrainToTiles();
        constrainToWindow();
    }

    void render() {
        p.imageMode(p.CENTER);
        p.image(sprite, pos.x, pos.y);
    }

    private void detectSurfaces() {
        surfaceLeft = surfaceUp = surfaceRight = surfaceDown = false;
        PVector playerMin = new PVector(pos.x - SIZEX_HALF, pos.y - SIZEY_HALF);
        PVector playerMax = new PVector(pos.x + SIZEX_HALF, pos.y + SIZEY_HALF);
        PVector tileMin, tileMax;
        for(Tile i : game.scene.tilemap.tiles) {
            tileMin = new PVector(i.pos.x - i.SIZE / 2F, i.pos.y - i.SIZE / 2F);
            tileMax = new PVector(i.pos.x + i.SIZE / 2F, i.pos.y + i.SIZE / 2F);
            if(playerMin.y < tileMax.y && playerMax.y > tileMin.y) {
                if(playerMin.x == tileMax.x) {
                    if(i.solid) {
                        surfaceLeft = true;
                        if(vel.x < 0) vel.x = 0;
                    }

                }
                else if(playerMax.x == tileMin.x) {
                    if(i.solid) {
                        surfaceRight = true;
                        if(vel.x > 0) vel.x = 0;
                    }
                }
            }
            if(playerMin.x < tileMax.x && playerMax.x > tileMin.x) {
                if(playerMin.y == tileMax.y) {
                    if(i.solid) {
                        surfaceUp = true;
                        if(vel.y < 0) vel.y = 0;
                    }
                }
                else if(playerMax.y == tileMin.y) {
                    if(i.solid) {
                        surfaceDown = true;
                        if(vel.y > 0) vel.y = 0;
                        if(i.type == Tiles.FINISH) {
                            game.initSceneVictory();
                            break;
                        }
                        if(i instanceof TileCollapsible) i.collapsed = true;
                        if(jumpDelay > 0) jumpDelay--;
                    }
                    if(i.type == Tiles.SPIKE) dead = true;
                }
            }
        }
    }

    private void addInputs() {
        if(INPUT_LEFT && !surfaceLeft) {
            vel.x += -ACCEL;
        }
        if(INPUT_RIGHT && !surfaceRight) {
            vel.x += ACCEL;
        }
        if(INPUT_UP && surfaceDown && jumpDelay <= 0 && !surfaceUp) {
            vel.y = -JUMPVEL;
            jumpDelay = 7;
        }
        if(KEY_ESCAPE) game.initScenePaused();
    }

    private void addFriction() {
        if(surfaceDown) {
            if(vel.x >= FRICTION) vel.x += -FRICTION;
            else if (vel.x <= -FRICTION) vel.x += FRICTION;
            if(vel.x > -FRICTION && vel.x < FRICTION) vel.x = 0;
        }
    }

    private void addGravity() {
        if(!surfaceDown) {
            vel.y += GRAVITY;
        }
    }

    private void addVelocity() {
        vel.x = constrain(vel.x, -MAXVEL, MAXVEL);
        PVector moveTileMap = new PVector(0, 0);
        if((pos.x + vel.x < p.width * 0.25 && vel.x < 0 ) || (pos.x + vel.x > p.width * 0.75 && vel.x > 0)) {
            moveTileMap.x = vel.x;
            if(game.scene.tilemap.scrollMap(new PVector(vel.x, 0))[0]) pos.x += vel.x;
        }
        else pos.x += vel.x;
        if((pos.y + vel.y < p.height * 0.3 && vel.y < 0 ) || (pos.y + vel.y > p.height * 0.7 && vel.y > 0)) {
            if(game.scene.tilemap.scrollMap(new PVector(0, vel.y))[1]) pos.y += vel.y;
        }
        else pos.y += vel.y;
    }

    private void constrainToTiles() {
        PVector playerMin, playerMax, tileMin, tileMax, overlap;
        for(Tile i : game.scene.tilemap.tiles) {
            playerMin = new PVector(pos.x - SIZEX_HALF, pos.y - SIZEY_HALF);
            playerMax = new PVector(pos.x + SIZEX_HALF, pos.y + SIZEY_HALF);
            overlap = new PVector(0, 0);
            tileMin = new PVector(i.pos.x - i.SIZE / 2F, i.pos.y - i.SIZE / 2F);
            tileMax = new PVector(i.pos.x + i.SIZE / 2F, i.pos.y + i.SIZE / 2F);
            if(playerMin.y < tileMax.y && playerMax.y > tileMin.y) {
                if(playerMin.x < tileMax.x && playerMin.x - vel.x >= tileMax.x) {
                    overlap.x = tileMax.x - playerMin.x;
                }
                else if(playerMax.x > tileMin.x && playerMax.x - vel.x <= tileMin.x) {
                    overlap.x = tileMin.x - playerMax.x;
                }
            }
            if(playerMin.x < tileMax.x && playerMax.x > tileMin.x) {
                if(playerMin.y < tileMax.y && playerMin.y - vel.y >= tileMax.y) {
                    overlap.y = tileMax.y - playerMin.y;
                }
                else if(playerMax.y > tileMin.y && playerMax.y - vel.y <= tileMin.y) {
                    overlap.y = tileMin.y - playerMax.y;
                }
            }
            if(i.solid) {
                if(overlap.x != 0 && overlap.y != 0) {
                    if(abs(overlap.x) > abs(overlap.y)) pos.y += overlap.y;
                    else if(abs(overlap.x) < abs(overlap.y)) pos.x += overlap.x;
                }
                else pos.add(overlap);
            }
            if(i.type == Tiles.SPIKE) {
                if(overlap.x != 0 || overlap.y != 0) {
                    dead = true;
                }
            }
        }
    }

    private void constrainToWindow() {
        pos.x = constrain(pos.x, SIZEX_HALF, p.width - SIZEX_HALF);
        pos.y = constrain(pos.y, -SIZEY_HALF, p.height + SIZEY_HALF);

        if(pos.x == SIZEX_HALF && vel.x < 0) vel.x = 0;
        if(pos.x == p.width - SIZEX / 2 && vel.x > 0) vel.x = 0;
        if(pos.y == -SIZEY_HALF && vel.y < 0) vel.y = 0;
        if(pos.y >= p.height + SIZEY_HALF) dead = true;
    }

    void loadSprite() {
        this.sprite = spriteManager.getSprite("e_player");
    }
}