package ru.abramov.sprites;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.ScaleButton;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.screen.MenuScreen;

public class ButtonMenuScreen extends ScaleButton {
    private final Game game;

    public ButtonMenuScreen(TextureAtlas atlas, Game game) throws GameException {
        super(atlas.findRegion("rollback"));
        this.game = game;
    }

    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setLeft(worldBounds.pos.x - 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void action() {
        game.setScreen( new MenuScreen(game));
    }
}
