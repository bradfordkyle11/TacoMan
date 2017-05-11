package com.kmbapps.tacoman.Screens;

import com.kmbapps.tacoman.TacoMan;
import com.kmbapps.tacoman.Helpers.ButtonFactory;
import com.kmbapps.tacoman.Helpers.FontFactory;
import com.kmbapps.tacoman.Helpers.MyPrefs;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.esotericsoftware.tablelayout.Cell;
import com.badlogic.gdx.utils.Align;

public class SettingsScreen implements Screen{

	private float H1_FONT_SCALE;
	private float H2_FONT_SCALE;
	private TacoMan game;
	private Stage stage;
	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;
	
	public SettingsScreen(final TacoMan game){
		this.game = game;
		MyPrefs.load();
		batch = new SpriteBatch();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		String[] controlTypes = {"Swipe"};
		
		
		
		Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		BitmapFont newDefaultFont = FontFactory.generateFont(Gdx.graphics.getWidth()/10);
		uiSkin.add("default-font", newDefaultFont, BitmapFont.class);
		
		
		Label settingsLabel = new Label("Settings", uiSkin);
		LabelStyle settingsLabelStyle = settingsLabel.getStyle();
		settingsLabelStyle.font = newDefaultFont;
		settingsLabel.setStyle(settingsLabelStyle);
		
		
		newDefaultFont = FontFactory.generateFont(Gdx.graphics.getWidth()/15);
		final SelectBox<String> controlType = new SelectBox<String>(uiSkin);
		SelectBoxStyle sbs = controlType.getStyle();
		sbs.font = newDefaultFont;
		ListStyle sbListStyle = sbs.listStyle;
		sbListStyle.font = newDefaultFont;
		sbs.listStyle = sbListStyle;
		controlType.setStyle(sbs);
		controlType.setItems(controlTypes);
		controlType.pack();
		
		final SelectBox<String> difficulty = new SelectBox<String>(uiSkin);
		String[] difficulties = {"Hard", "Medium", "Easy"};
		difficulty.setItems(difficulties);
		difficulty.pack();
		
		int currentDifficulty = MyPrefs.getDifficulty();
		switch (currentDifficulty){
		case 0:
			difficulty.setSelected("Easy");
			break;
		case 1:
			difficulty.setSelected("Medium");
			break;
		case 2:
			difficulty.setSelected("Hard");
			break;
		}
		
		
		LabelStyle sectionLabelStyle; 
		
		Label controlTypeLabel = new Label("Choose a control style:", uiSkin);
		controlTypeLabel.setTouchable(Touchable.disabled);
		sectionLabelStyle = controlTypeLabel.getStyle();
		sectionLabelStyle.font = newDefaultFont;
		controlTypeLabel.setStyle(sectionLabelStyle);
		
		Label difficultyLabel = new Label("Difficulty:", uiSkin);
		difficultyLabel.setStyle(sectionLabelStyle);
		
		TextButton backButton = (TextButton) ButtonFactory.getButton("Back", ButtonFactory.TEXT_BUTTON);
		
		Table table = new Table();
		table.setFillParent(true);
		table.pad(Gdx.graphics.getWidth()/ TacoMan.TILES_WIDE);
		table.add(settingsLabel).top().padBottom(2*Gdx.graphics.getWidth()/ TacoMan.TILES_WIDE);
		table.row();
		table.add(difficultyLabel).align(Align.left);
		table.row();
		table.add(difficulty).prefWidth(Gdx.graphics.getWidth()* TacoMan.BUTTON_W_SCALE).prefHeight(Gdx.graphics.getHeight()* TacoMan.BUTTON_H_SCALE/2).space(Gdx.graphics.getWidth()/20);
		table.row();
		//if(Gdx.app.getType()==ApplicationType.iOS||Gdx.app.getType()==ApplicationType.Android){
			table.add(controlTypeLabel).expandX().align(Align.left);
			table.row();
			table.add(controlType).prefWidth(Gdx.graphics.getWidth()* TacoMan.BUTTON_W_SCALE).prefHeight(Gdx.graphics.getHeight()* TacoMan.BUTTON_H_SCALE/2).space(Gdx.graphics.getWidth()/20);
			table.row().expandY();
		//}
		table.add(backButton).bottom().prefWidth(Gdx.graphics.getWidth()* TacoMan.BUTTON_W_SCALE).prefHeight(Gdx.graphics.getHeight()* TacoMan.BUTTON_H_SCALE).pad(Gdx.graphics.getWidth()/20);
		stage.addActor(table);
		
		difficulty.addListener(new ChangeListener(){
			public void changed (ChangeEvent event, Actor actor) {
				SelectBox box = (SelectBox) actor;
				if (box.getSelected().equals("Hard")){
					MyPrefs.setDifficulty(TacoMan.HARD);
				}
				else if (box.getSelected().equals("Medium")){
					MyPrefs.setDifficulty(TacoMan.MEDIUM);
				}
				else if (box.getSelected().equals("Easy")){
					MyPrefs.setDifficulty(TacoMan.EASY);
				}
			}
		});
		
		controlType.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				SelectBox box = (SelectBox) actor;
				if (box.getSelected().equals("Swipe")){
					MyPrefs.setControlType("swipe");
				}
				else if (box.getSelected().equals("Tilt")){
					MyPrefs.setControlType("tilt");
				}
			}
		});
		
		backButton.addListener(new ChangeListener() {
			public void changed (ChangeEvent event, Actor actor) {
				game.setScreen(game.getMainMenu());
			}
		});
		shapeRenderer = new ShapeRenderer();
	}
	
	@Override
	public void render(float delta) {
stage.act(Gdx.graphics.getDeltaTime());
		
		shapeRenderer.begin(ShapeType.Filled);

		// Draw Background color
		shapeRenderer.setColor(0, 0, 0, 1);
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
		// TODO Auto-generated method stub
		
	}

}
