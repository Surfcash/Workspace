package com.surf;

import processing.core.PApplet;
import processing.core.PImage;

import static com.surf.MainApp.deltaTime;
import static com.surf.MainApp.spriteManager;

class TileCollapsible extends Tile {
    private int collapseDelay = 20;
    private PImage overlay = spriteManager.getSprite("p_collapsible");

    TileCollapsible(int x, int y, Tiles type, PApplet p) {
        super(x, y, type, p);
    }

    void update() {
        if(collapsed){
            if(collapseDelay > 0) collapseDelay -= 1 * deltaTime;
            else {
                vel.y += 2 * deltaTime;
            }
        }
        pos.y += vel.y;
    }

    void render() {
        parent.imageMode(parent.CENTER);
        parent.image(sprite, pos.x, pos.y);
        parent.image(overlay, pos.x, pos.y);
    }
}