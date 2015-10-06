package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.lum.scram.Core;
import com.lum.scram.Scram;

public class SettingsScreen implements Screen{
	
	private Stage stage;
	private VisTable table;
	private VisWindow window;
	private Scram game;
	
	public SettingsScreen(final Scram game) {
		this.game = game;
		
		stage = new Stage();
		table = new VisTable(false);
		window = new VisWindow("fuck");
		window.addCloseButton();
		window.centerWindow();
		window.setWidth(400);
		table.setFillParent(true);
		
		VisUI.load();
		
		VisLabel video = new VisLabel("Video Settings");
		video.setSize(100, 20);
		video.setPosition(10, 100);
		
		VisCheckBox curve = new VisCheckBox("Curvature", true);
		curve.setSize(50, 10);
		curve.setPosition(20, 90);
		
		VisCheckBox back = new VisCheckBox("Background", true);
		back.setSize(50, 10);
		back.setPosition(Gdx.graphics.getWidth()/2-25, Gdx.graphics.getHeight()-250);
		
		VisTextButton apply = new VisTextButton("Apply");
		apply.setSize(100, 20);
		apply.setPosition(Gdx.graphics.getWidth()/2-50, Gdx.graphics.getHeight()-300);
		
		window.addActor(video);
		window.addActor(curve);
		window.addActor(back);
		window.addActor(apply);
		stage.addActor(window);
		
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Core.port.apply();
		Core.hudCam.update();
		
		Core.batch.setProjectionMatrix(Core.hudCam.combined);
		Core.batch.begin();
		stage.act(delta);
		stage.draw();
		Core.batch.end();
	}

	@Override
	public void resize(int width, int height) {
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
