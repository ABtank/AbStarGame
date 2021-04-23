package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import ru.abramov.base.BaseScreen;
import ru.abramov.base.Font;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.pool.BulletPool;
import ru.abramov.pool.EnemyPool;
import ru.abramov.pool.ExplosionPool;
import ru.abramov.pool.PerkPool;
import ru.abramov.sprites.Background;
import ru.abramov.sprites.Bullet;
import ru.abramov.sprites.ButtonNewGame;
import ru.abramov.sprites.Enemy;
import ru.abramov.sprites.GameOver;
import ru.abramov.sprites.GameWin;
import ru.abramov.sprites.Hero;
import ru.abramov.sprites.Logo;
import ru.abramov.sprites.Perk;
import ru.abramov.sprites.Star;
import ru.abramov.utils.EnemyEmitter;
import ru.abramov.utils.PerkEmitter;

public class GameScreen extends BaseScreen {

    private Texture lg;
    private Logo logo;

    private enum State {PLAYING, PAUSE, GAME_OVER, WIN}

    private static final int STAR_COUNT = 64;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String SCORE = "Score: ";
    private static final String RESULT_SCORE = "You score: ";
    private static final String RECORD = "Record: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private static final String FRAGS = "Frags: ";
    private static final String DAMAGE = "Damage: ";
    private static final String SPEED = "Speed: ";
    private static final String RELOAD = "Reload: ";

    private Hero hero;

    private Texture bg;
    private Background background;

    private TextureAtlas atlas;
    private TextureAtlas perksAtlas;
    private Star[] stars;
    private GameOver gameOver;
    private GameWin gameWin;
    private ButtonNewGame buttonNewGame;

    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private PerkPool perkPool;

    private EnemyEmitter enemyEmitter;
    private PerkEmitter perkEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private State state;
    private State prevState;

    private Font font;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;
    private StringBuilder sbScore;
    private StringBuilder sbResultScore;
    private StringBuilder sbRecord;
    private StringBuilder sbDamage;
    private StringBuilder sbSpeed;
    private StringBuilder sbReload;

    private int frags;
    private int score;
    private int record;
    private int win = 3000;

    @Override
    public void show() {
        super.show();
        bg = new Texture("background.jpg");
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        perksAtlas = new TextureAtlas(Gdx.files.internal("textures/perks/perks.pack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/piu.mp3"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        lg = new Texture("menuScreen.png");
        bulletPool = new BulletPool();
        perkPool = new PerkPool(worldBounds);
        explosionPool = new ExplosionPool(atlas, explosionSound);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, enemyPool, worldBounds, laserSound);
        perkEmitter = new PerkEmitter(perksAtlas, perkPool, worldBounds);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbScore = new StringBuilder();
        sbLevel = new StringBuilder();
        sbHP = new StringBuilder();
        sbDamage = new StringBuilder();
        sbSpeed = new StringBuilder();
        sbReload = new StringBuilder();
        sbRecord = new StringBuilder();
        sbResultScore = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/song.mp3"));
        music.setLooping(true);
        music.setVolume(0.1f);
        music.play();
        initSprites();
        state = State.PLAYING;
        prevState = State.PLAYING;
        frags = 0;
        score = 0;
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
        gameWin.resize(worldBounds);
        logo.resize(worldBounds);
        logo.setHeightProportion(0.25f);
        logo.setTop(0.4f);
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
        } else if (state == State.GAME_OVER || state == State.WIN) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            hero.touchUp(touch, pointer, button);
            return super.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER || state == State.WIN) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void initSprites() {
        try {
            background = new Background(bg);
            stars = new Star[STAR_COUNT];
            logo = new Logo(lg);
            gameOver = new GameOver(atlas);
            gameWin = new GameWin();
            buttonNewGame = new ButtonNewGame(atlas, this);
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
        if (state == State.PLAYING) {
            hero.update(deltatime);
            explosionPool.updateActiveSprites(deltatime);
            bulletPool.updateActiveSprites(deltatime);
            enemyPool.updateActiveSprites(deltatime);
            perkPool.updateActiveSprites(deltatime);
            enemyEmitter.generate(deltatime, frags);
        } else if (state == State.GAME_OVER || state == State.WIN) {
            buttonNewGame.update(deltatime);
        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        if (frags % 10 == 0) {
            frags++;
            enemyEmitter.setGenerateInterval(0.2f);
        }
        List<Enemy> enemyList = enemyPool.getActiveObjects();
        List<Bullet> bulletList = bulletPool.getActiveObjects();
        List<Perk> perks = perkPool.getActiveObjects();

        for (Enemy enemy : enemyList) {
            if (enemy.isDestroyed()) {
                continue;
            }
            float minDist = enemy.getHalfWidth() + hero.getHalfWidth();
            if (hero.pos.dst(enemy.pos) < minDist) {
                enemy.destroy();
                frags++;
                score += enemy.getDamage();
                perkEmitter.generate(enemy);
                if (enemyEmitter.getLevel() > win) {
                    state = State.WIN;
                }
                hero.damage(enemy.getDamage());
            }
            for (Bullet bullet : bulletList) {
                if (bullet.getOwner() != hero || bullet.isDestroyed()) {
                    continue;
                }
                if (enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                    if (enemy.isDestroyed()) {
                        frags++;
                        score += enemy.getDamage();
                        perkEmitter.generate(enemy);
                        if (enemyEmitter.getLevel() > win) {
                            state = State.WIN;
                        }
                    }
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

        for (Perk perk : perks) {
            if (perk.isDestroyed()) {
                continue;
            }
            float minDist = perk.getHalfWidth() + hero.getHalfWidth();
            if (hero.pos.dst(perk.pos) < minDist) {
                switch (perk.effect) {
                    case 1:
                        hero.setDamage(perk.bonus);
                        break;
                    case 3:
                        hero.switchBullet(perk.bonus);
                        break;
                    case 4:
                        hero.setHp(perk.bonus);
                        break;
                    case 5:
                        hero.setV0(perk.fbonus);
                        break;
                    case 7:
                        hero.setReloadInterval(perk.fbonus);
                        break;
                }
                perk.destroy();
            }
        }
        if (state == State.GAME_OVER || state == State.WIN) {
            writeScoreToFile(score);
            return;
        }
    }

    public void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
        perkPool.freeAllDestroyedActiveObjects();
    }

    private void writeScoreToFile(int score) {
        record();
        try (FileWriter out = new FileWriter("score.txt", true)) {
            out.write(score + "\n");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void record() {
        try (FileReader in = new FileReader("score.txt");
             BufferedReader br = new BufferedReader((in))) {
            String line;
            int max = Integer.MIN_VALUE;
            while ((line = br.readLine()) != null) {
                if (Integer.parseInt(line) > max) {
                    max = Integer.parseInt(line);
                }
            }
            record = max;
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                explosionPool.drawActiveSprites(batch);
                perkPool.drawActiveSprites(batch);
                break;
            case GAME_OVER:
                logo.draw(batch);
                gameOver.draw(batch);
                buttonNewGame.draw(batch);
                break;
            case WIN:
                logo.draw(batch);
                gameWin.draw(batch);
                buttonNewGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbScore.setLength(0);
        sbResultScore.setLength(0);
        sbRecord.setLength(0);
        sbFrags.setLength(0);
        sbLevel.setLength(0);
        sbHP.setLength(0);
        sbDamage.setLength(0);
        sbSpeed.setLength(0);
        sbReload.setLength(0);
        font.draw(batch, sbScore.append(SCORE).append(score), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getBottom() + FONT_MARGIN * 3);
        font.draw(batch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
        font.draw(batch, sbHP.append(HP).append(hero.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        font.draw(batch, sbDamage.append(DAMAGE).append(hero.getDamage()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getBottom() + FONT_MARGIN * 3, Align.right);
        font.draw(batch, sbSpeed.append(SPEED).append(hero.getSpeed()), worldBounds.pos.x - 0.15f, worldBounds.getBottom() + FONT_MARGIN * 3);
        font.draw(batch, sbReload.append(RELOAD).append(hero.getReload()), worldBounds.pos.x, worldBounds.getBottom() + FONT_MARGIN * 3);
        if (state == State.GAME_OVER || state == State.WIN) {
            if (score > record) {
                font.draw(batch, sbRecord.append("THIS NEW ").append(RECORD).append(score), worldBounds.pos.x, worldBounds.pos.y + 0.1f, Align.center);
            } else {
                font.draw(batch, sbResultScore.append(RESULT_SCORE).append(score), worldBounds.pos.x, worldBounds.pos.y + 0.15f, Align.center);
                font.draw(batch, sbRecord.append(RECORD).append(record), worldBounds.pos.x, worldBounds.pos.y + 0.1f, Align.center);
            }
        }

    }

    public void startNewGameScreen() {
        state = State.PLAYING;
        hero.startNewGameScreen(worldBounds);
        frags = 0;
        score = 0;
        enemyEmitter.startNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        perkPool.freeAllActiveObjects();
    }

    @Override
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
        music.play();
    }

    @Override
    public void dispose() {
        bg.dispose();
        lg.dispose();
        font.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        perkPool.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosionSound.dispose();
        super.dispose();
    }
}
