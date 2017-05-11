package com.kmbapps.tacoman.Screens;

import com.kmbapps.tacoman.TacoMan;
import com.kmbapps.tacoman.Helpers.ButtonFactory;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MainMenuScreen implements Screen {
	private TacoMan game;
	private Stage stage;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	public MainMenuScreen(final TacoMan game){
		this.game = game;
		batch = new SpriteBatch();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		TextButton playButton = (TextButton) ButtonFactory.getButton("Play!", ButtonFactory.TEXT_BUTTON);
		TextButton settingsButton = (TextButton) ButtonFactory.getButton("Settings", ButtonFactory.TEXT_BUTTON);
		
		Table table = new Table();
		table.setFillParent(true);
		table.add(playButton).prefWidth(Gdx.graphics.getWidth()* TacoMan.BUTTON_W_SCALE).prefHeight(Gdx.graphics.getHeight()* TacoMan.BUTTON_H_SCALE).space(Gdx.graphics.getWidth()/20);
		table.row();
		table.add(settingsButton).prefWidth(Gdx.graphics.getWidth()* TacoMan.BUTTON_W_SCALE).prefHeight(Gdx.graphics.getHeight()* TacoMan.BUTTON_H_SCALE).space(Gdx.graphics.getWidth()/20);
		stage.addActor(table);
		
		playButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(game.getGameScreen());
			}
		});
		
		settingsButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(game.getSettingsScreen());
			}
		});
		
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		
		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(255, 255, 255, 1);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		shapeRenderer.end();
		
		batch.begin();
		stage.draw();
		batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
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
		stage.dispose();
		batch.dispose();
		shapeRenderer.dispose();
		
	}

}
