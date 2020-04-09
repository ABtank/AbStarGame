package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.math.Rnd;

public class Enemy extends Sprite {
    private static final float HEIGHT = 0.01f;
    private Vector2 v;
    private Rect worldBounds;
    private int enemyNumber;
    private float animateInterval = 0.5f;
    private float animateTimer;



    public Enemy(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("enemy1"));
        float vx = Rnd.nextFloat(-0.01f, 0.01f);
        float vy = Rnd.nextFloat(-0.1f, -0.1f);
        v = new Vector2(vx, vy);
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(HEIGHT);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        this.pos.set(posX, posY);
    }

    public void update(float delta) {
        pos.mulAdd(v, delta);
        scale += 0.01f;
        animateTimer += delta;
        if (animateTimer >= animateInterval) {
            animateTimer = 0;
            scale = 1f;
        }
        if (getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
            scale =5f;
        }
        if (getLeft() > worldBounds.getRight()) {
            setRight(worldBounds.getLeft());
        }
        if (getRight() < worldBounds.getLeft()) {
            setLeft(worldBounds.getRight());
        }
    }
}
