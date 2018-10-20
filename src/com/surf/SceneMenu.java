package com.surf;

import processing.core.PApplet;

class SceneMenu extends Scene {

    SceneMenu(String label, PApplet parent) {
        super(parent);
        this.label = label;
    }

    void render() {
        p.background(0);
        renderTitle();
        renderCredit();
        renderButtons();
    }

    private void renderTitle() {
        p.textSize(150);
        p.fill(255);
        p.textAlign(p.CENTER, p.CENTER);
        p.text(label, p.width / 2F, p.height * 0.25F);
    }

    private void renderCredit() {
        p.textSize(36);
        p.fill(255);
        p.textAlign(p.CENTER, p.CENTER);
        p.text("Programming by Colin\nArt by Zac", p.width / 2F, p.height - 150);
    }
}