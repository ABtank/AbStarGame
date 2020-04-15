package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Sprite;
import ru.abramov.math.Rect;
import ru.abramov.math.Rnd;

public class Perk extends Sprite {

    public int bonus;
    public float fbonus;
    public int effect;
    private Vector2 v;
    private Rect worldBounds;

    public Perk(Rect worldBounds) {
        this.worldBounds=worldBounds;
        float vx = Rnd.nextFloat(-0.005f, 0.005f);
        float vy = Rnd.nextFloat(-0.05f, -0.1f);
        v = new Vector2(vx, vy);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        pos.mulAdd(v, delta);
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,
            int bonus,
            int frame,
            float height
    ) {
        this.regions = regions;
        this.v.set(v);
        this.bonus = bonus;
        this.frame = frame;
        this.effect = frame;
        setHeightProportion(height);
    }
    public void set(
            TextureRegion[] regions,
            float fbonus,
            int frame,
            float height
    ) {
        this.regions = regions;
        this.v.set(v);
        this.fbonus = fbonus;
        this.frame = frame;
        this.effect = frame;
        setHeightProportion(height);
    }
}
