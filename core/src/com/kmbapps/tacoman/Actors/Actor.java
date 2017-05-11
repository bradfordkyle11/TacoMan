package com.kmbapps.tacoman.Actors;

import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.kmbapps.tacoman.GameWorld.Grid;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Actor {

	protected float width;
	protected float height;
	
	protected Vector2 velocity;
	protected Vector2 previousVelocity;
	protected Vector2 position;
	protected Grid containingGrid;
	protected GameWorld world;
	protected float speed;
	protected GameMath.DIRECTION dir = GameMath.DIRECTION.None;
	protected Rectangle hitbox;
	protected boolean collidable = true;
	
	public Actor(){
		
	}
	public Actor(GameWorld world){
		this.world = world;
		speed = world.getTileWidth()/16f;
		width = world.getTileWidth();
		height = world.getTileHeight();
		
	}
	
	public void update(float delta){
		containingGrid = world.getGrid(this);
		updateDir();

		updateVelocity(dir);
		move();
		containingGrid = world.getGrid(this);
		//snapToGrid();
	}

	public Vector2 getPosition(){
		return position;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
	public Vector2 getCenter(){
		return new Vector2(position.x + (width/2f), position.y + (height/2f));
	}
	
	
	public Rectangle getHitbox() {
		return hitbox;
	}
	
	
	
	public boolean isCollidable() {
		return collidable;
	}
	
	
	public GameMath.DIRECTION getDir() {
		return dir;
	}
	
	
	public Grid getContainingGrid() {
		return containingGrid;
	}
	//must be overridden
	protected void updateDir(){
		
	}
	
	
	//This moves the character by his velocity amount
	private void move(){
		Vector2 prevPosition = position.cpy();
		Vector2 futurePosition = position.cpy().add(velocity.cpy());
		float xDistToCenter1 = 0;
		float xDistToCenter2 = 0;
		float yDistToCenter1 = 0;
		float yDistToCenter2 = 0;
		if (world.getGrid(this)!=null){
			xDistToCenter1 = containingGrid.getCenter().x - getCenter().x;
			xDistToCenter2 = containingGrid.getCenter().x - (futurePosition.x + width/2f);
			yDistToCenter1 = containingGrid.getCenter().y - getCenter().y;
			yDistToCenter2 = containingGrid.getCenter().y - (futurePosition.y + height/2f);
		}
		
		if (Math.signum(xDistToCenter1)!=Math.signum(xDistToCenter2) || Math.signum(yDistToCenter1)!=Math.signum(yDistToCenter2)){
			if (xDistToCenter1 != 0 || yDistToCenter1 != 0){
				setCenter(containingGrid.getCenter().cpy());
			}
			else if ((xDistToCenter2!=0 && GameMath.floatEquals(0, xDistToCenter2)) || (yDistToCenter2!=0 && GameMath.floatEquals(0, yDistToCenter2))){
				setCenter(containingGrid.getCenter().cpy());
			}
			else {
				position.add(velocity.cpy());
			}
		}
		else {
			position.add(velocity.cpy());
		}
		hitbox.setPosition(position.cpy());
		
	}
	
	private void updateVelocity(GameMath.DIRECTION dir){
		Vector2 directionVector = GameMath.getDirectionalUnitVector(dir);
		velocity.set(speed*directionVector.x, speed*directionVector.y);
	}
	
	private void setCenter(Vector2 center){
		this.position.set(center.x - (width/2f), center.y - (height/2f));
	}
	
	
	

}
