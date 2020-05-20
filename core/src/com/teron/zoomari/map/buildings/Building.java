package com.teron.zoomari.map.buildings;

import com.badlogic.gdx.utils.Array;

/**
 * Created by tejas on 9/17/2017.
 */
public class Building {
    /**
     * @wealth - in gold coins
     *
     */


    //not sure if these are needed as buildings do not have to be rectangular
//    private int width, height;
//    private int x, y; //debatable whether this is needed and if it is needed where it should be
//
    private int number_of_floors;

    private int wealth;

    private int number_of_people;

    private int areaCovered;

    /**
     *     positions of the points on the *Village tiles* array
     */
    private Array<int[]> positions;

    private int id;


    public Building(Array<int[]> positions, int areaCovered, int number_of_floors, int id) {
        this.positions = positions;
        this.areaCovered = areaCovered;
        this.id = id;
        this.number_of_floors = number_of_floors;

        this.wealth = 0;
        this.number_of_people = 0;

        createBuilding();
    }

    private void createBuilding() {
        //TODO: populate the building tiles

    }

}
