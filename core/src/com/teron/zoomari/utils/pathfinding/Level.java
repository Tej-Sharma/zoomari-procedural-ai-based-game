package com.teron.zoomari.utils.pathfinding;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * Created by Tejas Sharma on 5/12/2017.
 */

public class Level {
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private MapProperties properties;
    private GraphImp graph;

    public static int LevelTileWidth, LevelTileHeight, TilePixelWidth, TilePixelHeight;

    public Level(String filePath) {
        map = new TmxMapLoader().load(filePath);
        renderer = new OrthogonalTiledMapRenderer(map, 0.01f); // 0.01f scales it from pixels to meters (1 pixel = 0.01 meter)
        properties = map.getProperties();
        LevelTileWidth = properties.get("width", Integer.class);
        LevelTileHeight = properties.get("height", Integer.class);
        TilePixelWidth = properties.get("tilewidth", Integer.class);
        TilePixelHeight = properties.get("tileheight", Integer.class);

        graph = GraphGenerator.generateGraph(LevelTileWidth, LevelTileHeight, map);
    }


    public void dispose() {
        map.dispose();
        renderer.dispose();
    }



    public void generateGraph() {


    }


    public TiledMap getMap() { return map; }
    public OrthogonalTiledMapRenderer getRenderer() { return renderer; }
    public GraphImp getGraph() { return graph; }
}
