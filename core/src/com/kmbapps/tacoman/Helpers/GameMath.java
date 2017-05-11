package com.kmbapps.tacoman.Helpers;

import com.badlogic.gdx.math.Vector2;

public class GameMath {
	
	public static enum DIRECTION {Up, Down, Left, Right, None};
	private static final float DEFAULT_EPSILON = .001f;

	public static boolean floatEquals(float f1, float f2){
		return Math.abs(f1-f2) < DEFAULT_EPSILON;
	}
	
	public static Vector2 getDirectionalUnitVector(DIRECTION dir){
		switch (dir){
		case Up:
			return new Vector2(0,1);
		case Down:
			return new Vector2(0,-1);
		case Left:
			return new Vector2(-1,0);
		case Right:
			return new Vector2(1,0);
		default:
			return new Vector2(0,0);
		}
	}
	
}
