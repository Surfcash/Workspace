package com.surf;

import processing.core.PApplet;
import processing.core.PVector;

import static com.surf.MainApp.*;

class Button {
    PApplet parent;
    PVector pos, size;
    String label;
    int textSize;

    Button(String label, int posX, int posY, int bWidth, int bHeight, int textSize, PApplet p) {
        parent = p;
        this.label = label;
        this.pos = new PVector(posX, posY);
        this.size = new PVector(bWidth, bHeight);
        this.textSize = textSize;
    }

    void render() {
        if(mouseHovered()) {
            parent.stroke(0);
            parent.fill(255);
            parent.rectMode(parent.CENTER);
            parent.rect(pos.x, pos.y, size.x, size.y);

            parent.textAlign(parent.CENTER, parent.CENTER);
            parent.textSize(textSize);
            parent.noStroke();
            parent.fill(55);
            parent.text(label, pos.x, pos.y - size.y * 0.08F);
        }

        else {
            parent.stroke(0);
            parent.fill(200);
            parent.rectMode(parent.CENTER);
            parent.rect(pos.x, pos.y, size.x, size.y);

            parent.textAlign(parent.CENTER, parent.CENTER);
            parent.textSize(textSize);
            parent.noStroke();
            parent.fill(0);
            parent.text(label, pos.x, pos.y - size.y * 0.08F);
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
        return parent.mouseX >= pos.x - size.x / 2 && parent.mouseX <= pos.x + size.x / 2 && parent.mouseY >= pos.y - size.y / 2 && parent.mouseY <= pos.y + size.y / 2;
    }
}