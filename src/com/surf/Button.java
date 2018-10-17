package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import static com.surf.MainApp.*;

class Button {
    PApplet p;
    PVector pos, size;
    String label;
    int textSize;

    Button(String label, int posX, int posY, int bWidth, int bHeight, int textSize, PApplet parent) {
        p = parent;
        this.label = label;
        this.pos = new PVector(posX, posY);
        this.size = new PVector(bWidth, bHeight);
        this.textSize = textSize;
    }

    void render() {
        if(mouseHovered()) {
            p.noStroke();
            p.fill(255);
            p.rectMode(p.CENTER);
            p.rect(pos.x, pos.y, size.x, size.y);

            p.textAlign(p.CENTER, p.CENTER);
            p.textSize(textSize);
            p.noStroke();
            p.fill(55);
            p.text(label, pos.x, pos.y - size.y * 0.08F);
        }

        else {
            p.noStroke();
            p.fill(200);
            p.rectMode(p.CENTER);
            p.rect(pos.x, pos.y, size.x, size.y);

            p.textAlign(p.CENTER, p.CENTER);
            p.textSize(textSize);
            p.noStroke();
            p.fill(0);
            p.text(label, pos.x, pos.y - size.y * 0.08F);
        }
    }

    boolean buttonPressed() {
        if(mouseHovered() && MOUSE_LEFT) {
            MOUSE_LEFT = false;
            return true;
        }
        else return false;
    }

    private boolean mouseHovered() {
        return p.mouseX >= pos.x - size.x / 2 && p.mouseX <= pos.x + size.x / 2 && p.mouseY >= pos.y - size.y / 2 && p.mouseY <= pos.y + size.y / 2;
    }
}