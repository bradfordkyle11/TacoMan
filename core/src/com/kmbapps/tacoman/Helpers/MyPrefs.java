package com.kmbapps.tacoman.Helpers;

import com.kmbapps.tacoman.TacoMan;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class MyPrefs {
	
	private static Preferences prefs;
	
	public static void load(){
		prefs = Gdx.app.getPreferences("MexMan");
		
		if (!prefs.contains("controlType")){
			prefs.putString("controlType", "swipe");
			prefs.flush();
		}
	}
	
	public static void setControlType(String type){
		prefs.putString("controlType", type);
		prefs.flush();
	}
	
	public static void setDifficulty(int difficulty){
		prefs.putInteger("difficulty", difficulty);
		prefs.flush();
	}
	
	public static boolean isSwipe(){
		String test = prefs.getString("controlType", "swipe");
		return prefs.getString("controlType").equals("swipe");
	}
	
	public static void saveHiScore(int score){
		int currHiScore = prefs.getInteger("hiScore", 0);
		if (score > currHiScore){
			prefs.putInteger("hiScore", score);
		}
		prefs.flush();
	}
	
	public static int getHiScore(){
		return prefs.getInteger("hiScore");
	}

	public static void dispose() {
		
	}
	
	public static int getDifficulty(){
		return prefs.getInteger("difficulty", TacoMan.HARD);
	}

}
