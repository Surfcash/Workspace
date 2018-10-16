package com.surf;

import processing.core.PApplet;
import processing.core.PFont;

import static com.surf.MainApp.*;

class Game {
    PApplet p;
    PFont labelFont, guiFont;
    Scene scene, tempHold;
    short level;
    int deaths;

    Game(PApplet parent) {
        p = parent;
        labelFont = parent.createFont("assets/fonts/Pianaforma.ttf", 150);
        guiFont = parent.createFont("assets/fonts/Pianaforma.ttf", 24);
        parent.textFont(labelFont);
        scene = new Scene(parent);
        tempHold = new Scene(parent);
        initNewGame();
    }

    void update() {
        scene.update();
    }

    void render() {
        scene.render();
    }

    void initNewGame() {
        deaths = 0;
        level = 0;
        initSceneTitle();
    }

    void initSceneTitle() {
        scene.buttons.clear();
        scene = new SceneMenu("WORKSPACE", p);
        if(level == 0) {
            scene.buttons.add(new Button("START", p.width / 2, p.height / 2, 250, 70, 60, p));
            scene.buttons.add(new Button("OPTIONS", p.width / 2, p.height / 2 + 80, 250, 70, 60, p));
            scene.buttons.add(new Button("EXIT", p.width / 2, p.height / 2 + 160, 250, 70, 60, p));
        }
        else {
            scene.buttons.add(new Button("CONTINUE", p.width / 2, p.height / 2, 250, 70, 60, p));
            scene.buttons.add(new Button("RESTART", p.width / 2, p.height / 2 + 80, 250, 70, 60, p));
            scene.buttons.add(new Button("OPTIONS", p.width / 2, p.height / 2 + 160, 250, 70, 60, p));
            scene.buttons.add(new Button("EXIT", p.width / 2, p.height / 2 + 240, 250, 70, 60, p));
        }
    }

    void initSceneLevel() {
        scene.buttons.clear();
        scene = new SceneLevel(p);
    }

    void initSceneOptions() {
        scene.buttons.clear();
        scene = new SceneMenu("OPTIONS", p);
        scene.buttons.add(new Button("TEXTURES", p.width / 2 - 200, p.height /2, 250, 70, 60, p));
        scene.buttons.add(new Button("BACK TO TITLE", p.width / 2, p.height /2 + 240, 350, 70, 60, p));
    }

    void initSceneTextures() {
        scene.buttons.clear();
        scene = new SceneMenu("TEXTURES", p);
        scene.buttons.add(new Button("DEFAULT", p.width / 2 - 200, p.height /2, 350, 70, 60, p));
        scene.buttons.add(new Button("MINECRAFT", p.width / 2 + 200, p.height /2, 350, 70, 60, p));
        scene.buttons.add(new Button("BACK TO TITLE", p.width / 2, p.height /2 + 240, 350, 70, 60, p));
    }

    void initScenePaused() {
        KEY_ESCAPE = false;
        tempHold = scene;
        scene.buttons.clear();
        game.scene = new SceneMenu("PAUSED", p);
        scene.buttons.add(new Button("RETURN", p.width / 2, p.height / 2, 350, 50, 45, p));
        scene.buttons.add(new Button("BACK TO TITLE", p.width / 2, p.height / 2 + 60, 350, 50, 45, p));
        scene.buttons.add(new Button("EXIT", p.width / 2, p.height / 2 + 120, 350, 50, 45, p));
    }

    void initSceneVictory() {
        level++;
        scene.buttons.clear();
        tempHold = new SceneLevel(p);
        scene = new SceneMenu("VICTORY!", p);
        scene.buttons.add(new Button("NEXT LEVEL", p.width / 2, p.height / 2, 350, 50, 45, p));
        scene.buttons.add(new Button("BACK TO TITLE", p.width / 2, p.height / 2 + 60, 350, 50, 45, p));
        scene.buttons.add(new Button("EXIT", p.width / 2, p.height / 2 + 120, 350, 50, 45, p));
    }

    void windowResize() {
        scene.windowResize();
    }
}