package com.lum.scram;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import static com.lum.scram.Core.PIM;
import com.lum.scram.screens.MenuScreen;

public class Scram extends Game {
	
	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		Core.batch = new SpriteBatch();
		Core.srend = new ShapeRenderer();
		
		Core.mainCam = new OrthographicCamera();
		Core.port = new ExtendViewport(w*PIM, h*PIM, w*PIM, h*PIM, Core.mainCam);
		Core.port.apply();
		
		Core.hudCam = new OrthographicCamera();
		Core.hudCam.setToOrtho(true, w, h);
		Core.hudCam.update();
		
		Core.hudPort = new ScreenViewport(Core.hudCam);
		Core.hudPort.apply();
		
		Core.batch.setProjectionMatrix(Core.mainCam.combined);
		
		
		this.setScreen(new MenuScreen(this));
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1f, 1f, 1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		super.render();
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}
}
