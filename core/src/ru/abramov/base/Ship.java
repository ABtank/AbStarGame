package ru.abramov.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.ExplosionPool;
import ru.abramov.sprites.Bullet;
import ru.abramov.sprites.Explosion;

public abstract class Ship extends Sprite {

    protected static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;

    protected Rect worldBounds;
    protected BulletPool bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int damage;
    protected Sound shootSound;
    protected int hp;

    protected Vector2 v0;
    protected Vector2 v;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) throws GameException {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void damage(int damage) {
        damageAnimateTimer = 0f;
        frame = 1;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    protected void autoShoot(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
    }

    private void shoot() {
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play(0.2f);
    }

    private void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(pos, getHeight());
    }
}
