package com.surf;

import processing.core.PApplet;
import processing.core.PVector;
import processing.opengl.PJOGL;

public class MainApp extends PApplet {

    static PVector window;
    static final int TILESIZE = 32;
    static Game game;
    static SpriteManager spriteManager;
    static boolean MOUSE_LEFT, INPUT_LEFT, INPUT_RIGHT, INPUT_UP, KEY_ESCAPE = false;

    public static void main(String[] args) {
        PApplet.main("com.surf.MainApp");
    }

    public void setup() {
        game = new Game(this);
        spriteManager = new SpriteManager(this);
        surface.setTitle("Colin's Workspace - Platformer");
        surface.setResizable(false);
        window = new PVector(width, height);
    }

    public void settings() {
        size(displayWidth, displayHeight - 70, P2D);
        PJOGL.setIcon("assets/icon.png");
    }

    public void draw() {
        render();
        update();
    }

    private void render() {
        game.render();
    }

    private void update() {
        game.update();
        windowListener();
    }

    private void windowListener() {
        if (window.x != width || window.y != height) {
            game.windowResize();
            window = new PVector(width, height);
        }
    }

    public void keyPressed() {
        if(key == ESC) key = 0;
        switch(keyCode) {
            case 27: {
                KEY_ESCAPE = true;
                break;
            }
            case 32: {
                INPUT_UP = true;
                break;
            }
            case 37: {
                INPUT_LEFT = true;
                break;
            }
            case 38: {
                INPUT_UP = true;
                break;
            }
            case 39: {
                INPUT_RIGHT = true;
                break;
            }
            case 65: {
                INPUT_LEFT = true;
                break;
            }
            case 68: {
                INPUT_RIGHT = true;
                break;
            }
            case 87: {
                INPUT_UP = true;
                break;
            }
        }
    }

    public void keyReleased() {
        switch(keyCode) {
            case 27: {
                KEY_ESCAPE = false;
                break;
            }
            case 32: {
                INPUT_UP = false;
                break;
            }
            case 37: {
                INPUT_LEFT = false;
                break;
            }
            case 38: {
                INPUT_UP = false;
                break;
            }
            case 39: {
                INPUT_RIGHT = false;
                break;
            }
            case 65: {
                INPUT_LEFT = false;
                break;
            }
            case 68: {
                INPUT_RIGHT = false;
                break;
            }
            case 87: {
                INPUT_UP = false;
                break;
            }
        }
    }

    public void mousePressed() {
        switch(mouseButton) {
            case LEFT : {
                MOUSE_LEFT = true;
                break;
            }
        }
    }

    public void mouseReleased() {
        switch(mouseButton) {
            case LEFT : {
                MOUSE_LEFT = false;
                break;
            }
        }
    }
}