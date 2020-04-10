package ru.abramov.sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Ship;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;

public class Enemy extends Ship {

    public Enemy(BulletPool bulletPool, Rect worldBounds) {
        this.bulletPool = bulletPool;
        this.worldBounds = worldBounds;
        v = new Vector2();
        v0 = new Vector2();
        bulletV = new Vector2();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
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
        this.reloadTimer = reloadInterval*0.9f;
        this.shootSound = shootSound;
        this.hp = hp;
        this.v.set(v0);
        setHeightProportion(height);
    }
}
