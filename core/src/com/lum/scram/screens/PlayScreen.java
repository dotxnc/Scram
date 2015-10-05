package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lum.scram.Background;
import com.lum.scram.Core;
import com.lum.scram.LaserBeam;
import com.lum.scram.Player;
import com.lum.scram.Scram;
import com.lum.scram.net.GameClient;
import com.lum.scram.net.GameServer;
import com.lum.scram.net.packets.PlayerShootPacket;
import com.lum.scram.postprocessing.PostProcessor;
import com.lum.scram.postprocessing.effects.Bloom;
import com.lum.scram.postprocessing.effects.CrtMonitor;
import com.lum.scram.postprocessing.effects.Curvature;
import com.lum.scram.postprocessing.effects.Glitcher;
import com.lum.scram.postprocessing.filters.Blur.BlurType;
import com.lum.scram.postprocessing.filters.CrtScreen;
import com.lum.scram.postprocessing.filters.CrtScreen.Effect;
import java.util.HashMap;
import java.util.Map;

/*
The 1837 Racer's Storm was one of the most powerful and destructive hurricanes in
the 19th century, causing heavy damage to many cities on its 2,000+ mile path.
The Racer's Storm was the 10th known tropical storm in the 1837 Atlantic hurricane season.
*/

public class PlayScreen implements Screen {
	
	private Scram game;
	
	private GameServer server;
	private GameClient client;
	
	private Box2DDebugRenderer debug;
	
	private Fixture closestFixture;
	private Vector2 hitPoint;
	private RayCastCallback callback;
	
	private Background bg;
	
	private PostProcessor effects;
	private Glitcher glitch;
	private Bloom bloom;
	private Curvature curve;
	
	private static float shakeAmount = 0;
	private static float shakeSpeed = 0;
	
	private float time = 0f;
	
	public PlayScreen(Scram game) {
		this.game = game;
		
		Core.world = new World(new Vector2(0, 0), true);
		Core.players = new HashMap<Integer, Player>();
		Core.toCreate = new Array<Integer>();
		Core.beams = new Array<LaserBeam>();
		
		Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("crosshair.png")), 16/2, 16/2));
		
		server = new GameServer();
		client = new GameClient();
		
		debug = new Box2DDebugRenderer();
		
		effects = new PostProcessor(true, true, true);
		
		int e = Effect.TweakContrast.v | Effect.PhosphorVibrance.v | Effect.Scanlines.v | Effect.Tint.v;
		
		curve = new Curvature();
		glitch = new Glitcher();
		bloom = new Bloom((int) (Gdx.graphics.getWidth()), (int) (Gdx.graphics.getHeight()));
		bloom.setBaseIntesity(1);
		bloom.setBlurType(BlurType.Gaussian5x5b);
		bloom.setBlurPasses(20);
		bloom.setBlurAmount(10);
		bloom.setBloomIntesity(2.15f);
		bloom.setBaseSaturation(1.5f);
		
		effects.addEffect(curve);
		effects.addEffect(glitch);
		effects.addEffect(bloom);
		
		callback = new RayCastCallback() {
			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if (fixture.getBody().getUserData() instanceof Player && ((Player)fixture.getBody().getUserData()).uid_local == Core.localPlayer.uid_local)
					return -1;
				
				closestFixture = fixture;
				hitPoint = new Vector2().set(point);
				return fraction;
			}
		};
		
		bg = new Background();
		
		if (Core.isServer) {
			server.ConnectListeners();
			server.Listen();
		}
		
		client.ConnectListeners();
		client.Connect();
		
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		
		if (shakeAmount > 0)
			shakeAmount = MathUtils.lerp(shakeAmount, 0f, 0.1f);
		if (shakeSpeed > 0)
			shakeSpeed = MathUtils.lerp(shakeSpeed, 0f, 0.1f);
		
		time += delta;
		glitch.setTime(time);
		glitch.setAmplitude(shakeAmount);
		glitch.setSpeed(shakeSpeed);
		
		effects.capture();
		
		// TODO: fix up background
		bg.render(Core.srend, delta);
		
		// Update world
		Core.world.step(1/60.f, 8, 6);
		
		// Generate player bodies
		for (int i = 0; i < Core.toCreate.size; i++) {
			Core.players.get(Core.toCreate.get(i)).Create();
			Core.toCreate.removeIndex(i);
		}
		
		// render map
		if (Core.map != null) {
			Core.map.render();
		};
		
		// Player rendering
		Core.batch.setProjectionMatrix(Core.hudCam.combined);
		Core.batch.begin();
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			if (playerEntry.getValue().body == null)
				continue;
			
			Player p = (Player)playerEntry.getValue();
		}
		
		Core.batch.end();
		
		// render laser beams
		for (int i = 0; i < Core.beams.size; i++) {
			LaserBeam beam = Core.beams.get(i);
			beam.render(Core.srend, delta);
			if (beam.lifetime <= 0)
				Core.beams.removeIndex(i);
		}
		
		/* UPDATE AND RENDER PLAYERS */
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet())
			((Player)playerEntry.getValue()).Render(Core.batch, delta);
		
		if (Core.toCreate.contains(client.GetID(), true))
			return;
		
		Core.localPlayer = (Player) Core.players.get(client.GetID());
		
		if (Core.localPlayer == null || Core.localPlayer.body == null)
			return;
		
		// lerp camera to player position
		float lerpAmt = 0.001f;	
		Vector3 newPos = Core.mainCam.position.lerp(new Vector3(Core.localPlayer.GetPosition().x,Core.localPlayer.GetPosition().y, 0) , (float) (1 - Math.pow(lerpAmt, delta)));
		Core.mainCam.position.set(newPos.x, newPos.y, 0);
		Core.mainCam.update();
		
		// unproject mouse position
		Vector2 aimPosition = new Vector2(Gdx.input.getX(),Gdx.input.getY());
		Vector3 unprojectedPos = Core.mainCam.unproject(new Vector3(aimPosition,0));
		aimPosition = new Vector2(unprojectedPos.x, unprojectedPos.y);
		
		// Get delta x/y for velocity impulse
		float angle = MathUtils.atan2(aimPosition.y - Core.localPlayer.GetPosition().y, aimPosition.x - Core.localPlayer.GetPosition().x);
		float dx = MathUtils.cos(angle)*20*delta;
		float dy = MathUtils.sin(angle)*20*delta;
		
		// player movement	
		Vector2 vel = Core.localPlayer.GetVelocity();
		
		if (Gdx.input.isKeyPressed(Keys.W))
			Core.localPlayer.body.applyLinearImpulse(0, 40*delta, Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.S))
			Core.localPlayer.body.applyLinearImpulse(0, -40*delta, Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.A))
			Core.localPlayer.body.applyLinearImpulse(-40*delta, 0, Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, true);
		if (Gdx.input.isKeyPressed(Keys.D))
			Core.localPlayer.body.applyLinearImpulse(40*delta, 0, Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, true);
		
		if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			client.Disconnect();
			server.Stop();
			game.setScreen(new MenuScreen(game));
		}
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched() && Core.localPlayer.zapTimer <= 0) {
			Core.localPlayer.zapTimer = Core.localPlayer.zapMax;
			Core.localPlayer.body.applyLinearImpulse(-dx*20, -dy*20, Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, true);
			
			Vector2 infinite = new Vector2();
			infinite.x = Core.localPlayer.GetPosition().x+dx*2000;
			infinite.y = Core.localPlayer.GetPosition().y+dy*2000;
			Core.world.rayCast(callback, new Vector2(Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y), infinite);
			
			int uid = 0;
			if (closestFixture.getBody().getUserData() instanceof Player)
				uid = ((Player)closestFixture.getBody().getUserData()).uid_local;
			client.Send(new PlayerShootPacket(client.GetID(), uid, new Vector2(dx*30, dy*30), hitPoint));
			
		}
		
		Core.localPlayer.body.setTransform(Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, angle);
		client.SendPosition(Core.localPlayer.GetPosition().x, Core.localPlayer.GetPosition().y, Core.localPlayer.GetPosition().z);
		
		effects.render();
	}

	@Override
	public void resize(int width, int height) {
		Core.port.update(width, height);
		Core.port.apply();
		
		Core.hudCam.setToOrtho(true, width, height);
		Core.hudCam.update();
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
		client.Disconnect();
		server.Stop();
		
		Core.world.dispose();
		
		Core.beams.clear();
		Core.players.clear();
		Core.toCreate.clear();
		Core.map.dispose();
	}
	
	public static void shakeScreen() {
		shakeAmount = 5f;
		shakeSpeed = 5f;
	}

}
