package ru.abramov.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;

public class Logo extends Sprite {

    private final float V_LEN = 0.01f;
    private Vector2 v;
    private Vector2 tmp;
    private Vector2 targetPoint;

    public Logo(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        v = new Vector2();
        targetPoint = new Vector2();
        tmp = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        setHeightProportion(0.2f);
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
        targetPoint.set(touch);
        v.set(touch.sub(pos)).setLength(V_LEN);
        return false;
    }

}
