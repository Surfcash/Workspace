package com.surf;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static com.surf.MainApp.*;

enum Tiles {
    TILE("tile", new float[]{0, 0, 0}),
    BRICK("brick", new float[]{125, 125, 125}),
    SPIKE("spike", new float[]{225, 225, 225}),
    INVISIBLE("invisible", new float[]{50, 50, 50}),
    FINISH("finish", new float[]{250, 250, 250});

    String name;
    float[] colorValue;

    Tiles(String name, float[] colorValue) {
        this.name = name;
        this.colorValue = colorValue;
    }
}

class Tile {
    PVector pos;
    PVector vel = new PVector(0, 0);
    PApplet parent;
    int SIZE = TILESIZE;
    int SIZE_HALF = SIZE / 2;
    Tiles type;
    PImage sprite;
    boolean solid = true;
    boolean collapsed = false;

    Tile(int x, int y, Tiles type, PApplet p) {
        parent = p;
        this.pos = new PVector(x * SIZE + SIZE_HALF, parent.height - SIZE_HALF - y * SIZE);
        this.type = type;
        loadSprite();
    }

    void update() {
    }

    void render() {
        parent.imageMode(parent.CENTER);
        parent.image(sprite, pos.x, pos.y);
    }

    void loadSprite() {
        this.sprite = spriteManager.getSprite("t_" + type.name);
    }
}
