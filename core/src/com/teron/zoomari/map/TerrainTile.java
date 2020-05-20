package com.teron.zoomari.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum TerrainTile
{
    //TODO: play around with treeDensity

    WATER(MapManager.getTextures().get("water"), false, "water", 0 ,0),
    GRASS(MapManager.getTextures().get("grass"), true, "grass", 5, 1),
    SAND(MapManager.getTextures().get("sand"),true, "sand", 0, 2),
    SNOW(MapManager.getTextures().get("snow"),true, "snow", 0, 2),
    MOUNTAIN(MapManager.getTextures().get("mountain"), false, "mountain", 0, 0),
    FOREST(MapManager.getTextures().get("forest"), true, "forest", 6, 0),
    SCORCHED(MapManager.getTextures().get("scorched"), true, "scorched", 0, 4),
    BARE(MapManager.getTextures().get("bare"), true, "bare", 0, 1),
    TUNDRA(MapManager.getTextures().get("tundra"), true, "tundra", 4, 0),
    SHRUB_DESERT(MapManager.getTextures().get("shrubDesert"), true, "shrubDesert", 0, 7),
    SHRUB_LAND(MapManager.getTextures().get("shrubLand"), true, "shrubLand", 0, 7),
    DECIDUOUS_FOREST(MapManager.getTextures().get("deciduousForest"), true, "deciduousForest", 7, 0),
    TREE_DESERT(MapManager.getTextures().get("treeDesert"), true, "treeDesert", 5, 0),
    ERROR(MapManager.getTextures().get("ERROR"), true, "ERROR", 0, 0); // tile that appears when an invalid noise value is encountered

    private TextureRegion textureRegion;
    private boolean passable;


    private String code;

    public static float TILE_DIMENSION = 64f;

    //densities on a scale of 0 - 10, 10 being highest
    private int treeDensity;
    private int shrubDensity;


    TerrainTile(TextureRegion textureRegion, boolean passable, String code, int treeDensity, int shrubDensity)
    {
        this.textureRegion = textureRegion;
        this.code = code;
        this.passable = passable;
        this.treeDensity = treeDensity;
        this.shrubDensity = shrubDensity;
    }


    public TextureRegion getTextureRegion()
        {return textureRegion;}
    
    public boolean isPassable()
        {return passable;}

    public String getCode() {
        return code;
    }

    public int getTreeDensity() {
        return treeDensity;
    }

    public int getShrubDensity() {
        return shrubDensity;
    }


}
