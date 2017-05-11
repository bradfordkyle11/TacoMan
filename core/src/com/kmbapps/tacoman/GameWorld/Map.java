package com.kmbapps.tacoman.GameWorld;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.kmbapps.tacoman.Actors.Wall;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.badlogic.gdx.Gdx;

public class Map {
	public static final char WALL = 'x';
	public static final char EMPTY = ' ';
	public static final char BORDER_PATROL = 'B';
	public static final char CHARACTER = 'C';
	
	public char[][] map;
	private ArrayList<ArrayList<Grid>> gridMap;
	private BufferedReader bufferedReader;
	private int mapWidth;
	private int mapHeight;
	private String line;
	private String mapFilename = "maps-16-9.txt";
	private int charPerMap; //17 because 2 character return at the end of each line, +2 because return at the end of each maze
	private GameWorld gameWorld;
	
	public Map() throws FileNotFoundException{
		mapWidth = (int) GameWorld.getTilesWide();
		mapHeight = (int) GameWorld.getTilesHigh();
		charPerMap = (mapWidth + 2) * mapHeight + 2;//17 because 2 character return at the end of each line, +2 because return at the end of each maze
		map = new char[mapWidth][mapHeight];
		gridMap = new ArrayList<ArrayList<Grid>>();

	}
	
	public void loadMap(int mapNumber, GameWorld gameWorld) throws IOException{
		this.gameWorld = gameWorld;
		bufferedReader =Gdx.files.internal(mapFilename).reader(8192);
		bufferedReader.skip(charPerMap*mapNumber);
		for (int i = 0; true; i++){
			line = bufferedReader.readLine();
			if (line == null){
				break;
			}
			else if (line.equals("")){
				break;
			}
			for(int j = 0; j<mapWidth; j++){
				map[j][i]=line.charAt(j);
			}
			
		}
		bufferedReader.close();
		
		generateGridMap();
		
	}
	
	public ArrayList<ArrayList<Grid>> getGridMap(){
		return gridMap;
	}
	
	public Grid getAdjacentGrid(Grid grid, GameMath.DIRECTION dir){
		switch (dir){
		case Up:
			return grid.getAbove();
		case Down:
			return grid.getBelow();
		case Left:
			return grid.getLeft();
		case Right:
			return grid.getRight();
		default:
			return null;
		}
	}
	
	private void generateGridMap(){
		gridMap.clear();
		
		//add all the rows to the grid map
		for (int i = 0; i < mapHeight; i++){
			gridMap.add(new ArrayList<Grid>());
		}
		//start from the bottom left because xy coord of 0,0 is at the bottom left
		for (int i = mapHeight - 1, y = 0; i >= 0;i--, y++){
			for(int j = 0; j<mapWidth; j++){
					//Grid grid = new Grid(j * gameWorld.getTileWidth(), y * gameWorld.getTileHeight(), gameWorld.getTileWidth(), gameWorld.getTileHeight(), new Wall());
					gridMap.get(y).add(new Grid(j * gameWorld.getTileWidth(), y * gameWorld.getTileHeight(), gameWorld.getTileWidth(), gameWorld.getTileHeight(), map[j][i],
							gameWorld));
			}
			
		}
		
		for (int i = 0; i < gridMap.size(); i++){
			for (int j = 0; j < gridMap.get(i).size(); j++){
				Grid grid = gridMap.get(i).get(j);
				if (j-1 >= 0){
					grid.setLeft(gridMap.get(i).get(j-1));
				}
				else {
					grid.setLeft(gridMap.get(i).get(gridMap.get(i).size()-1));
				}
				if (j+1 < gridMap.get(i).size()){
					grid.setRight(gridMap.get(i).get(j+1));
				}
				else {
					grid.setRight(gridMap.get(i).get(0));
				}
				
				if (i+1 < gridMap.size()){
					grid.setAbove(gridMap.get(i+1).get(j));
				}
				else {
					grid.setAbove(gridMap.get(0).get(j));
				}
				if (i-1 >= 0){
					grid.setBelow(gridMap.get(i-1).get(j));
				}
				else {
					grid.setBelow(gridMap.get(gridMap.size()-1).get(j));
				}
				
			}
		}
		
		
	}
	
	
	
}
