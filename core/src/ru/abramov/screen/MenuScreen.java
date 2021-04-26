package ru.abramov.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.BaseScreen;
import ru.abramov.base.Font;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.ButtonExit;
import ru.abramov.sprites.ButtonGym;
import ru.abramov.sprites.ButtonPlay;
import ru.abramov.sprites.Star;

public class MenuScreen extends BaseScreen {

    private static final int STAR_COUNT = 256;
    private static final float FONT_SIZE = 0.06f;

    private Font font;
    private StringBuilder sbAllRounds;
    private StringBuilder sbTime;

    private final Game game;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private TextureAtlas atlasArrow;

    private Star[] stars;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private ButtonGym buttonGym;

    public MenuScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        atlasArrow = new TextureAtlas((Gdx.files.internal("textures/arrow/arrows.pack")));
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbAllRounds = new StringBuilder();
        sbTime = new StringBuilder();
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
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        buttonGym.resize(worldBounds);

    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        buttonGym.touchDown(touch, pointer, button);

        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        buttonGym.touchUp(touch, pointer, button);

        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            buttonExit = new ButtonExit(atlasArrow);
            buttonPlay = new ButtonPlay(atlas, game);
            buttonGym = new ButtonGym(atlasArrow, game);

        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float deltatime) {
//        logo.update(deltatime);
        for (Star star : stars) {
            star.update(deltatime);
        }
        buttonPlay.update(deltatime);
        buttonGym.update(deltatime);

    }


    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (Star star : stars) {
            star.draw(batch);
        }
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        buttonGym.draw(batch);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bg.dispose();
        atlas.dispose();

        super.dispose();
    }
}
