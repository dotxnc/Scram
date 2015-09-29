package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.lum.scram.Core;
import com.lum.scram.Scram;

public class MenuScreen implements Screen {
	
	private Scram game;
	
	private Window mainWindow;
	private Skin skin;
	
	public MenuScreen(Scram game) {
		this.game = game;
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("scp.ttf"));
		FreeTypeFontParameter par = new FreeTypeFontParameter();
		par.size = 12;
		par.flip = true;
		Core.font = gen.generateFont(par);
		gen.dispose();
		
		//Core.font.getData().setScale(Core.PIM);
		Core.font.setUseIntegerPositions(false);
		
		skin = new Skin();
		skin.add("font", Core.font, BitmapFont.class);
		
		//mainWindow = new Window();
		
		Core.isServer = false;
		
	}
	
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyJustPressed(Keys.NUM_1)) {
			Core.isServer = true;
			game.setScreen(new PlayScreen(game));
		}
		if (Gdx.input.isKeyJustPressed(Keys.NUM_2)) {
			Core.isServer = false;
			game.setScreen(new PlayScreen(game));
		}
		
		Core.port.apply();
		Core.mainCam.update();
		
		Core.batch.setProjectionMatrix(Core.hudCam.combined);
		Core.batch.begin();
		Core.font.draw(Core.batch, "<1: start as server> <2: start as client>", 10, 10);
		Core.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {
		Core.port.update(width, height);
		Core.port.apply();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
	
}
