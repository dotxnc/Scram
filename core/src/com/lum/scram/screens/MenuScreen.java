package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.lum.scram.Core;
import com.lum.scram.Scram;

public class MenuScreen implements Screen {
	
	private Scram game;
	
	private Window mainWindow;
	private VisTable table;
	private Stage stage;
	
	public MenuScreen(final Scram game) {
		this.game = game;
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("scp.ttf"));
		FreeTypeFontParameter par = new FreeTypeFontParameter();
		par.size = 12;
		par.flip = true;
		Core.font = gen.generateFont(par);
		gen.dispose();
		
		//Core.font.getData().setScale(Core.PIM);
		Core.font.setUseIntegerPositions(false);
		
		VisUI.load();
		stage = new Stage();
		
		final VisTextArea ip = new VisTextArea("localhost");
		ip.setWidth(100f);
		ip.setHeight(25f);
		ip.setPosition(Gdx.graphics.getWidth()/2-110, Gdx.graphics.getHeight()-150);
		ip.setAlignment(Align.center);
		
		final VisTextArea port = new VisTextArea("7777");
		port.setWidth(100f);
		port.setHeight(25f);
		port.setPosition(Gdx.graphics.getWidth()/2+10, Gdx.graphics.getHeight()-150);
		
		VisTextButton host = new VisTextButton("Host Game");
		host.setWidth(200f);
		host.setHeight(25f);
		host.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-200);
		
		VisTextButton join = new VisTextButton("Join Game");
		join.setWidth(200f);
		join.setHeight(25f);
		join.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-245);
		
		VisTextButton quit = new VisTextButton("Quit");
		quit.setSize(200, 25);
		quit.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-290);
		
		
		host.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Core.isServer = true;
				Core.netip = ip.getText();
				Core.netport = Integer.parseInt(port.getText());
				Gdx.input.setInputProcessor(null);
				game.setScreen(new PlayScreen(game));
			}
		});
		
		join.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Core.isServer = false;
				Core.netip = ip.getText();
				Core.netport = Integer.parseInt(port.getText());
				Gdx.input.setInputProcessor(null);
				game.setScreen(new PlayScreen(game));
			}
		});
		
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		stage.addActor(ip);
		stage.addActor(port);
		stage.addActor(host);
		stage.addActor(join);
		stage.addActor(quit);
		
		Gdx.input.setInputProcessor(stage);
		
		Core.isServer = false;
		
	}
	
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Core.port.apply();
		Core.mainCam.update();
		
		Core.batch.setProjectionMatrix(Core.hudCam.combined);
		Core.batch.begin();
		stage.act(delta);
		stage.draw();
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
		VisUI.dispose();
	}
	
}
