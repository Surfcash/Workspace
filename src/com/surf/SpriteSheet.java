package com.surf;

import processing.core.PImage;
import processing.core.PVector;


class SpriteSheet {

    PImage[] sprites;
    PVector size;

    SpriteSheet(int spriteCountX, int spriteCountY, PImage spriteSheet) {
        sprites = new PImage[spriteCountX * spriteCountY];
        int spriteCount = 0;
        size = new PVector(spriteSheet.width / (float)spriteCountX, spriteSheet.height / (float)spriteCountY);
        for(int i = 0; i < spriteCountX; i++) {
            for(int j = 0; j < spriteCountY; j++) {
                sprites[spriteCount] = spriteSheet.get(i * (int)size.x, j * (int)size.y, (int)size.x, (int)size.y);
                spriteCount++;
            }
        }
    }
}
