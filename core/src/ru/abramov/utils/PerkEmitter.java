package ru.abramov.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import ru.abramov.math.Rect;
import ru.abramov.pool.PerkPool;
import ru.abramov.sprites.Enemy;
import ru.abramov.sprites.Perk;

public class PerkEmitter {

    private static final float PERK_HEIGHT = 0.06f;

    private static final int PERK_HP = 10;
    private static final int PERK_DAMAGE = 1;
    private static final float PERK_SPEED = 0.02f;
    private static final float PERK_RELOAD = 0.02f;
    private static final int PERK_SPLIT_BULLET = 1;
    private static final int PERK_DEFAULT = 0;
    private static final Random random = new Random();

    private final TextureRegion[] perks;

    private Vector2 perkV;

    private Rect worldBounds;
    private PerkPool perkPool;


    public PerkEmitter(TextureAtlas atlas, PerkPool perkPool, Rect worldBounds) {
        this.worldBounds = worldBounds;
        this.perkPool = perkPool;
        TextureRegion regionPerks = atlas.findRegion("all-perks25");
        this.perks = Regions.split(regionPerks, 2, 5, 10);
        this.perkV = new Vector2(0, -0.2f);
    }

    public void generate(Enemy enemy) {
        float chance = (float) Math.random();
        int framePerk = random.nextInt(10);
        System.out.println("perk= " + framePerk + "\nchance= " + chance);
        if (chance > 0.1f) {
            Perk perk = perkPool.obtain();
            switch (framePerk) {
                case 1:
                    perk.set(perks, PERK_DAMAGE, framePerk, PERK_HEIGHT);
                    break;
                case 3:
                    perk.set(perks, PERK_SPLIT_BULLET, framePerk, PERK_HEIGHT);
                    break;
                case 4:
                    perk.set(perks, PERK_HP, framePerk, PERK_HEIGHT);
                    break;
                case 5:
                    perk.set(perks, PERK_SPEED, framePerk, PERK_HEIGHT);
                    break;
                case 7:
                    perk.set(perks, PERK_RELOAD, framePerk, PERK_HEIGHT);
                    break;
                default:
                    perk.set(perks, PERK_DEFAULT, framePerk, 0);
                    break;
            }
            perk.pos.set(enemy.pos);
        }
    }
}

