package com.teron.zoomari.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.teron.zoomari.TextureManager;
import com.teron.zoomari.map.settlements.Village;
import com.teron.zoomari.map.settlements.VillageDrawer;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by tejas on 8/11/2017.
 */
public class Map {

    private double[][] elevationHeightMap;
    private double[][] moistureHeightMap;
    private TextureRegion[][] plantMap;

    private TerrainTile[][] terrainTileMap; //tile[x][y] where x and y are the width and height respectively in pixels
    private int width, height;


    private Array<Village> villages;

    private VillageDrawer vDrawer;

    public Map(double[][] elevationHeightMap, double[][] moistureHeightMap) {
        this.elevationHeightMap = elevationHeightMap;
        this.moistureHeightMap = moistureHeightMap;

        width = elevationHeightMap.length;
        height = elevationHeightMap[0].length;

        terrainTileMap = new TerrainTile[width][height];

        convertHeightMapsToTileMap();

        villages = new Array<Village>();

        vDrawer = new VillageDrawer();


    }

    private void convertHeightMapsToTileMap() {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double elevationValue = elevationHeightMap[x][y];
                double moistureValue = moistureHeightMap[x][y];
                if(elevationValue < 0.1f) {
                    terrainTileMap[x][y] = TerrainTile.WATER;
                    continue;
                } else if(elevationValue < 0.12f) {
                    //Beach/Sand:
                    terrainTileMap[x][y] = TerrainTile.SAND;
                    continue;
                }

                if(elevationValue > 0.9f) {
                    //Mountain
                    terrainTileMap[x][y] = TerrainTile.MOUNTAIN;
                    continue;
                }

                if(elevationValue > 0.8f) {
                    if (moistureValue < 0.1f) {
                        //Scorched
                        terrainTileMap[x][y] = TerrainTile.SCORCHED;
                        continue;
                    }
                    if (moistureValue < 0.2f) {
                        //Bare
                        terrainTileMap[x][y] = TerrainTile.BARE;
                        continue;
                    }
                    if(moistureValue < 0.5f) {
                        //Tundra
                        terrainTileMap[x][y] = TerrainTile.TUNDRA;
                        continue;
                    }
                    //SNOW
                    terrainTileMap[x][y] = TerrainTile.SNOW;
                    continue;
                }

                if(elevationValue > 0.6f) {
                    if (moistureValue < 0.33f) {
                        //Temperate Desert
                        terrainTileMap[x][y] = TerrainTile.SHRUB_DESERT;
                        continue;
                    }
                    if (moistureValue < 0.66f) {
                        //Shrubland
                        terrainTileMap[x][y] = TerrainTile.SHRUB_LAND;
                        continue;
                    }
                    //Taiga
                    terrainTileMap[x][y] = TerrainTile.TUNDRA;
                    continue;


                }

                if(elevationValue > 0.3f) {
                    if(moistureValue < 0.16f) {
                        //Temperate desert
                        terrainTileMap[x][y] = TerrainTile.SHRUB_DESERT;
                        continue;
                    }
                    if(moistureValue < 0.5f) {
                        //Grassland
                        terrainTileMap[x][y] = TerrainTile.GRASS;
                        continue;
                    }
                    if(moistureValue < 0.83f) {
                        //Temperate Deciduous Forest
                        terrainTileMap[x][y] = TerrainTile.DECIDUOUS_FOREST;
                        continue;
                    }
                    // Temperate Rain Forest
                    terrainTileMap[x][y] = TerrainTile.FOREST;
                    continue;

                }

                if (moistureValue < 0.16f) {
                    //Subtropical desert
                    terrainTileMap[x][y] = TerrainTile.TREE_DESERT;
                    continue;
                }

                if(moistureValue < 0.33f) {
                    //Grassland
                    terrainTileMap[x][y] = TerrainTile.GRASS;
                    continue;
                }
                if(moistureValue < 0.66) {
                    //Tropical seasonal forest
                    terrainTileMap[x][y] = TerrainTile.FOREST;
                    continue;
                }

                terrainTileMap[x][y] = TerrainTile.FOREST;


            }
        }
    }

    public TerrainTile getTile(float x, float y) {
        if(x < 0 || y < 0 || x >= width || y >= height)
            return TerrainTile.MOUNTAIN;
        x *= 100; y *= 100;

        TerrainTile terrainTile = terrainTileMap[(int)(x/ TerrainTile.TILE_DIMENSION)][(int)(y/ TerrainTile.TILE_DIMENSION)];
        return terrainTileMap[(int)(x/ TerrainTile.TILE_DIMENSION)][(int)(y/ TerrainTile.TILE_DIMENSION)];
//        System.out.println(x + " " + y);
//        System.out.println((int)(x/ TerrainTile.TILE_DIMENSION) + " " + (int)(y/ TerrainTile.TILE_DIMENSION));
//        System.out.println(terrainTile.getCode());
    }

    /**
     * @return false if could not find a place to put the village
     */
    public boolean placeVillage() {

        int numberOfTimesToTry = 25;

        int villageMapDimension = ThreadLocalRandom.current().nextInt(35, 80);


        for(int i = 0; i < numberOfTimesToTry; i++) {
            int villageX = ThreadLocalRandom.current().nextInt(0 + 2 * villageMapDimension, width - 2 * villageMapDimension);
            int villageY = ThreadLocalRandom.current().nextInt(0 + 2 * villageMapDimension, height - 2 *  villageMapDimension);

            if(!terrainTileMap[villageX][villageY].isPassable())
                continue;

            int impassableTerrainTiles = 0;
            boolean nearWater = false, waterInVillage = false;
            float closestWaterSourceDistance = 1000000000f; //TODO: might need to adjust this if the map is really that big
            int waterSourceX = -5, waterSourceY = -5;

            for(int x = villageX - villageMapDimension; x < villageX + villageMapDimension; x++) {
                for(int y = villageY - villageMapDimension; y < villageY + villageMapDimension; y++) {
                    if(terrainTileMap[x][y].getCode().equals("water")) {
                        nearWater = true;
                        if(x >= villageX && x <= villageX + villageMapDimension && y >= villageY && y <= villageY + villageMapDimension) {
                            impassableTerrainTiles++;
                            waterInVillage = true;

                            //closest water source to center of village
                            if (calcDistance(x, y, villageX + villageMapDimension/2, villageY + villageMapDimension/2) < closestWaterSourceDistance) {
                                closestWaterSourceDistance = calcDistance(x, y, villageX, villageY);
                                waterSourceX = x;
                                waterSourceY = y;
                            }
                        } else {
                            //closest water source to bottom left corner and a water source in the village hasn't been found
                            if (calcDistance(x, y, villageX, villageY) < closestWaterSourceDistance && !waterInVillage) {
                                closestWaterSourceDistance = calcDistance(x, y, villageX, villageY);
                                waterSourceX = x;
                                waterSourceY = y;
                            }
                        }
                    } else if(terrainTileMap[x][y].getCode().equals("mountain")) {
                        if(x > villageX && x < villageX + villageMapDimension && y > villageY && y < villageY + villageMapDimension) {
                            impassableTerrainTiles++;
                        }
                    }
                }
            }


            //if its suitable create a village right there
            if(impassableTerrainTiles < (1f/2f) * (float) (villageMapDimension * villageMapDimension)) {
                System.out.println("Village created at " + villageX + " , " + villageY);
                Village village = new Village(villageX, villageY, villageMapDimension, nearWater, waterInVillage, this);

                if(nearWater) {
                    village.waterSourceX = waterSourceX;
                    village.waterSourceY = waterSourceY;
                }

                villages.add(village);

                return true;
            }

        }


        System.out.println("Could not create a village.");
        return false;
    }

    public void drawVillages(TextureManager textureManager) {
        Texture tileSheet = new Texture("play_screen/tilesheet.png");

        for(Village village : villages) {
            vDrawer.drawVillage(village.getTiles(), village.getWidth(), village.getHeight(), village.getX(), village.getY(), textureManager);
        }
    }



    private float calcDistance(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }


    public Array<Village> getVillages() {
        return villages;
    }

    public double[][] getElevationHeightMap() {
        return elevationHeightMap;
    }

    public double[][] getMoistureHeightMap() {
        return moistureHeightMap;
    }

    public TerrainTile[][] getTerrainTileMap() {
        return terrainTileMap;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TextureRegion[][] getPlantMap() {
        return plantMap;
    }

    public void setPlantMap(TextureRegion[][] plantMap) {
        this.plantMap = plantMap;
    }
}
