package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisList;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.lum.scram.Core;
import com.lum.scram.Scram;
import com.lum.scram.net.packets.master.GetServerListPacket;

public class MenuScreen implements Screen {
	
	private Scram game;
	
	private VisWindow serverWindow;
	private VisTable table;
	private Stage stage;
	
	private static VisList list;
	private static String selected;
	
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
		
		list = new VisList();
		
		// Main UI
		
		final VisTextArea name = new VisTextArea(Gdx.app.getPreferences("Scram").getString("name", System.getProperty("user.name")));
		name.setWidth(200f);
		name.setHeight(25f);
		name.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-100);
		
		final VisTextArea ip = new VisTextArea(Gdx.app.getPreferences("Scram").getString("ip", "localhost"));
		ip.setWidth(150f);
		ip.setHeight(25f);
		ip.setPosition(Gdx.graphics.getWidth()/2-160, Gdx.graphics.getHeight()-150);
		
		final VisTextArea port = new VisTextArea(Gdx.app.getPreferences("Scram").getString("port", "7777"));
		port.setWidth(150f);
		port.setHeight(25f);
		port.setPosition(Gdx.graphics.getWidth()/2+10, Gdx.graphics.getHeight()-150);
		
		VisTextButton host = new VisTextButton("Host Game");
		host.setWidth(200f);
		host.setHeight(25f);
		host.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-200);
		
		VisTextButton join = new VisTextButton("Join Game");
		join.setWidth(200f);
		join.setHeight(25f);
		join.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-250);
		
		VisTextButton showBrowser = new VisTextButton("Server Browser");
		showBrowser.setWidth(200f);
		showBrowser.setHeight(25f);
		showBrowser.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-300);
		
		final VisTextButton video = new VisTextButton("Video Settings");
		video.setWidth(200f);
		video.setHeight(25);
		video.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-350);
		video.setDisabled(true);
		
		VisTextButton quit = new VisTextButton("Quit");
		quit.setSize(200, 25);
		quit.setPosition(Gdx.graphics.getWidth()/2-100, Gdx.graphics.getHeight()-400);
		
		
		host.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisUI.dispose();
				Core.isServer = true;
				Gdx.input.setInputProcessor(null);
				Gdx.app.getPreferences("Scram").putString("name", name.getText());
				Gdx.app.getPreferences("Scram").putString("ip", ip.getText());
				Gdx.app.getPreferences("Scram").putInteger("port", Integer.parseInt(port.getText()));
				game.setScreen(new PlayScreen(game));
			}
		});
		
		join.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				VisUI.dispose();
				Core.isServer = false;
				Gdx.input.setInputProcessor(null);
				Gdx.app.getPreferences("Scram").putString("name", name.getText());
				Gdx.app.getPreferences("Scram").putString("ip", ip.getText());
				Gdx.app.getPreferences("Scram").putInteger("port", Integer.parseInt(port.getText()));
				game.setScreen(new PlayScreen(game));
			}
		});
		
		showBrowser.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				createServerBrowser();
			}
		});
		
		video.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (video.isDisabled()) return;
				game.setScreen(new SettingsScreen(game));
			}
		});
		
		quit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		stage.addActor(name);
		stage.addActor(ip);
		stage.addActor(port);
		stage.addActor(host);
		stage.addActor(join);
		stage.addActor(showBrowser);
		stage.addActor(video);
		stage.addActor(quit);
		
		Gdx.input.setInputProcessor(stage);
		
		Core.isServer = false;
		
		Core.masterClient.Send(new GetServerListPacket());
		
	}
	
	public static void setServerList(Object[] l) {
		if (list == null) return;
		list.setItems(l);
	}
	
	private void createServerBrowser() {
		// Server browser
		list.setItems((Object[]) new String[] {});
		
		serverWindow = new VisWindow("Server Browser");
		serverWindow.setSize(460, 300);
		serverWindow.centerWindow();
		
		selected = (String)list.getSelected();
		
		final VisScrollPane browser = new VisScrollPane(list);
		browser.setPosition(0, 0);
		browser.setWidth(460);
		browser.setHeight(220);
		
		VisTextButton connect = new VisTextButton("Connect");
		connect.setPosition(10, 235);
		connect.setSize(75, 25);
		
		VisTextButton refresh = new VisTextButton("Refresh");
		refresh.setPosition(100, 235);
		refresh.setSize(75, 25);
		
		VisTextButton cancel = new VisTextButton("Cancel");
		cancel.setPosition(375, 235);
		cancel.setSize(75, 25);
		
		connect.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.getPreferences("Scram").putString("ip", selected.substring(4));
				Gdx.app.getPreferences("Scram").putInteger("port", 7777);
				game.setScreen(new PlayScreen(game));
			}
		});
		
		refresh.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Core.masterClient.Send(new GetServerListPacket());
			}
		});
		
		cancel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				serverWindow.remove();
			}
		});
		
		serverWindow.addActor(browser);
		serverWindow.addActor(connect);
		serverWindow.addActor(refresh);
		serverWindow.addActor(cancel);
		stage.addActor(serverWindow);
	}
	
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Core.port.apply();
		Core.mainCam.update();
		
		Core.batch.setProjectionMatrix(Core.hudCam.combined);
		stage.act(delta);
		stage.draw();
		
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
		Core.masterClient.Disconnect();
		VisUI.dispose();
	}
	
}
