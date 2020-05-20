package com.teron.zoomari.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;

/**
 * Created by tejas on 8/6/2017.
 */
public class MapManager
{
    //this class will do with all the Map related things like WorldManager deals with World related things

    /*
     store all the maps in this int[x][y] where x = map width IN TILES, y = map height IN TILES (in the future the user can select the map size)
     for example, int[0][0] is the tile in the first row and first column
     a random point will be generated for the player to land on
    */

    private LinkedList<Map> maps;

    /*
     most efficient way: stores the texture with a strong saying what the texture represents ("water", "ground", "rocks", etc.)
     the string is they key and the texture is the value i.e. textures.get("water") will return the water texture
    */

    private static HashMap<String, TextureRegion> textures;

    Texture tileSheet;
    public MapManager()
    {
        maps = new LinkedList<Map>();
        textures = new HashMap<String, TextureRegion>();
        loadTextures();
    }

    private void loadTextures() {
        //add textures to the Map<String, Texture>
        tileSheet = new Texture("play_screen/tilesheet.png");
        TextureRegion grass = new TextureRegion(tileSheet, 0, 0, 16, 16);
        TextureRegion water = new TextureRegion(tileSheet, 64, 48, 16, 16); //TODO: def make sure to change this
        TextureRegion sand =  new TextureRegion(tileSheet, 0, 16, 16, 16);
        TextureRegion ground = new TextureRegion(tileSheet, 0, 48, 16, 16);
        TextureRegion dirt = new TextureRegion(tileSheet, 80, 32, 16, 16);
        TextureRegion snow = new TextureRegion(tileSheet, 64, 0, 16, 16);
        TextureRegion rock = new TextureRegion(tileSheet, 96, 32, 16, 16);
        TextureRegion mountain = new TextureRegion(tileSheet, 48, 0, 16, 16);
        TextureRegion forest = grass;
        TextureRegion scorched = new TextureRegion(tileSheet, 0, 32, 16, 16); // the textures do not include the trees, only the background
        TextureRegion bare = new TextureRegion(tileSheet, 0, 32, 16, 16);
        TextureRegion error = new TextureRegion(tileSheet, 112, 32, 16, 16);
        TextureRegion tundra = snow;
        TextureRegion shrubDesert = sand;
        TextureRegion shrubLand = grass;
        TextureRegion deciduousForest = grass;
        TextureRegion treeDesert = sand;
        TextureRegion coniferTree = new TextureRegion(tileSheet, 80, 48, 16, 16);
        TextureRegion desertTree = new TextureRegion(tileSheet, 96, 48, 16, 16);
        TextureRegion deciduousTree = new TextureRegion(tileSheet, 16, 0, 16, 16);
        TextureRegion shrub = new TextureRegion(tileSheet, 112, 48, 16, 16);


        textures.put("grass", grass);
        textures.put("water", water);
        textures.put("sand", sand);
        textures.put("ground", ground);
        textures.put("dirt", dirt);
        textures.put("snow", snow);
        textures.put("rock", rock);
        textures.put("mountain", mountain);
        textures.put("forest", forest);
        textures.put("scorched", scorched);
        textures.put("bare", bare);
        textures.put("tundra", tundra);
        textures.put("shrubDesert", shrubDesert);
        textures.put("shrubLand", shrubLand);
        textures.put("deciduousForest", deciduousForest);
        textures.put("treeDesert", treeDesert);
        textures.put("coniferTree", coniferTree);
        textures.put("desertTree", desertTree);
        textures.put("deciduousTree", deciduousTree);
        textures.put("shrub", shrub);
        textures.put("ERROR", error);
    }


    /**
     *
     * @param mapWidthInTiles
     * @param mapHeightInTiles
     * @return the player spawn position
     */
    public int[] createMap(int mapWidthInTiles, int mapHeightInTiles)
    {
        double[][] elevationHeightMap = MapGenerator.generateMapUsingPerlin(mapWidthInTiles, mapHeightInTiles, 55f, 1.9f);
        double[][] moistureHeightMap = MapGenerator.generateMapUsingPerlin(mapWidthInTiles, mapHeightInTiles, 55f, 1.9f);
        Map map = new Map(elevationHeightMap, moistureHeightMap);
        map.setPlantMap(MapGenerator.generatePlants(map, (int) ((mapWidthInTiles * mapHeightInTiles)/10f)));

        int numberOfVillages = 1;

        for(int i = 0; i < numberOfVillages; i++) {
            map.placeVillage();
        }

        int[] playerSpawn = new int[2];
        if(!map.getVillages().get(0).isNearWater() && !map.getVillages().get(0).isWaterInVillage()) {
            try {
                playerSpawn[0] = map.getVillages().get(0).getX();
                playerSpawn[1] = map.getVillages().get(0).getY();
            } catch(Exception e) {
                System.err.println("There are no wells in village?");
                playerSpawn[0] = map.getVillages().get(0).getX();
                playerSpawn[1] = map.getVillages().get(0).getY();
            }
            System.out.println("NOT NEAR WATER");
        } else if(map.getVillages().get(0).isWaterInVillage()) {
            System.out.println("WATER IN VILLAGE");
            playerSpawn[0] = map.getVillages().get(0).getX() + map.getVillages().get(0).getWidth()/2;
            playerSpawn[1] = map.getVillages().get(0).getY() + map.getVillages().get(0).getHeight()/2;
        } else {
            System.out.println("Spawning player near a village NEAR water");
            playerSpawn[0] = map.getVillages().get(0).getX();
            playerSpawn[1] = map.getVillages().get(0).getY();
        }
        maps.push(map);

        return playerSpawn;
    }


    public  LinkedList<Map> getMaps()
    {
        return maps;
    }

    public static HashMap<String, TextureRegion> getTextures() {
        return textures;
    }

    public void dispose() {
        tileSheet.dispose();
    }
}
