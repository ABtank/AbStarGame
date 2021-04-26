package ru.abramov.sprites;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.abramov.base.ScaleButton;
import ru.abramov.exception.GameException;
import ru.abramov.math.Rect;
import ru.abramov.screen.MenuGym;

public class ButtonMenu extends ScaleButton {
    MenuGym menuGym;

    public ButtonMenu(TextureAtlas atlas,MenuGym menuGym) throws GameException {
        super(atlas.findRegion("play"));
        this.menuGym = menuGym;
    }

    public void resize(Rect worldBounds) {
        setHeightProportion(0.1f);
        setLeft(worldBounds.getLeft() + 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void update(float delta) {
        animateTimer += delta;
        if (animateTimer < ANIMATE_INTERVAL) {
            return;
        }
        animateTimer = 0f;
        if (isGrow) {
            scale += delta;
            if (scale >= MAX_SCALE) {
                scale = MAX_SCALE;
                isGrow = false;
            }
        } else {
            scale -= delta;
            if (scale <= MIN_SCALE) {
                scale = MIN_SCALE;
                isGrow = true;
            }
        }
    }

    @Override
    public void action() {
        menuGym.startMenuGym();
    }
}
