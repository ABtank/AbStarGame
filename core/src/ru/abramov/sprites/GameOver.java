package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.Sprite;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;

public class GameOver extends Sprite {


    public GameOver(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.08f);
        setTop(0.1f);
    }
}
