package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exeption.GameExaption;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.base.BaseScreen;

public class MenuScreen extends BaseScreen {


    private Vector2 pos;
    private Texture img;
    private Texture bg;
    private Background background;

    @Override
    public void show() {
        super.show();
        img = new Texture("menuScreen.png");
        bg =new Texture("background.jpg");
        try {
            background = new Background(bg);
        } catch (GameExaption e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        pos = new Vector2();
    }

    @Override
    public void render(float deltatime) {
        update(deltatime);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        pos.set(touch);
        return false;
    }

    private void update(float deltatime) {
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.drow(batch);
        batch.draw(img, pos.x, pos.y, 0.2f, 0.2f);
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
        bg.dispose();
        super.dispose();
    }
}
