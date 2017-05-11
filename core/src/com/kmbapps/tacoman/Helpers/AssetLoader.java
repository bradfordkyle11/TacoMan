package com.kmbapps.tacoman.Helpers;

import com.kmbapps.tacoman.TacoMan;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetLoader {
	public static Texture tacoTexture, walls, mexManTextureR1, mexManTextureR2,
			mexManTextureD1, mexManTextureD2, mexManTextureU1, mexManTextureU2,
			mexManTextureL1, mexManTextureL2, bPCarTextureR, bPCarTextureL,
			bPCarChaseTextureR1, bPCarChaseTextureR2, bPCarChaseTextureR3,
			bPCarChaseTextureR4, bPCarChaseTextureL1, bPCarChaseTextureL2,
			bPCarChaseTextureL3, bPCarChaseTextureL4, greenCardTexture,
			readyScreensTexture, readyTouchScreenTexture, powerUpTexture;
	public static TextureRegion taco, wall, mexManR1, mexManR2, mexManL1,
			mexManL2, mexManU1, mexManU2, mexManD1, mexManD2, bPCarRight,
			bPCarLeft, bPCarChaseR1, bPCarChaseR2, bPCarChaseR3, bPCarChaseR4,
			bPCarChaseL1, bPCarChaseL2, bPCarChaseL3, bPCarChaseL4;
	public static Sprite greenCard, desktopReadyPrompt, touchScreenReadyPrompt,
			lifeSprite, powerUp;

	public static Animation mexManAnimR, mexManAnimL, mexManAnimU, mexManAnimD,
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

		taco = new TextureRegion(tacoTexture, 9, 5, TacoMan.GRID_W, TacoMan.GRID_H/2);

		walls = new Texture(Gdx.files.internal("Wall.png"), true);
		walls.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		wall = new TextureRegion(walls, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);

		mexManTextureR1 = new Texture(Gdx.files.internal("MexMan.png"), true);
		mexManTextureR1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManR1 = new TextureRegion(mexManTextureR1, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		lifeSprite = new Sprite(mexManR1);
		mexManTextureR2 = new Texture(Gdx.files.internal("MexManR2.png"), true);
		mexManTextureR2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManR2 = new TextureRegion(mexManTextureR2, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		TextureRegion[] mexManRight = { mexManR1, mexManR2 };
		mexManAnimR = new Animation(.2f, mexManRight);
		mexManAnimR.setPlayMode(Animation.PlayMode.LOOP);

		mexManTextureL1 = new Texture(Gdx.files.internal("MexManL1.png"), true);
		mexManTextureL1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManL1 = new TextureRegion(mexManTextureL1, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		mexManTextureL2 = new Texture(Gdx.files.internal("MexManL2.png"), true);
		mexManTextureL2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManL2 = new TextureRegion(mexManTextureL2, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		TextureRegion[] mexManLeft = { mexManL1, mexManL2 };
		mexManAnimL = new Animation(.2f, mexManLeft);
		mexManAnimL.setPlayMode(Animation.PlayMode.LOOP);

		mexManTextureD1 = new Texture(Gdx.files.internal("MexManD1.png"), true);
		mexManTextureD1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManD1 = new TextureRegion(mexManTextureD1, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		mexManTextureD2 = new Texture(Gdx.files.internal("MexManD2.png"), true);
		mexManTextureD2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManD2 = new TextureRegion(mexManTextureD2, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		TextureRegion[] mexManDown = { mexManD1, mexManD2 };
		mexManAnimD = new Animation(.2f, mexManDown);
		mexManAnimD.setPlayMode(Animation.PlayMode.LOOP);

		mexManTextureU1 = new Texture(Gdx.files.internal("MexManU1.png"), true);
		mexManTextureU1.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManU1 = new TextureRegion(mexManTextureU1, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		mexManTextureU2 = new Texture(Gdx.files.internal("MexManU2.png"), true);
		mexManTextureU2.setFilter(TextureFilter.MipMapLinearLinear, TextureFilter.Linear);
		mexManU2 = new TextureRegion(mexManTextureU2, 9, 9, TacoMan.GRID_W, TacoMan.GRID_H);
		TextureRegion[] mexManUp = { mexManU1, mexManU2 };
		mexManAnimU = new Animation(.2f, mexManUp);
		mexManAnimU.setPlayMode(Animation.PlayMode.LOOP);

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
		mexManTextureR1.dispose();
		mexManTextureR2.dispose();
		mexManTextureD1.dispose();
		mexManTextureD2.dispose();
		mexManTextureU1.dispose();
		mexManTextureU2.dispose();
		mexManTextureL1.dispose();
		mexManTextureL2.dispose();
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
