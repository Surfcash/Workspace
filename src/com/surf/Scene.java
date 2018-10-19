package com.surf;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

import static com.surf.MainApp.*;

class Scene {
    String label;
    PApplet p;
    Player player;
    TileMap tilemap;
    ArrayList<Button> buttons = new ArrayList<>();
    PImage levelForeground, levelBackground;

    Scene(PApplet parent) {
        p = parent;
        tilemap = new TileMap(0, 0, p);
    }

    void update() {
        checkButtonPress();
    }

     private void checkButtonPress() {
        ButtonLoop: for(Button i : buttons) {
            if(i.buttonPressed()) {
                switch(i.label) {
                    case "START" : {
                        game.level = 1;
                        game.initSceneLevel();
                        break ButtonLoop;
                    }
                    case "CONTINUE" : {
                        game.scene = game.tempHold;
                        break ButtonLoop;
                    }
                    case "OPTIONS" : {
                        game.initSceneOptions();
                        break ButtonLoop;
                    }
                    case "TEXTURES" : {
                        game.initSceneTextures();
                        break ButtonLoop;
                    }
                    case "DEFAULT" : {
                        game.scene.label = "DEFAULT";
                        spriteManager.setTexture("default");
                        loadSprite();
                        break ButtonLoop;
                    }
                    case "MINECRAFT" : {
                        game.scene.label = "MINECRAFT";
                        spriteManager.setTexture("minecraft");
                        loadSprite();
                        break ButtonLoop;
                    }
                    case "RESTART" : {
                        game.initNewGame();
                        break ButtonLoop;
                    }
                    case "RETURN" : {
                        game.scene = game.tempHold;
                        break ButtonLoop;
                    }
                    case "BACK TO TITLE" : {
                        game.initSceneTitle();
                        break ButtonLoop;
                    }
                    case "NEXT LEVEL" : {
                        game.initSceneLevel();
                        break ButtonLoop;
                    }
                    case "EXIT" : {
                        p.exit();
                    }
                }
            }
        }
    }

    void windowResize() {
        for(Button i : buttons) {
            i.pos.x += (p.width - window.x) / 2;
            i.pos.y += (p.height - window.y) / 2;
        }
        if(tilemap != null) tilemap.windowResize();
    }

    void render() {
        p.background(128,128,256);
    }

    void renderButtons() {
        for(Button i : buttons) {
            i.render();
        }
    }

    private void loadSprite() {
        if(game.tempHold.tilemap != null) game.tempHold.tilemap.loadSprites();
        if(game.tempHold.player != null) game.tempHold.player.loadSprite();
    }
}