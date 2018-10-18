package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import static com.surf.MainApp.*;

class TileMap {
    ArrayList<Tile> tiles = new ArrayList<>();
    PVector tileMapSize, scrollValue, scrollMax;
    PApplet p;

    TileMap(int wide, int high, PApplet parent) {
        p = parent;
        this.scrollValue = new PVector(0, 0);
        this.scrollMax = new PVector((wide * TILESIZE) - p.width, p.height - (high * TILESIZE));
        this.tileMapSize = new PVector(wide* TILESIZE, high * TILESIZE);
    }

    void update() {
        trimOutOfBoundsTiles();
        for(Tile i : tiles) {
            i.update();
        }
    }

    void render() {
        for(Tile i : tiles) {
            i.render();
        }
    }

    boolean[] scrollMap(PVector delta) {
        PVector tileMove = new PVector(0, 0);
        if(scrollValue.x + delta.x <= scrollMax.x && scrollValue.x + delta.x >= 0) {
            tileMove.x = -delta.x;
        }
        else if(scrollValue.x < scrollMax.x && delta.x > 0) {
            tileMove.x = scrollValue.x - scrollMax.x;
        }
        else if(scrollValue.x > 0 && delta.x < 0) {
            tileMove.x = scrollValue.x;
        }
        if(scrollValue.y + delta.y >= scrollMax.y && scrollValue.y + delta.y <= 0) {
            tileMove.y = -delta.y;
        }
        else if(scrollValue.y > scrollMax.y && delta.y < 0) {
            tileMove.y = scrollValue.y - scrollMax.y;
        }
        else if(scrollValue.y < 0 && delta.y > 0) {
            tileMove.y = scrollValue.y;
        }
        for(Tile i : tiles) {
            i.pos.add(tileMove);
        }
        scrollValue.sub(tileMove);
        return new boolean[]{(scrollValue.x == 0 || scrollValue.x == scrollMax.x), (scrollValue.y == 0 || scrollValue.y == scrollMax.y)};
    }

    void windowResize() {
        for(Tile i : tiles) {
            i.pos.y += p.height - window.y;
        }
        if(game.scene.player != null) game.scene.player.pos.y += p.height - window.y;
        scrollMax.x += window.x - p.width;
        scrollMax.y += window.y - p.height;
    }

    private void trimOutOfBoundsTiles() {
        for(Tile i : tiles) {
            if(i.pos.y > p.height + TILESIZE && i.collapsed) {
                tiles.remove(i);
                break;
            }
        }
    }

    void loadSprites() {
        for(Tile i : tiles) {
            i.loadSprite();
        }
    }
}