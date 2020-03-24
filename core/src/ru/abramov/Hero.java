package ru.abramov;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Hero {
    Texture heroBlue;
    Texture heroRed;
    Texture heroGreen;
    Texture heroGolden;

    public Hero() {

        heroBlue = new Texture("heroBlue.png");
        heroGreen = new Texture("heroGreen.png");
        heroRed = new Texture("heroRed.png");
        heroGolden = new Texture("heroGolden.png");
    }

    public void render(SpriteBatch batch){
        batch.draw(heroBlue, 400, 0);
        batch.draw(heroGolden, 200, 0);
        batch.draw(heroGreen, 0, 0);
        batch.draw(heroRed, 200, 200);
    }
}
