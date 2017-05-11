package com.kmbapps.tacoman.Actors;

import java.util.ArrayList;

import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.kmbapps.tacoman.GameWorld.Map;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.kmbapps.tacoman.Helpers.GameMath.DIRECTION;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MainCharacter extends Actor {

	private final static int STARTING_LIVES = 3;

	private float width;
	private float height;
	private int lives;
	private Rectangle characterCollision;
	private Rectangle wall;
	private ArrayList<Rectangle> allWalls;
	private ArrayList<Rectangle> allTacos;
	private ArrayList<BPCar> allBPCars;
	private ArrayList<Rectangle> allTeleporters;

	private float startingX, startingY;
	private float centerX, centerY;

	private boolean tryUp;
	private boolean tryDown;
	private boolean tryRight;
	private boolean tryLeft;

	private boolean isReset;

	private boolean hasGreenCard = false;

	private int tryCount = 0;
	private int frames = 0;
	private int greenCardTimer = 0;

	private int score;
	private boolean scoreChanged = true;

	private float delta;
	
	private int difficulty;

	public MainCharacter(GameWorld world, float startingX, float startingY, int difficulty) {
		super(world);
		this.difficulty = difficulty;
		this.world = world;
		this.startingX = startingX;
		this.startingY = startingY;
		centerX = startingX + world.getTileWidth() / 2;
		centerY = startingY + world.getTileHeight() / 2;
		velocity = new Vector2(0, 0);
		previousVelocity = new Vector2(0, 0);
		position = new Vector2(startingX, startingY);
		hitbox = new Rectangle(position.x, position.y, world.getTileWidth(),
				world.getTileHeight());
		lives = STARTING_LIVES;
		tryUp = tryDown = tryRight = tryLeft = false;
		allWalls = world.getAllWalls();
		allTacos = world.getAllTacos();
		allBPCars = world.getAllBPCars();
		allTeleporters = world.getAllTeleporters();
		width = world.getTileWidth();
		height = world.getTileHeight();
		score = 0;
	}

	@Override
	public void update(float delta) {
		wrapAround();
		super.update(delta);
		scoreChanged = false;
		this.delta = delta;
		if (isReset == true) {
			isReset = false;
		}
		if (greenCardTimer > 0) {
			greenCardTimer -= 1;
		}
		if (greenCardTimer == 0) {
			hasGreenCard = false;
		}

		checkEnemyCollision();
		if (containingGrid != null) {
			eat(containingGrid.getConsumable());
		}
		// eatTacos();
		// checkGreenCard();

		centerX = position.x + world.getTileWidth() / 2;
		centerY = position.y + world.getTileHeight() / 2;

	}

	@Override
	protected void updateDir() {
		// TODO Auto-generated method stub

		GameMath.DIRECTION tempDir = GameMath.DIRECTION.None;

		if (tryUp) {
			tempDir = GameMath.DIRECTION.Up;
		}
		if (tryDown) {
			tempDir = GameMath.DIRECTION.Down;
		}
		if (tryLeft) {
			tempDir = GameMath.DIRECTION.Left;
		}
		if (tryRight) {
			tempDir = GameMath.DIRECTION.Right;
		}

		// going off map
		if (containingGrid == null) {
			//
		}

		// ensures movement is stopped when player is reset
		else if (tempDir == GameMath.DIRECTION.None) {
			dir = tempDir;
		}

		else {
			if (containingGrid.centeredInGrid(this)) {
				if (!containingGrid.getAdjacentGrid(tempDir).isBlocked()) {
					dir = tempDir;
				} else if (dir != GameMath.DIRECTION.None) {
					if (containingGrid.getAdjacentGrid(dir).isBlocked()) {

						dir = GameMath.DIRECTION.None;
					}
				}
			} else {
				Vector2 directionVector = GameMath
						.getDirectionalUnitVector(tempDir);
				if (velocity.hasOppositeDirection(directionVector)
						|| velocity.hasSameDirection(directionVector)) {
					dir = tempDir;
				}
			}
		}

	}

	private void checkGreenCard() {
		for (int i = 0; i < world.getAllGreenCards().size(); i++) {
			if (characterCollision.overlaps(world.getAllGreenCards().get(i))) {
				hasGreenCard = true;
				world.getAllGreenCards().remove(i);
				i -= 1;
				greenCardTimer = 180;
			}
		}

	}

	private void wrapAround() {
		if (position.x > world.getTileWidth() * GameWorld.getTilesWide() - width / 10) {
			position.x = -width + (width / 10);
		} else if (position.x < -width + (width / 10)) {
			position.x = world.getTileWidth() * GameWorld.getTilesWide() - width / 10;
		} else if (position.y > world.getTileHeight()
				* GameWorld.getTilesHigh() - height / 10) {
			position.y = -height + (height / 10);
		} else if (position.y < -height + (height / 10)) {
			position.y = world.getTileHeight() * GameWorld.getTilesHigh()
					- height / 10;
		}

	}

	private void eat(Consumable c) {
		containingGrid.removeConsumable();
		if (c != null) {
			switch (c.getType()) {
			case Taco:
				score += c.getValue();
				world.eatTaco();
				break;
			case GreenCard:
				hasGreenCard = true;
				greenCardTimer = c.getValue();
				break;
			default:
				break;
			}
		}
	}

	private void eatTacos() {
		for (int i = 0; i < allTacos.size(); i++) {
			if (characterCollision.overlaps(allTacos.get(i))) {
				allTacos.remove(i);
				i -= 1;
				score += 10;
				scoreChanged = true;
			}
		}

	}

	private void movePlayer() {

		// add twice the normal velocity if turning to keep from turning a
		// distance that won't get you anywhere
		if (tryLeft == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(-world.getTileWidth() * world.getTilesWide() / 125.0f,
					0);
			position.add(velocity.cpy());
		} else if (tryRight == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(world.getTileWidth() * world.getTilesWide() / 125.0f,
					0);
			position.add(velocity.cpy());
		} else if (tryUp == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(0, world.getTileWidth() * world.getTilesWide()
					/ 125.0f);
			position.add(velocity.cpy());
		} else if (tryDown == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(0, -world.getTileWidth() * world.getTilesWide()
					/ 125.0f);
			position.add(velocity.cpy());
		} else {
			position.add(velocity.cpy());
		}

		characterCollision.setPosition(
				(position.x + (world.getTileWidth() * .0225f)), position.y
						+ (world.getTileWidth() * .0225f));

		// don't keep trying to turn if you have turned
		if ((checkWallCollision() == false)
				&& ((tryLeft == true) || (tryRight == true) || (tryUp == true) || (tryDown == true))) {

			// scale velocity back to normal value since it was set at double
			// earlier
			velocity.set(velocity.cpy().scl(.5f));
			position.add(velocity.cpy().scl(-1));
			tryLeft = tryRight = tryUp = tryDown = false;
		} else if ((checkWallCollision() == true)
				&& (tryLeft == true || tryRight == true || tryUp == true || tryDown == true)) {

			position.add(velocity.cpy().scl(-1));
			velocity.set(previousVelocity.cpy());
			position.add(velocity.cpy());
			characterCollision.setPosition(
					(position.x + (world.getTileWidth() * .0225f)), position.y
							+ (world.getTileWidth() * .0225f));
			if (checkWallCollision() == true) {
				position.add(velocity.cpy().scl(-1));
				velocity.set(0, 0);
			}
		} else if ((checkWallCollision() == false)
				&& (tryLeft == false && tryRight == false && tryUp == false && tryDown == false)) {

		} else {
			position.add(velocity.cpy().scl(-1));
			velocity.set(0, 0);
		}

		if (checkEnemyCollision() == true) {
			if (hasGreenCard) {

			} else {
				lives -= 1;
			}
		}

	}

	private boolean checkEnemyCollision() {
		if (containingGrid != null) {
			for (Actor actor : containingGrid.getContent()) {
				if (actor.getClass().equals(BPCar.class)) {
					BPCar enemy = (BPCar) actor;
					if (hasGreenCard) {
						enemy.forceReturn();
						if (enemy.isCollidable()) {
							score += BPCar.getValue();
							scoreChanged = true;
						}
					} else if (enemy.isCollidable()) {
						lives -= 1;
						return true;
					}
				}
			}
		}
		// for (int i = 0; i < allBPCars.size(); i++) {
		// if (hitbox.overlaps(allBPCars.get(i).getHitbox())) {
		// if (hasGreenCard) {
		// allBPCars.get(i).forceReturn();
		// if (allBPCars.get(i).isCollidable()) {
		// score += BPCar.getValue();
		// scoreChanged = true;
		// }
		// }
		// else if (allBPCars.get(i).isCollidable()) {
		// lives -= 1;
		// return true;
		// }
		// }
		// }
		return false;
	}

	public boolean isScoreChanged() {
		return scoreChanged;
	}

	public void goLeft() {
		tryLeft = true;
		tryDown = tryRight = tryUp = false;

	}

	public void goRight() {
		tryRight = true;
		tryDown = tryLeft = tryUp = false;

	}

	public void goUp() {
		tryUp = true;
		tryDown = tryLeft = tryRight = false;

	}

	public void goDown() {
		tryDown = true;
		tryUp = tryLeft = tryRight = false;

	}

	public boolean checkWallCollision() {

		for (int i = 0; i < allWalls.size(); i++) {
			if (characterCollision.overlaps(allWalls.get(i))) {
				/*
				 * if (velocity.x > 0) { position.add(velocity.cpy().scl(-1));
				 * characterCollision.setPosition(position.cpy()); } else if
				 * (velocity.x < 0) { position.add(velocity.cpy().scl(-1));
				 * characterCollision.setPosition(position.cpy()); } else if
				 * (velocity.y > 0) { position.add(velocity.cpy().scl(-1));
				 * characterCollision.setPosition(position.cpy()); } else if
				 * (velocity.y < 0) { position.add(velocity.cpy().scl(-1));
				 * characterCollision.setPosition(position.cpy()); }
				 */
				return true;
			}
		}

		return false;
	}

	public Vector2 getPosition() {
		return position;
	}

	public int getLives() {
		return lives;
	}

	public void resetPlayerPosition(){
		resetPlayerPosition(startingX, startingY);
	}
	// only resets player position, not lives
	public void resetPlayerPosition(float startX, float startY) {
		startingX = startX; startingY = startY;
		position.set(startingX, startingY);
		hitbox.setPosition(position.cpy());
		velocity.set(0, 0);
		previousVelocity.set(0, 0);
		tryUp = tryDown = tryRight = tryLeft = false;
		hasGreenCard = false;
		greenCardTimer = 0;

		isReset = true;

	}

	// resets player position and lives
	public void resetPlayer(float startX, float startY) {
		startingX = startX; startingY = startY;
		resetPlayerPosition(startingX, startingY);
		lives = 3;
		score = 0;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public boolean isReset() {
		return isReset;
	}

	public float getCenterX() {
		return centerX;
	}

	public float getCenterY() {
		return centerY;
	}

	public Rectangle getCharacterCollision() {
		return characterCollision;
	}

	public boolean hasGreenCard() {
		return hasGreenCard;
	}

	public int getScore() {
		return score;
	}
}
