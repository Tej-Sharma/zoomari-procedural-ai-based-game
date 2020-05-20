package com.teron.zoomari;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

public class WorldManager
{
    private World world;
    Box2DDebugRenderer debugRenderer;
    private RayHandler rayHandler;
    private Array<PointLight> lights;

    public WorldManager()
    {
        Box2D.init();
        
        world = new World(new Vector2(0, 0), true);

        debugRenderer = new Box2DDebugRenderer();
        
        rayHandler = new RayHandler(world);
        rayHandler.setAmbientLight(0.3f);
        RayHandler.useDiffuseLight(true);
        
        lights = new Array<PointLight>();
    }


    public void addLight(int rayNumber, Color color, float width, float x,
            float y)
    {
        // Add a static light
        PointLight newLight = new PointLight(rayHandler, rayNumber, color,
                width, x, y);
        newLight.setXray(true);
        lights.add(newLight);
        System.out.println(lights.size);

    }

    public void addLight(int rayNumber, Color color, float width, float x,
            float y, Body body)
    {
        // Add a light that follows the body
        PointLight newLight = new PointLight(rayHandler, rayNumber, color,
                width, x, y);
        newLight.setSoftnessLength(0f); // Prevents light from bleeding in
        newLight.attachToBody(body); // Make the light follow the body
        newLight.setXray(true);
        lights.add(newLight);
    }

    public void dispose()
    {
        world.dispose();
        debugRenderer.dispose();
        rayHandler.dispose();
        for (Light light: lights)
            light.dispose();
    }

    public World getWorld()
        {return world;}
    
    public Box2DDebugRenderer getDebugRenderer()
        {return debugRenderer;}
    
    public RayHandler getRayHandler()
        {return rayHandler;}



}
