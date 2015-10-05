package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Background {
	public float timer = 0;
	private Array<Vector2> stars;
	
	public Background() {
		stars = new Array<Vector2>();
		for (int i = 0; i < 150; i++)
			stars.add(new Vector2(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight())));
	}
	
	public void render(ShapeRenderer srend, float delta) {
		if (Core.map == null || Core.localPlayer == null)
			return;
		
		timer += delta;
		srend.setProjectionMatrix(Core.hudCam.combined);
		srend.begin(ShapeType.Filled);
		srend.setColor(0.5f, 0.5f, 0.5f, 1);
		
		for (Vector2 star : stars) {
			
			star.x -= Core.localPlayer.GetVelocity().x/2;
			star.y += Core.localPlayer.GetVelocity().y/2;
			
			if (star.x < 0) star.x = Gdx.graphics.getWidth();
			if (star.x > Gdx.graphics.getWidth()) star.x = 0;
			if (star.y < 0) star.y = Gdx.graphics.getHeight();
			if (star.y > Gdx.graphics.getHeight()) star.y = 0;
			
			srend.circle(star.x, star.y, 2, 100);
		}
		
		srend.end();
	}
	
}
