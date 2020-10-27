package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.ScaleButton;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.screen.MenuGym;

public class ButtonTimeAdd extends ScaleButton {
    MenuGym menuGym;

    public ButtonTimeAdd(TextureAtlas atlas,MenuGym menuGym) throws GameException {
        super(atlas.findRegion("left"));
        angle = 180;
        this.menuGym = menuGym;
    }

    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setRight(worldBounds.pos.x + 0.2f);
        setBottom(worldBounds.pos.y + 0.1f);
    }

    @Override
    public void action() {
        menuGym.setRoundTime(15f);
    }
}
