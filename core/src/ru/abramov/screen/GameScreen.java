package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.BaseScreen;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.Hero;
import ru.abramov.sprites.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 64;

    private TextureAtlas shipsAtlas;
    private TextureAtlas heroAtlas;
    private Hero hero;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private Star[] stars;
    private BulletPool bulletPool;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        bulletPool= new BulletPool();
        initSprites();
    }

    @Override
    public void render(float deltatime) {
        super.render(deltatime);
        update(deltatime);
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
            hero = new Hero(atlas,bulletPool);
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
    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
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
        batch.end();
    }
    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        bulletPool.dispose();
        super.dispose();
    }
}