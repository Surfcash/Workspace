package com.surf;

import processing.core.PApplet;

class TilePassable extends Tile {
    TilePassable(int x, int y, Tiles type, PApplet p) {
        super(x, y, type, p);
        super.solid = false;
    }
}