package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedNode;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Tejas Sharma on 5/15/2017.
 */


public class Node implements IndexedNode<Node> {
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    public int x, y;

    public int type, index;

    public Node() {
        index = Node.Indexer.getIndex();
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public Array<Connection<Node>> getConnections() {
        return connections;
    }

    public void createConnection(Node toNode, float cost) {
        connections.add(new ConnectionImp(this, toNode, cost));
    }

    private static class Indexer {
        private static int index = 0;
        public static int getIndex() {
            return index++;
        }
    }

    public static class Type {
        public static final int REGULAR = 1;
    }
}
