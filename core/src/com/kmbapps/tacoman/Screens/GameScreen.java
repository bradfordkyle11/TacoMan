package com.kmbapps.tacoman.Screens;

import java.io.IOException;

import com.kmbapps.tacoman.TacoMan;
import com.kmbapps.tacoman.GameWorld.GameRenderer;
import com.kmbapps.tacoman.GameWorld.GameWorld;
import com.kmbapps.tacoman.Helpers.ButtonFactory;
import com.kmbapps.tacoman.Helpers.FontFactory;
import com.kmbapps.tacoman.Helpers.InputHandler;
import com.kmbapps.tacoman.Helpers.MyGestureListener;
import com.kmbapps.tacoman.Helpers.MyPrefs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class GameScreen implements Screen {


	private GameWorld gameWorld;
	private GameRenderer renderer;
	private TacoMan game;
	private Stage pauseMenu, gameOverMenu;
	private SpriteBatch batch;
	private int difficulty;
	
	private TextButton mainMenuButton, mainMenuButton2, resumeButton, restartButton, restartButton2;
	
	private Label currScoreLabel, hiScoreLabel;

	float midPointX;
	float midPointY;
	private float runTime;

	public GameScreen(final TacoMan game, int difficulty) throws IOException {
		this.difficulty = difficulty;
		this.game = game;
		batch = new SpriteBatch();
		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();
		float gameWidth = screenWidth;
		float gameHeight = screenHeight;
		int midPointY = (int) (gameHeight / 2);
		int midPointX = (int) (gameWidth / 2);
		runTime = 0;
		pauseMenu = new Stage();
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));

		Table pauseTable = new Table();
		
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.down = uiSkin.getDrawable("default-round-down");
		textButtonStyle.up = uiSkin.getDrawable("default-round");
		textButtonStyle.font = new BitmapFont(Gdx.files.internal("large.fnt"));
		

		mainMenuButton = (TextButton) ButtonFactory.getButton("Main Menu", ButtonFactory.TEXT_BUTTON);
		mainMenuButton.setColor(mainMenuButton.getColor().r, mainMenuButton.getColor().g, mainMenuButton.getColor().b, .90f);
		mainMenuButton2 = (TextButton) ButtonFactory.getButton("Main Menu", ButtonFactory.TEXT_BUTTON);
		mainMenuButton2.setColor(mainMenuButton2.getColor().r, mainMenuButton2.getColor().g, mainMenuButton2.getColor().b, .90f);
		resumeButton = (TextButton) ButtonFactory.getButton("Resume", ButtonFactory.TEXT_BUTTON);
		resumeButton.setColor(resumeButton.getColor().r, resumeButton.getColor().g, resumeButton.getColor().b, .90f);
		restartButton = (TextButton) ButtonFactory.getButton("Restart", ButtonFactory.TEXT_BUTTON);
		restartButton.setColor(restartButton.getColor().r, restartButton.getColor().g, restartButton.getColor().b, .90f);
		restartButton2 = (TextButton) ButtonFactory.getButton("Restart", ButtonFactory.TEXT_BUTTON);
		restartButton2.setColor(restartButton2.getColor().r, restartButton2.getColor().g, restartButton2.getColor().b, .90f);
		
		pauseTable.setFillParent(true);
		pauseTable.add(mainMenuButton).prefWidth(gameWidth* TacoMan.BUTTON_W_SCALE).prefHeight(gameHeight* TacoMan.BUTTON_H_SCALE).space(gameWidth/20);
		pauseTable.row();
		pauseTable.add(resumeButton).prefWidth(gameWidth* TacoMan.BUTTON_W_SCALE).prefHeight(gameHeight* TacoMan.BUTTON_H_SCALE).space(gameWidth/20);
		pauseTable.row();
		pauseTable.add(restartButton).prefWidth(gameWidth* TacoMan.BUTTON_W_SCALE).prefHeight(gameHeight* TacoMan.BUTTON_H_SCALE).space(gameWidth/20);
		pauseMenu.addActor(pauseTable);
		
		

		gameWorld = new GameWorld(midPointY, midPointX, difficulty);
		
		BitmapFont f = FontFactory.generateFont((int) gameWorld.getTileHeight()*2);
		currScoreLabel = new Label("", uiSkin);
		LabelStyle scoreLabelStyle = currScoreLabel.getStyle();
		scoreLabelStyle.font = f;
		scoreLabelStyle.fontColor = Color.BLACK;
		currScoreLabel.setStyle(scoreLabelStyle);
		
		hiScoreLabel = new Label("", uiSkin);
		hiScoreLabel.setStyle(scoreLabelStyle);
		gameOverMenu = new Stage();
		Table gameOverTable = new Table();
		gameOverTable.setFillParent(true);
		gameOverTable.add(currScoreLabel);
		gameOverTable.row();
		gameOverTable.add(hiScoreLabel);
		gameOverTable.row();
		gameOverTable.add(mainMenuButton2).prefWidth(gameWidth* TacoMan.BUTTON_W_SCALE).prefHeight(gameHeight* TacoMan.BUTTON_H_SCALE).space(gameWidth/20);
		gameOverTable.row();
		gameOverTable.add(restartButton2).prefWidth(gameWidth* TacoMan.BUTTON_W_SCALE).prefHeight(gameHeight* TacoMan.BUTTON_H_SCALE).space(gameWidth/20);
		gameOverMenu.addActor(gameOverTable);

		switch (Gdx.app.getType()) {
		default:
			Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
					gameWorld), new GestureDetector(new MyGestureListener(gameWorld))));
			break;
		case iOS:
			Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
					gameWorld), new GestureDetector(new MyGestureListener(
					gameWorld))));
			break;
		case Android:
			Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
					gameWorld), new GestureDetector(new MyGestureListener(
					gameWorld))));
			break;

		}
		renderer = new GameRenderer(gameWorld);

		
		//listener implementations for the buttons
		updateButtonListeners();
		
	}

	private void updateButtonListeners() {
		mainMenuButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				try {
					gameWorld.resetGame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.setScreen(game.getMainMenu());
			}
		});
		mainMenuButton2.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				try {
					gameWorld.resetGame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.setScreen(game.getMainMenu());
			}
		});

		resumeButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				
				//reset the input processor
				switch (Gdx.app.getType()) {
				default:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld)));
					break;
				case iOS:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;
				case Android:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;

				}
				gameWorld.resume();
			}
		});
		
		restartButton.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					gameWorld.resetGame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//reset the input processor
				switch (Gdx.app.getType()) {
				default:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld)));
					break;
				case iOS:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;
				case Android:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;

				}
			}
			
		});
		restartButton2.addListener(new ChangeListener(){

			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					gameWorld.resetGame();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//reset the input processor
				switch (Gdx.app.getType()) {
				default:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld)));
					break;
				case iOS:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;
				case Android:
					Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
							gameWorld), new GestureDetector(new MyGestureListener(
							gameWorld))));
					break;

				}
			}
			
		});
		
	}

	@Override
	public void render(float delta) {
		float myDelta = 1.0f / 60.0f;

//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		if (gameWorld.isPaused()) {
			Gdx.input.setInputProcessor(pauseMenu);
			renderer.render(myDelta, runTime);
			pauseMenu.act();
			pauseMenu.draw();
		} else if (gameWorld.isGameOver()){
			Gdx.input.setInputProcessor(gameOverMenu);
			currScoreLabel.setText("Score: " + Integer.toString(gameWorld.getPlayerCharacter().getScore()));
			hiScoreLabel.setText("Hi-score: " + Integer.toString(MyPrefs.getHiScore()));
			renderer.render(myDelta, runTime);
			gameOverMenu.act();
			gameOverMenu.draw();
		}
		else {
			runTime += delta;
			try {
				gameWorld.update(myDelta);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			renderer.render(myDelta, runTime);
		}

		

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		switch (Gdx.app.getType()) {
		default:
			Gdx.input.setInputProcessor(new InputHandler(gameWorld));
			break;
		case iOS:
			Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
					gameWorld), new GestureDetector(new MyGestureListener(
					gameWorld))));
			break;
		case Android:
			Gdx.input.setInputProcessor(new InputMultiplexer(new InputHandler(
					gameWorld), new GestureDetector(new MyGestureListener(
					gameWorld))));
			break;

		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		renderer.dispose();
		gameOverMenu.dispose();
		pauseMenu.dispose();
		batch.dispose();
		

	}

}
