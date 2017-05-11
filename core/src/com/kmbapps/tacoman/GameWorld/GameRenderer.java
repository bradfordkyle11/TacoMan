package com.kmbapps.tacoman.GameWorld;

import java.util.ArrayList;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.kmbapps.tacoman.Actors.BPCar;
import com.kmbapps.tacoman.Actors.Consumable;
import com.kmbapps.tacoman.Actors.MainCharacter;
import com.kmbapps.tacoman.Helpers.AssetLoader;
import com.kmbapps.tacoman.Helpers.FontFactory;
import com.kmbapps.tacoman.Helpers.GameMath;
import com.kmbapps.tacoman.TweenAccessors.SpriteAccessor;

public class GameRenderer {
	
	public static final int TEXTURE_GRID_W = 64;
	public static final int TEXTURE_GRID_H = 64;
	
	public static float yGridOffset;
	
	private float midPointX;
	private float midPointY;
	private OrthographicCamera camera;
	private ShapeRenderer shapeRenderer;
	private SpriteBatch spriteBatch;
	private Map map;
	private GameWorld world;
	private Stage HUD;
	private Label scoreLabel;
	private Label levelLabel;

	private String platform;

	private TweenManager tweenManager;

	private int lives;

	private Texture tacoTexture, walls, tacoManTexture, bPCarTexture,
			bPCarChaseTexture, greenCardTexture, readyScreensTexture,
			readyTouchScreenTexture;
	private TextureRegion taco, wall, tacoMan;
	private Sprite bPCar, bPCarRight, bPCarLeft, bPCarUp, bPCarDown,
			bPCarChaseRight, bPCarChaseLeft, bPCarChaseUp, tacoManRight,
			tacoManLeft, tacoManUp, tacoManDown, greenCard, desktopReadyPrompt,
			touchScreenReadyPrompt;

	private Animation currentMexMan;
	private Sprite player;
	private Sprite alphaTween;

	private float tileWidth;
	private float tileHeight;

	private float tacoWidth;
	private float tacoHeight;

	private ArrayList<Rectangle> allTacos;
	private ArrayList<BPCar> allBPCars;

	private MainCharacter playerCharacter;
	private Sprite bPCarDead;
	
	BitmapFont scoreFont;

	public GameRenderer(GameWorld world) {
		this.world = world;
		this.platform = platform;
		camera = new OrthographicCamera();
		camera.setToOrtho(false);

		currentMexMan = AssetLoader.tacoManAnimR;

		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());

		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setProjectionMatrix(camera.combined);

		tileWidth = world.getTileWidth();
		tileHeight = world.getTileHeight();

		allTacos = world.getAllTacos();
		allBPCars = world.getAllBPCars();

		map = world.getMap();

		bPCarDead = new Sprite((TextureRegion)AssetLoader.bPCarAnimR.getKeyFrame(0));
		alphaTween = new Sprite();
		AssetLoader.powerUp.setBounds(0, 0, Gdx.graphics.getWidth(), tileHeight*GameWorld.getTilesHigh());
		
		yGridOffset = Gdx.graphics.getHeight()- ((GameWorld.getTilesHigh()+1)*tileHeight);
		
		HUD = new Stage();
		Table HUDTable = new Table();
		HUDTable.align(Align.topLeft);
		HUDTable.moveBy(world.getXOffs(), 0);
		HUDTable.setFillParent(true);
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		if (scoreFont==null){
			scoreFont = FontFactory.generateFont((int)tileHeight);
		}
		
		
		levelLabel = new Label("Lvl 1", uiSkin);
		LabelStyle levelLabelStyle = levelLabel.getStyle();
		levelLabelStyle.font = scoreFont;
		levelLabelStyle.fontColor = Color.BLACK;
		levelLabel.setStyle(levelLabelStyle);
		HUDTable.add(levelLabel);
		
		scoreLabel = new Label("Score: 0", uiSkin);
		LabelStyle scoreLabelStyle = scoreLabel.getStyle();
		scoreLabelStyle.font = scoreFont;
		scoreLabelStyle.fontColor = Color.BLACK;
		scoreLabel.setStyle(scoreLabelStyle);
		HUDTable.add(scoreLabel).top().space(20f);
		
		HUD.addActor(HUDTable);
		

		//loadImages()
		initActors();

		Timeline.createSequence()
				.push(Tween.to(alphaTween, SpriteAccessor.ALPHA, .4f).target(.4f))
				.push(Tween.to(alphaTween, SpriteAccessor.ALPHA, .4f).target(1))
				.repeat(Tween.INFINITY, 0f)
				.start(tweenManager);
		
	}

	private void initActors() {
		playerCharacter = world.getPlayerCharacter();

	}


	public void render(float delta, float runTime) {
		if (world.isReady()) {
			map = world.getMap();
			initActors();
		}

		tweenManager.update(delta);


		Gdx.gl.glClearColor(1, 1, 1, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(Color.valueOf("fbfab3"));
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
			shapeRenderer.end();
		

		// render hitboxes

		//drawHitBoxes();

		spriteBatch.begin();

		drawMap();
//		drawTacos();
//		drawGreenCards();
		drawConsumables();
		drawBPCars(runTime);
		drawMexMan(runTime);

//		shapeRenderer.begin(ShapeType.Filled);
//
//		shapeRenderer.setColor(255, 255, 255, 1);
//		shapeRenderer.rect(
//				0,
//				world.getTileHeight() * GameWorld.getTilesHigh(),
//				Gdx.graphics.getWidth(),
//				Gdx.graphics.getHeight()
//						- (world.getTileHeight() * GameWorld.getTilesHigh()));
//		shapeRenderer.end();

		//drawLives();
		if (world.isReady()) {
			drawReadyPrompt();
		}
		if (playerCharacter.hasGreenCard()){
			drawPowerUpIndicator();
		}
		
		//newDefaultFont.draw(spriteBatch, Integer.toString(playerCharacter.getScore()), 0, 0);

		
		spriteBatch.end();
		shapeRenderer.begin(ShapeType.Filled);

		//draw top and bottom hud background
		shapeRenderer.setColor(Color.valueOf("fbfab3"));
		shapeRenderer.rect(0, Gdx.graphics.getHeight()-tileHeight - world.getYOffs(), Gdx.graphics.getWidth(),
				tileHeight);
		shapeRenderer.setColor(Color.valueOf("000000"));
		shapeRenderer.rect(0, 0, world.getXOffs(), Gdx.graphics.getHeight());
		shapeRenderer.rect(Gdx.graphics.getWidth() - world.getXOffs(), 0, world.getXOffs(), Gdx.graphics.getHeight());
		//shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), yGridOffset);	
		shapeRenderer.end();
		
		spriteBatch.begin();
		drawLives();
		spriteBatch.end();
		scoreLabel.setText("Score: " + Integer.toString(playerCharacter.getScore()));
		levelLabel.setText("Lvl " + world.getCurrentLevelNum());
		HUD.draw();
	}

	private void drawPowerUpIndicator() {
		AssetLoader.powerUp.setColor(AssetLoader.powerUp.getColor().r, AssetLoader.powerUp.getColor().g, AssetLoader.powerUp.getColor().b, alphaTween.getColor().a);
		AssetLoader.powerUp.setPosition(0, yGridOffset);
		AssetLoader.powerUp.draw(spriteBatch);
	}

	private void drawReadyPrompt() {
		if (Gdx.app.getType() == Application.ApplicationType.Android
				|| Gdx.app.getType() == Application.ApplicationType.iOS) {
			AssetLoader.touchScreenReadyPrompt.draw(spriteBatch);
		} else {
			AssetLoader.desktopReadyPrompt.draw(spriteBatch);
		}
	}
	
	private void drawConsumables(){
		for (ArrayList<Grid> row : map.getGridMap()){
			for (Grid grid : row){
				Consumable c = grid.getConsumable();
				if (c != null){
					if (c.getType() == Consumable.TYPE.Taco){
						spriteBatch.draw(AssetLoader.taco, grid.getX() + (tileWidth - c.getWidth())/2 + world.getXOffs(), 
								grid.getY() + (tileHeight - c.getHeight())/2 + yGridOffset - world.getYOffs(),
								c.getWidth(), c.getHeight());
					}
					else if (c.getType() == Consumable.TYPE.GreenCard){
						AssetLoader.greenCard.setBounds(grid.getX() + world.getXOffs(), grid.getY() + yGridOffset - world.getYOffs(), tileWidth, tileHeight);
						AssetLoader.greenCard.draw(spriteBatch);
					}
				}
			}
		}
	}

	private void drawGreenCards() {
		for (int i = 0; i < world.getAllGreenCards().size(); i++) {
			AssetLoader.greenCard.setBounds(world.getAllGreenCards().get(i).x + world.getXOffs(), world
					.getAllGreenCards().get(i).y + yGridOffset - world.getYOffs(), tileWidth, tileHeight);
			AssetLoader.greenCard.draw(spriteBatch);
		}

	}

	private void drawLives() {
		int offset = 1;
		this.lives = playerCharacter.getLives();
		for (int i = lives; i > 0; i--) {
			AssetLoader.lifeSprite.setBounds(Gdx.graphics.getWidth() - tileWidth * offset - world.getXOffs(),
					Gdx.graphics.getHeight() - tileHeight, tileWidth,
					tileHeight);
			AssetLoader.lifeSprite.draw(spriteBatch);
			offset += 1;
		}

	}

	private void drawHitBoxes() {
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(1, 0, 0, 1);

		// BP car entire hit box
		for (int i = 0; i < allBPCars.size(); i++) {

			/* ** BP total hit box ** */
			 shapeRenderer.rect(allBPCars.get(i).getBPTotalHitBox().x,
			 allBPCars
			 .get(i).getBPTotalHitBox().y + yGridOffset, allBPCars.get(i)
			 .getBPTotalHitBox().width, allBPCars.get(i)
			 .getBPTotalHitBox().height);

			/* ** BP Vision ** */
			 shapeRenderer.rect(allBPCars.get(i).getBPVision().x,
			 allBPCars.get(i).getBPVision().y + yGridOffset, allBPCars.get(i)
			 .getBPVision().width, allBPCars.get(i)
			 .getBPVision().height);

			/* ** BP small hit box ** */
//			 shapeRenderer.rect(allBPCars.get(i).getBPCollision().x, allBPCars
//			 .get(i).getBPCollision().y + yOffset, allBPCars.get(i)
//			 .getBPCollision().width,
//			 allBPCars.get(i).getBPCollision().height);

		}

		/* ** Player hit box ** */
//		shapeRenderer.rect(
//				world.getPlayerCharacter().getCharacterCollision().x, world
//						.getPlayerCharacter().getCharacterCollision().y, world
//						.getPlayerCharacter().getCharacterCollision()
//						.getWidth(), world.getPlayerCharacter()
//						.getCharacterCollision().getHeight());
		shapeRenderer.end();

	}

	private void drawMexMan(float runTime) {

		if (playerCharacter.getVelocity().x > 0) {
			currentMexMan = AssetLoader.tacoManAnimR;
		} else if (playerCharacter.getVelocity().x < 0) {
			currentMexMan = AssetLoader.tacoManAnimL;
		} else if (playerCharacter.getVelocity().y > 0) {
			currentMexMan = AssetLoader.tacoManAnimU;
		} else if (playerCharacter.getVelocity().y < 0) {
			currentMexMan = AssetLoader.tacoManAnimD;
		} else if (playerCharacter.isReset()) {
			currentMexMan = AssetLoader.tacoManAnimR;
		}

		if (playerCharacter.getVelocity().len() > 0) {
			player = new Sprite((TextureRegion)currentMexMan.getKeyFrame(runTime));
			player.setBounds(playerCharacter.getPosition().x + world.getXOffs(),
					playerCharacter.getPosition().y + yGridOffset - world.getYOffs(), tileWidth, tileHeight);
			player.draw(spriteBatch);
			// spriteBatch.draw(currentMexMan.getKeyFrame(runTime),
			// playerCharacter.getPosition().x + world.getXOffs(), playerCharacter.getPosition().y,
			// tileWidth, tileHeight);
		} else {
			player = new Sprite((TextureRegion)currentMexMan.getKeyFrame(.2f));
			player.setBounds(playerCharacter.getPosition().x + world.getXOffs(),
					playerCharacter.getPosition().y + yGridOffset - world.getYOffs(), tileWidth, tileHeight);
			player.draw(spriteBatch);
			// spriteBatch.draw(currentMexMan.getKeyFrame(.2f),
			// playerCharacter.getPosition().x + world.getXOffs(), playerCharacter.getPosition().y,
			// tileWidth, tileHeight);
		}

	}

	private void drawBPCars(float runTime) {
		for (int i = 0; i < allBPCars.size(); i++) {

			if (allBPCars.get(i).isChasing()) {
				if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Right
						|| allBPCars.get(i).getDir() == GameMath.DIRECTION.None) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarChaseAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Left) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarChaseAnimL.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Up) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarChaseAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.setOrigin(tileWidth, tileHeight / 2);
					bPCar.setRotation(allBPCars.get(i).getCurrentDirection() * 90 + 90);
					bPCar.draw(spriteBatch);

				} else {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarChaseAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.setOrigin(tileWidth, tileHeight / 2);
					bPCar.setRotation(allBPCars.get(i).getCurrentDirection() * 90 + 90);
					bPCar.draw(spriteBatch);
				}
			} else if (allBPCars.get(i).isPatrolling() || allBPCars.get(i).isScared()) {
				if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Right
						|| allBPCars.get(i).getDir() == GameMath.DIRECTION.None) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Left) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimL.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Up) {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.setOrigin(tileWidth, tileHeight / 2);
					bPCar.setRotation(allBPCars.get(i).getCurrentDirection() * 90 + 90);
					bPCar.draw(spriteBatch);

				} else {
					bPCar = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCar.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(), allBPCars
							.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2, tileHeight);
					bPCar.setOrigin(tileWidth, tileHeight / 2);
					bPCar.setRotation(allBPCars.get(i).getCurrentDirection() * 90 + 90);
					bPCar.draw(spriteBatch);
				}
			}

			else if (allBPCars.get(i).isReturning()) {
				if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Right
						|| allBPCars.get(i).getDir() == GameMath.DIRECTION.None) {
					bPCarDead = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCarDead.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(),
							allBPCars.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2,
							tileHeight);
					bPCarDead.setColor(bPCarDead.getColor().r, bPCarDead.getColor().g, bPCarDead.getColor().b, alphaTween.getColor().a);
					bPCarDead.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Left) {
					bPCarDead = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimL.getKeyFrame(runTime));
					bPCarDead.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(),
							allBPCars.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2,
							tileHeight);
					bPCarDead.setColor(bPCarDead.getColor().r, bPCarDead.getColor().g, bPCarDead.getColor().b, alphaTween.getColor().a);
					bPCarDead.draw(spriteBatch);

				} else if (allBPCars.get(i).getDir() == GameMath.DIRECTION.Up) {
					bPCarDead = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCarDead.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(),
							allBPCars.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2,
							tileHeight);
					bPCarDead.setOrigin(tileWidth, tileHeight / 2);
					bPCarDead.setRotation(allBPCars.get(i)
							.getCurrentDirection() * 90 + 90);
					bPCarDead.setColor(bPCarDead.getColor().r, bPCarDead.getColor().g, bPCarDead.getColor().b, alphaTween.getColor().a);
					bPCarDead.draw(spriteBatch);

				} else {
					bPCarDead = new Sprite((TextureRegion)
							AssetLoader.bPCarAnimR.getKeyFrame(runTime));
					bPCarDead.setBounds(allBPCars.get(i).getPosition().x + world.getXOffs(),
							allBPCars.get(i).getPosition().y + yGridOffset - world.getYOffs(), tileWidth * 2,
							tileHeight);
					bPCarDead.setOrigin(tileWidth, tileHeight / 2);
					bPCarDead.setRotation(allBPCars.get(i)
							.getCurrentDirection() * 90 + 90);
					bPCarDead.setColor(bPCarDead.getColor().r, bPCarDead.getColor().g, bPCarDead.getColor().b, alphaTween.getColor().a);
					bPCarDead.draw(spriteBatch);
				}
			}
		}
	}

	private void drawTacos() {
		for (int i = 0; i < allTacos.size(); i++) {
			spriteBatch.draw(AssetLoader.taco, allTacos.get(i).x + world.getXOffs(), allTacos.get(i).y + yGridOffset - world.getYOffs(),
					tileWidth, tileHeight/2,
					allTacos.get(i).getWidth(), allTacos.get(i).getHeight(), 1,
					1, 0);
		}

	}

	private void drawMap() {
		for (int x = 0; x < GameWorld.getTilesWide(); x++) {
			for (int y = 0; y < GameWorld.getTilesHigh(); y++) {
				switch (map.map[x][y]) {
				case 'x':
					spriteBatch.draw(AssetLoader.wall, x * tileWidth + world.getXOffs(),
							((GameWorld.getTilesHigh() - 1 - y) * tileHeight) + yGridOffset - world.getYOffs(),
							tileWidth, tileHeight);
					break;
				}
			}

		}

	}

	public void resetMexMan() {
		currentMexMan = AssetLoader.tacoManAnimR;
	}

	public void dispose() {
		spriteBatch.dispose();
		shapeRenderer.dispose();
//		tacoTexture.dispose();
//		walls.dispose();
//		tacoManTexture.dispose();
//		bPCarTexture.dispose();
//		bPCarChaseTexture.dispose();
//		greenCardTexture.dispose();
//		readyScreensTexture.dispose();
//		readyTouchScreenTexture.dispose();
	}
	
	

}
