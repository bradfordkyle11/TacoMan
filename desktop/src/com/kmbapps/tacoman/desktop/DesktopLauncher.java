package com.kmbapps.tacoman.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kmbapps.tacoman.TacoMan;

public class DesktopLauncher {

	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "MexMan";
		cfg.width = 1440/2;
		cfg.height = 2560/2;
		cfg.resizable = false;

		new LwjglApplication(new TacoMan(), cfg);
	}
}
