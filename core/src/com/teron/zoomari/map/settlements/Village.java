package com.teron.zoomari.map.settlements;

import com.badlogic.gdx.utils.Array;
import com.teron.zoomari.map.Map;
import com.teron.zoomari.map.buildings.Building;
import com.teron.zoomari.map.buildings.BuildingTile;
import com.teron.zoomari.map.buildings.Plot;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by tejas on 8/27/2017.
 */
public class Village {



    private int startingPopulation, population;
    private float populationDensity; //population density per tile as in population / area of the map

    private int startingWidth, startingHeight;

    private int x, y; // bottom left corner of the village
    public int waterSourceX, waterSourceY;
    private int width, height;

    private BuildingTile[][] tiles;

    private boolean nearWater, waterInVillage;

    private int areaCovered = 0;

    private final int MINIMUM_BUILDING_AREA = 4, MAXIMUM_BUILDING_AREA = 20, MAXIMUM_NUMBER_OF_FLOORS = 4;


    private Array<Building> buildings;
    private int currentBuildingID = 0;

    private Array<int[]> wellPositions;

    private Map map;

    public Village(int villageX, int villageY, int mapDimension, boolean nearWater, boolean waterInVillage,  Map map) {

        this.map = map;
        this.nearWater = nearWater;
        this.waterInVillage = waterInVillage;
        this.x = villageX;
        this.y = villageY;

        float populationMinControl = 0.01f;
        float populationMaxControl = 0.03f;
        startingPopulation = ThreadLocalRandom.current().nextInt((int)((populationMinControl) * (float) (mapDimension * mapDimension)), (int) ((populationMaxControl) * (float) (mapDimension * mapDimension)));
        population = startingPopulation;
        populationDensity = ((float) population) / ((float) mapDimension * mapDimension);

        startingWidth = mapDimension; startingHeight = mapDimension;
        width = startingWidth; height = startingHeight;

        /*
        store it as an array of string therefore the ids of the building can be stored, for example if a tile belongs to
        a building then on that spot it will be "building 12", where 12 is the Id of the building
         */
        tiles = new BuildingTile[startingWidth][startingHeight];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                tiles[x][y] = BuildingTile.EMPTY;
            }
        }

        buildings = new Array<Building>();
        wellPositions = new Array<int[]>();

        System.out.println("Population: " + population);
        System.out.println("Population Density: " + populationDensity);
        System.out.println("Dimension: " + startingWidth);


        markImpassable();

        generateStructures();



        writeTilesToFile();






    }



    /**
     * Mark tiles that are impassable on the tiles array (water & mountain)
     */
    private void markImpassable() {
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();
        for(int x2 = x; x2 < x + width; x2++) {
            for(int y2 = y; y2 < y + height; y2++) {
                if(map.getTerrainTileMap()[x2][y2].getCode().equals("water")) {
                    tiles[x2 - getX()][y2 - getY()] = BuildingTile.WATER;
                } else if(map.getTerrainTileMap()[x2][y2].getCode().equals("mountain")) {
                    tiles[x2 - getX()][y2 - getY()] = BuildingTile.MOUNTAIN;
                }
            }
        }
    }




    /**
     * Note: Plots produced will not be limited to rectangles and can come in various shapes and sizes (this helps in diversity & combating plainness)
     * @return An array of 2 arrays: one containg plots that can house buildings, the other containing plots smaller than that
     */
    private Array<Array<Plot>> getPlots() {
        Array<Array<Plot>> plots =  new Array<Array<Plot>>();
        Array<Plot> buildingPlots = new Array<Plot>();
        Array<Plot> notBuildingPlots = new Array<Plot>();

        BuildingTile[][] plottedTiles = new BuildingTile[width][height];
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                plottedTiles[x][y] = tiles[x][y];
            }
        }

        Plot plot;
        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                if(tiles[x][y].getCode().equals("empty") && plottedTiles[x][y].getCode().equals("empty")) {
                    Array<int[]> positions = new Array<int[]>();

                    outerloop:
                    for(int y2 = y; y2 < height; y2++) {
                        for(int x2 = x; x2 < width; x2++) {
                            if(tiles[x2][y2].getCode().equals("empty") && plottedTiles[x2][y2].getCode().equals("empty")) {
                                //add it to the plot's indexes
                                int[] position = new int[2];
                                position[0] = x2;
                                position[1] = y2;
                                positions.add(position);

                                //mark it in plottedTiles
                                plottedTiles[x2][y2] = BuildingTile.BLOCK;

                            } else {
                                break outerloop;
                            }
                        }
                    }
                    plot = new Plot(positions);
                    System.out.println("Size of position array: " + positions.size);
                    if(plot.getTilesCovered() >= MINIMUM_BUILDING_AREA) {
                        buildingPlots.add(plot);
                    } else {
                        notBuildingPlots.add(plot);
                    }

                } else {
                    if(!plottedTiles[x][y].getCode().equals("block"))
                        System.out.println("For skipped in tiles array: " + tiles[x][y].getCode());
                }
            }
        }

        plots.add(notBuildingPlots);
        plots.add(buildingPlots);
        return plots;
    }

    /**
     * Based the # of wells on the population but not completly (there could be overcrowding of people going to fetch water
     * & fights could develo which could lead to quests
     * Possible way to generate the village: after making the wells, pick a random place for a building and connect it to the well
     * and keep connecting until the village is mostly occupied
     */

    private void generateStructures() {

        //TODO: pick these numbers carefully

        if(!nearWater && waterInVillage) {
            System.err.println("Village is not near water... but water in village...\n Changing near Water to true...");
            nearWater = true;
        }

        if(!nearWater || nearWater) {
            int peoplePerWell = ThreadLocalRandom.current().nextInt(15, 60);
            int totalWells = population / peoplePerWell + 1;

            if(totalWells < 0) totalWells = 1;

            System.out.println("People per well:" + peoplePerWell);
            System.out.println("Total Wells: " + totalWells);

            for (int i = 0; i < totalWells; i++) {
                int wellPosX = ThreadLocalRandom.current().nextInt(width);
                int wellPosY = ThreadLocalRandom.current().nextInt(height);


                if (tiles[wellPosX][wellPosY].getCode().equals("empty")) { // if there is nothing there, then place the well
                    tiles[wellPosX][wellPosY] = BuildingTile.WELL;
                    areaCovered++;
                    int[] wellPosition = new int[2];
                    wellPosition[0] = wellPosX;
                    wellPosition[1] = wellPosY;
                    wellPositions.add(wellPosition);
                }

            }

            int buildingCount = 0;

            System.out.println(getPlots().peek().size);

            while(getPlots().peek().size > 0) { //add buildings
                //TODO: generate buildings and connect them by road to the wells
                //pick a random plot

                Plot randomPlot = getPlots().peek().random();

                //randomly generate the area the building will use up based on the plot


                int buildingArea;
                //Random is exclusive of the upper bound so add 1
                if(randomPlot.getTilesCovered() != MINIMUM_BUILDING_AREA) {
                    if (randomPlot.getTilesCovered() < MAXIMUM_BUILDING_AREA) {
                        buildingArea = ThreadLocalRandom.current().nextInt(MINIMUM_BUILDING_AREA, randomPlot.getTilesCovered()+1);
                    } else {
                        buildingArea = ThreadLocalRandom.current().nextInt(MINIMUM_BUILDING_AREA, MAXIMUM_BUILDING_AREA + 1);
                    }
                } else {
                    buildingArea = MINIMUM_BUILDING_AREA;

                }

                //based on the building area, reserve that amount of tiles for the building (connected tiles, of course)

                Array<int[]> positions = new Array<int[]>();
                Building building;
                //TODO: for now just start from the 0th position -> (buildingArea-1)th position, if this does not produce deseriable results
                //TODO: randomly generate whether to start buildings from left or right of array
                if(buildingArea != MINIMUM_BUILDING_AREA) {
                    int flag = ThreadLocalRandom.current().nextInt(2);
                    if(flag == 0) { // from left of array
                        for (int i = 0; i < buildingArea; i++) {
                            positions.add(randomPlot.getPositions().get(i));
                        }
                    } else {
                        for (int i = randomPlot.getPositions().size-1; i > randomPlot.getPositions().size-1-buildingArea; i--) {
                            positions.add(randomPlot.getPositions().get(i));
                        }
                    }

                } else {
                    for(int i = 0; i < MINIMUM_BUILDING_AREA; i++) {
                        positions.add(randomPlot.getPositions().get(i));
                    }
                }

                int number_of_floors = ThreadLocalRandom.current().nextInt(1, MAXIMUM_NUMBER_OF_FLOORS+1);
                building = new Building(positions, buildingArea, number_of_floors, currentBuildingID);

                for(int[] position : positions) {
                    int x = position[0], y = position[1];
                    tiles[x][y] = BuildingTile.BLOCK;
                    tiles[x][y].setId(currentBuildingID);
                }

                currentBuildingID++;

            }


            while(getPlots().get(1).size > 0) { //add smaller structures
                //TODO: generate smaller strructures
                break;
            }


        } else {
            //TODO: generate a road to the water source
            //determine which direction is the water source
            if(waterInVillage) { //Is inside the village
                //TODO: find out the shape of the water source inside the
            } else {
                //TODO: generate a wealthy building and then connect the water source to the building
            }


        }






    }


    /** IMPORTANT NOTE: THIS Method is full of bugs (for example, it doesn't break out of loops properly (only breaks out of inner loop
     * and will think marked/impassable land is "open" while it is not)
     * Find plots of land that are open and determine whether they are big enough to house a building or just a small structure
     * @return an array an array of String[] that contains the biggest, open rectangular plots of land in the format of:
     * First topmost array will contain the bulding plots, the 2nd array will contain the structure plots
     * Indexes of the String[]
     * indexes: 0 - x position, 1 - y position, 2 - width, 3 - height
     * Note: Can be used to check if expansion is necessary (if "NONE" is the answer but the current buildings cant
     * support the population
     */
//    private Array<Array<String[]>>  getOpenLandVersion2() {
//
//        Array<Array<String[]>> plots =  new Array<Array<String[]>>();
//        Array<String[]> buildingPlots = new Array<String[]>();
//        Array<String[]> notBuildingPlots = new Array<String[]>();
//
//        int buildingCount = 0, notBuildingCount = 0;
//
//
//
//        BuildingTile[][] markedtiles = new BuildingTile[width][height];
//        for(int i = 0; i < width; i++) {
//            for(int n = 0; n < height; n++) {
//                if(tiles[i][n].getCode().equals("well")) {
//                    markedtiles[i][n] = BuildingTile.WELL;
//                }
//                markedtiles[i][n] = BuildingTile.WELL;
//            }
//        }
//        int structureWidth, structureHeight;
////      this adding wells into plots?
//        for(int x = 0; x < width; x++) {
//            for(int y = 0; y < height; y++) {
//                if(tiles[x][y].getCode().equals("empty") && markedtiles[x][y].getCode().equals("empty")) {
//                    int xPos = x, yPos = y;
//                    for(yPos = y; yPos < height; yPos++) {
//                        System.out.println(tiles[xPos][yPos].getCode());
//                        if(!(tiles[xPos][yPos].getCode().equals("empty")) || !(markedtiles[xPos][yPos].getCode().equals("empty"))) {
//                            break;
//                        }
//                    }
//                    //NOTE: yPos will be equal to the height if it runs through fully (java increments variable before exiting loop)
//                    if(yPos == height) {
//                        structureHeight = height;
//                    } else {
//                        structureHeight = yPos - y;
//                    }
//
//                    mainloop:
//                    for(xPos = x; xPos < width; xPos++) {
//                        for(int y2 = y; y2 < yPos; y2++) {
//                            System.out.println(tiles[xPos][y2].getCode());
//                            if(!(tiles[xPos][y2].getCode().equals("empty")) || !(markedtiles[xPos][y2].getCode().equals("empty"))) {
//                                break mainloop;
//                            }
//                        }
//                    }
//
//                    //java increments variable before exitting loop
//                    if(xPos == width) {
//                        structureWidth = width;
//                    } else {
//                        structureWidth = xPos - x;
//                    }
//
//
//                    for(int markerX  = x; markerX < x + structureWidth; markerX++) {
//                        for(int markerY = y; markerY < y + structureHeight; markerY++) {
//                            markedtiles[markerX][markerY] = BuildingTile.BLOCK;
//                        }
//                    }
//
//                    if(structureWidth > 0 && structureHeight > 0) {
//                        //Can create a plot
//
//                        String[] plot = new String[4];
//
//                        if(structureWidth >= MINIMUM_BUILDING_WIDTH && structureHeight >= MINIMUM_BUILDING_HEIGHT) {
//                            //building
//                            buildingCount++;
//                            plot[0] = Integer.toString(x);
//                            plot[1] = Integer.toString(y);
//                            plot[2] = Integer.toString(structureWidth);
//                            plot[3] = Integer.toString(structureHeight);
//                            buildingPlots.add(plot);
//
//                            for(int p = x; p < x + structureWidth; p++) {
//                                for(int z = y; z < y + structureHeight; z++) {
//                                    if(tiles[p][z].getCode().equals("well")) {
//                                        System.out.println("Well in plot!???????????????????");
//                                    }
//                                }
//                            }
//
//
//                        } else {
//                            //small structure
//                            notBuildingCount++;
//                            plot[0] = Integer.toString(x);
//                            plot[1] = Integer.toString(y);
//                            plot[2] = Integer.toString(structureWidth);
//                            plot[3] = Integer.toString(structureHeight);
//                            notBuildingPlots.add(plot);
//                        }
//
//
//                    } else {
//                        System.out.println("Structure Width & Height BOTH not greater than 0!");
//                    }
//
//
//                }
//            }
//        }
//
//        plots.add(notBuildingPlots);
//        plots.add(buildingPlots);
//        return plots;
//    }
//
//    /**
//     * Note this version does not work properly (no use of marked tiles, for example) and has many logic bugs
//     */
//    private Array<Array<String[]>> getOpenLand() {
//        Array<Array<String[]>> plots =  new Array<Array<String[]>>();
//        Array<String[]> buildingPlots = new Array<String[]>();
//        Array<String[]> notBuildingPlots = new Array<String[]>();
//
//        int buildingCount = 0, notBuildingCount = 0;
//        for(int x = 0; x < width; x++) {
//            for(int y = 0; y < height; y++) {
//                if(tiles[x][y].getCode().equals("empty")) {
//                    int structureWidth, structureHeight;
//                    int xPos, yPos;
//                    for(xPos = x; xPos < width; xPos++) {
//                        if(!tiles[x][y].getCode().equals("empty")) {
//                            break;
//                        }
//                    }
//                    for(yPos = y; yPos < height; yPos++) {
//                        if(!tiles[x][y].getCode().equals("empty")) {
//                            break;
//                        }
//                    }
//
//                    // might have to adjusut this value (add 1 or not), but am sure that it will give 0by0 for 1by1
//                    structureWidth = xPos - x;
//                    structureHeight = yPos - y;
//
//                    // determine the type of structure based on width and height
//                    String[] plot = new String[4];
//
//                    if(structureWidth >= MINIMUM_BUILDING_WIDTH && structureHeight >= MINIMUM_BUILDING_HEIGHT) {
//                        //building
//                        buildingCount++;
//                        plot[0] = Integer.toString(x);
//                        plot[1] = Integer.toString(y);
//                        plot[2] = Integer.toString(structureWidth);
//                        plot[3] = Integer.toString(structureHeight);
//                        buildingPlots.add(plot);
//
//
//                    } else {
//                        //small structure
//                        notBuildingCount++;
//                        plot[0] = Integer.toString(x);
//                        plot[1] = Integer.toString(y);
//                        plot[2] = Integer.toString(structureWidth);
//                        plot[3] = Integer.toString(structureHeight);
//                        notBuildingPlots.add(plot);
//                    }
//
//                }
//            }
//        }
//
//        plots.add(notBuildingPlots);
//        plots.add(buildingPlots);
//        return plots;
//    }

    private void writeTilesToFile() {
        String FILENAME = "villageMap.txt";
//            if (!infile.canRead()) {
//                infile.setReadable(true);
//            }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {

            for (BuildingTile[] row : tiles)
            {
                bw.write(Arrays.toString(row) + "\n");
                //System.out.println(Arrays.toString(row));
            }





            // no need to close it.
            //bw.close();

            System.out.println("Done making structures of village");

        } catch (IOException e) {

            e.printStackTrace();

        }
    }





    public BuildingTile[][] getTiles() {
        return tiles;
    }

    public boolean isWaterInVillage() {
        return waterInVillage;
    }

    public Array<int[]> getWellPositions() {
        return wellPositions;
    }

    public boolean isNearWater() {
        return nearWater;
    }

    public void setNearWater(boolean nearWater) {
        this.nearWater = nearWater;
    }

    public int getStartingPopulation() {
        return startingPopulation;
    }

    public int getPopulation() {
        return population;
    }

    public float getPopulationDensity() {
        return populationDensity;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWaterInVillage(boolean value) {
        this.waterInVillage = value;
    }
}
