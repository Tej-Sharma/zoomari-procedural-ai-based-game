package com.teron.zoomari.map.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.teron.zoomari.TextureManager;
import com.teron.zoomari.map.MapManager;

/**
 * Created by tejas on 8/27/2017.
 */
public enum BuildingTile {


    //TODO: get these textures and upload them to the array
    WELL(TextureManager.getBuildingTextures().get("well"), false, "well"),
    WATER(MapManager.getTextures().get("water"), false, "water"),
    MOUNTAIN(MapManager.getTextures().get("mountain"), false, "mountain"),
    BLOCK(TextureManager.getBuildingTextures().get("block"), true, "block"),
    EMPTY(TextureManager.getBuildingTextures().get("empty"), true, "empty");

    private TextureRegion textureRegion;
    private boolean passable;


    private String code;

    //will stay -1 if not set
    private int id;

    public static float TILE_DIMENSION = 48f;



    BuildingTile(TextureRegion textureRegion, boolean passable, String code)
    {
        this.textureRegion = textureRegion;
        this.code = code;
        this.passable = passable;
        this.id = -1;

    }

    public void setId(int id) {
        this.id = id;
    }

    public TextureRegion getTextureRegion()
    {return textureRegion;}

    public boolean isPassable()
    {return passable;}

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }


}
