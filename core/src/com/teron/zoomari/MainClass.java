package com.teron.zoomari;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.teron.zoomari.screens.MenuScreen;

public class MainClass extends Game
{
    // used to draw everything, don't mess with it
    public static SpriteBatch batch;
    // PPM = pixel per meter, EVERYTHING will be scaled by this
    public static final int WIDTH = 1366, HEIGHT = 768;
    public static final float PPM = 100; // will cause rounding errors sometimes if its not float

    @Override
    public void create()
    {
        batch = new SpriteBatch();
        // will immediately set the screen to menu screen
        setScreen(new MenuScreen(this));
    }


    @Override
    public void render()
    {
        super.render();
    }

    @Override
    public void dispose()
    {
        // very IMPORTANT to dispose of everything
        super.dispose();
        batch.dispose();
    }
}
