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
import ru.abramov.math.Rnd;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.EnemyPool;
import ru.abramov.pool.ExplosionPool;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.Bullet;
import ru.abramov.sprites.Enemy;
import ru.abramov.sprites.GameOver;
import ru.abramov.sprites.Hero;
import ru.abramov.sprites.ButtonNewGame;
import ru.abramov.sprites.Star;
import ru.abramov.utils.EnemyEmitter;

public class GameScreen extends BaseScreen {

    private enum State {PLAYING, PAUSE, GAME_OVER, WIN}

    private static final int STAR_COUNT = 64;

    private Hero hero;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private Star[] stars;
    private GameOver gameOver;
    private ButtonNewGame buttonNewGame;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;

    private EnemyEmitter enemyEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private State state;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/piu.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, laserSound);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/song.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        initSprites();
        state = State.PLAYING;
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
        gameOver.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            hero.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            hero.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            hero.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            hero.touchUp(touch, pointer, button);
            return super.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            gameOver = new GameOver(atlas);
            buttonNewGame = new ButtonNewGame(atlas,this);
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            hero = new Hero(atlas, bulletPool, explosionPool, bulletSound);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float deltatime) {
        for (Star star : stars) {
            star.update(deltatime);
        }
        explosionPool.updateActiveSprites(deltatime);
        if (state == State.PLAYING) {
            hero.update(deltatime);
            bulletPool.updateActiveSprites(deltatime);
            enemyPool.updateActiveSprites(deltatime);
            enemyEmitter.generate(deltatime);
        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + hero.getHalfWidth();
            if (hero.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
                hero.damage(enemy.getDamage());
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != hero || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
            if (hero.isDestroyed()) {
                state = State.GAME_OVER;
            }
        }
        for (Bullet bullet : bulletList) {
            if (bullet.getOwner() == hero || bullet.isDestroyed()) {
                continue;
            }
            if (hero.isBulletCollision(bullet)) {
                hero.damage(bullet.getDamage());
                bullet.destroy();
            }
        }
    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        switch (state) {
            case PAUSE:
                System.out.println("PAUSE");
                break;
            case PLAYING:
                hero.draw(batch);
                bulletPool.drawActiveSprites(batch);
                enemyPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                gameOver.draw(batch);
                buttonNewGame.draw(batch);
                System.out.println("GAME_OVER");
                break;
        }
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }

    public void startNewGameScreen(){
        state = State.PLAYING;
        bulletPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        hero.startNewGameScreen();
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        for (Enemy enemy : enemyList) {
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
            enemy.setBottom(worldBounds.getTop());
        }

    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        super.dispose();
    }
}
