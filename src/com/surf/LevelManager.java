package com.surf;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;

import static com.surf.MainApp.*;

abstract class LevelManager {
    static TileMap levelLoader(String reference, PApplet p) {
        TileMap map;
        PImage level = spriteManager.getSprite("l_" + reference);
        if(level != null) {
            map = new TileMap(level.width, level.height, p);
        }
        else map = new TileMap(0, 0, p);
        if(level != null) {
            for (int i = level.height; i >= 0; i--) {
                for (int j = 0; j < level.width; j++) {
                    int tileValue = level.get(j, i);
                    int[] colorValue = {(int)p.red(tileValue), (int)p.green(tileValue), (int)p.blue(tileValue)};
                    Tiles type = Tiles.TILE;

                    for (Tiles k : Tiles.values()) {
                        if (Arrays.equals(k.colorValue, colorValue)) {
                            type = k;
                        }
                    }
                    switch ((int)p.alpha(tileValue)) {
                        case 0: {
                            break;
                        }
                        case 125: {
                            map.tiles.add(new TileCollapsible(j, abs((i - level.height) + 1), type, p));
                            break;
                        }
                        case 200: {
                            map.tiles.add(new TilePassable(j, abs((i - level.height) + 1), type, p));
                            break;
                        }
                        default: {
                            map.tiles.add(new Tile(j, abs((i - level.height) + 1), type, p));
                            break;
                        }
                    }
                }
            }
            return map;
        }
        else return null;
    }
}
