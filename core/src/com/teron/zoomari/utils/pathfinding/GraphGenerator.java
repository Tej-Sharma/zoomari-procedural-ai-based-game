package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Tejas Sharma on 5/12/2017.
 */

public class GraphGenerator {

    public static GraphImp generateGraph(int mapWidth, int mapHeight, TiledMap map) {
        Array<Node> nodes = new Array<Node>();
        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get(0);

        for (int y = 0; y < mapHeight; ++y) { // mapWidth and mapHeight are in tiles
            for (int x = 0; x < mapWidth; ++x) {
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                node.x = x;
                node.y = y;
                nodes.add(node);
            }
        }



        for (int y = 0; y < mapHeight; ++y) {
            for (int x = 0; x < mapWidth; ++x) {
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);
                TiledMapTileLayer.Cell left = tiles.getCell(x-1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x+1,y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);

                Node targetNode = nodes.get(mapWidth * y + x);


                //TODO: Tiled increments the ID in the editor by 1
                if(target.getTile().getId() == 18) {
                    if(y != mapHeight - 1 && up.getTile().getId() == 18) {
                        Node upNode = nodes.get(mapWidth * (y + 1) + x);
                        targetNode.createConnection(upNode, 1);
                    }
                    if (y != 0 && down.getTile().getId() == 18) {
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        targetNode.createConnection(downNode, 1);
                    }
                    if (x != 0 && left.getTile().getId() == 18) {
                        Node leftNode = nodes.get(mapWidth * y + x - 1);
                        targetNode.createConnection(leftNode, 1);
                    }
                    if(x != mapWidth - 1 && right.getTile().getId() == 18) {
                        Node rightNode = nodes.get(mapWidth * y + x + 1);
                        targetNode.createConnection(rightNode, 1);
                    }
                }
            }
        }

        return new GraphImp(nodes);


    }
}
