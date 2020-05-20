package com.teron.zoomari.map.settlements;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.teron.zoomari.MainClass;
import com.teron.zoomari.TextureManager;
import com.teron.zoomari.map.TerrainTile;
import com.teron.zoomari.map.buildings.BuildingTile;

/**
 * Created by tejas on 10/7/2017.
 */
public class VillageDrawer {


    BitmapFont font = new BitmapFont();

    public VillageDrawer() {

    }

    public void drawVillage(BuildingTile[][] tiles, int width, int height, int villageX, int villageY, TextureManager textureManager) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                if(tiles[x][y].getCode().equals("block")) {
                    MainClass.batch.draw(textureManager.getBuildingTextures().get("block"), ( (x + villageX) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, ( (y + villageY) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM);
                    font.getData().setScale(10/MainClass.PPM);
                    //font.draw(MainClass.batch, tiles[x][y].replace("b-", ""), ( (x + villageX) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, ( (y + villageY) * TerrainTile.TILE_DIMENSION) / MainClass.PPM);

                } else if(tiles[x][y].getCode().equals("well")) {
                    MainClass.batch.draw(textureManager.getBuildingTextures().get("well"), ( (x + villageX) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, ( (y + villageY) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM);

                } else if(tiles[x][y].getCode().equals("empty")) {
                    MainClass.batch.draw(textureManager.getBuildingTextures().get("empty"), ( (x + villageX) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, ( (y + villageY) * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM, TerrainTile.TILE_DIMENSION / MainClass.PPM);

                }
            }
        }

    }
}
