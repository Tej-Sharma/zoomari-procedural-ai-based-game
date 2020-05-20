package com.teron.zoomari;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

/**
 * Created by tejas on 8/27/2017.
 */
public class TextureManager {

    private HashMap<String, TextureRegion> projectileTextures;
    private HashMap<String, TextureRegion> playerTextures;
    private static HashMap<String, TextureRegion> buildingTextures;

    public TextureManager() {
        projectileTextures = new HashMap<String, TextureRegion>();
        playerTextures = new HashMap<String, TextureRegion>();
        buildingTextures = new HashMap<String, TextureRegion>();

        Texture tileSheet = new Texture("play_screen/tilesheet.png");

        buildingTextures.put("block", new TextureRegion(tileSheet, 32, 48, 16, 16));
        buildingTextures.put("well", new TextureRegion(tileSheet, 112, 16, 16, 16));
        buildingTextures.put("empty", new TextureRegion(tileSheet, 112, 32, 16, 16));

        projectileTextures.put("arrow", new TextureRegion(new Texture("play_screen/arrow.png")));

    }

    public void dispose() {
        //TODO dispose resources

    }

    public HashMap<String, TextureRegion> getProjectileTextures() {
        return projectileTextures;
    }

    public static HashMap<String, TextureRegion> getBuildingTextures() {
        return buildingTextures;
    }

    public HashMap<String, TextureRegion> getPlayerTextures() {
        return playerTextures;
    }
}
