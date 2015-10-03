package com.lum.scram;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Background {
	public float timer = 0;
	
	public Background() {
		
	}
	
	public void render(ShapeRenderer srend, float delta) {
		if (Core.map == null)
			return;
		
		timer += delta;
		srend.setProjectionMatrix(Core.mainCam.combined);
		srend.begin(ShapeType.Line);
		float size = 20*Core.PIM;
		for (int i = 0; i < 100*32/20; i++) {
			for (int j = 0; j < 100*32/20; j++) {
				float offsetX = MathUtils.cos(1*timer)*MathUtils.random(1, 2)*Core.PIM;
				float offsetY = MathUtils.sin(1*timer)*MathUtils.random(1, 2)*Core.PIM;
				
				srend.setColor(0.5f + MathUtils.cos(1*timer)/2, 0.5f + MathUtils.sin(1*timer)/2, 0.5f + MathUtils.sin(1*timer)/2, 0.4f);
				Gdx.gl20.glLineWidth(2);
				srend.rect((i*size)+offsetX, (j*size), size, size);
				//srend.rect(i*20, j*20, 20, 20);
			}
		}
		
		srend.end();
	}
	
}
