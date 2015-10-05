package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Background {
	public float timer = 0;
	private Array<Vector3> stars;
	
	public Background() {
		stars = new Array<Vector3>();
		for (int i = 0; i < 150; i++)
			stars.add(new Vector3(MathUtils.random(Gdx.graphics.getWidth()), MathUtils.random(Gdx.graphics.getHeight()), MathUtils.random(5,10)));
	}
	
	public void render(ShapeRenderer srend, float delta) {
		if (Core.map == null || Core.localPlayer == null)
			return;
		
		timer += delta;
		srend.setProjectionMatrix(Core.hudCam.combined);
		srend.begin(ShapeType.Filled);
		
		for (Vector3 star : stars) {
			if (timer > 0.3f) {
				srend.setColor(MathUtils.random(1f), MathUtils.random(1f), MathUtils.random(1f), 1f);
			}
			
			star.x -= Core.localPlayer.GetVelocity().x/4;
			star.y += Core.localPlayer.GetVelocity().y/4;
			
			if (star.x < 0) star.x = Gdx.graphics.getWidth();
			if (star.x > Gdx.graphics.getWidth()) star.x = 0;
			if (star.y < 0) star.y = Gdx.graphics.getHeight();
			if (star.y > Gdx.graphics.getHeight()) star.y = 0;
			
			srend.circle(star.x, star.y, MathUtils.cos(star.z*timer)*3, 4);
		}
		
		srend.end();
	}
	
}
