package com.teron.zoomari.sprites.ai;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by tejas on 9/11/2017.
 */
public abstract class NPC extends Sprite {

    public NPC(float x, float y, float width, float height) {
        setBounds(x, y, width, height);
    }
}
