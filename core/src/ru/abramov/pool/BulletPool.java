package ru.abramov.pool;

import ru.abramov.base.SpritesPool;
import ru.abramov.sprites.Bullet;

public class BulletPool extends SpritesPool<Bullet> {

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }
}
