package ru.abramov.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;

public class GameWin extends Sprite {

    public GameWin() throws GameException {
        super(new TextureRegion(new Texture("win.png")));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.2f);
    }
}
