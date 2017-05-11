package com.kmbapps.tacoman;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kmbapps.tacoman.Helpers.AssetLoader;
import com.kmbapps.tacoman.Helpers.MyPrefs;
import com.kmbapps.tacoman.Screens.GameScreen;
import com.kmbapps.tacoman.Screens.MainMenuScreen;
import com.kmbapps.tacoman.Screens.SettingsScreen;

public class TacoMan extends Game {
	GameScreen gameScreen = null;
	MainMenuScreen mainMenu = null;
	SettingsScreen settings = null;
	
	public static final int EASY = 0;
	public static final int MEDIUM = 1;
	public static final int HARD = 2;
	
	public static final float BUTTON_W_SCALE = .75f;
	public static final float BUTTON_H_SCALE = (float) (1f/8f);
	
	public static final int GRID_W = 45;
	public static final int GRID_H = 45;
	//TODO: redo all characters to this grid size
	public static final int NEW_GRID_W = 72;
	public static final int NEW_GRID_H = 72;
	
	public static final int TILES_HIGH = 32;
	public static final int TILES_WIDE = 32;
	


	@Override
	public void create() {

		MyPrefs.load();
		AssetLoader.loadGraphics(Gdx.graphics.getWidth() / TILES_WIDE, Gdx.graphics.getWidth() / TILES_WIDE);

		mainMenu = new MainMenuScreen(this);
		settings = new SettingsScreen(this);
//		try {
//			gameScreen = new GameScreen(this);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		setScreen(mainMenu);
	}

	@Override
	public void dispose() {
		mainMenu.dispose();
		AssetLoader.dispose();
		super.dispose();

	}

	public GameScreen getGameScreen() {
		int difficulty = MyPrefs.getDifficulty();
		try {
			return new GameScreen(this, difficulty);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public MainMenuScreen getMainMenu() {
		return mainMenu;
	}

	public SettingsScreen getSettingsScreen() {
		return settings;
	}

}
