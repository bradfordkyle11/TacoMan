package com.kmbapps.tacoman.GameWorld;

import java.util.ArrayList;

import com.kmbapps.tacoman.Actors.Actor;
import com.kmbapps.tacoman.Actors.Consumable;
import com.kmbapps.tacoman.Actors.Wall;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Grid {
	private float width;
	private float height;
	private float x;
	private float y;
	private Vector2 center;
	private Rectangle hitbox;
	private boolean blocked = false;

	public Grid parent;
	public double distance;

	private Grid left, right, above, below;

	// the Actor that is in the grid
	private ArrayList<Actor> content;
	private GameWorld world;

	// the consumable in the grid, such as taco or green card
	private Consumable consumable;

	public Grid(float x, float y, float width, float height, char content, GameWorld world) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.center = new Vector2(x + (width / 2f), y + (height / 2f));
		this.hitbox = new Rectangle(x, y, width, height);
		this.content = new ArrayList<Actor>();
		this.world = world;
		setContent(content);
	}

	public Grid getLeft() {
		return left;
	}

	public void setLeft(Grid left) {
		this.left = left;
	}

	public Grid getRight() {
		return right;
	}

	public void setRight(Grid right) {
		this.right = right;
	}

	public Grid getAbove() {
		return above;
	}

	public void setAbove(Grid above) {
		this.above = above;
	}

	public Grid getBelow() {
		return below;
	}

	public void setBelow(Grid below) {
		this.below = below;
	}

	// returns true if the grid contains a wall
	public boolean isBlocked() {
		return blocked;
	}

	// returns true if the grid contains a wall or a member of the same class
	public boolean isBlocked(Actor actor) {
		if (blocked) {
			return blocked;
		}
		for (Actor other : content) {
			if (!other.equals(actor)
					&& other.getClass().equals(actor.getClass())
					&& other.isCollidable()
					&& actor.isCollidable()) {
				return true;
			}
		}

		return false;
	}

	public Grid getAdjacentGrid(GameMath.DIRECTION dir) {
		switch (dir) {
		case Up:
			return getAbove();
		case Down:
			return getBelow();
		case Left:
			return getLeft();
		case Right:
			return getRight();
		default:
			return null;
		}
	}

	private void setContent(char content) {
		switch (content) {
		case Map.WALL:
			this.content.add(new Wall(x, y, width, height));
			blocked = true;
			break;
		case Map.EMPTY:
			break;
		case Map.BORDER_PATROL:
			break;
		case Map.CHARACTER:
			break;

		default:
			consumable = new Consumable(content, width, height);
			break;

		}
	}

	public boolean contains(Actor actor) {
		float centerX = actor.getPosition().x + (actor.getWidth() / 2f);
		float centerY = actor.getPosition().y + (actor.getHeight() / 2f);
		return hitbox.contains(centerX, centerY);
	}

	public boolean contains(Vector2 point) {
		return hitbox.contains(point.cpy());
	}

	public void update() {
		ArrayList<Actor> copy = (ArrayList) content.clone();
		for (Actor actor : content) {
			if (!contains(actor)) {
				copy.remove(actor);
			}
			content = copy;
		}
	}

	public boolean centeredInGrid(Actor actor) {
		return actor.getCenter().x == center.x
				&& actor.getCenter().y == center.y;
	}

	public Vector2 getCenter() {
		return center;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Consumable getConsumable() {
		return consumable;
	}

	public void removeConsumable() {
		consumable = null;
	}

	public void addContent(Actor actor) {
		if (!content.contains(actor)) {
			content.add(actor);
		}
	}

	public ArrayList<Actor> getContent() {
		return content;
	}

	public GameMath.DIRECTION directionTo(Grid adjacent) {
		if (adjacent.equals(above)) {
			return GameMath.DIRECTION.Up;
		}
		if (adjacent.equals(below)) {
			return GameMath.DIRECTION.Down;
		}
		if (adjacent.equals(left)) {
			return GameMath.DIRECTION.Left;
		}
		if (adjacent.equals(right)) {
			return GameMath.DIRECTION.Right;
		}
		return GameMath.DIRECTION.None;
	}

}
