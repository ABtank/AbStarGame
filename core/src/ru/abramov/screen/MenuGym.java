package ru.abramov.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.concurrent.TimeUnit;

import ru.abramov.base.BaseScreen;
import ru.abramov.base.Font;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.ButtonCountExerciseAdd;
import ru.abramov.sprites.ButtonCountExerciseSub;
import ru.abramov.sprites.ButtonCountSetAdd;
import ru.abramov.sprites.ButtonCountSetSub;
import ru.abramov.sprites.ButtonExit;
import ru.abramov.sprites.ButtonMenu;
import ru.abramov.sprites.ButtonMenuScreen;
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
    private ButtonMenuScreen buttonMenuScreen;

    private ButtonCountSetAdd buttonCountSetAdd;
    private ButtonCountSetSub buttonCountSetSub;
    private ButtonCountExerciseAdd buttonCountExerciseAdd;
    private ButtonCountExerciseSub buttonCountExerciseSub;
    private ButtonRoundAdd buttonRoundAdd;
    private ButtonRoundSub buttonRoundSub;
    private ButtonTimeAdd buttonTimeAdd;
    private ButtonTimeSub buttonTimeSub;

    public MenuGym(Game game) {
        this.game = game;
    }

    private enum State {PLAYING, PAUSE, SETTINGS, WIN}

    private static final int STAR_COUNT = 264;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.1f;
    private static final String SET = "Set: ";
    private static final String EX = "Ex.: ";
    private static final String ALL_SET = "All: ";
    private static final String GET_READY = "GET READY !";
    private static float setRoundTime = 120f;
    private static float countdownTime;
    private static float roundTime;
    private static float timeGetReady = 10f;
    private static int round;
    private static int exercise;
    private static int allRound;
    private static int flagsound;
    private static int setCountSet = 6;
    private static int setExerciseSet = 3;
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
    private Font fontInfo;
    private Font fontReady;
    private Font fontTime;
    private Font fontSet;
    private StringBuilder sbSet;
    private StringBuilder sbEx;
    private StringBuilder sbCountSet;
    private StringBuilder sbExerciseSet;
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

    public void setCountSet(int setCountSet) {
        if (MenuGym.setCountSet > 1 && setCountSet < 0 || setCountSet > 0) {
            MenuGym.setCountSet += setCountSet;
            MenuGym.setEndTrain = MenuGym.setCountSet * MenuGym.setExerciseSet;
        }
    }

    public void setExerciseSet(int setExerciseSet) {
        if (MenuGym.setExerciseSet > 1 && setExerciseSet < 0 || setExerciseSet > 0) {
            MenuGym.setExerciseSet += setExerciseSet;
            MenuGym.setEndTrain = MenuGym.setCountSet * MenuGym.setExerciseSet;
        }
    }

    private void foundLastSettings() {
        FileHandle file = Gdx.files.local("settings.txt");
        if (file.exists()) {
            String[] text = (file.readString()).split("\n");
            if (text.length > 0) {
                text = text[text.length - 1].split(" ");
                setRoundTime = Float.parseFloat(text[0]);
                setExerciseSet = Integer.parseInt(text[1]);
                setCountSet = Integer.parseInt(text[2]);
            }
        } else {
            file.writeString(setRoundTime + " " + setExerciseSet + " " + setCountSet + "\n", false);
        }
    }

    public static String secondsToString(long seconds) {
        if(TimeUnit.SECONDS.toHours(seconds)>0)
        {
            return  String.format("%02d:%02d:%02d",
                    TimeUnit.SECONDS.toHours(seconds),
                    TimeUnit.SECONDS.toMinutes(seconds) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)),
                    TimeUnit.SECONDS.toSeconds(seconds) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
        }
        else {
            return  String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(seconds) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds)),
                    TimeUnit.SECONDS.toSeconds(seconds) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds)));
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
        atlasArrow = new TextureAtlas((Gdx.files.internal("textures/arrow/arrows.pack")));
        fontInfo = new Font("font/font.fnt", "font/font.png");
        font = new Font("font/font.fnt", "font/font.png");
        fontTime = new Font("font/font.fnt", "font/font.png");
        fontSet = new Font("font/font.fnt", "font/font.png");
        fontReady = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE * 0.8f);
        fontInfo.setSize(FONT_SIZE * 0.2f);
        fontTime.setSize(FONT_SIZE * 1.4f);
        fontSet.setSize(FONT_SIZE * 0.4f);
        fontReady.setSize(FONT_SIZE * 0.5f);
        sbTime = new StringBuilder();
        sbSet = new StringBuilder();
        sbEx = new StringBuilder();
        sbCountSet = new StringBuilder();
        sbExerciseSet = new StringBuilder();
        sbAllSet = new StringBuilder();
        sbCountdownTime = new StringBuilder();
        initSprites();
        state = State.SETTINGS;
        prevState = State.PAUSE;
        round = 0;
        exercise = 0;
        allRound = round;
        foundLastSettings();
        setEndTrain = setCountSet * setExerciseSet;
        roundTime = timeGetReady;
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
        buttonMenuScreen.resize(worldBounds);
        buttonCountSetAdd.resize(worldBounds);
        buttonCountSetSub.resize(worldBounds);
        buttonCountExerciseAdd.resize(worldBounds);
        buttonCountExerciseSub.resize(worldBounds);
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
        } else if (state == MenuGym.State.SETTINGS || state == MenuGym.State.WIN) {
            buttonExit.touchDown(touch, pointer, button);
            buttonMenuScreen.touchDown(touch, pointer, button);
            buttonCountSetAdd.touchDown(touch, pointer, button);
            buttonCountSetSub.touchDown(touch, pointer, button);
            buttonCountExerciseAdd.touchDown(touch, pointer, button);
            buttonCountExerciseSub.touchDown(touch, pointer, button);
//            buttonRoundAdd.touchDown(touch, pointer, button);
//            buttonRoundSub.touchDown(touch, pointer, button);
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
        } else if (state == MenuGym.State.SETTINGS || state == MenuGym.State.WIN) {
            buttonExit.touchUp(touch, pointer, button);
            buttonMenuScreen.touchUp(touch, pointer, button);
            buttonCountSetAdd.touchUp(touch, pointer, button);
            buttonCountSetSub.touchUp(touch, pointer, button);
            buttonCountExerciseAdd.touchUp(touch, pointer, button);
            buttonCountExerciseSub.touchUp(touch, pointer, button);
//            buttonRoundAdd.touchUp(touch, pointer, button);
//            buttonRoundSub.touchUp(touch, pointer, button);
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
            buttonMenu = new ButtonMenu(atlasArrow, this);
            buttonExit = new ButtonExit(atlasArrow);
            buttonMenuScreen = new ButtonMenuScreen(atlasArrow, game);
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] = new Star(atlas);
            }
            buttonCountSetAdd = new ButtonCountSetAdd(atlasArrow, this);
            buttonCountSetSub = new ButtonCountSetSub(atlasArrow, this);
            buttonCountExerciseAdd = new ButtonCountExerciseAdd(atlasArrow, this);
            buttonCountExerciseSub = new ButtonCountExerciseSub(atlasArrow, this);
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
        } else if (state == MenuGym.State.SETTINGS || state == MenuGym.State.WIN) {
            // buttonNewGame.update(deltatime);
            buttonCountSetAdd.update(deltatime);
            buttonCountSetSub.update(deltatime);
            buttonCountExerciseAdd.update(deltatime);
            buttonCountExerciseSub.update(deltatime);
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
            case SETTINGS:
                buttonExit.draw(batch);
                buttonMenuScreen.draw(batch);
                buttonCountSetAdd.draw(batch);
                buttonCountSetSub.draw(batch);
                buttonCountExerciseAdd.draw(batch);
                buttonCountExerciseSub.draw(batch);
//                buttonRoundAdd.draw(batch);
//                buttonRoundSub.draw(batch);
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
        sbEx.setLength(0);
        sbAllSet.setLength(0);
        sbCountdownTime.setLength(0);
        float d = 2.0f;
        String strRoundTime = secondsToString((long)(roundTime));
        String allTime = secondsToString((long)(countdownTime));

        fontTime.draw(batch, sbTime.append(strRoundTime), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / d, Align.center);

        if (allRound != 0) {
            font.draw(batch, sbCountdownTime.append(allTime), worldBounds.pos.x, worldBounds.getTop(), Align.center);
        } else {
            fontReady.draw(batch, sbCountdownTime.append(GET_READY), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN * 10, Align.center);
        }
        fontSet.draw(batch, sbEx.append(EX).append(exercise).append("/").append(setExerciseSet), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 6, Align.center);
        font.draw(batch, sbSet.append(SET).append(round).append("/").append(setCountSet), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y - FONT_MARGIN, Align.center);
        font.draw(batch, sbAllSet.append(ALL_SET).append(allRound).append("/").append(setEndTrain), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y - FONT_MARGIN * 14, Align.center);
    }

    private void printSet() {
        sbTime.setLength(0);
        sbCountSet.setLength(0);
        sbExerciseSet.setLength(0);
        sbAllSet.setLength(0);
        String allTime = secondsToString((long)(setEndTrain * setRoundTime));
        String strRoundTime = secondsToString((long)(setRoundTime));

        fontSet.draw(batch, sbTime.append(strRoundTime), worldBounds.pos.x - FONT_MARGIN, worldBounds.getTop() / 1.95f + FONT_MARGIN, Align.center);
        fontInfo.draw(batch, "Round time", worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 30, Align.center);
        fontInfo.draw(batch, "Exercise", worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 17, Align.center);
        fontSet.draw(batch, sbExerciseSet.append(setExerciseSet), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 13, Align.center);
        fontInfo.draw(batch, "Set", worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 4, Align.center);
        fontSet.draw(batch, sbCountSet.append(setCountSet), worldBounds.pos.x - FONT_MARGIN, worldBounds.pos.y + FONT_MARGIN * 0.9f, Align.center);
        fontSet.draw(batch, sbAllSet.append("Total: ").append(setEndTrain).append(" (").append(allTime).append(")"), worldBounds.pos.x, worldBounds.pos.y - FONT_MARGIN * 16, Align.center);
    }

    private void startRound() {
        if (roundTime <= 0) {
            roundTime = setRoundTime;
            roundAdd();
        }
    }

    private void playCountdown() {
        if (roundTime - setCountSet <= 0 && flagsound == 0) {
            soundCountdownTime.play();
            flagsound = 1;
        } else if (roundTime - 1 <= 0 && flagsound == 1) {
            soundTimeEnd.play();
            flagsound = 2;
        }
    }

    private void roundAdd() {
        flagsound = 0;
        if (round == 0 && exercise == 0) {
            exercise++;
        }
        round++;
        allRound++;
        if (round == (setCountSet - 1)) {
            soundAreYouReady.play();
        }
        if (round == setCountSet) {
            soundEndSet.play();
            round = 0;
            exercise++;
        }
        roundTime = setRoundTime;
        if (allRound == setEndTrain) {
            state = State.SETTINGS;
        }
    }

    public void startMenuGym() {
        if (state == State.SETTINGS) {
            state = MenuGym.State.PLAYING;
            countdownTime = (setEndTrain - 1) * setRoundTime + timeGetReady;
            Gdx.files.local("settings.txt").writeString(setRoundTime + " " + setExerciseSet + " " + setCountSet + "\n", false);
        } else {
            state = State.SETTINGS;
        }
        round = 0;
        allRound = round;
        roundTime = timeGetReady;
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
