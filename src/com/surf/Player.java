package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import static com.surf.MainApp.*;

class Player {
    private SpriteSheet spritesheet;
    PVector pos;
    private PVector vel;
    private PApplet p;
    private int SIZEX, SIZEY, SIZEX_HALF, SIZEY_HALF, animationState;
    private float GRAVITY, ACCEL, MAXVEL, JUMPVEL, FRICTION, jumpDelay;
    private boolean surfaceLeft, surfaceUp, surfaceRight, surfaceDown = false;
    boolean dead = false;

    Player(int x, int y, PApplet parent) {
        p = parent;
        this.pos = new PVector(x, y);
        this.vel = new PVector(0, 0);
        this.GRAVITY = 2;
        this.ACCEL = 6;
        this.MAXVEL = 8;
        this.JUMPVEL = 28;
        this.FRICTION = 3;

        this.animationState = 1;

        this.jumpDelay = 0;
        loadSprite();

        this.SIZEX = (int)spritesheet.size.x;
        this.SIZEY = (int)spritesheet.size.y;
        this.SIZEX_HALF = SIZEX / 2;
        this.SIZEY_HALF = SIZEY / 2;
    }

    void update() {
        deltaPhysics();
        detectSurfaces();

        addFriction();
        addInputs();
        addGravity();
        addVelocity();

        constrainToTiles();
        constrainToWindow();
    }

    void render() {
        p.imageMode(p.CENTER);
        p.image(spritesheet.sprites[animationState], pos.x, pos.y);
    }

    private void deltaPhysics() {
        this.ACCEL = 6 * deltaTime;
        this.FRICTION = 3 * deltaTime;
        this.MAXVEL = 8 * deltaTime;
        this.GRAVITY = 2 * sq(deltaTime);
        this.JUMPVEL = 1.75F + 26.25F * deltaTime;
    }

    private void detectSurfaces() {
        surfaceLeft = surfaceUp = surfaceRight = surfaceDown = false;
        PVector playerMin = new PVector(pos.x - SIZEX_HALF, pos.y - SIZEY_HALF);
        PVector playerMax = new PVector(pos.x + SIZEX_HALF, pos.y + SIZEY_HALF);
        PVector tileMin, tileMax;
        for(Tile i : game.scene.tilemap.tiles) {
            tileMin = new PVector(i.pos.x - i.SIZE_HALF, i.pos.y - i.SIZE_HALF);
            tileMax = new PVector(i.pos.x + i.SIZE_HALF, i.pos.y + i.SIZE_HALF);
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
                        if(jumpDelay > 0) jumpDelay += -1 * deltaTime;
                    }
                    if(i.type == Tiles.SPIKE) dead = true;
                }
            }
        }
    }

    private void addInputs() {
        if(INPUT_LEFT) {
            animationState = 0;
            if(!surfaceLeft) {
                if(vel.x + -ACCEL >= -MAXVEL) {
                    vel.x += -ACCEL;
                }
                else {
                    vel.x = -MAXVEL;
                }
            }
            else {
                if(!surfaceDown && vel.y > 0) {
                    vel.y = 0;
                    if(jumpDelay > 0) jumpDelay += -0.5 * deltaTime;
                }
            }
        }
        if(INPUT_RIGHT) {
            animationState = 1;
            if(!surfaceRight) {
                if(vel.x + ACCEL <= MAXVEL) {
                    vel.x += ACCEL;
                }
                else {
                    vel.x = MAXVEL;
                }
            }
            else {
                if(!surfaceDown && vel.y > 0) {
                    vel.y = 0;
                    if(jumpDelay > 0) jumpDelay += -0.5 * deltaTime;
                }
            }
        }
        if(INPUT_UP && jumpDelay <= 0 && !surfaceUp) {
            vel.y = -JUMPVEL;
            jumpDelay = 7;
            if(!surfaceDown) {
                if(surfaceLeft && !surfaceRight) {
                    vel.x = JUMPVEL * 0.75F;
                }
                else if(surfaceRight && !surfaceLeft) {
                    vel.x = -JUMPVEL * 0.75F;
                }
            }
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
        PVector moveTileMap = new PVector(0, 0);
        if((pos.x + vel.x < p.width * 0.3 && vel.x < 0 ) || (pos.x + vel.x > p.width * 0.7 && vel.x > 0)) {
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
        boolean down, up, right, left;
        down = up = right = left = false;

        for(Tile i : game.scene.tilemap.tiles) {
            overlap = new PVector(0, 0);
            playerMin = new PVector(pos.x - SIZEX_HALF, pos.y - SIZEY_HALF);
            playerMax = new PVector(pos.x + SIZEX_HALF, pos.y + SIZEY_HALF);
            tileMin = new PVector(i.pos.x - i.SIZE_HALF, i.pos.y - i.SIZE_HALF);
            tileMax = new PVector(i.pos.x + i.SIZE_HALF, i.pos.y + i.SIZE_HALF);

            if(playerMin.y < tileMax.y && playerMax.y > tileMin.y) {
                if(playerMin.x - vel.x >= tileMax.x) {
                    if(playerMin.x < tileMax.x) {
                        left = true;
                        overlap.x = tileMax.x - playerMin.x;
                    }
                }
                if(playerMax.x - vel.x <= tileMin.x) {
                    if(playerMax.x > tileMin.x) {
                        right = true;
                        overlap.x = tileMin.x - playerMax.x;
                    }
                }
            }

            if(playerMin.x < tileMax.x && playerMax.x > tileMin.x) {
                if(playerMin.y - vel.y >= tileMax.y) {
                    if(playerMin.y < tileMax.y) {
                        up = true;
                        overlap.y = tileMax.y - playerMin.y;
                    }
                }
                if(playerMax.y - vel.y <= tileMin.y) {
                    if(playerMax.y > tileMin.y) {
                        down = true;
                        overlap.y = tileMin.y - playerMax.y;
                    }
                }
            }

            if (i.solid) {
                if(overlap.x != 0 && overlap.y != 0) {
                    if(abs(overlap.x) > abs(overlap.y)) {
                        pos.y += overlap.y;
                    }
                    else if (abs(overlap.x) < abs(overlap.y)){
                        pos.x += overlap.x;
                    }
                }
                else {
                    pos.add(overlap);
                }
            }
        }

        if(right || left) {
            vel.x = 0;
        }
        if(up || down) {
            vel.y = 0;
        }
    }

    private void constrainToWindow() {
        pos.x = constrain(pos.x, SIZEX_HALF, p.width - SIZEX_HALF);
        pos.y = constrain(pos.y, -SIZEY_HALF, p.height + SIZEY);

        if(pos.x == SIZEX_HALF && vel.x < 0) vel.x = 0;
        if(pos.x == p.width - SIZEX / 2 && vel.x > 0) vel.x = 0;
        if(pos.y == -SIZEY_HALF && vel.y < 0) vel.y = 0;
        if(pos.y >= p.height + SIZEY) dead = true;
    }

    void loadSprite() {
        this.spritesheet = new SpriteSheet(2, 1, spriteManager.getSprite("e_player_sheet"));
    }
}