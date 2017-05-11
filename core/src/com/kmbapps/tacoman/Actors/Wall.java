package com.kmbapps.tacoman.Actors;

import com.badlogic.gdx.math.Vector2;

public class Wall extends Actor {

	public Wall(float x, float y, float width, float height){
		this.position = new Vector2(x, y);
		this.width = width;
		this.height = height;
	}
}
