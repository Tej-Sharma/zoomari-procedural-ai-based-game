package com.teron.zoomari.map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import squidpony.squidmath.PerlinNoise;

import java.util.Random;


/**
 * Created by tejas on 8/7/2017.
 */
public  class MapGenerator {
    private static int SEED_BOUND = 10000000;


    /***
     *
     * @param width - in tiles
     * @param height - in tiles
     * @param frequency - the smaller the frequency, the higher the features; higher the frequency, the smaller the features
     * @param exponent higher exponents make middle elevations become valleys and low exponents make middle elevations become mountains (aka make it all flat if the exponent reaches a certain low value or it reaches a certain high value)
     * @return a double[][] array that is basically a map from which a map can be created by assigning certain values to be certain tiles
     */
    public static double[][] generateMapUsingPerlin(int width, int height, float frequency, float exponent) {
        //Use PerlinNoise to generate the map
        double[][] map = new double[width][height];

        double[][] elevation = new double[width][height];

        Random random = new Random();
        int seed = random.nextInt(SEED_BOUND);
        System.out.println(seed);

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                double nx = (double) x/(double) width - 0.5f, ny = (double) y/ (double) height - 0.5f;
                //noise is between -1 and 1
                elevation[x][y] =  1 * PerlinNoise.noise(frequency * (nx + seed), frequency * (ny + seed)) + 0.5 * PerlinNoise.noise(2 * (nx + seed), 2 * (ny+ seed)) + 0.25 * PerlinNoise.noise(4 * (nx + seed), 4 * (ny+ seed));
                elevation[x][y] = (elevation[x][y] + 1)/2f; // convert to a value between 0 and 1
                elevation[x][y] = Math.pow(elevation[x][y], exponent);

                map[x][y] = elevation[x][y];

            }
        }


        return map;
    }

    /***
     *
     * @param map - the map in which this will take effect
     * @param objectCount - the amount of objects to be placed
     * @return a TextureRegion[][[ that can be drawn on top of the map as the method makes sure the objects are placed in the right place
     */
    public static TextureRegion[][] generatePlants(Map map, int objectCount) {
        int width = map.getWidth();
        int height = map.getHeight();
        TextureRegion[][] plantMap = new TextureRegion[width][height];
        Random rand = new Random();
        int totalPlaced = 0;
        TerrainTile[][] terrainTileMap = map.getTerrainTileMap();

        while(totalPlaced < objectCount) {
            int x = rand.nextInt(width); int y = rand.nextInt(height);
            TerrainTile terrainTile = terrainTileMap[x][y];
            if(plantMap[x][y] == null) {
                if (terrainTile.getTreeDensity() > 0 && terrainTile.getShrubDensity() > 0) { // tree or shrub
                    int flag = (terrainTile.getTreeDensity() + terrainTile.getShrubDensity());
                    if (flag <= terrainTile.getTreeDensity()) {
                        //tree
                        if(terrainTile.getCode().equals("deciduousForest")) {
                            plantMap[x][y] = MapManager.getTextures().get("deciduousTree");
                        } else if(terrainTile.getCode().equals("treeDesert")) {
                            plantMap[x][y] = MapManager.getTextures().get("desertTree");
                        } else {
                            plantMap[x][y] = MapManager.getTextures().get("coniferTree");
                        }
                    } else {
                        //shrub
                        plantMap[x][y] = MapManager.getTextures().get("shrub");
                    }
                } else if (terrainTile.getTreeDensity() > 0) { // tree
                    if(terrainTile.getCode().equals("deciduousForest")) {
                        plantMap[x][y] = MapManager.getTextures().get("deciduousTree");
                    } else if(terrainTile.getCode().equals("treeDesert")) {
                        plantMap[x][y] = MapManager.getTextures().get("desertTree");
                    } else {
                        plantMap[x][y] = MapManager.getTextures().get("coniferTree");
                    }
                } else if (terrainTile.getShrubDensity() > 0) { // shrub
                    plantMap[x][y] = MapManager.getTextures().get("shrub");
                }
                totalPlaced += 1;
            }
        }




        return plantMap;
    }


}
