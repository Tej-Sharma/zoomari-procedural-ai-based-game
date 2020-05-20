package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

/**
 * Created by Tejas Sharma on 5/15/2017.
 */

public class HeuristicImp implements Heuristic<Node> {


    @Override
    public float estimate(Node startNode, Node endNode) {
        int startIndex = startNode.getIndex();
        int endIndex = endNode.getIndex();

        int startX = startIndex % Level.LevelTileWidth;
        int startY = startIndex / Level.LevelTileWidth;

        int endX = endIndex / Level.LevelTileWidth;
        int endY = endIndex / Level.LevelTileWidth;

        float distance = Math.abs(startX - endX) + Math.abs(startY - endY);

        return distance;
    }
}
