package ru.abramov.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exception.GameException;

public abstract class ScaleButton extends Sprite {

    private static final float SCALE = 0.9f;
    private int pointer; //номер пальца
    private boolean pressed; // состояние кнопки
    protected static final float MAX_SCALE = 1.05f;
    protected static final float MIN_SCALE = 1f;
    protected static final float ANIMATE_INTERVAL = 0.05f;
    protected boolean isGrow;
    protected float animateTimer;

    public ScaleButton(TextureRegion region) throws GameException {
        super(region);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (pressed || !isMe(touch)) {
            return false;
        }
        this.pointer = pointer;
        scale = SCALE;
        pressed = true;
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (this.pointer != pointer || !pressed) {
            return false;
        }
        if (isMe(touch)) {
            action();
        }
        pressed = false;
        scale = 1f;
        return false;
    }

    public abstract void action();

}
