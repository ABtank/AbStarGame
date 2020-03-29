package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.exeption.GameExaption;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Background;
import ru.abramov.base.BaseScreen;
import ru.abramov.sprites.Logo;

public class MenuScreen extends BaseScreen {


    private Texture bg;
    private Texture lg;
    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        lg = new Texture("menuScreen.png");
        bg =new Texture("background.jpg");
        try {
            background = new Background(bg);
            logo = new Logo(lg);
        } catch (GameExaption e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        logo.touchDown(touch,pointer,button);
        return false;
    }

    private void update(float deltatime) {
        logo.update(deltatime);
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        logo.draw(batch);
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        lg.dispose();
        bg.dispose();
        super.dispose();
    }
}
