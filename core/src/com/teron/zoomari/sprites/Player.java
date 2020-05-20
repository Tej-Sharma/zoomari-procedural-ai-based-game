package com.teron.zoomari.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.teron.zoomari.MainClass;
import com.teron.zoomari.map.Map;
import com.teron.zoomari.map.TerrainTile;
import com.teron.zoomari.sprites.projectiles.Arrow;
import com.teron.zoomari.sprites.projectiles.Projectile;

/**
 * Created by tejas on 8/3/2017.
 */
public class Player extends Sprite {




    private Body body;
    private Fixture fixture;
    //TODO: have to adjust this speed so while moving the player cant outspeed the projectiles, at 4f for testing purposes
    private final float MAX_SPEED = 10f;
    private float bodyRadius;
    float xMove = 0, yMove = 0;
    private float hp, mana, stamina;
    private double gunOffsetX, gunOffsetY;


    public boolean canSwim = false;
    public int viewRadius = 200;

    private Array<Projectile> projectiles;

    public Player(float x, float y, float width, float height, Texture texture, World world) {
        setTexture(texture);
        setBounds(x, y, width, height);
        bodyRadius = getWidth()/2;
        gunOffsetX = 0f; gunOffsetY = 0f;
        hp = 100f; mana = 100f; stamina = 100f;

        projectiles = new Array<Projectile>();

        createBody(world);
    }

    private void createBody(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + getWidth()/2, getY() + getHeight()/2);
        body = world.createBody(bodyDef);
        CircleShape shape = new CircleShape();
        shape.setRadius(bodyRadius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0f;
        fixture = body.createFixture(fixtureDef);
    }

    public void update(float delta, Camera camera, Map map, World world) {

        xMove = 0; yMove = 0;

        getInput(camera, world);
        move(delta, map);



        for(int i = 0; i < projectiles.size; i++) {
            Projectile projectile = projectiles.get(i);
            projectile.update();
            float deltaX = projectile.getX() - projectile.getInitialX();
            float deltaY = projectile.getY() - projectile.getInitialY();
            float distance = (float) Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));
            if(distance > projectile.getMaxTravelDistance()) {
                projectile.dispose(world);
                projectiles.removeIndex(i);
            }
        }

//        //TODO: rotation logic just gotta change some stuffz
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)); // VERY IMPORTANT!
        float angle = (float) Math.atan2(mousePos.y - (body.getPosition().y), mousePos.x - (body.getPosition().x));
        angle = (float) Math.toDegrees(angle);
        if(angle < 0)
            angle = 360 - (-angle);

        setRotation(angle);

    }

    private void getInput(Camera camera, World world) {
        if(Gdx.input.isKeyPressed(Input.Keys.W))
            yMove = MAX_SPEED;
        if(Gdx.input.isKeyPressed(Input.Keys.S))
            yMove = -MAX_SPEED;
        if(Gdx.input.isKeyPressed(Input.Keys.D))
            xMove = MAX_SPEED;
        if(Gdx.input.isKeyPressed(Input.Keys.A))
            xMove = -MAX_SPEED;
        if(Gdx.input.justTouched()) {
            //shoot
            double angle = Math.toRadians(getRotation());

            float bulletX = (float) (body.getPosition().x + (gunOffsetX * Math.cos(angle) - gunOffsetY * Math.sin(angle)));
            float bulletY = (float) (body.getPosition().y + (gunOffsetX * Math.sin(angle) + gunOffsetY * Math.cos(angle)));

            projectiles.add(new Arrow(bulletX, bulletY, getRotation(), new Vector2(1, 0), world));

        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            if(canSwim) canSwim = false;
            else canSwim = true;
        }
    }
    private void move(float delta, Map map) {


        //moveX()
        if(xMove > 0) { // moving right
            //get position of the tile
            //if want a custom collision box, can create an offset variable for getX() and getY()
            float tx =  getX() + (xMove/MainClass.PPM) + getWidth();
            float ty = getY();
            if(!collidesWithTile(tx, ty, map) && !collidesWithTile(tx, ty + getHeight(), map)) { //will prevent the player from moving if it will collide with terrain
                translateX(xMove/MainClass.PPM);
            }
        } else if(xMove < 0) { //moving left
            float tx =  getX() + (xMove/MainClass.PPM);
            float ty = getY();
            if(!collidesWithTile(tx, ty, map) && !collidesWithTile(tx, ty + getHeight(), map)) {
                translateX(xMove/MainClass.PPM);
            }
        }

        if(yMove > 0) {
            float tx = getX();
            float ty = getY() + (yMove/MainClass.PPM) + getHeight();
            if(!collidesWithTile(tx, ty, map) && !collidesWithTile(tx + getWidth(), ty, map)) {
                translateY(yMove/MainClass.PPM);
            }
        } else if(yMove < 0) {
            float tx = getX();
            float ty = getY() + (yMove/MainClass.PPM);
            if(!collidesWithTile(tx, ty, map) && !collidesWithTile(tx + getWidth(), ty, map)) {
                translateY(yMove/MainClass.PPM);
            }
        }

        body.setTransform(getX() + bodyRadius, getY() + bodyRadius, 0);



    }


    private boolean collidesWithTile(float x, float y, Map map) {
        TerrainTile terrainTile = map.getTile(x, y);
        if(terrainTile.getCode().equals("water") && canSwim)
            return false;
        return !terrainTile.isPassable();
    }

    public void dispose() {
        getTexture().dispose();

    }

    public Body getBody() {
        return body;
    }

    public float getBodyRadius() {
        return bodyRadius;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getMana() {
        return mana;
    }

    public void setMana(float mana) {
        this.mana = mana;
    }

    public float getStamina() {
        return stamina;
    }

    public void setStamina(float stamina) {
        this.stamina = stamina;
    }

    public boolean canSwim() {
        return canSwim;
    }

    public Array<Projectile> getProjectiles() {
        return projectiles;
    }
}
