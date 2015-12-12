package com.jja.ld34;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.jja.ld34.screens.PlayScreen;

public class Ld34Game extends Game {

    public static final int GAME_WIDTH = 640;  // in px
    public static final int GAME_HEIGHT = 480;  // in px
	public static final float PIXELS_PER_METER = 100f;

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
