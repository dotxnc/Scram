package com.lum.scram;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class GameMap {
	
	private Box2DMapObjectParser collisionParser;
	private TiledMap tmap;
	public OrthogonalTiledMapRenderer tiledRenderer;
	
	public GameMap(String map) {
		LoadMap(map);
	}
	
	private void LoadMap(String map) {
		tmap = new TmxMapLoader().load(map);
		
		collisionParser = new Box2DMapObjectParser();
		collisionParser.setUnitScale(Core.PIM);
		collisionParser.load(Core.world, tmap);
		
		tiledRenderer = new OrthogonalTiledMapRenderer(tmap, Core.PIM);
		
		System.out.println("MAP LOADED");
	}
	
	public void render() {
		//Core.mainCam.zoom = 10;
		tiledRenderer.setView(Core.mainCam);
		tiledRenderer.render();
	}
	
	public void dispose() {
		tiledRenderer.dispose();
		tmap.dispose();
	}
	
}
