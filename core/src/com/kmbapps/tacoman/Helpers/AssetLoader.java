package com.kmbapps.tacoman.Helpers;

import com.kmbapps.tacoman.TacoMan;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	public static Texture tacoTexture, walls, tacoManTextureR1, tacoManTextureR2,
			tacoManTextureD1, tacoManTextureD2, tacoManTextureU1, tacoManTextureU2,
			tacoManTextureL1, tacoManTextureL2, bPCarTextureR, bPCarTextureL,
			bPCarChaseTextureR1, bPCarChaseTextureR2, bPCarChaseTextureR3,
			bPCarChaseTextureR4, bPCarChaseTextureL1, bPCarChaseTextureL2,
			bPCarChaseTextureL3, bPCarChaseTextureL4, greenCardTexture,
			readyScreensTexture, readyTouchScreenTexture, powerUpTexture;
	public static TextureRegion taco, wall, tacoManR1, tacoManR2, tacoManL1,
			tacoManL2, tacoManU1, tacoManU2, tacoManD1, tacoManD2, bPCarRight,
			bPCarLeft, bPCarChaseR1, bPCarChaseR2, bPCarChaseR3, bPCarChaseR4,
			bPCarChaseL1, bPCarChaseL2, bPCarChaseL3, bPCarChaseL4;
	public static Sprite greenCard, desktopReadyPrompt, touchScreenReadyPrompt,
			lifeSprite, powerUp;

	public static Animation tacoManAnimR, tacoManAnimL, tacoManAnimU, tacoManAnimD,
			bPCarAnimR, bPCarAnimL, bPCarAnimU, bPCarAnimD, bPCarChaseAnimR,
			bPCarChaseAnimL, bPCarChaseAnimU, bPCarChaseAnimD, bPCarDeadAnimR,
			bPCarDeadAnimL, bPCarDeadAnimU, bPCarDeadAnimD;

	public static void loadGraphics(float tileWidth, float tileHeight) {
		readyScreensTexture = new Texture(
				Gdx.files.internal("ReadyScreen.png"));
		readyScreensTexture.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);

		desktopReadyPrompt = new Sprite(new TextureRegion(readyScreensTexture,
				0, 0, 480, 240));
		float readyScreenWidth = Gdx.graphics.getWidth();
		float readyScreenHeight = readyScreenWidth / 2.0f;
		desktopReadyPrompt.setBounds(0, Gdx.graphics.getHeight() / 2.0f
				- (readyScreenHeight / 2.0f), readyScreenWidth,
				readyScreenHeight);

		readyTouchScreenTexture = new Texture(
				Gdx.files.internal("ReadyTouchScreen.png"));
		readyTouchScreenTexture.setFilter(TextureFilter.Linear,
				TextureFilter.Linear);

		touchScreenReadyPrompt = new Sprite(new TextureRegion(
				readyTouchScreenTexture, 0, 0, 480, 240));
		touchScreenReadyPrompt.setBounds(0, Gdx.graphics.getHeight() / 2.0f
				- (readyScreenHeight / 2.0f), readyScreenWidth,
				readyScreenHeight);

		tacoTexture = new Texture(Gdx.files.internal("Taco.png"), true);
		tacoTexture.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);

		taco = new TextureRegion(tacoTexture, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);

		walls = new Texture(Gdx.files.internal("Wall.png"), true);
		walls.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		wall = new TextureRegion(walls, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);

		tacoManTextureR1 = new Texture(Gdx.files.internal("TacoMan.png"), true);
		tacoManTextureR1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManR1 = new TextureRegion(tacoManTextureR1, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		lifeSprite = new Sprite(tacoManR1);
		tacoManTextureR2 = new Texture(Gdx.files.internal("TacoManR2.png"), true);
		tacoManTextureR2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManR2 = new TextureRegion(tacoManTextureR2, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		TextureRegion[] tacoManRight = { tacoManR1, tacoManR2 };
		tacoManAnimR = new Animation(.2f, tacoManRight);
		tacoManAnimR.setPlayMode(Animation.PlayMode.LOOP);

		tacoManTextureL1 = new Texture(Gdx.files.internal("TacoManL1.png"), true);
		tacoManTextureL1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManL1 = new TextureRegion(tacoManTextureL1, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		tacoManTextureL2 = new Texture(Gdx.files.internal("TacoManL2.png"), true);
		tacoManTextureL2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManL2 = new TextureRegion(tacoManTextureL2, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		TextureRegion[] tacoManLeft = { tacoManL1, tacoManL2 };
		tacoManAnimL = new Animation(.2f, tacoManLeft);
		tacoManAnimL.setPlayMode(Animation.PlayMode.LOOP);

		tacoManTextureD1 = new Texture(Gdx.files.internal("TacoManD1.png"), true);
		tacoManTextureD1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManD1 = new TextureRegion(tacoManTextureD1, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		tacoManTextureD2 = new Texture(Gdx.files.internal("TacoManD2.png"), true);
		tacoManTextureD2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManD2 = new TextureRegion(tacoManTextureD2, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		TextureRegion[] tacoManDown = { tacoManD1, tacoManD2 };
		tacoManAnimD = new Animation(.2f, tacoManDown);
		tacoManAnimD.setPlayMode(Animation.PlayMode.LOOP);

		tacoManTextureU1 = new Texture(Gdx.files.internal("TacoManU1.png"), true);
		tacoManTextureU1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManU1 = new TextureRegion(tacoManTextureU1, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		tacoManTextureU2 = new Texture(Gdx.files.internal("TacoManU2.png"), true);
		tacoManTextureU2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		tacoManU2 = new TextureRegion(tacoManTextureU2, 0, 0, TacoMan.NEW_GRID_W, TacoMan.NEW_GRID_H);
		TextureRegion[] tacoManUp = { tacoManU1, tacoManU2 };
		tacoManAnimU = new Animation(.2f, tacoManUp);
		tacoManAnimU.setPlayMode(Animation.PlayMode.LOOP);

		bPCarTextureR = new Texture(
				Gdx.files.internal("BorderPatrolCarR.png"), true);
		bPCarTextureR.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		bPCarTextureL = new Texture(
				Gdx.files.internal("BorderPatrolCarL.png"), true);
		bPCarTextureL.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		bPCarChaseTextureR1 = new Texture(
				Gdx.files.internal("BPCarChaseR1.png"), true);
		bPCarChaseTextureR1.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureR2 = new Texture(
				Gdx.files.internal("BPCarChaseR2.png"), true);
		bPCarChaseTextureR2.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureR3 = new Texture(
				Gdx.files.internal("BPCarChaseR3.png"), true);
		bPCarChaseTextureR3.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureR4 = new Texture(
				Gdx.files.internal("BPCarChaseR4.png"), true);
		bPCarChaseTextureR4.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureL1 = new Texture(
				Gdx.files.internal("BPCarChaseL1.png"), true);
		bPCarChaseTextureL1.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureL2 = new Texture(
				Gdx.files.internal("BPCarChaseL2.png"), true);
		bPCarChaseTextureL2.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureL3 = new Texture(
				Gdx.files.internal("BPCarChaseL3.png"), true);
		bPCarChaseTextureL3.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);
		bPCarChaseTextureL4 = new Texture(
				Gdx.files.internal("BPCarChaseL4.png"), true);
		bPCarChaseTextureL4.setFilter(TextureFilter.MipMapLinearLinear,
				TextureFilter.Linear);

		bPCarRight = new TextureRegion(bPCarTextureR, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarAnimR = new Animation(60, bPCarRight);
		bPCarAnimR.setPlayMode(Animation.PlayMode.LOOP);

		bPCarChaseR1 = new TextureRegion(bPCarChaseTextureR1, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseR2 = new TextureRegion(bPCarChaseTextureR2, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseR3 = new TextureRegion(bPCarChaseTextureR3, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseR4 = new TextureRegion(bPCarChaseTextureR4, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		TextureRegion[] bPCarChaseR = { bPCarChaseR1, bPCarChaseR2,
				bPCarChaseR1, bPCarChaseR3, bPCarChaseR4, bPCarChaseR3 };
		bPCarChaseAnimR = new Animation(.1f, bPCarChaseR);
		bPCarChaseAnimR.setPlayMode(Animation.PlayMode.LOOP);

		bPCarLeft = new TextureRegion(bPCarTextureL, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarAnimL = new Animation(60, bPCarLeft);
		bPCarAnimL.setPlayMode(Animation.PlayMode.LOOP);

		bPCarChaseL1 = new TextureRegion(bPCarChaseTextureL1, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseL2 = new TextureRegion(bPCarChaseTextureL2, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseL3 = new TextureRegion(bPCarChaseTextureL3, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		bPCarChaseL4 = new TextureRegion(bPCarChaseTextureL4, 19, 41, TacoMan.GRID_W*2, TacoMan.GRID_H);
		TextureRegion[] bPCarChaseL = { bPCarChaseL1, bPCarChaseL2,
				bPCarChaseL1, bPCarChaseL3, bPCarChaseL4, bPCarChaseL3 };
		bPCarChaseAnimL = new Animation(.1f, bPCarChaseL);
		bPCarChaseAnimL.setPlayMode(Animation.PlayMode.LOOP);

		greenCardTexture = new Texture(Gdx.files.internal("GreenCard.png"));
		greenCardTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		greenCard = new Sprite(
				new TextureRegion(greenCardTexture, 0, 0, 32, 32));

		powerUpTexture = new Texture(Gdx.files.internal("PowerUp.png"));
		powerUpTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		powerUp = new Sprite(new TextureRegion(powerUpTexture, 0, 0, 480, 800));

	}

	public static void dispose() {
		tacoTexture.dispose();
		walls.dispose();
		tacoManTextureR1.dispose();
		tacoManTextureR2.dispose();
		tacoManTextureD1.dispose();
		tacoManTextureD2.dispose();
		tacoManTextureU1.dispose();
		tacoManTextureU2.dispose();
		tacoManTextureL1.dispose();
		tacoManTextureL2.dispose();
		bPCarTextureR.dispose();
		bPCarTextureL.dispose();
		bPCarChaseTextureR1.dispose();
		bPCarChaseTextureR2.dispose();
		bPCarChaseTextureR3.dispose();
		bPCarChaseTextureR4.dispose();
		bPCarChaseTextureL1.dispose();
		bPCarChaseTextureL2.dispose();
		bPCarChaseTextureL3.dispose();
		bPCarChaseTextureL4.dispose();
		greenCardTexture.dispose();
		readyScreensTexture.dispose();
		readyTouchScreenTexture.dispose();
	}
}
