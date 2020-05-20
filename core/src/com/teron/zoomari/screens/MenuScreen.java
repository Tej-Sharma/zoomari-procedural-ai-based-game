package com.teron.zoomari.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.teron.zoomari.MainClass;

/**
 * Created by tejas on 8/3/2017.
 */
public class MenuScreen implements Screen {
    //have to keep a copy of mainClass to change screens since setScreen can't be called from a static context
    private MainClass mainClass;

    //viewing stuff
    private Viewport viewport, stageViewport;
    private OrthographicCamera camera;
    private Stage stage;

    private Texture background;

    public MenuScreen(MainClass mainClass) {
        this.mainClass = mainClass;

        camera = new OrthographicCamera();
        viewport = new StretchViewport(MainClass.WIDTH / MainClass.PPM, MainClass.HEIGHT / MainClass.PPM, camera);
        camera.position.set(viewport.getWorldWidth()/2f, viewport.getWorldHeight()/2f, 0);
        stageViewport = new StretchViewport(MainClass.WIDTH, MainClass.HEIGHT, new OrthographicCamera());
        stage = new Stage(stageViewport, MainClass.batch);
        Gdx.input.setInputProcessor(stage);

        addUI();

    }

    private void addUI() {
        background = new Texture("menu_screen/windowTitle.png");

        //NOTE: All actors in a stage are NOT scaled by PPM; the res is 1366x768

        int buttonWidth = 220;
        int buttonHeight = 55;

        int buttonXOffset = 0;
        int playButtonYOffset = -100;
        int exitButtonYOffset = -200;

        //TODO: add the UI logic here

        TextButton playButton, exitButton;

        TextButton.TextButtonStyle textButtonStyle;
        BitmapFont font = new BitmapFont();
        Skin playButtonSkin = new Skin();
        Skin exitButtonSkin = new Skin();
        TextureAtlas buttonAtlas;

        buttonAtlas = new TextureAtlas(Gdx.files.internal("menu_screen/buttonPlay/buttonPlayPack.pack"));
        playButtonSkin.addRegions(buttonAtlas);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = playButtonSkin.getDrawable("buttonPlay");
        textButtonStyle.down = playButtonSkin.getDrawable("buttonPlayHighlight");

        playButton = new TextButton("", textButtonStyle);
        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        playButton.setPosition(MainClass.WIDTH/2-buttonWidth/2+buttonXOffset, MainClass.HEIGHT/2-buttonHeight/2+playButtonYOffset);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                dispose();
                mainClass.setScreen(new PlayScreen(mainClass));
            }
        });


        buttonAtlas = new TextureAtlas(Gdx.files.internal("menu_screen/buttonExit/buttonExitPack.pack"));
        exitButtonSkin.addRegions(buttonAtlas);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = exitButtonSkin.getDrawable("buttonExit");
        textButtonStyle.down = exitButtonSkin.getDrawable("buttonExitHighlight");

        exitButton = new TextButton("", textButtonStyle);
        exitButton.setWidth(buttonWidth);
        exitButton.setHeight(buttonHeight);
        exitButton.setPosition(MainClass.WIDTH/2-buttonWidth/2+buttonXOffset, MainClass.HEIGHT/2-buttonHeight/2+exitButtonYOffset);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed (ChangeListener.ChangeEvent event, Actor actor) {
                dispose();
                System.exit(0);
            }
        });


        stage.addActor(playButton);
        stage.addActor(exitButton);

        //buttonAtlas.dispose(); for some reason disposing this will not make the exit button work
        playButtonSkin.dispose();
        exitButtonSkin.dispose();
        font.dispose();


    }
    @Override
    public void show() {

    }

    private void update(float delta) {
        camera.update();

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.15f, 0.5f, 0.8f, 1.0f);
        MainClass.batch.setProjectionMatrix(camera.combined);
        MainClass.batch.begin();

        //Drawing begins
        ////////////////////////////////////////////
        MainClass.batch.draw(background, 0, 0, MainClass.WIDTH/MainClass.PPM, MainClass.HEIGHT/MainClass.PPM);





        ////////////////////////////////////////////
        //Drawing ends

        MainClass.batch.end();

        MainClass.batch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //will scale the entire game
        viewport.update(width, height);
        stageViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        //make sure to dispose all resources
        background.dispose();
        stage.dispose();
    }
}
