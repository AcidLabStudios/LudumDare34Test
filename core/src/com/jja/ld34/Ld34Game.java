package com.jja.ld34;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.jja.ld34.screens.PlayScreen;

public class Ld34Game extends Game {

    public static final int GAME_WIDTH = 640;  // in px
    public static final int GAME_HEIGHT = 480;  // in px
	public static final float PIXELS_PER_METER = 100f;

	public static final short DEFAULT_BIT = 1;
	public static final short ENVIRONMENT_BIT = 2;
	public static final short PROTAGONIST_BIT = 4;
	public static final short COLLECTIBLES_BIT = 8;
	public static final short ENEMY_BIT = 16;
	public static final short ALL_FLAGS = DEFAULT_BIT | ENVIRONMENT_BIT | PROTAGONIST_BIT | COLLECTIBLES_BIT | ENEMY_BIT;

	private Screen currentScreen;

	@Override
	public void create() {
		setCurrentScreen(new PlayScreen());
	}

	@Override
	public void render() {
		super.render();
	}

	public void setCurrentScreen(Screen currentScreen) {
		this.currentScreen = currentScreen;
		setScreen(currentScreen);
	}
}
