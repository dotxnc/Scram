package com.lum.scram;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Map;

public class Core {
	public static OrthographicCamera mainCam;
	public static OrthographicCamera hudCam;
	
	public static ExtendViewport port;
	public static ScreenViewport hudPort;
	
	public static Map<Integer, Player> players;
	public static Array<Integer> toCreate;
	
	public static Array<LaserBeam> beams;
	
	public static World world;
	
	public static float PIM = 1/32f;
	public static float MIP = 32f;
	
	public static BitmapFont font;
	public static ShapeRenderer srend;
	public static SpriteBatch batch;
	
	public static GameMap map;
	
	public static boolean isServer;
	
	public static Player localPlayer;
}
