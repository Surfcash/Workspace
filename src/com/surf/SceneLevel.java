package com.surf;

import processing.core.PApplet;

import static com.surf.MainApp.*;

class SceneLevel extends Scene {
    SceneLevel(PApplet p) {
        super(p);
        initLevel();
        overlayUI = spriteManager.getSprite("s_overlay");
    }

    void update() {
        checkPlayerDeath();
        tilemap.update();
        player.update();
    }

    void render() {
        p.background(255);
        player.render();
        tilemap.render();
        renderUI();
        renderDeathCounter();
        renderFrameRate();
        renderTitle();
    }

    private void initLevel() {
        switch(game.level) {
            case 1 : {
                label = "1";
                tilemap = LevelManager.levelLoader(label, p);
                player = new Player(p.width / 2, p.height - tileSize * 2, p);
                break;
            }
            case 2 : {
                label = "2";
                tilemap = LevelManager.levelLoader(label, p);
                player = new Player(tileSize, p.height - tileSize * 2, p);
                break;
            }
            default : {
                label = "default";
                tilemap = LevelManager.levelLoader(label, p);
                player = new Player(p.width / 2, p.height - tileSize * 2, p);
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
        p.noStroke();
        p.textFont(game.guiFont, 30);
        p.text("Deaths: " + game.deaths, 30, 30);
    }

    private void renderFrameRate() {
        p.textAlign(RIGHT);
        p.fill(0);
        p.noStroke();
        p.textFont(game.guiFont, 30);
        p.text("FPS: " + (int)p.frameRate, p.width - 30, 30);
    }

    private void renderTitle() {
        p.textAlign(CENTER, CENTER);
        p.fill(0);
        p.noStroke();
        p.textFont(game.labelFont, 100);
        p.text("LEVEL " + label.toUpperCase(), p.width / 2F, 75);
    }

    private void renderUI() {
        if(overlayUI.width != p.width || overlayUI.height != p.height) {
            overlayUI.resize(p.width, p.height);
        }
        p.image(overlayUI, p.width / 2F, p.height / 2F);
    }

    void windowResize() {
        overlayUI.resize(p.width, p.height);
        tilemap.windowResize();
    }
}