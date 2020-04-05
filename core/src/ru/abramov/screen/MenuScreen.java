package ru.abramov.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.BaseScreen;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.ButtonExit;
import ru.abramov.sprites.ButtonPlay;
import ru.abramov.sprites.Logo;
import ru.abramov.sprites.Star;

public class MenuScreen extends BaseScreen {

    private static final int STAR_COUNT = 256;

    private final Game game;

    private Texture lg;
    private Logo logo;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;

    private Star[] stars;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;

    public MenuScreen(Game game) {
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/song.mp3"));
        this.game = game;
        music.play();
        music.setLooping(true);
    }

    @Override
    public void show() {
        super.show();
        lg = new Texture("menuScreen.png");
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        initSprites();
    }

    @Override
    public void render(float deltatime) {
        update(deltatime);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        logo.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
//        logo.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            logo = new Logo(lg);
            buttonExit = new ButtonExit(atlas);
            buttonPlay = new ButtonPlay(atlas, game);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float deltatime) {
//        logo.update(deltatime);
        for (Star star : stars) {
            star.update(deltatime);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        logo.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        lg.dispose();
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }
}
