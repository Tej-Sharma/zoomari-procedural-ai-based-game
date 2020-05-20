package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Tejas Sharma on 5/15/2017.
 */

public class ConnectionImp implements Connection<Node> {
    private Node fromNode;
    private Node toNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return 0;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
