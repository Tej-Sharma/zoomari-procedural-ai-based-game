package com.teron.zoomari.sprites.projectiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by tejas on 8/25/2017.
 */
public abstract class Projectile extends Sprite {

    static int ARROW_ID = 0;
    int id;
    protected Body body;
    protected Fixture fixture;

    protected float initialX, initialY, maxTravelDistance; // to know when to delete it


    public Projectile(float x, float y, float rotation, int id) {
        setPosition(x, y);
        setRotation(rotation);
        initialX = x; initialY = y;
        this.id = id;
    }

    abstract void createBody(World world);

    public abstract void update();

    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public int getId() {
        return id;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return fixture;
    }

    public float getMaxTravelDistance() {
        return maxTravelDistance;
    }


    public void dispose(World world) {
        //texture will be stored in TextureManager
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
