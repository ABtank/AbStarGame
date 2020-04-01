package ru.abramov.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;

public class Background extends Sprite {

    public Background(Texture texture) throws GameException {
        super(new TextureRegion(texture));
        angle = 90f;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(1f);
        pos.set(worldBounds.pos);
    }

}
