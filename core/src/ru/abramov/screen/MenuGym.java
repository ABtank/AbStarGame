package ru.abramov.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.abramov.base.BaseScreen;
import ru.abramov.base.Font;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.ButtonExit;
import ru.abramov.sprites.ButtonMenu;
import ru.abramov.sprites.ButtonRoundAdd;
import ru.abramov.sprites.ButtonRoundSub;
import ru.abramov.sprites.ButtonTimeAdd;
import ru.abramov.sprites.ButtonTimeSub;
import ru.abramov.sprites.Logo;
import ru.abramov.sprites.Star;

public class MenuGym extends BaseScreen {
    private Texture lg;
    private Logo logo;
    private final Game game;
    private ButtonMenu buttonMenu;
    private ButtonExit buttonExit;

    private ButtonRoundAdd buttonRoundAdd;
    private ButtonRoundSub buttonRoundSub;
    private ButtonTimeAdd buttonTimeAdd;
    private ButtonTimeSub buttonTimeSub;

    public MenuGym(Game game) {
        this.game = game;
    }

    private enum State {PLAYING, PAUSE, GAME_OVER, WIN}

    private static final int STAR_COUNT = 264;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.1f;
    private static final float FONT_SIZE_TIME = 0.14f;
    private static final float FONT_SIZE_SET = 0.04f;
    private static final String SET = "Set: ";
    private static final String ALL_SET = "All: ";
    private static final String GET_READY = "GET READY !";
    private static float setRoundTime = 120f;
    private static float countdownTime;
    private static float roundTime;
    private static int round;
    private static int allRound;
    private static int flagsound;
    private static int setEndTrain;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private TextureAtlas atlasArrow;

    private Star[] stars;

    private Sound soundCountdownTime;
    private Sound soundTimeEnd;
    private Sound soundEndSet;
    private Sound soundAreYouReady;

    private MenuGym.State state;
    private MenuGym.State prevState;

    private Font font;
    private Font fontReady;
    private Font fontTime;
    private Font fontSet;
    private StringBuilder sbSet;
    private StringBuilder sbAllSet;
    private StringBuilder sbTime;
    private StringBuilder sbCountdownTime;

    public void setRoundTime(float setRoundTime) {
        if (MenuGym.setRoundTime > 15 && setRoundTime < 0 || setRoundTime > 0) {
            MenuGym.setRoundTime += setRoundTime;
        }
    }

    public void setEndTrain(int setEndTrain) {
        if (MenuGym.setEndTrain > 1 && setEndTrain < 0 || setEndTrain > 0) {
            MenuGym.setEndTrain += setEndTrain;
        }
    }

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        soundCountdownTime = Gdx.audio.newSound(Gdx.files.internal("sounds/elearning-clock-ticking.mp3"));
        soundTimeEnd = Gdx.audio.newSound(Gdx.files.internal("sounds/ringRingRing.mp3"));
        soundEndSet = Gdx.audio.newSound(Gdx.files.internal("sounds/yxyy.mp3"));
        soundAreYouReady = Gdx.audio.newSound(Gdx.files.internal("sounds/holy_shit_unreal_tournamen.mp3"));
        lg = new Texture("menuScreen.png");
        atlasArrow = new TextureAtlas((Gdx.files.internal("textures/arrow/BlackArrows.pack")));
        font = new Font("font/font.fnt", "font/font.png");
        fontTime = new Font("font/font.fnt", "font/font.png");
        fontSet = new Font("font/font.fnt", "font/font.png");
        fontReady = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE*0.8f);
        fontTime.setSize(FONT_SIZE_TIME);
        fontSet.setSize(FONT_SIZE_SET);
        fontReady.setSize(FONT_SIZE*0.5f);
        sbTime = new StringBuilder();
        sbSet = new StringBuilder();
        sbAllSet = new StringBuilder();
        sbCountdownTime = new StringBuilder();
        initSprites();
        state = State.GAME_OVER;
        prevState = MenuGym.State.PLAYING;
        round = 0;
        allRound = round;
        setEndTrain = 18;
        roundTime = 10;
        flagsound = 0;
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
        logo.resize(worldBounds);
        logo.setHeightProportion(0.25f);
        logo.setTop(0.4f);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        buttonMenu.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonRoundAdd.resize(worldBounds);
        buttonRoundSub.resize(worldBounds);
        buttonTimeAdd.resize(worldBounds);
        buttonTimeSub.resize(worldBounds);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (state == MenuGym.State.PLAYING) {

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == MenuGym.State.PLAYING) {

        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonMenu.touchDown(touch, pointer, button);
        if (state == MenuGym.State.PLAYING) {
        } else if (state == MenuGym.State.GAME_OVER || state == MenuGym.State.WIN) {
            buttonExit.touchDown(touch, pointer, button);
            buttonRoundAdd.touchDown(touch, pointer, button);
            buttonRoundSub.touchDown(touch, pointer, button);
            buttonTimeSub.touchDown(touch, pointer, button);
            buttonTimeAdd.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonMenu.touchUp(touch, pointer, button);
        if (state == MenuGym.State.PLAYING) {
            return super.touchUp(touch, pointer, button);
        } else if (state == MenuGym.State.GAME_OVER || state == MenuGym.State.WIN) {
            buttonExit.touchUp(touch, pointer, button);
            buttonRoundAdd.touchUp(touch, pointer, button);
            buttonRoundSub.touchUp(touch, pointer, button);
            buttonTimeAdd.touchUp(touch, pointer, button);
            buttonTimeSub.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            logo = new Logo(lg);
            buttonMenu = new ButtonMenu(atlas, this);
            buttonExit = new ButtonExit(atlas);
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            buttonRoundAdd = new ButtonRoundAdd(atlasArrow, this);
            buttonRoundSub = new ButtonRoundSub(atlasArrow, this);
            buttonTimeAdd = new ButtonTimeAdd(atlasArrow, this);
            buttonTimeSub = new ButtonTimeSub(atlasArrow, this);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float deltatime) {
        for (Star star : stars) {
            star.update(deltatime);
        }
        if (state == MenuGym.State.PLAYING) {
            roundTime -= deltatime;
            countdownTime -= deltatime;
            startRound();
            playCountdown();
        } else if (state == MenuGym.State.GAME_OVER || state == MenuGym.State.WIN) {
            // buttonNewGame.update(deltatime);
            buttonRoundAdd.update(deltatime);
            buttonRoundSub.update(deltatime);
            buttonTimeAdd.update(deltatime);
            buttonTimeSub.update(deltatime);
        }
    }


    public void freeAllDestroyed() {

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
                printInfo();
                break;
            case GAME_OVER:
                buttonExit.draw(batch);
                buttonRoundAdd.draw(batch);
                buttonRoundSub.draw(batch);
                buttonTimeAdd.draw(batch);
                buttonTimeSub.draw(batch);
                printSet();
                break;
            case WIN:
                logo.draw(batch);

        }

        buttonMenu.draw(batch);
        batch.end();
    }

    private void printInfo() {
        sbTime.setLength(0);
        sbSet.setLength(0);
        sbAllSet.setLength(0);
        sbCountdownTime.setLength(0);
        int min = ((int) roundTime) / 60;
        int sec = ((int) roundTime) % 60;
        int allMin = ((int) countdownTime) / 60;
        int allSec = ((int) countdownTime) % 60;
        if (min < 10 && sec < 10) {
            fontTime.draw(batch, sbTime.append(0).append(min).append(":").append(0).append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 2, Align.center);
        } else if (min >= 10 && sec <= 10) {
            fontTime.draw(batch, sbTime.append(min).append(":").append(0).append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 2, Align.center);
        } else if (min <= 10 && sec >= 10) {
            fontTime.draw(batch, sbTime.append(0).append(min).append(":").append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 2, Align.center);
        } else {
            fontTime.draw(batch, sbTime.append(min).append(":").append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 2, Align.center);
        }
        if (allRound != 0) {
            font.draw(batch, sbCountdownTime.append(allMin).append(":").append(allSec), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        }else{
            fontReady.draw(batch, sbCountdownTime.append(GET_READY), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN*10, Align.center);
        }
        font.draw(batch, sbSet.append(SET).append(round), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y, Align.center);
        font.draw(batch, sbAllSet.append(ALL_SET).append(allRound).append("/").append(setEndTrain), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y - FONT_MARGIN * 14, Align.center);
    }

    private void printSet() {
        sbTime.setLength(0);
        sbSet.setLength(0);
        sbAllSet.setLength(0);
        int min = ((int) setRoundTime) / 60;
        int sec = ((int) setRoundTime) % 60;
        if (min < 10 && sec < 10) {
            fontSet.draw(batch, sbTime.append(0).append(min).append(":").append(0).append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 3 + FONT_MARGIN, Align.center);
        } else if (min >= 10 && sec <= 10) {
            fontSet.draw(batch, sbTime.append(min).append(":").append(0).append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 3 + FONT_MARGIN, Align.center);
        } else if (min <= 10 && sec >= 10) {
            fontSet.draw(batch, sbTime.append(0).append(min).append(":").append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 3 + FONT_MARGIN, Align.center);
        } else {
            fontSet.draw(batch, sbTime.append(min).append(":").append(sec), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 3 + FONT_MARGIN, Align.center);
        }
        fontSet.draw(batch, sbAllSet.append(setEndTrain), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y - FONT_MARGIN*3, Align.center);
    }

    private void startRound() {
        if (roundTime <= 0) {
            roundTime = setRoundTime;
            roundAdd();
        }
    }

    private void playCountdown() {
        if (roundTime - 6 <= 0 && flagsound == 0) {
            soundCountdownTime.play();
            flagsound = 1;
        } else if (roundTime - 1 <= 0 && flagsound == 1) {
            soundTimeEnd.play();
            flagsound = 2;
        }
    }

    private void roundAdd() {
        flagsound = 0;
        round++;
        allRound++;
        if (round == 4) {
            soundAreYouReady.play();
        }
        if (round == 6) {
            soundEndSet.play();
            round = 0;
        }
        roundTime = setRoundTime;
        if (allRound == setEndTrain) {
            state = State.GAME_OVER;
        }
    }

    public void startMenuGym() {
        if (state == State.GAME_OVER) {
            state = MenuGym.State.PLAYING;
            countdownTime=(setEndTrain-1)*setRoundTime+10;
        } else {
            state = State.GAME_OVER;
        }
        round = 0;
        allRound = round;
        roundTime = 10;
        flagsound = 0;
    }

    @Override
    public void pause() {
        prevState = state;
        state = MenuGym.State.PAUSE;

    }

    @Override
    public void resume() {
        state = prevState;
    }

    @Override
    public void dispose() {
        bg.dispose();
        lg.dispose();
        font.dispose();
        fontTime.dispose();
        fontSet.dispose();
        fontReady.dispose();
        atlas.dispose();
        atlasArrow.dispose();
        soundCountdownTime.dispose();
        soundTimeEnd.dispose();
        soundEndSet.dispose();
        super.dispose();
    }
}
