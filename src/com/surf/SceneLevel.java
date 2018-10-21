package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import static com.surf.MainApp.*;

class SceneLevel extends Scene {
    SceneLevel(PApplet p) {
        super(p);
        initLevel();
        levelForeground = spriteManager.getSprite("s_levelOne_Foreground");
        levelBackground = spriteManager.getSprite("s_levelOne_Background");
    }

    void update() {
        checkPlayerDeath();
        tilemap.update();
        player.update();
    }

    void render() {
        p.background(255);
        p.imageMode(CORNER);
        p.image(levelBackground, -tilemap.scrollValue.x * 0.5F, p.height - levelBackground.height - tilemap.scrollValue.y * 0.5F);
        player.render();
        tilemap.render();
        p.imageMode(CORNER);
        p.image(levelForeground, -tilemap.scrollValue.x, p.height - levelForeground.height - tilemap.scrollValue.y);
        renderDeathCounter();
        renderFrameRate();
        renderTitle();
    }

    private void initLevel() {
        switch(game.level) {
            case 1 : {
                label = "1";
                tilemap = LevelManager.levelLoader(label, p);
                player = new Player(TILESIZE * 2, p.height - TILESIZE * 5, p);
                break;
            }
            case 2 : {
                label = "2";
                tilemap = LevelManager.levelLoader(label, p);
                player = new Player(TILESIZE * 2, p.height - TILESIZE * 3, p);
                break;
            }
            default : {
                label = "default";
                tilemap = LevelManager.levelLoader(label, p);
                if(tilemap != null) {
                    tilemap.scrollValue.x = tilemap.scrollValue.x / 2;
                    tilemap.scrollMap(new PVector(tilemap.scrollMax.x / 2, 0));
                    player = new Player(p.width / 2, p.height - TILESIZE * 2, p);
                }
            }
        }
    }

    private void checkPlayerDeath() {
        if(player.dead) {
            game.deaths++;
            initLevel();
        }
    }

    private void renderDeathCounter() {
        p.textAlign(LEFT);
        p.fill(0);
        p.textFont(game.labelFont, 60);
        p.text("Deaths: " + game.deaths, 30, 60);
    }

    private void renderFrameRate() {
        p.textAlign(RIGHT);
        p.fill(0);
        p.textFont(game.labelFont, 60);
        p.text("FPS: " + (int)p.frameRate, p.width - 30, 60);
    }

    private void renderTitle() {
        p.textAlign(CENTER, CENTER);
        p.fill(0);
        p.textFont(game.labelFont, 100);
        p.text("LEVEL " + label.toUpperCase(), p.width / 2F, 75);
    }


    void windowResize() {
        tilemap.windowResize();
    }
}