package ru.abramov.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.abramov.base.ScaleButton;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.screen.GameScreen;

public class ButtonPlay extends ScaleButton {

    private final Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) throws GameException {
        super(atlas.findRegion("btPlay"));
        this.game = game;
    }

    public void resize(Rect worldBounds) {
        setHeightProportion(0.25f);
        setLeft(worldBounds.getLeft() + 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }
}
