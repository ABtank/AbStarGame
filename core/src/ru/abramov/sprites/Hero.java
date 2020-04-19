package ru.abramov.sprites;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.Ship;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.ExplosionPool;

public class Hero extends Ship {

    private static final float SHIP_HEIGHT = 0.15f;
    private static final float BOTTOM_MARGIN = 0.05f;
    private static final float BOTTOM_MARGIN_BULLET = 0.01f;
    private static final int INVALID_POINTER = -1;

    private int startHp = 50;
    private int startDamage = 1;
    private Vector2 startSpeed = new Vector2(0.5f, 0);
    private float startReloadInterval = 0.4f;
    private float startBulletHeight = 0.01f;
    private float startShotSoundVolume =0.2f;
    private int maxDamage = 5;
    private Vector2 bulletPosLeft = new Vector2(getLeft(), pos.y + getHalfHeight());
    private Vector2 bulletPosCenter = new Vector2(pos.x, pos.y + getHalfHeight());
    private Vector2 bulletPosRight = new Vector2(getRight(), pos.y + getHalfHeight());
    private Vector2[] arrBulletPos = {bulletPosLeft, bulletPosRight};
    private Vector2[] tripleBulletPos = {bulletPosLeft, bulletPosCenter, bulletPosRight};


    private boolean pressedLeft; //состояния нажатия клавиши
    private boolean pressedRight;
    private int switchBullet = 2;
    private float shotSoundVolume = startShotSoundVolume;

    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;


    public Hero(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool, Sound shootSound) throws GameException {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.shootSound = shootSound;
        bulletRegion = atlas.findRegion("bulletMainShip");
        bulletV = new Vector2(0, 0.5f);
        bulletPos = new Vector2();
        v0 = startSpeed;
        v = new Vector2();
        reloadInterval = startReloadInterval;
        reloadTimer = reloadInterval;
        bulletHeight = startBulletHeight;
        damage = startDamage;
        hp = startHp;
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        setHeightProportion(SHIP_HEIGHT);
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }



    @Override
    public void update(float delta) {
        super.update(delta);
        switch (switchBullet) {
            case 1:
                bulletPos.set(pos.x, pos.y + getHalfHeight());
                autoShoot(delta);
                break;
            case 2:
                bulletPosLeft.set(getLeft() + BOTTOM_MARGIN_BULLET, pos.y + getHalfHeight() - BOTTOM_MARGIN);
                bulletPosRight.set(getRight() - BOTTOM_MARGIN_BULLET, pos.y + getHalfHeight() - BOTTOM_MARGIN);
                autoShoot(delta, arrBulletPos,shotSoundVolume);
                break;
            case 3:
                bulletPosLeft.set(getLeft() + BOTTOM_MARGIN_BULLET, pos.y + getHalfHeight() - BOTTOM_MARGIN);
                bulletPosRight.set(getRight() - BOTTOM_MARGIN_BULLET, pos.y + getHalfHeight() - BOTTOM_MARGIN);
                bulletPosCenter.set(pos.x, pos.y + getHalfHeight());
                autoShoot(delta, tripleBulletPos, shotSoundVolume);
                break;
        }

        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (touch.x < worldBounds.pos.x) {
            if (leftPointer != INVALID_POINTER) {
                return false;
            }
            leftPointer = pointer;
            moveLeft();
        } else {
            if (rightPointer != INVALID_POINTER) {
                return false;
            }
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if (rightPointer != INVALID_POINTER) {
                moveRight();
            } else {
                stop();
            }
        } else if (pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if (leftPointer != INVALID_POINTER) {
                moveLeft();
            } else {
                stop();
            }
        }
        return false;
    }

    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
        return false;
    }

    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if (pressedRight) {
                    moveRight();
                } else {
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if (pressedLeft) {
                    moveLeft();
                } else {
                    stop();
                }
                break;
        }
        return false;
    }

    public boolean isBulletCollision(Rect bullet) {
        return !(bullet.getRight() < getLeft()
                || bullet.getLeft() > getRight()
                || bullet.getTop() < getBottom()
                || bullet.getBottom() > pos.y
        );
    }

    private void moveRight() {
        v.set(v0);
    }

    private void moveLeft() {
        v.set(v0).rotate(180);
    }

    private void stop() {
        v.setZero();
    }

    public void startNewGameScreen(Rect worldBounds) {
        flushDestroy();
        hp = startHp;
        v0 = startSpeed;
        damage = startDamage;
        reloadInterval = startReloadInterval;
        bulletHeight = startBulletHeight;
        switchBullet = 1;
        pressedLeft = false;
        pressedRight = false;
        leftPointer = INVALID_POINTER;
        rightPointer = INVALID_POINTER;
        stop();
        pos.x = worldBounds.pos.x;
    }

    @Override
    public void setHp(int hp) {
        if (this.hp <= startHp) {
            if (this.hp + hp >= startHp) {
                this.hp = startHp;
            } else {
                this.hp += hp;
            }
        }
    }

    @Override
    public void setV0(float v0) {
        if (this.v0.x < startSpeed.x * 2)
            this.v0.x += v0;
    }

    public int getSpeed() {
        return (int) (v0.x * 100f);
    }

    public int getReload() {
        return (int) (reloadInterval * 100f);
    }

    @Override
    public void setReloadInterval(float interval) {
        if (reloadInterval >= 0.16f)
            reloadInterval -= interval;
    }

    @Override
    public void setDamage(int damage) {
        if (this.damage < maxDamage) {
            this.damage += damage;
            bulletHeight += (float) damage / 300;
        }
    }

    public void switchBullet(int doubleBullet) {
        if (this.switchBullet < 3) {
            this.switchBullet += doubleBullet;
            shotSoundVolume = startShotSoundVolume/switchBullet;
        }
    }
}
