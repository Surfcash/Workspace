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
                    int pixelColor = level.get(j, i);
                    float[] rgbArray = {p.red(pixelColor), p.green(pixelColor), p.blue(pixelColor)};
                    float alphaValue = p.alpha(pixelColor);
                    Tiles type = null;

                    for (Tiles k : Tiles.values()) {
                        if (Arrays.equals(k.colorValue, rgbArray)) {
                            type = k;
                            break;
                        }
                        else type = Tiles.TILE;
                    }

                    if(alphaValue == 200) {
                        map.tiles.add(new TilePassable(j, abs((i - level.height) + 1), type, p));
                    }
                    else if(alphaValue == 125) {
                        map.tiles.add(new TileCollapsible(j, abs((i - level.height) + 1), type, p));
                    }
                    else if(alphaValue != 0) {
                        map.tiles.add(new Tile(j, abs((i - level.height) + 1), type, p));
                    }
                }
            }
            return map;
        }
        else return null;
    }
}
