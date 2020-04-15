package ru.abramov.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.utils.Regions;

public class Sprite extends Rect {
    protected float angle; //
    protected float scale = 1f; //
    protected TextureRegion[] regions; // массив текстур
    protected int frame; //указатель на текущую текстуру
    private boolean destroyed = false;

    public Sprite(TextureRegion region) throws GameException {
        if (region == null) {
            throw new GameException("Region is null");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public Sprite(TextureRegion region, int row, int cols, int frames) throws GameException {
        if (region == null) {
            throw new GameException("Region is null");
        }
        this.regions = Regions.split(region, row, cols, frames);
    }

    public Sprite() {
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(), // точка отрисовки
                halfWidth, halfHeight,  // точка вращения
                getWidth(), getHeight(),// размеры
                scale, scale,           // скалирование
                angle                   // угол поворота
        );
    }

    public void resize(Rect worldBounds) {
    }

    public void update(float deltatime) {
    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void flushDestroy() {
        destroyed = false;
    }
}
