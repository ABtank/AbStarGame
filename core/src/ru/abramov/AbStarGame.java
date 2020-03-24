package ru.abramov;

import com.badlogic.gdx.Game;

import ru.abramov.screen.MenuScreen;

public class AbStarGame extends Game {

    @Override
    public void create() {
        setScreen(new MenuScreen());
    }
}
