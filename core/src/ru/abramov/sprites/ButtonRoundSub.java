package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.ScaleButton;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.screen.MenuGym;

public class ButtonRoundSub extends ScaleButton {
MenuGym menuGym;

    public ButtonRoundSub(TextureAtlas atlas, MenuGym menuGym) throws GameException {
        super(atlas.findRegion("black_arrow"));
        angle = 180;
        this.menuGym = menuGym;
    }

    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setRight(worldBounds.pos.x - 0.12f);
        setBottom(worldBounds.pos.y - 0.23f);
    }

    @Override
    public void action() {
        menuGym.setEndTrain(-1);
    }
}