package com.kmbapps.tacoman.Actors;

public class Consumable {

	public static enum TYPE {
		Taco, GreenCard 
	}
	
	private TYPE type;
	private int value;
	private float width;
	private float height;
	
	public Consumable(char c, float gridW, float gridH){
		width = gridW;
		height = gridH;
		type = getType(c);
		
		if (type == TYPE.Taco){
			width = 30f * (gridW/32f);
			height = 14.5f * (width/30f);
		}
		
		value = getValue(type);
	}
	
	public TYPE getType(){
		return type;
	}
	
	public int getValue(){
		return value;
	}
	
	
	
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	private TYPE getType(char c){
		switch (c){
		case '.':
			return TYPE.Taco;
		case 'G':
			return TYPE.GreenCard;
		default:
			return null;	
		}
	}
	
	private int getValue(TYPE t){
		if (type == null){
			return 0;
		}
		switch (t){
			case Taco:
				return 10;
			case GreenCard:
				return 360;
			default:
				return 0;
		}
	}
	
	
}
