package com.lum.scram;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import net.dermetfan.gdx.physics.box2d.Box2DMapObjectParser;

public class GameMap {
	
	private Box2DMapObjectParser collisionParser;
	private TiledMap tmap;
	public OrthogonalTiledMapRenderer tiledRenderer;
	
	private Array<Vector2> spawns;
	
	public GameMap(String map) {
		spawns = new Array<Vector2>();
		LoadMap(map);
	}
	
	private void LoadMap(String map) {
		tmap = new TmxMapLoader().load(map);
		
		collisionParser = new Box2DMapObjectParser();
		collisionParser.setUnitScale(Core.PIM);
		collisionParser.load(Core.world, tmap);
		
		tiledRenderer = new OrthogonalTiledMapRenderer(tmap, Core.PIM);
		
		for(MapObject object : tmap.getLayers().get(1).getObjects()) {
			if(object.getName()!=null) {
				if(object.getName().matches("spawn")) {
					RectangleMapObject spawnpoint = (RectangleMapObject)object;
					spawns.add(new Vector2(spawnpoint.getRectangle().x * Core.PIM, spawnpoint.getRectangle().y * Core.PIM));
					System.out.println("FOUND SPAWN");
				}
			}
		}
		System.out.println("MAP LOADED");
	}
	
	public Vector2 getRandomSpawn() {
		if (spawns.size == 0) return new Vector2(0, 0);
		return (spawns.get(MathUtils.random(spawns.size-1)));
	}
	
	public void render() {
		tiledRenderer.setView(Core.mainCam);
		tiledRenderer.render();
	}
	
	public void dispose() {
		tiledRenderer.dispose();
		tmap.dispose();
	}
	
}
