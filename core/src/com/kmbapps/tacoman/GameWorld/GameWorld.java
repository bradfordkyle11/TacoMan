package com.kmbapps.tacoman.GameWorld;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.kmbapps.tacoman.TacoMan;
import com.kmbapps.tacoman.Actors.Actor;
import com.kmbapps.tacoman.Actors.BPCar;
import com.kmbapps.tacoman.Actors.MainCharacter;
import com.kmbapps.tacoman.Helpers.InputHandler;
import com.kmbapps.tacoman.Helpers.MyGestureListener;
import com.kmbapps.tacoman.Helpers.MyPrefs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class GameWorld {

	private static final float TILES_WIDE = 15;
	private static final float TILES_HIGH = 25;
	private static final int NUM_MAPS = 3;

	private float midPointX;
	private float midPointY;
	private int currentLevel = 0;
	private int currentMap = 0;
	private Map map;

	private float tileWidth;
	private float tileHeight;
	private float xOffs;
	private float yOffs;

	private float tacoWidth;
	private float tacoHeight;

	private float teleporterWidth;
	private float teleporterHeight;

	private float playerStartX;
	private float playerStartY;
	private int lives;
	private int numTacos = 0;
	
	private InputHandler iH = null;
	private MyGestureListener gL = null;

	private MainCharacter playerCharacter;

	private ArrayList<Rectangle> allWalls = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> allTacos = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> allTeleporters = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> allGreenCards = new ArrayList<Rectangle>();
	private ArrayList<BPCar> allBPCars = new ArrayList<BPCar>();
	
	private int difficulty;

	public enum GameState {
		MENU, READY, RUNNING, GAMEOVER, HIGHSCORE, LEVEL_OVER, LEVEL_RESTART, NEXT_LEVEL, PAUSED
	}

	private GameState currentState;
	private boolean ready = false;
	private boolean gameOver = false;

	public GameWorld(float midPointY, float midPointX, int difficulty) throws IOException {
		this.difficulty = difficulty;
		currentState = GameState.READY;
		this.midPointY = midPointY;
		this.midPointX = midPointX;

		// set tile width and height
		float height = Gdx.graphics.getHeight();
		float width = Gdx.graphics.getWidth();
		if (height >= (16.f/9.f) * width){
			tileWidth = width / TILES_WIDE;
			tileHeight = tileWidth;
		}
		else {
			tileHeight = height / (TILES_HIGH + 1);
			tileWidth = tileHeight;
		}
		xOffs = (width % (tileWidth * TILES_WIDE)) / 2.f;
		yOffs = height % (tileHeight * (TILES_HIGH + 1));
		tacoWidth = 27 * tileWidth / 32;
		tacoHeight = 12 * tacoWidth / 27;
		teleporterWidth = tileWidth + (tileWidth / 32);
		teleporterHeight = teleporterWidth;

		// init map

		initMap();

		// create main character
//		playerStartX = 7 * tileWidth;
//		playerStartY = 5 * tileHeight;
		//playerCharacter = new MainCharacter(this, playerStartX, playerStartY, difficulty);
//		this.lives = playerCharacter.getLives();

	}

	public void initMap() throws IOException {
		map = new Map();
		map.loadMap(currentMap, this);
		numTacos = 0;
		for (int x = 0; x < TILES_WIDE; x++) {
			for (int y = 0; y < TILES_HIGH; y++) {
				if (map.map[x][y] == 'x') {
					Rectangle wall = new Rectangle(x* tileWidth,
							(TILES_HIGH - 1 - y) * tileHeight, tileWidth,
							tileHeight);
					allWalls.add(wall);
				} else if (map.map[x][y] == '.') {
					Rectangle taco = new Rectangle(x * tileWidth
							+ ((tileWidth - tacoWidth) / 2),
							(TILES_HIGH - 1 - y) * tileHeight
									+ ((tileHeight - tacoHeight) / 2),
							tacoWidth, tacoHeight);
					allTacos.add(taco);
					numTacos += 1;
				} else if (map.map[x][y] == 'B' || map.map[x][y] == 'P') {
					BPCar enemy = new BPCar(this, x * tileWidth
							- (tileWidth / 2f), (TILES_HIGH - 1 - y)
							* tileHeight, difficulty);
					allBPCars.add(enemy);
				} else if (map.map[x][y] == 'G') {
					Rectangle GreenCard = new Rectangle(x * tileWidth + xOffs,
							(TILES_HIGH - 1 - y) * tileHeight, tileWidth,
							tileHeight);
					allGreenCards.add(GreenCard);
				}
				else if (map.map[x][y] == 'C'){
					playerStartX = x * tileWidth;
					playerStartY = (TILES_HIGH - 1 - y) * tileHeight;
					if (playerCharacter == null){
						playerCharacter = new MainCharacter(this, playerStartX, playerStartY, difficulty);
						this.lives = playerCharacter.getLives();
					}
					else {
						if (currentState == GameState.NEXT_LEVEL) {
							playerCharacter.resetPlayerPosition(playerStartX, playerStartY);
						}
						else {
							playerCharacter.resetPlayer(playerStartX, playerStartY);
						}
					}

				}
			}
		}

	}

	public ArrayList<Rectangle> getAllGreenCards() {
		return allGreenCards;
	}

	public ArrayList<Rectangle> getAllTeleporters() {
		return allTeleporters;
	}

	public ArrayList<BPCar> getAllBPCars() {
		return allBPCars;
	}

	public void update(float delta) throws IOException {
		switch (currentState) {
		case RUNNING:
			if (!gameOver) {
				

				
				for (int i = 0; i < allBPCars.size(); i++) {
					allBPCars.get(i).update(delta);
				}
				for (ArrayList<Grid> row : map.getGridMap()) {
					for (Grid grid : row) {
						grid.update();
					}
				}
				playerCharacter.update(delta);
				if (playerCharacter.getLives() < this.lives) {

					if (lives != 0) {
						this.lives -= 1;
						currentState = GameState.LEVEL_RESTART;
					}
				}

				// dead
				if (lives == 0) {
					currentState = GameState.GAMEOVER;
					MyPrefs.saveHiScore(playerCharacter.getScore());
				}

				if (numTacos == 0) {
					currentState = GameState.NEXT_LEVEL;
				}
			} else {

			}
			break;
		case GAMEOVER:
			gameOver = true;

			currentState = GameState.RUNNING;
			break;
		case LEVEL_RESTART:
			InputMultiplexer iM = null;
			InputProcessor iP = Gdx.input.getInputProcessor();
			if (iP.getClass().equals(InputHandler.class)) {
				iH = (InputHandler) iP;
			} else {
				iM = (InputMultiplexer) iP;
				Array<InputProcessor> iPs = iM.getProcessors();
				for (InputProcessor processor : iPs) {
					if (processor.getClass().equals(InputHandler.class)) {
						iH = (InputHandler) processor;
					} else if (processor.getClass().equals(
							MyGestureListener.class)) {
						gL = (MyGestureListener) processor;
					}
				}
			}

			if (gL != null) {
				gL.pause();
			}
			if (iH != null) {
				iH.pause();
			}
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {

				// Handle exception
			}

			resetLevel();
			currentState = GameState.READY;

			break;

		// if (ready == false){
		// //wait for ready
		// }
		// else{
		// ready = false;
		// resetLevel();
		// currentState = GameState.READY;
		// break;
		// }
		// break;

		case NEXT_LEVEL:
			currentLevel += 1;
			currentMap += 1;

			for (int i = 1; i < allBPCars.size(); i++) {
				allBPCars.get(i).increaseVelocity();
			}
			if (currentMap == NUM_MAPS) {
				currentMap = 0;
			}
			nextLevel();
			currentState = GameState.READY;
			break;

		case READY:
			//reduce number of BPCars if on easy mode
			if (difficulty == TacoMan.EASY){
				if (allBPCars.size()>2){
					Random r = new Random();
					allBPCars.remove(r.nextInt(allBPCars.size()));
				}
			}
			if (ready == false) {
				if (iH != null) {
					iH.resume();
					iH = null;
				}
				if (gL != null) {
					gL.resume();
					gL = null;
				}
				waitForReady();
			} else {

				ready = false;
				currentState = GameState.RUNNING;

			}
		}

	}

	private void nextLevel() {
		allBPCars.clear();
		allTacos.clear();
		allWalls.clear();
		try {
			initMap();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//no need to reset player anymore here, since now player is reset when map initialized

	}

	private void waitForReady() {
		// TODO Auto-generated method stub

	}

	public Map getMap() {
		return map;
	}

	public float getTileWidth() {
		return tileWidth;
	}

	public float getTileHeight() {
		return tileHeight;
	}

	public float getTacoWidth() {
		return tacoWidth;
	}

	public float getTacoHeight() {
		return tacoHeight;
	}

	public static float getTilesWide() {
		return TILES_WIDE;
	}

	public static float getTilesHigh() {
		return TILES_HIGH;
	}

	public ArrayList<Rectangle> getAllWalls() {
		return allWalls;
	}

	public MainCharacter getPlayerCharacter() {
		return playerCharacter;
	}

	public ArrayList<Rectangle> getAllTacos() {
		return allTacos;
	}

	public boolean isRunning() {
		return currentState == GameState.RUNNING;
	}

	public boolean isLevelOver() {
		return currentState == GameState.LEVEL_OVER;
	}

	public boolean isRestartingLevel() {
		return currentState == GameState.LEVEL_RESTART;
	}

	public boolean isReady() {
		return currentState == GameState.READY;
	}

	public boolean isPaused() {
		return currentState == GameState.PAUSED;
	}

	public boolean isGameOver() {
		return currentState == GameState.GAMEOVER;
	}

	public void resetLevel() throws IOException {

		resetBPCars();
		playerCharacter.resetPlayerPosition();

	}

	private void resetBPCars() {
		for (ArrayList<Grid> grids : map.getGridMap()) {
			for (Grid grid : grids) {
				ArrayList<Actor> copy = (ArrayList<Actor>) grid.getContent()
						.clone();
				for (Actor actor : copy) {
					if (actor.getClass().equals(BPCar.class)) {
						grid.getContent().remove(actor);
					}
				}
			}
		}
		allBPCars.clear();
		for (int x = 0; x < TILES_WIDE; x++) {
			for (int y = 0; y < TILES_HIGH; y++) {
				if (map.map[x][y] == 'B' || map.map[x][y] == 'P') {
					BPCar enemy = new BPCar(this, x * tileWidth,
							(TILES_HIGH - 1 - y) * tileHeight, difficulty);
					allBPCars.add(enemy);
				}
			}
		}

	}

	public void setReady(boolean ready) {
		this.ready = true;

	}

	public void resume() {
		currentState = GameState.RUNNING;

	}

	public void pause() {
		currentState = GameState.PAUSED;

	}

	// completely resets the game, starting at level 0
	public void resetGame() throws IOException {
		currentLevel = 0;
		currentMap = 0;
		allBPCars.clear();
		allWalls.clear();
		allTacos.clear();
		initMap();
		//init map resets player now that player can have starting spots
		currentState = GameState.READY;
		lives = playerCharacter.getLives();

	}

	/*
	 * This function returns the grid containing an actor and updates the grid
	 * to have that actor
	 * 
	 * inputs - The actor to search for returns - The grid containing that actor
	 */
	public Grid getGrid(Actor actor) {
		ArrayList<ArrayList<Grid>> gridMap = map.getGridMap();
		for (ArrayList<Grid> row : gridMap) {
			for (Grid grid : row) {
				if (grid.contains(actor)) {
					grid.addContent(actor);
					return grid;
				}
			}
		}
		return null;
	}

	public void eatTaco() {
		numTacos -= 1;
	}

	// This returns the level as it will be displayed on the screen, meaning it
	// starts at 1 instead of 0
	public int getCurrentLevelNum() {
		return currentLevel + 1;
	}
	
	public float getXOffs(){
		return xOffs;
	}
	public float getYOffs(){
		return yOffs;
	}

}
