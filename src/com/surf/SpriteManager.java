package com.surf;

import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.util.ArrayList;

final class SpriteManager {
    private ArrayList<Sprite> sprites = new ArrayList<>();
    private String texture = "default";
    PApplet p;

    SpriteManager(PApplet parent) {
        p = parent;
        loadSpriteFolders();
    }

    private void loadSpritesFromFolder(String filePath, String prefix) {
        File folder = new File(p.sketchPath(filePath));
        File[] files = folder.listFiles();
        if (files != null) {
            for (File i : files) {
                String fileName = i.getName();
                int pos = fileName.lastIndexOf(".");
                fileName = pos > 0 ? fileName.substring(0, pos) : fileName;
                if (i.isFile()) {
                    sprites.add(new Sprite(p.loadImage(i.getPath()), prefix + fileName));
                }
            }
        }
    }

    private void loadSpriteFolders() {
        sprites.clear();
        loadSpritesFromFolder("assets/textures/" + texture + "/tiles", "t_");
        loadSpritesFromFolder("assets/textures/" + texture + "/particles", "p_");
        loadSpritesFromFolder("assets/textures/" + texture + "/entities", "e_");
        loadSpritesFromFolder("assets/screen", "s_");
        loadSpritesFromFolder("assets/levels", "l_");
    }

    PImage getSprite(String reference) {
        for(Sprite i : sprites) {
            if(i.reference.equals(reference)) {
                return i.img;
            }
        }
        return null;
    }


    void setTexture(String value) {
        this.texture = value;
        loadSpriteFolders();
    }

    private class Sprite {
        PImage img;
        String reference;

        Sprite(PImage sprite, String reference) {
            this.img = sprite;
            this.reference = reference;
        }
    }
}