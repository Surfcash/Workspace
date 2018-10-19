package com.surf;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

import static com.surf.MainApp.*;

enum Tiles {
    TILE("tile", new int[]{0, 0, 0}),
    BRICK("brick", new int[]{125, 125, 125}),
    GRASS("grass", new int[]{150, 150, 150}),
    DIRT("dirt", new int[]{175, 175, 175}),
    STONE("stone", new int[]{200, 200, 200}),
    SPIKE("spike", new int[]{225, 225, 225}),
    INVISIBLE("invisible", new int[]{50, 50, 50}),
    FINISH("finish", new int[]{250, 250, 250});

    String name;
    int[] colorValue;

    Tiles(String name, int[] colorValue) {
        this.name = name;
        this.colorValue = colorValue;
    }
}

class Tile {
    PVector pos;
    PVector vel = new PVector(0, 0);
    PApplet parent;
    int SIZE = TILESIZE;
    private int SIZE_HALF = SIZE / 2;
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
