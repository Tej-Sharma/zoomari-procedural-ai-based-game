package com.teron.zoomari.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teron.zoomari.InputCore;
import com.teron.zoomari.MainClass;
import com.teron.zoomari.WorldManager;
import com.teron.zoomari.map.Map;
import com.teron.zoomari.map.MapManager;
import com.teron.zoomari.map.TerrainTile;
import com.teron.zoomari.sprites.Player;
import com.teron.zoomari.TextureManager;
import com.teron.zoomari.sprites.projectiles.Projectile;

public class PlayScreen implements Screen
{

    private MainClass mainClass;
    private Viewport viewport, stageViewport;
    private OrthographicCamera camera;
    private Stage stage;

    private Player player;

    private WorldManager worldManager;

    private MapManager mapManager;

    private TextureManager textureManager;


    private Map map;

    private final float CAMERA_ZOOM = 2f;
    private InputCore inputCore;

    public PlayScreen(MainClass mainClass) {

        this.mainClass = mainClass;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainClass.WIDTH / MainClass.PPM,
                MainClass.HEIGHT / MainClass.PPM, camera);
        camera.position.set(viewport.getWorldWidth() / 2f,
                viewport.getWorldHeight() / 2f, 0);
        
        // The first parameter will literally flip the game upside down
        camera.setToOrtho(false, MainClass.WIDTH / MainClass.PPM,
                MainClass.HEIGHT / MainClass.PPM);

        inputCore = new InputCore(CAMERA_ZOOM);

        
        // TODO: decide on a zoom level
        camera.zoom = inputCore.getCameraZoom();
        stageViewport = new StretchViewport(MainClass.WIDTH, MainClass.HEIGHT,
                new OrthographicCamera());
        stage = new Stage(stageViewport, MainClass.batch);
        Gdx.input.setInputProcessor(inputCore);

        worldManager = new WorldManager();

        textureManager = new TextureManager();

        mapManager = new MapManager();
        int[] playerSpawn = mapManager.createMap(3000, 3000);
        map = mapManager.getMaps().get(0);



        player = new Player((playerSpawn[0] * TerrainTile.TILE_DIMENSION) / MainClass.PPM,
                (playerSpawn[1] * TerrainTile.TILE_DIMENSION) / MainClass.PPM, 16f / 100, 16f / 100,
                new Texture("play_screen/player.png"),
                worldManager.getWorld());

        addUI();
        createBodies();
    }

    private void createBodies() {
        //causes so much lagggggggggg!
//        int rows = map.getTerrainTileMap().length; int columns = map.getTerrainTileMap()[0].length;
//        for(int x = 0; x < rows; x++) {
//            for(int y = 0; y < columns; y++) {
//                if(!map.getTerrainTileMap()[x][y].isPassable()) {
//                    BodyDef bodyDef = new BodyDef();
//                    bodyDef.type = BodyDef.BodyType.StaticBody;
//                    bodyDef.position.set((x * TerrainTile.TILE_DIMENSION)/MainClass.PPM, (y * TerrainTile.TILE_DIMENSION)/MainClass.PPM);
//                    Body body = worldManager.getWorld().createBody(bodyDef);
//                    PolygonShape shape = new PolygonShape();
//                    shape.setAsBox(TerrainTile.TILE_DIMENSION/MainClass.PPM/2f, TerrainTile.TILE_DIMENSION/MainClass.PPM/2f);
//                    FixtureDef fixtureDef = new FixtureDef();
//                    fixtureDef.shape = shape;
//                    fixtureDef.density = 1f;
//                    fixtureDef.friction = 0f;
//                    fixtureDef.restitution = 0f;
//                    body.createFixture(fixtureDef);
//                }
//            }
//        }
    }

    private void addUI()
    {
        // TODO: add the UI logic here
    }


    @Override
    public void show()
    {

    }
    
    private void update(float delta)
    {


        worldManager.getWorld().step(1f/60f, 6, 2);
        worldManager.getRayHandler().update();

        player.update(delta, camera, map, worldManager.getWorld());


        camera.position.x = player.getX() + player.getWidth()/2;
        camera.position.y = player.getY() + player.getHeight()/2;

        camera.zoom = inputCore.getCameraZoom();

        camera.update();


    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 0, 0, 1.0f);

        stage.draw();

        MainClass.batch.setProjectionMatrix(camera.combined);
        MainClass.batch.begin();
        
        draw();

        MainClass.batch.end();

        worldManager.getDebugRenderer().render(worldManager.getWorld(),
                camera.combined);

        stage.act(delta);
        stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            mainClass.setScreen(new MenuScreen(mainClass));
        }
    }
    
    private void draw()
    {
        //int numberofTiles = (int) (TerrainTile.TILE_DIMENSION/TerrainTile.SUBTILE_DIMENSION);

        int rows = map.getTerrainTileMap().length;
        int columns = map.getTerrainTileMap()[0].length;

        int i = 0;

        int tileX = (int) ((player.getX() * MainClass.PPM)/TerrainTile.TILE_DIMENSION);
        int tileY = (int) ((player.getY() * MainClass.PPM)/TerrainTile.TILE_DIMENSION);

        int offset = player.viewRadius;

        /***
         * will only produce a rectangle/square, not an irregular viewing area which is better looking; however this can
         * be combined with the distance formula version (make the offset bigger and use the distance formula on some
         * of the tiles) to produce that irregular effect
         */
        for(int x = tileX - offset; x < tileX + offset; x++) {
            for(int y = tileY - offset; y < tileY + offset; y++) {
                if(x >= 0 && x < rows && y >= 0 && y < columns) {
                    MainClass.batch.draw(map.getTerrainTileMap()[x][y].getTextureRegion(), (x * TerrainTile.TILE_DIMENSION) / MainClass.PPM, (y * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / 100f, TerrainTile.TILE_DIMENSION / 100f);
                    i++;
                    if (map.getPlantMap()[x][y] != null) {
                        MainClass.batch.draw(map.getPlantMap()[x][y], (x * TerrainTile.TILE_DIMENSION) / MainClass.PPM, (y * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / 100f, TerrainTile.TILE_DIMENSION / 100f);
                    }
                }
            }
        }

        map.drawVillages(textureManager);


        for(Projectile projectile : player.getProjectiles()) {
            MainClass.batch.draw(textureManager.getProjectileTextures().get("arrow"), projectile.getX() - projectile.getWidth()/2, projectile.getY() - projectile.getHeight()/2, projectile.getWidth()/2, projectile.getHeight()/2, projectile.getWidth(), projectile.getHeight(), 1, 1, projectile.getRotation());
        }

        MainClass.batch.draw(new TextureRegion(player.getTexture()), player.getX(), player.getY(), player.getWidth()/2, player.getHeight()/2, player.getWidth(), player.getHeight(), 1, 1, player.getRotation());


//this code can be combined with the current code to produce an effect of an irregular viewing area instead of a rectangle/square
//        for(int x = 0; x < rows; x++) {
//            for(int y = 0; y < columns; y++) {
//
//                if(calcDistance((x * TerrainTile.TILE_DIMENSION) / MainClass.PPM, (y * TerrainTile.TILE_DIMENSION) / MainClass.PPM, player.getX(), player.getY()) < player.viewRadius) {
//                    MainClass.batch.draw(map.getTerrainTileMap()[x][y].getTextureRegion(), (x * TerrainTile.TILE_DIMENSION) / MainClass.PPM, (y * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / 100f, TerrainTile.TILE_DIMENSION / 100f);
//                    i++;
//                    if(map.getPlantMap()[x][y] != null) {
//                        MainClass.batch.draw(map.getPlantMap()[x][y], (x * TerrainTile.TILE_DIMENSION) / MainClass.PPM, (y * TerrainTile.TILE_DIMENSION) / MainClass.PPM, TerrainTile.TILE_DIMENSION / 100f, TerrainTile.TILE_DIMENSION / 100f);
//                    }
//                }
//
//
//            }
//        }
    }

    @Override
    public void resize(int width, int height)
    {
        // Will scale the entire game
        viewport.update(width, height);
        stageViewport.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    private float calcDistance(float x1, float y1, float x2, float y2) {
        float deltaX = x2 - x1;
        float deltaY = y2 - y1;
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    @Override
    public void dispose()
    {
        stage.dispose();
        player.dispose();
        worldManager.dispose();
        mapManager.dispose();
        textureManager.dispose();
    }


}
