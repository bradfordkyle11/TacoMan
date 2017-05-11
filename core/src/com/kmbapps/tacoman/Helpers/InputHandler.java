package com.kmbapps.tacoman.Helpers;

import com.kmbapps.tacoman.Actors.MainCharacter;
import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.input.GestureDetector;

public class InputHandler implements InputProcessor {

	private GameWorld world;
	private MainCharacter playerCharacter;
	private boolean paused = false;

	public InputHandler(GameWorld world) {
		this.world = world;
		this.playerCharacter = world.getPlayerCharacter();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {

		case Keys.UP:
			if (world.isRunning()&&!paused) {
				playerCharacter.goUp();
				return true;
			}

			return false;

		case Keys.DOWN:
			if (world.isRunning()&&!paused) {
				playerCharacter.goDown();
				return true;
			}

			return false;

		case Keys.RIGHT:
			if (world.isRunning()&&!paused) {
				playerCharacter.goRight();
				return true;
			}

			return false;

		case Keys.LEFT:
			if (world.isRunning()&&!paused) {
				playerCharacter.goLeft();
				return true;
			}

			return false;

		case Keys.ENTER:
			if (world.isReady()&&!paused) {
				world.setReady(true);
				return true;
			}
			break;
			
		case Keys.ESCAPE:
			if (world.isRunning()&&!paused){
				world.pause();
				return true;
			}

		return false;
		}
		
		return false;
		
		
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (world.isReady()&&!paused) {
			world.setReady(true);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void pause(){
		paused = true;
	}
	
	public void resume(){
		paused = false;
	}

}
