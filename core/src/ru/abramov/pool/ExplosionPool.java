package ru.abramov.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.SpritesPool;
import ru.abramov.exception.GameException;
import ru.abramov.sprites.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    private  final TextureAtlas atlas;
    private Sound explosionSound;

    public ExplosionPool(TextureAtlas atlas, Sound explosionSound) {
        this.atlas = atlas;
        this.explosionSound = explosionSound;
    }

    @Override
    protected Explosion newObject(){
        try {
            return new Explosion(atlas, explosionSound);
        } catch (GameException e) {
            throw new RuntimeException("Не удалось получить текстуру взрыва");
        }
    }
}
