package com.kmbapps.tacoman.Actors;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kmbapps.tacoman.TacoMan;
import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.kmbapps.tacoman.GameWorld.Grid;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BPCar extends Actor {
	public static final int VALUE = 200;

	private static final int UP = 0;
	private static final int RIGHT = 1;
	private static final int DOWN = 2;
	private static final int LEFT = 3;
	private static final int NONE = 4;
	private static final float CRITICAL_VELOCITY = 125.0f;

	private static final int TURN_TIME = 0;

	private float BP_TOTAL_HITBOX_Y_OFF;
	private float BP_TOTAL_HITBOX_H;

	private Vector2 startPosition;
	private Vector2 potentialMove, bestMove;;
	private Vector2 distanceFromTarget;
	private Vector2 directionTestCenter;
	private Vector2 targetTile;
	private Vector2 currTarget = new Vector2(0, 0);
	private Rectangle bPSmallHitBox;
	private Rectangle bPTotalHitBox;
	private Rectangle wall;
	private Rectangle bPVision;
	private Rectangle directionTest;
	private Rectangle directionTest2;
	private ArrayList<Rectangle> allWalls;
	private ArrayList<BPCar> allBPCars;
	private ArrayList<Grid> path;
	private int currentDirection;
	private int oppositeDirection;
	
	private Grid targetGrid;
	private Vector2 danger;

	private float normalSpeed;
	private float doubleSpeed;

	private int turnTime = 0;

	private boolean oppositeDirectionPossible;

	private float velocityDivisor = 140.0f;
	private float standardVelocity;

	private boolean returning = false;

	private List<Vector2> possibleMoves;

	private Random randomGenerator = new Random();
	private int randomNumber;

	private boolean tryUp;
	private boolean tryDown;
	private boolean tryRight;
	private boolean tryLeft;
	private boolean tryStop;
	
	private int difficulty;

	public Rectangle getBPTotalHitBox() {
		return bPTotalHitBox;
	}

	public enum BPCarState {
		PATROL, CHASE, SCARED, RETURN
	}

	private BPCarState currentState;
	private int changeDirectionCount;// ghost will change directions when this
										// hits 0;

	public BPCar(GameWorld world, float startingX, float startingY, int difficulty) {
		super(world);
		this.difficulty = difficulty;
		this.world = world;
		path = new ArrayList<Grid>();
		velocity = new Vector2(0, 0);
		previousVelocity = new Vector2(0, 0);
		position = new Vector2(startingX, startingY);
		startPosition = new Vector2(startingX + world.getTileWidth()/2, startingY
				+ world.getTileHeight() / 2);
		distanceFromTarget = new Vector2();
		potentialMove = new Vector2();
		bestMove = new Vector2();
		directionTestCenter = new Vector2();
		targetTile = new Vector2();
		danger = new Vector2();

		this.width = world.getTileWidth() * 2;
		this.height = world.getTileHeight();



		BP_TOTAL_HITBOX_Y_OFF = this.height / 6f;
		BP_TOTAL_HITBOX_H = this.height / 2f;

		hitbox = new Rectangle(position.x + (width / 4), position.y,
				world.getTileWidth(), world.getTileHeight());
		bPSmallHitBox = new Rectangle(position.x
				+ (world.getTileWidth() * .0225f) + (width / 4), position.y
				+ (world.getTileWidth() * .0225f), world.getTileWidth()
				- (world.getTileWidth() * .045f), world.getTileHeight()
				- (world.getTileWidth() * .045f));

		bPTotalHitBox = new Rectangle(position.x, position.y
				+ BP_TOTAL_HITBOX_Y_OFF, this.width, BP_TOTAL_HITBOX_H);
		directionTest = new Rectangle();
		directionTest2 = new Rectangle();

		possibleMoves = new ArrayList<Vector2>();

		switch (difficulty) {
		case TacoMan.EASY:
			bPVision = new Rectangle(position.x, position.y, this.width * 2.5f,
					this.height * 4.5f);
			this.normalSpeed = world.getTileWidth() / 22f;
			break;
		case TacoMan.MEDIUM:
			bPVision = new Rectangle(position.x, position.y, this.width * 3f,
					this.height * 5f);
			this.normalSpeed = world.getTileWidth() / 20f;
			break;
		case TacoMan.HARD:
			bPVision = new Rectangle(position.x, position.y, this.width * 4f,
					this.height * 6f);
			this.normalSpeed = world.getTileWidth() / 19f;
			break;
			
		}
		

		this.doubleSpeed = normalSpeed * 2;
		speed = normalSpeed;
		
		bPVision.setCenter(bPTotalHitBox.x + bPTotalHitBox.width / 2,
				bPTotalHitBox.y + bPTotalHitBox.height / 2);
		tryUp = tryDown = tryRight = tryLeft = false;
		allWalls = world.getAllWalls();
		changeDirectionCount = 1;
		currentDirection = NONE;
		currentState = BPCarState.PATROL;
	}

	@Override
	public void update(float delta) {

		// keep from going off edge of map
		wrapAround();
		updateState();
		switch (currentState) {
		// patrol randomly if player is not in vision
		case PATROL:
			if (changeDirectionCount <= 0) {
				changeDirectionCount = 60 * 30;
				// targetTile = randomLocation();

			}

			// if (turnTime == 0) {
			// moveCloserTo(targetTile);
			// }
			// updateBPCar();
			speed = normalSpeed;
			break;
		case CHASE:// chase if player is in vision
			// if (turnTime == 0) {
			// moveCloserTo(world.getPlayerCharacter().getPosition());
			// }
			// updateBPCar();
			speed = normalSpeed;
			break;
		case RETURN:
			collidable = false;
			// if (turnTime == 0) {
			// moveCloserTo(startPosition);
			// }
			// updateBPCar();
			if (!(containingGrid == null)) {
				if (containingGrid.contains(startPosition)&&!containingGrid.isBlocked(this)&&!world.getPlayerCharacter().hasGreenCard()) {
					collidable = true;
					returning = false;
					// updateState();
				}
			}

			speed = normalSpeed * 2;
			break;
		case SCARED:
		}
		changeDirectionCount -= 1;
		super.update(delta);

		this.allBPCars = world.getAllBPCars();

		

		updateBPVision();

		// turn around if running into another car
		// if (checkBPCollision(bPTotalHitBox) == true) {
		// position.add(velocity.cpy().scl(-1));
		// // velocity.set(0, 0);
		//
		// if (!possibleMoves.isEmpty()) {
		// possibleMoves.clear();
		// }
		//
		// findPossibleDirections();
		//
		// if (!possibleMoves.isEmpty()) {
		// bestMove = possibleMoves.get(randomGenerator
		// .nextInt(possibleMoves.size() - 1));
		// tryDirection((int) bestMove.x);
		// }
		//
		// }
		if (turnTime > 0) {
			turnTime--;
		}
		// updateTotalHitBox();

	}

	@Override
	protected void updateDir() {
		if (turnTime > 0) {
			dir = GameMath.DIRECTION.None;
		} else {
			switch (currentState) {
			case PATROL:
				if (changeDirectionCount <= 0
						|| containingGrid.contains(targetTile)) {
					targetTile = randomGrid().getCenter().cpy();

				}

				if (turnTime == 0) {
					dir = getNextDirTowards(targetTile);
				} else {
					dir = GameMath.DIRECTION.None;
				}
				break;
			case CHASE:
				if (world.getGrid(world.getPlayerCharacter()) != null) {
					targetTile = world.getGrid(world.getPlayerCharacter()).getCenter().cpy();
					dir = getNextDirTowards(targetTile);
				} else {
					targetTile = world.getPlayerCharacter().getCenter();
					dir = getNextDirTowards(targetTile);
				}
				break;
			case SCARED:
				if (world.getPlayerCharacter().getContainingGrid() == null){
					
				}
				else if (!world.getPlayerCharacter().getContainingGrid().getCenter().equals(danger)){
					danger = world.getPlayerCharacter().getContainingGrid().getCenter().cpy();
					targetTile = getFurthestFrom(world.getPlayerCharacter().getContainingGrid()).getCenter().cpy();
					
				}
				dir = getNextDirTowards(targetTile);
				break;
			case RETURN:
				dir = getNextDirTowards(startPosition);
				break;
			}
		}
	}

	private GameMath.DIRECTION getNextDirTowards(Vector2 targetTile2) {

		if (dir == GameMath.DIRECTION.None || !targetTile2.equals(currTarget)) {
			currTarget = targetTile2;
			path = pathTo(containingGrid, targetTile2);
		} else if (!containingGrid.centeredInGrid(this)) {
			if (path.size() - 1 > 0) {
				if (path.get(path.size() - 2).isBlocked(this)) {
					path = pathTo(containingGrid, targetTile2);
				}
			}
		} else {
			if (path.size() - 1 > 0) {
				if (path.get(path.size() - 2).isBlocked(this)) {
					path = pathTo(containingGrid, targetTile2);
				}
			}

			if (dir != GameMath.DIRECTION.None
					&& containingGrid.equals(path.get(path.size() - 1))) {
				path.remove(path.size() - 1);
			}
		}
		if (path.size() > 0) {
			GameMath.DIRECTION tempDir = containingGrid.directionTo(path
					.get(path.size() - 1));
			if (tempDir == GameMath.DIRECTION.None) {
				tempDir = dir;
			}
			return tempDir;
		} else {
			return GameMath.DIRECTION.None;
		}

		// ArrayList<GameMath.DIRECTION> possibleDirs = new
		// ArrayList<GameMath.DIRECTION>();
		// if (!containingGrid.getAbove().isBlocked(this)) {
		// possibleDirs.add(GameMath.DIRECTION.Up);
		// }
		// if (!containingGrid.getBelow().isBlocked(this)) {
		// possibleDirs.add(GameMath.DIRECTION.Down);
		// }
		// if (!containingGrid.getLeft().isBlocked(this)) {
		// possibleDirs.add(GameMath.DIRECTION.Left);
		// }
		// if (!containingGrid.getRight().isBlocked(this)) {
		// possibleDirs.add(GameMath.DIRECTION.Right);
		// }
		//
		// ArrayList<GameMath.DIRECTION> copy = (ArrayList<DIRECTION>)
		// possibleDirs
		// .clone();
		// for (GameMath.DIRECTION direction : possibleDirs) {
		// if (GameMath.getDirectionalUnitVector(dir).hasOppositeDirection(
		// GameMath.getDirectionalUnitVector(direction))) {
		// if (possibleDirs.size() > 0) {
		// copy.remove(direction);
		// possibleDirs = copy;
		// }
		// }
		// }
		//
		// Vector2 desiredDirection = new Vector2(targetTile2.x - getCenter().x,
		// targetTile2.y - getCenter().y);
		//
		// float minAngle = 361;
		// GameMath.DIRECTION bestDir = GameMath.DIRECTION.None;
		// for (GameMath.DIRECTION dir : possibleDirs) {
		// Vector2 directionalVector = GameMath.getDirectionalUnitVector(dir);
		// float angle = Math.abs(directionalVector.angle(desiredDirection));
		// if (angle < minAngle) {
		// minAngle = angle;
		// bestDir = dir;
		// }
		// }
		// if (containingGrid.centeredInGrid(this)
		// || GameMath.getDirectionalUnitVector(bestDir)
		// .hasOppositeDirection(
		// GameMath.getDirectionalUnitVector(dir))
		// || dir == GameMath.DIRECTION.None) {
		// return bestDir;
		// } else {
		// return dir;
		// }
	}

	private ArrayList<Grid> pathTo(Grid start, Vector2 end) {
		for (ArrayList<Grid> grids : world.getMap().getGridMap()) {
			for (Grid grid : grids) {
				grid.distance = Double.POSITIVE_INFINITY;
				grid.parent = null;
			}
		}

		ArrayDeque<Grid> queue = new ArrayDeque<Grid>();

		start.distance = 0;
		queue.add(start);
		Grid current = start;
		while (!queue.isEmpty()) {
			current = queue.remove();
			if (current.contains(end)) {
				queue.clear();
			} else {
				Grid above = current.getAbove();
				Grid below = current.getBelow();
				Grid left = current.getLeft();
				Grid right = current.getRight();
				if (!above.isBlocked(this) && !above.equals(current)
						&& above.distance == Double.POSITIVE_INFINITY) {
					above.distance = current.distance + 1;
					above.parent = current;
					queue.add(above);
				}
				if (!below.isBlocked(this) && !below.equals(current)
						&& below.distance == Double.POSITIVE_INFINITY) {
					below.distance = current.distance + 1;
					below.parent = current;
					queue.add(below);
				}
				if (!left.isBlocked(this) && !left.equals(current)
						&& left.distance == Double.POSITIVE_INFINITY) {
					left.distance = current.distance + 1;
					left.parent = current;
					queue.add(left);
				}
				if (!right.isBlocked(this) && !right.equals(current)
						&& right.distance == Double.POSITIVE_INFINITY) {
					right.distance = current.distance + 1;
					right.parent = current;
					queue.add(right);
				}
			}
		}

		ArrayList<Grid> path = new ArrayList<Grid>();
		while (current != null) {
			if (current.equals(start)){
				break;
			}
			path.add(current);
			current = current.parent;
		}
		if (path.isEmpty()) {
			path.add(start);
		}
		return path;
	}

	private Vector2 randomLocation() {
		Vector2 result = new Vector2();
		result.x = randomGenerator.nextInt((int) (world.getTileWidth() * world
				.getTilesWide()));
		result.y = randomGenerator.nextInt((int) (world.getTileHeight() * world
				.getTilesHigh()));
		return result;
	}

	private Grid randomGrid() {
		ArrayList<ArrayList<Grid>> grids = world.getMap().getGridMap();
		int i = randomGenerator.nextInt(grids.size());
		int j = randomGenerator.nextInt(grids.get(0).size());

		Grid grid = grids.get(i).get(j);
		while (grid.isBlocked()) {
			i = randomGenerator.nextInt(grids.size());
			j = randomGenerator.nextInt(grids.get(0).size());
			grid = grids.get(i).get(j);
		}
		return grid;
	}
	
	private Grid getFurthestFrom(Grid danger){
		ArrayList<ArrayList<Grid>> gridMap = world.getMap().getGridMap();
		
		Grid result = danger;
		float maxDist = 0;
		
		for (ArrayList<Grid> grids : gridMap){
			for (Grid grid : grids){
				if (!grid.isBlocked()){
					Vector2 direction = new Vector2(danger.getX() - grid.getX(), danger.getY() - grid.getY());
					float dist = direction.len2();
					if (dist > maxDist){
						maxDist = dist;
						result = grid;
					}
				}
			}
		}
		
		return result;
	}

	public static int getValue() {
		return VALUE;
	}

	private void randomPatrol() {
		if (!possibleMoves.isEmpty()) {
			possibleMoves.clear();
		}

		findPossibleDirections();

		if (!possibleMoves.isEmpty()) {
			if (possibleMoves.size() > 1) {
				bestMove = possibleMoves.get(randomGenerator
						.nextInt(possibleMoves.size() - 1));
			} else {
				bestMove = possibleMoves.get(0);
			}
			tryDirection((int) bestMove.x);
		}
		// randomNumber = randomGenerator.nextInt(4);
		// while (randomNumber == oppositeDirection) {
		// randomNumber = randomGenerator.nextInt(4);
		// }
		// switch (randomNumber) {
		// case UP:
		// goUp();
		// break;
		// case RIGHT:
		// goRight();
		// break;
		// case DOWN:
		// goDown();
		// break;
		// case LEFT:
		// goLeft();
		// break;
		// }

	}

	private void updateBPVision() {
		bPVision.setCenter(hitbox.x + hitbox.width / 2, hitbox.y
				+ hitbox.height / 2);

	}

	private void updateState() {
		if (bPVision.overlaps(world.getPlayerCharacter().getHitbox())
				&& !world.getPlayerCharacter().hasGreenCard()
				&& returning == false) {
			currentState = BPCarState.CHASE;
		} else if (returning == true) {
			currentState = BPCarState.RETURN;
		} else if (world.getPlayerCharacter().hasGreenCard()){
			currentState = BPCarState.SCARED;
		} else {
			currentState = BPCarState.PATROL;
		}

	}

	private void moveCloserTo(Vector2 position2) {

		if (!possibleMoves.isEmpty()) {
			possibleMoves.clear();
		}

		findPossibleDirections(position2);

		if (!possibleMoves.isEmpty()) {
			bestMove = possibleMoves.get(0);
			for (int i = 1; i < possibleMoves.size(); i++) {
				if (possibleMoves.get(i).y < bestMove.y) {
					bestMove = possibleMoves.get(i);
				}

			}
		} else if (oppositeDirectionPossible) {
			bestMove.set(oppositeDirection, 0);
		}

		tryDirection((int) bestMove.x);
	}

	// updates possibleMoves with possible directions, and their distances from
	// the provided target
	private void findPossibleDirections(Vector2 target) {
		oppositeDirectionPossible = false;
		Rectangle currentHitBox = new Rectangle();
		currentHitBox = bPTotalHitBox;
		standardVelocity = world.getTileWidth() * GameWorld.getTilesWide()
				/ (velocityDivisor * 2);

		directionTest.set(bPSmallHitBox);

		// test RIGHT, direction 0
		directionTest.setPosition(bPSmallHitBox.x + velocity.len(),
				bPSmallHitBox.y);
		directionTest.getCenter(directionTestCenter);
		distanceFromTarget.set(
				directionTestCenter.x - (target.x + world.getTileWidth() / 2),
				directionTestCenter.y - (target.y + world.getTileHeight() / 2));
		potentialMove.set(RIGHT, distanceFromTarget.len());

		bPTotalHitBox.set(position.cpy().x + standardVelocity,
				position.cpy().y, width, height);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		// test LEFT
		directionTest.setPosition(bPSmallHitBox.x - standardVelocity,
				bPSmallHitBox.y);
		directionTest.getCenter(directionTestCenter);
		distanceFromTarget.set(
				directionTestCenter.x - (target.x + world.getTileWidth() / 2),
				directionTestCenter.y - (target.y + world.getTileHeight() / 2));
		potentialMove.set(LEFT, distanceFromTarget.len());

		bPTotalHitBox.set(position.cpy().x - standardVelocity,
				position.cpy().y, width, height);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		/* test UP */
		directionTest.setPosition(bPSmallHitBox.x,
				bPSmallHitBox.y + velocity.len());
		directionTest.getCenter(directionTestCenter);
		distanceFromTarget.set(
				directionTestCenter.x - (target.x + world.getTileWidth() / 2),
				directionTestCenter.y - (target.y + world.getTileHeight() / 2));
		potentialMove.set(UP, distanceFromTarget.len());

		bPTotalHitBox.set(position.cpy().x + width / 4, position.cpy().y
				- width / 4 + standardVelocity, height, width);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		/* test Down */
		directionTest.setPosition(bPSmallHitBox.x,
				bPSmallHitBox.y - velocity.len());
		directionTest.getCenter(directionTestCenter);
		distanceFromTarget.set(
				directionTestCenter.x - (target.x + world.getTileWidth() / 2),
				directionTestCenter.y - (target.y + world.getTileHeight() / 2));
		potentialMove.set(DOWN, distanceFromTarget.len());

		bPTotalHitBox.set(position.cpy().x + width / 4, position.cpy().y
				- width / 4 - standardVelocity, height, width);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		// removed opposite direction if there are other options
		if (possibleMoves.size() > 1) {
			for (int i = 0; i < possibleMoves.size(); i++) {
				if ((int) possibleMoves.get(i).x == oppositeDirection) {
					possibleMoves.remove(i);
					oppositeDirectionPossible = true;
					break;
				}
			}
		}

		// reset hitbox
		bPTotalHitBox.set(currentHitBox);

	}

	private void findPossibleDirections() {
		directionTest.set(bPSmallHitBox);

		// test RIGHT, direction 0
		directionTest.setPosition(bPSmallHitBox.x + standardVelocity,
				bPSmallHitBox.y);
		potentialMove.set(RIGHT, 0);

		directionTest2.set(bPTotalHitBox);
		directionTest2.setPosition(bPTotalHitBox.x + velocity.len(),
				bPTotalHitBox.y);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false
				&& potentialMove.x != oppositeDirection) {
			possibleMoves.add(potentialMove.cpy());
		}

		directionTest.setPosition(bPSmallHitBox.x - standardVelocity,
				bPSmallHitBox.y);
		directionTest.getCenter(directionTestCenter);
		potentialMove.set(LEFT, 0);

		directionTest2.setPosition(bPTotalHitBox.x - velocity.len(),
				bPTotalHitBox.y);

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		directionTest.setPosition(bPSmallHitBox.x, bPSmallHitBox.y
				+ standardVelocity);
		directionTest.getCenter(directionTestCenter);
		potentialMove.set(UP, 0);

		directionTest2.setPosition(bPTotalHitBox.x,
				bPTotalHitBox.y + velocity.len());

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		directionTest.setPosition(bPSmallHitBox.x, bPSmallHitBox.y
				- standardVelocity);
		directionTest.getCenter(directionTestCenter);
		potentialMove.set(DOWN, 0);

		directionTest2.setPosition(bPTotalHitBox.x,
				bPTotalHitBox.y - velocity.len());

		if (checkWallCollision(directionTest) == false
				&& checkBPCollision(bPTotalHitBox) == false) {
			possibleMoves.add(potentialMove.cpy());
		}

		// remove the option of turning around if that is not the only option
		if (possibleMoves.size() > 1) {
			for (int i = 0; i < possibleMoves.size(); i++) {
				if ((int) possibleMoves.get(i).x == oppositeDirection) {
					possibleMoves.remove(i);
					break;
				}
			}
		}

	}

	private void tryDirection(int x) {
		switch (x) {
		case UP:
			goUp();
			break;
		case RIGHT:
			goRight();
			break;
		case DOWN:
			goDown();
			break;
		case LEFT:
			goLeft();
			break;
		case NONE:
			stop();
		}

	}

	private void updateTotalHitBox() {
		// 0
		// 3 1
		// 2
		if (velocity.x > 0) {
			currentDirection = RIGHT;
			oppositeDirection = LEFT;
			bPTotalHitBox.set(position.cpy().x, position.cpy().y
					+ BP_TOTAL_HITBOX_Y_OFF, width, BP_TOTAL_HITBOX_H);
			// BPCollision.set(position.x
			// + (world.getTileWidth() * .0225f)+(width/4), position.y
			// + (world.getTileWidth() * .0225f), world.getTileWidth()
			// - (world.getTileWidth() * .045f), world.getTileHeight()
			// - (world.getTileWidth() * .045f));
		} else if (velocity.x < 0) {
			currentDirection = LEFT;
			oppositeDirection = RIGHT;
			bPTotalHitBox.set(position.cpy().x, position.cpy().y
					+ BP_TOTAL_HITBOX_Y_OFF, width, BP_TOTAL_HITBOX_H);
			// BPCollision.set(position.x
			// + (world.getTileWidth() * .0225f)-(width/4), position.y
			// + (world.getTileWidth() * .0225f), world.getTileWidth()
			// - (world.getTileWidth() * .045f), world.getTileHeight()
			// - (world.getTileWidth() * .045f));
		} else if (velocity.y > 0) {
			currentDirection = UP;
			oppositeDirection = DOWN;
			bPTotalHitBox.set(position.cpy().x + width / 4
					+ ((this.height / 2f) - BP_TOTAL_HITBOX_Y_OFF),
					position.cpy().y - width / 4, BP_TOTAL_HITBOX_H, width);
			// BPCollision.set(position.x
			// + (world.getTileWidth() * .0225f), position.y
			// + (world.getTileWidth() * .0225f)+(height/4),
			// world.getTileWidth()
			// - (world.getTileWidth() * .045f), world.getTileHeight()
			// - (world.getTileWidth() * .045f));
		} else if (velocity.y < 0) {
			currentDirection = DOWN;
			oppositeDirection = UP;
			bPTotalHitBox.set(position.cpy().x + width / 4
					+ BP_TOTAL_HITBOX_Y_OFF, position.cpy().y - width / 4,
					BP_TOTAL_HITBOX_H, width);
			// BPCollision.set(position.x
			// + (world.getTileWidth() * .0225f), position.y
			// + (world.getTileWidth() * .0225f)-(height/4),
			// world.getTileWidth()
			// - (world.getTileWidth() * .045f), world.getTileHeight()
			// - (world.getTileWidth() * .045f));
		} else {
			currentDirection = NONE;
			oppositeDirection = NONE;
			bPTotalHitBox.set(position.cpy().x, position.cpy().y
					+ BP_TOTAL_HITBOX_Y_OFF, width, BP_TOTAL_HITBOX_H);
		}

	}

	private boolean checkBPCollision(Rectangle hitBox) {
		for (int i = 0; i < allBPCars.size(); i++) {
			if (collidable
					&& !allBPCars.get(i).getBPTotalHitBox().equals(hitBox)
					&& allBPCars.get(i).isCollidable()) {
				if (allBPCars.get(i).getBPTotalHitBox().overlaps(hitBox)) {

					return true;

				}
			}
		}
		return false;

	}

	private boolean checkBPOverlap(Rectangle hitBox) {
		for (int i = 0; i < allBPCars.size(); i++) {
			if (!allBPCars.get(i).getBPTotalHitBox().equals(hitBox)
					&& allBPCars.get(i).isCollidable()) {
				if (allBPCars.get(i).getBPTotalHitBox().overlaps(hitBox)) {

					return true;

				}
			}
		}
		return false;

	}

	private void updateBPCar() {

		// add twice the normal velocity if turning to keep from turning a
		// distance that won't get you anywhere
		if (tryLeft == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(-world.getTileWidth() * GameWorld.getTilesWide()
					/ velocityDivisor, 0);
			position.add(velocity.cpy());
		} else if (tryRight == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(world.getTileWidth() * GameWorld.getTilesWide()
					/ velocityDivisor, 0);
			position.add(velocity.cpy());
		} else if (tryUp == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(0, world.getTileWidth() * GameWorld.getTilesWide()
					/ velocityDivisor);
			position.add(velocity.cpy());
		} else if (tryDown == true) {
			previousVelocity.set(velocity.cpy());
			velocity.set(0, -world.getTileWidth() * GameWorld.getTilesWide()
					/ velocityDivisor);
			position.add(velocity.cpy());
		} else if (tryStop == true) {
			velocity.set(0, 0);
			tryStop = false;
		} else {
			position.add(velocity.cpy());
		}
		bPSmallHitBox.set(position.x + (world.getTileWidth() * .0225f)
				+ (width / 4), position.y + (world.getTileWidth() * .0225f),
				world.getTileWidth() - (world.getTileWidth() * .045f),
				world.getTileHeight() - (world.getTileWidth() * .045f));
		// BPCollision.setPosition(position.cpy());

		// don't keep trying to turn if you have turned
		if ((checkWallCollision(bPSmallHitBox) == false)
				&& ((tryLeft == true) || (tryRight == true) || (tryUp == true) || (tryDown == true))) {

			// scale velocity back to normal value since it was set at double
			// earlier

			if (!previousVelocity.hasSameDirection(velocity)
					&& !previousVelocity.hasOppositeDirection(velocity)
					&& TURN_TIME != 0 || turnTime > 0) {
				if (turnTime == 0) {
					turnTime = TURN_TIME;
				}
				position.add(velocity.cpy().scl(-1));

				bPSmallHitBox.set(position.x + (world.getTileWidth() * .0225f)
						+ (width / 4), position.y
						+ (world.getTileWidth() * .0225f), world.getTileWidth()
						- (world.getTileWidth() * .045f), world.getTileHeight()
						- (world.getTileWidth() * .045f));
			} else {
				velocity.set(velocity.cpy().scl(.5f));
				position.add(velocity.cpy().scl(-1));
				tryLeft = tryRight = tryUp = tryDown = false;
			}
		} else if ((checkWallCollision(bPSmallHitBox) == true)
				&& (tryLeft == true || tryRight == true || tryUp == true || tryDown == true)) {

			position.add(velocity.cpy().scl(-1));
			velocity.set(previousVelocity.cpy());
			position.add(velocity.cpy());
			bPSmallHitBox.set(position.x + (world.getTileWidth() * .0225f)
					+ (width / 4),
					position.y + (world.getTileWidth() * .0225f),
					world.getTileWidth() - (world.getTileWidth() * .045f),
					world.getTileHeight() - (world.getTileWidth() * .045f));

			// BPCollision.setPosition(position.cpy());
			if (checkWallCollision(bPSmallHitBox) == true) {
				position.add(velocity.cpy().scl(-1));
				// velocity.set(0, 0);
			}
		} else if ((checkWallCollision(bPSmallHitBox) == false)
				&& (tryLeft == false && tryRight == false && tryUp == false && tryDown == false)) {

		} else {
			position.add(velocity.cpy().scl(-1));
			// velocity.set(0, 0);
		}

	}

	private void wrapAround() {
		if (position.x > world.getTileWidth() * GameWorld.getTilesWide() - width / 2) {
			position.x = -width + (width / 2);
		} else if (position.x < -width + (width / 2)) {
			position.x = world.getTileWidth() * GameWorld.getTilesWide() - width / 2;
		} else if (position.y > world.getTileHeight()
				* GameWorld.getTilesHigh() - height / 2) {
			position.y = -height + (height / 2);
		} else if (position.y < -height + (height / 2)) {
			position.y = world.getTileHeight() * GameWorld.getTilesHigh()
					- height / 2;
		}

	}

	public boolean checkWallCollision(Rectangle thisRectangle) {

		for (int i = 0; i < allWalls.size(); i++) {
			if (thisRectangle.overlaps(allWalls.get(i))) {
				return true;
			}
		}

		return false;
	}

	public void goLeft() {
		tryLeft = true;
		tryDown = tryRight = tryUp = false;

	}

	public Rectangle getBPVision() {
		return bPVision;
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

	private void stop() {
		tryStop = true;
		tryUp = tryLeft = tryRight = tryDown = false;

	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public Vector2 getPosition() {
		return position;
	}

	public int getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(int currentDirection) {
		this.currentDirection = currentDirection;
	}

	public Rectangle getBPCollision() {
		return bPSmallHitBox;
	}

	public boolean isChasing() {
		return currentState == BPCarState.CHASE;
	}

	public boolean isPatrolling() {
		return currentState == BPCarState.PATROL;
	}

	public boolean isReturning() {
		return currentState == BPCarState.RETURN;
	}
	
	public boolean isScared() {
		return currentState == BPCarState.SCARED;
	}

	public void forceReturn() {
		returning = true;
	}

	public void increaseVelocity() {
		velocityDivisor -= 1.0;
		if (velocityDivisor < CRITICAL_VELOCITY) {
			velocityDivisor = CRITICAL_VELOCITY;
		}
	}

	public boolean isCollidable() {
		return collidable;
	}

}
