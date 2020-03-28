package ru.abramov.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exeption.GameExaption;
import ru.abramov.math.Rect;

public class Sprite extends Rect {
    protected float angle; //
    protected float scale = 1f; //
    protected TextureRegion[] regions; // массив текстур
    protected int frame; //указатель на текущую текстуру

    public Sprite(TextureRegion region) throws GameExaption {
        if(region==null){
            throw new GameExaption("Region is null");
        }
        regions = new TextureRegion[1];
        regions[0] =region;
    }

    public void setHeightProportion(float height){
        setHeight(height);
        float aspect = regions[frame].getRegionWidth()/(float)regions[frame].getRegionHeight();
        setWidth(height*aspect);
    }

    public void drow(SpriteBatch batch){
        batch.draw(
                regions[frame],
                getLeft(),getBottom(),
                halfWidth,halfHeight,
                getWidth(),getHeight(),
                scale,scale,
                angle
        );
    }

    public void resize(Rect worldBounds) {
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
}
