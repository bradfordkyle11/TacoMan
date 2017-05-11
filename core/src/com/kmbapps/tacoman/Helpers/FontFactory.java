package com.kmbapps.tacoman.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontFactory {

	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"�`'<> ";

	public static BitmapFont generateFont(int size){
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Caladea-Regular.ttf"));
	    FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
	    params.size = size;
	    params.characters = FONT_CHARACTERS;
	    params.borderColor = new Color(0f, 0f, 0f, 1f);
	    BitmapFont f = generator.generateFont(params);
	    f.setColor(Color.BLACK);
	    Color color = f.getColor();
	    generator.dispose();
	    return f;
	}
}
