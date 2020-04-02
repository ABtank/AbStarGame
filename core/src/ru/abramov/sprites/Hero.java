package ru.abramov.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;

public class Hero extends Sprite {

    private final float V_LEN = 0.01f;
    private Vector2 v;
    private Vector2 tmp;
    private Vector2 targetPoint;
    private Rect worldBounds;

    private int pointer; //номер пальца
    private boolean pressed; // состояние кнопки


    public Hero(TextureAtlas atlas) throws GameException {
        super(new TextureRegion(atlas.findRegion("main_ship"), 0, 0, 390 / 2, 286));
//        super(atlas.findRegion("heroRed"));
        v = new Vector2();
        targetPoint = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.15f);
        setBottom(worldBounds.getBottom());
    }

    @Override
    public void update(float deltatime) {
        super.update(deltatime);
        tmp.set(targetPoint);
        if (tmp.sub(pos).len() > V_LEN) {
            pos.add(v);
        } else {
            pos.set(targetPoint);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (pressed) {
            return false;
        }
        touch.y = pos.y;
        targetPoint.set(touch);
        v.set(touch.sub(pos)).setLength(V_LEN);
        if (touch.x < 0) {
            v.setLength(-V_LEN);
        } else {
            v.setLength(V_LEN);
        }

        this.pointer = pointer;
        pressed = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !pressed) {
            return false;
        }
        pressed = false;
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public boolean keyDown(int keycode) {

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            pos.x -= V_LEN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            pos.x += V_LEN;
        }

        return false;
    }


    public boolean keyUp(int keycode) {
        return false;
    }
}
