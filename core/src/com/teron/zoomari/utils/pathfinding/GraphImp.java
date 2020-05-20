package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.DefaultIndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Tejas Sharma on 5/15/2017.
 */

public class GraphImp extends DefaultIndexedGraph<Node> {
    private Array<Node> nodes = new Array<Node>();
    public GraphImp() {
        super();
    }

    public GraphImp(int capacity) {
        super(capacity);
    }

    public GraphImp(Array<Node> nodes) {
        super(nodes);
        this.nodes = nodes;
    }

    public Node getNodeByXY(int x, int y) {
        int modX = x / Level.TilePixelWidth;
        int modY = y / Level.TilePixelHeight;
        return nodes.get(Level.LevelTileWidth * modY + modX);
    }




    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return super.getConnections(fromNode);
    }

    @Override
    public int getNodeCount() {
        return super.getNodeCount();
    }



}
