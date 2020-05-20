package com.teron.zoomari.map.buildings;

import com.badlogic.gdx.utils.Array;

/**
 * Created by tejas on 10/16/2017.
 */

public class Plot {

    /**
     * Stores the positions of tiles that are in the plot
     */
    private Array<int[]> positions; // pos that is in the plot

    /**
     * The area covered by the plots (number of tiles)
     */
    private int tilesCovered;

    /**
     *     the value of the plot ( how much it costs), plots near important resources and with benefits will cost more
     */
    private int value;

    public Plot(Array<int[]> positions) {
        this.positions = positions;
        // number of positions = number of tiles covered
        tilesCovered = positions.size;
    }

    public Array<int[]> getPositions() {
        return positions;
    }

    public void setPositions(Array<int[]> positions) {
        this.positions = positions;
    }

    public int getTilesCovered() {
        return tilesCovered;
    }

    public void setTilesCovered(int tilesCovered) {
        this.tilesCovered = tilesCovered;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
