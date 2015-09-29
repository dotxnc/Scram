package com.lum.scram;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import static com.lum.scram.Core.PIM;
import com.lum.scram.screens.MenuScreen;

public class Scram extends Game {
	
	@Override
	public void create () {
		Core.batch = new SpriteBatch();
		Core.srend = new ShapeRenderer();
		
		Core.mainCam = new OrthographicCamera();
		Core.port = new ExtendViewport(800*PIM, 600*PIM, 1024*PIM, 768*PIM, Core.mainCam);
		Core.port.apply();
		
		Core.hudCam = new OrthographicCamera();
		Core.hudCam.setToOrtho(true, 800, 600);
		Core.hudCam.update();
		
		Core.batch.setProjectionMatrix(Core.mainCam.combined);
		
		
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		super.render();
	}
}
