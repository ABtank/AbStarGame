package ru.abramov.pool;

import ru.abramov.base.SpritesPool;
import ru.abramov.math.Rect;
import ru.abramov.sprites.Perk;

public class PerkPool extends SpritesPool<Perk> {

    private Rect worldBounds;

    public PerkPool(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    @Override
    protected Perk newObject() {
        return new Perk(worldBounds);
    }
}
