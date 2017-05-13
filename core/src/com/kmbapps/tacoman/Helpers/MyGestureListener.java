package com.kmbapps.tacoman.Helpers;

import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;

public class MyGestureListener implements GestureListener {

	private GameWorld world;
	private boolean paused = false;

	public MyGestureListener(GameWorld world) {
		this.world = world;
	}

	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		if (count >= 2&&!paused) {
			world.pause();
			return true;
		}
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		if (world.isRunning()&&MyPrefs.isSwipe()&&!paused) {
			if ((float) Math.abs(velocityX) > (float) Math.abs(velocityY)) {
				if (velocityX > 0) {
					world.getPlayerCharacter().goRight();
				} else {
					world.getPlayerCharacter().goLeft();
				}
			} else {
				if (velocityY > 0) {
					world.getPlayerCharacter().goDown();
				} else {
					world.getPlayerCharacter().goUp();
				}

			}
			//return true;
		}
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void pinchStop() {
		// TODO Auto-generated method stub
		
	}
	
	public void pause(){
		paused = true;
	}
	public void resume(){
		paused = false;
	}

}
