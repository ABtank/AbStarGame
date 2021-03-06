package ru.abramov.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Ship;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.ExplosionPool;

public class Enemy extends Ship {

    private final Vector2 descentV;

    public Enemy(BulletPool bulletPool, ExplosionPool explosionPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
        bulletPos = new Vector2();
        descentV = new Vector2(0, -0.3f);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        bulletPos.set(pos.x, pos.y - getHalfHeight());
        if (getBottom() + getHalfHeight() >= worldBounds.getTop()) {
            reloadTimer = reloadInterval * 0.9f;
            v.scl(1.05f);
        } else if (getTop() > worldBounds.getTop() && v.y<= v0.y) {
            v.scl(0.9f);
        } else {
            v.set(v0);
            autoShoot(delta);
        }
        if (getBottom() <= worldBounds.getBottom()) {
            destroy();
        }
    }

    public void set(
            TextureRegion[] regions,        // регион корабля
            Vector2 v0,                     // начальная скорость
            TextureRegion bulletRegion,     // регион пули
            float bulletHeight,             // размер пули
            float bulletVY,                 // скорость пули по оси у
            int damage,                     // урон пули
            float reloadInterval,           // перезарядка
            Sound shootSound,               // звук выстрела
            int hp,                         // жизни
            float height                    // размер корабля
    ) {
        this.regions = regions;
        this.v0.set(v0);
        this.bulletRegion = bulletRegion;
        this.bulletHeight = bulletHeight;
        this.bulletV.set(0, bulletVY);
        this.damage = damage;
        this.reloadInterval = reloadInterval;
        this.reloadTimer = reloadInterval;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(descentV);
        setHeightProportion(height);
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getBottom() > getTop()
                || bullet.getTop() < pos.y
        );
    }
}
