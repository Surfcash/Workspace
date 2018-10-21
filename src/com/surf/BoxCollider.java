package com.surf;

import processing.core.PVector;

class BoxCollider {
    float left;
    float right;
    float top;
    float bottom;

    BoxCollider(PVector pos, float width, float height) {
        left = pos.x - width / 2f;
        right = pos.x + width / 2f;
        top = pos.y - height / 2f;
        bottom = pos.y + height / 2f;
    }
}
