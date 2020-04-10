package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import ru.abramov.base.BaseScreen;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.EnemyPool;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.Enemy;
import ru.abramov.sprites.Hero;
import ru.abramov.sprites.Star;
import ru.abramov.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private Hero hero;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private Star[] stars;
    private BulletPool bulletPool;

    private EnemyPool enemyPool;
    private EnemyEmitter enemyEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPool= new BulletPool();
        enemyPool = new EnemyPool(bulletPool, worldBounds);
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/piu.mp3"));
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, laserSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/song.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
    }

    @Override
    public void render(float deltatime) {
        super.render(deltatime);
        update(deltatime);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        hero.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        hero.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        hero.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        hero.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        hero.touchUp(touch, pointer, button);
        return super.touchUp(touch, pointer, button);
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
            hero = new Hero(atlas,bulletPool,bulletSound);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update (float deltatime){
        for (Star star : stars) {
            star.update(deltatime);
        }
        hero.update(deltatime);
        bulletPool.updateActiveSprites(deltatime);
        enemyPool.updateActiveSprites(deltatime);
        enemyEmitter.generate(deltatime);
    }

    private void checkCollisions() {
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + hero.getHalfWidth();
            if (hero.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
            }
        }
    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
    }

    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        hero.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        batch.end();
    }
    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        super.dispose();
    }
}
