package com.teron.zoomari;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by tejas on 10/7/2017.
 */
public class InputCore implements InputProcessor {

    float cameraZoom;

    public InputCore(float startingZoom){
        cameraZoom = startingZoom;
    }

// I deleted useless methods for the sake of keeping this short.


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {


        if (amount == 1) {
            cameraZoom += .4f;
        } else if (amount == -1 && cameraZoom >= 0.4f) {
            cameraZoom -= .4f;
        }

        //Unclear what to return
        return false;
    }

    public float getCameraZoom() {
        return cameraZoom;
    }
}

