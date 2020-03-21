package ru.abramov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background {
    Texture texture;

    public Background() {
        texture = new Texture("background.jpg");

    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, -100, 0,1080,1920);
    }


}
