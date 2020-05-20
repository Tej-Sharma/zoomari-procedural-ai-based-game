package com.teron.zoomari.sprites.projectiles;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.teron.zoomari.MainClass;

/**
 * Created by tejas on 8/25/2017.
 */
public class Arrow extends Projectile {

    private final int WIDTH = 16, HEIGHT = 8;
    private float arrowMaxTravelDistance = 1.8f;
    private float maxSpeed = 2.4f;


    public Arrow(float x, float y, float rotation, Vector2 movement, World world) {
        super(x, y, rotation, Projectile.ARROW_ID); // has a call to create body
        setBounds(x, y, WIDTH/ MainClass.PPM, HEIGHT/MainClass.PPM);
        createBody(world);

        maxTravelDistance = arrowMaxTravelDistance;

        Vector2 velocity = movement.nor().scl(maxSpeed).rotate(rotation); //Normalize the vector to prevent the bullet from traveling faster depending on the distance of the mouseclick from the player
        body.setLinearVelocity(velocity.x, velocity.y);
        body.setTransform(new Vector2(body.getPosition().x, body.getPosition().y), (float) Math.toRadians(rotation));
    }

    @Override
    public void update() {
        setPosition(body.getPosition().x, body.getPosition().y);
    }

    @Override
    void createBody(World world) {
        BodyDef bdef = new BodyDef();
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.position.set(getX() + getWidth()/2, getY() + getHeight()/2);
        body = world.createBody(bdef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(getWidth()/2, getHeight()/2);
        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        fdef.isSensor = true;
        fixture = body.createFixture(fdef);



    }


}
