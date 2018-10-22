package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import static com.surf.MainApp.*;

class Player {
    private SpriteSheet spritesheet;
    PVector pos;
    private PVector vel, tilesMoved;
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
        TileMap tilemap = game.scene.tilemap;
        tilesMoved = new PVector(0, 0);

        if((pos.x + vel.x < p.width * 0.3 && vel.x < 0 ) || (pos.x + vel.x > p.width * 0.7 && vel.x > 0)) {
            tilesMoved.x = tilemap.scrollMap(new PVector(vel.x, 0)).x;
        }
        if((pos.y + vel.y < p.height * 0.3 && vel.y < 0 ) || (pos.y + vel.y > p.height * 0.7 && vel.y > 0)) {
            tilesMoved.y = tilemap.scrollMap(new PVector(0, vel.y)).y;
        }

        if(tilesMoved.x == 0) pos.x += vel.x;
        if(tilesMoved.y == 0) pos.y += vel.y;
    }

    private void constrainToTiles() {
        BoxCollider p, t;
        PVector deltaFix;
        for(Tile i : game.scene.tilemap.tiles) {
            p = new BoxCollider(pos, SIZEX, SIZEY);
            t = new BoxCollider(i.pos, i.SIZE, i.SIZE);
            deltaFix = new PVector(0,0);
            if (((p.right > t.left) && (p.left < t.right) && (p.bottom > t.top) && (p.top < t.bottom)) || (pos.x < t.right && pos.x > t.left && pos.y > t.top && pos.y < t.bottom)) {
                if(tilesMoved.y == 0) {
                    if (p.bottom - vel.y <= t.top) {
                        deltaFix.y = t.top - p.bottom;
                    } else if (p.top - vel.y >= t.bottom) {
                        deltaFix.y = t.bottom - p.top;
                    }
                }
                else {
                    if (p.bottom <= t.top - tilesMoved.y) {
                        deltaFix.y = t.top - p.bottom;
                    } else if (p.top >= t.bottom - tilesMoved.y) {
                        deltaFix.y = t.bottom - p.top;
                    }
                }

                if(tilesMoved.x == 0) {
                    if (p.left - vel.x >= t.right) {
                        deltaFix.x = t.right - p.left;
                    } else if (p.right - vel.x <= t.left) {
                        deltaFix.x = t.left - p.right;
                    }
                }
                else {
                    if (p.left >= t.right - tilesMoved.x) {
                        deltaFix.x = t.right - p.left;
                    } else if (p.right <= t.left - tilesMoved.x) {
                        deltaFix.x = t.left - p.right;
                    }
                }


                if (i.solid) {
                    if(deltaFix.x != 0 && deltaFix.y != 0) {
                        if(abs(deltaFix.x) > abs(deltaFix.y)) {
                            pos.y += deltaFix.y;
                        }
                        else if (abs(deltaFix.x) < abs(deltaFix.y)){
                            pos.x += deltaFix.x;
                        }
                        else {
                            pos.add(deltaFix);
                        }
                    }
                    else {
                        pos.add(deltaFix);
                    }
                }
            }
        }
    }

    private void detectSurfaces() {
        surfaceLeft = surfaceUp = surfaceRight = surfaceDown = false;
        BoxCollider p, t;

        for(Tile i : game.scene.tilemap.tiles) {
            p = new BoxCollider(pos, SIZEX, SIZEY);
            t = new BoxCollider(i.pos, i.SIZE, i.SIZE);

            if(p.top < t.bottom && p.bottom > t.top) {
                if (p.left == t.right) {
                    if (i.solid) {
                        surfaceLeft = true;
                        if(vel.x < 0) {
                            vel.x = 0;
                        }
                    }
                }
                if (p.right == t.left) {
                    if (i.solid) {
                        surfaceRight = true;
                        if(vel.x > 0) {
                            vel.x = 0;
                        }
                    }
                }
            }
            if(p.left < t.right && p.right > t.left) {
                if (p.top == t.bottom) {
                    if (i.solid) {
                        surfaceUp = true;
                        if(vel.y < 0) {
                            vel.y = 0;
                        }
                    }
                }
                if (p.bottom == t.top) {
                    if (i.solid) {
                        surfaceDown = true;
                        if(vel.y > 0) {
                            vel.y = 0;
                        }
                        if(jumpDelay > 0) {
                            jumpDelay += -1 * deltaTime;
                        }
                    }
                }
            }
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