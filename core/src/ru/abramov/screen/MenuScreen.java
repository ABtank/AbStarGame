package ru.abramov.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import ru.abramov.Background;
import ru.abramov.base.BaseScreen;

public class MenuScreen extends BaseScreen {


    private Vector2 pos;
    private Vector2 point;
    private Texture img;
    private Background background;
    private float speed=5f;

    @Override
    public void show() {
        super.show();
        img = new Texture("menuScreen.png");
        background = new Background();
        pos = new Vector2();
        point = new Vector2();
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        point.set(screenX, Gdx.graphics.getHeight() - screenY);
        return false;
    }

    private void update(float deltatime) {
            if (point.x < pos.x) {
                pos.x -= speed;
            }
            if (point.x > pos.x) {
                pos.x += speed;
            }
            if (point.y > pos.y) {
                pos.y += speed;
            }
            if (point.y < pos.y) {
                pos.y -= speed;
            }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        background.render(spriteBatch);
        spriteBatch.draw(img, pos.x, pos.y);
        spriteBatch.end();
    }


    @Override
    public void dispose() {
        spriteBatch.dispose();
        img.dispose();
        super.dispose();
    }
}
