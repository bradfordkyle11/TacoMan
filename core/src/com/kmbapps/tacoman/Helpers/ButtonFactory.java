package com.kmbapps.tacoman.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonFactory {
	public static final int TEXT_BUTTON = 0;
	public static Button getButton(String label, int type){
		
		switch (type){
		case TEXT_BUTTON:
			Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
			TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
			textButtonStyle.up = uiSkin.getDrawable("default-round");
			textButtonStyle.down = uiSkin.newDrawable("default-round-down");

		    BitmapFont f = FontFactory.generateFont(Gdx.graphics.getWidth()/10);
			textButtonStyle.font = f;
			uiSkin.add("default", textButtonStyle);
			
			
			TextButton textButton = new TextButton(label, uiSkin);
			return textButton;
		default:
			return null;
		}
	}
}
