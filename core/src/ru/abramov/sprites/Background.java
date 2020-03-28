package ru.abramov.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.abramov.base.Sprite;
import ru.abramov.exeption.GameExaption;
import ru.abramov.math.Rect;

public class Background extends Sprite {
    Texture texture;

    public Background(Texture texture) throws GameExaption {
        super(new TextureRegion(texture));
        texture = new Texture("background.jpg");

    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(1f);
        pos.set(worldBounds.pos);
    }

}
