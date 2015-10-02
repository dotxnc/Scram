package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lum.scram.Core;
import com.lum.scram.LaserBeam;
import com.lum.scram.Player;
import com.lum.scram.Scram;
import com.lum.scram.bloom.Bloom;
import com.lum.scram.net.GameClient;
import com.lum.scram.net.GameServer;
import com.lum.scram.net.packets.PlayerShootPacket;
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
	private Player localPlayer;
	
	private Bloom bloom;
	
	public PlayScreen(Scram game) {
		this.game = game;
		
		Core.world = new World(new Vector2(0, 0), true);
		Core.players = new HashMap<Integer, Player>();
		Core.toCreate = new Array<Integer>();
		Core.beams = new Array<LaserBeam>();
		
		server = new GameServer();
		client = new GameClient();
		
		debug = new Box2DDebugRenderer();
		
		callback = new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
				if (fixture.getBody().getUserData() instanceof Player && ((Player)fixture.getBody().getUserData()).uid_local == localPlayer.uid_local)
					return -1;
				
				closestFixture = fixture;
				hitPoint = new Vector2().set(point);
				return fraction;
			}
			
		};
		
		bloom = new Bloom(800, 600, true, true, true);
		bloom.blurPasses = 15;
		bloom.setTreshold(0.4f);
		bloom.setBloomIntesity(1.4f);
		
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
		
		bloom.capture();
		
		// Update world
		Core.world.step(1/60.f, 8, 6);
		
		// Generate player bodies
		for (int i = 0; i < Core.toCreate.size; i++) {
			Core.players.get(Core.toCreate.get(i)).Create();
			Core.toCreate.removeIndex(i);
		}
		
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
		
		localPlayer = (Player) Core.players.get(client.GetID());
		
		if (localPlayer == null || localPlayer.body == null)
			return;
		
		// lerp camera to player position
		float lerpAmt = 0.0001f;	
		//Vector3 newPos = Core.mainCam.position.lerp(new Vector3(localPlayer.GetPosition().x,localPlayer.GetPosition().y, 0) , (float) (1 - Math.pow(lerpAmt, delta)));
		Core.mainCam.position.set(localPlayer.GetPosition().x, localPlayer.GetPosition().y, 0);
		Core.mainCam.update();
		
		// unproject mouse position
		Vector2 aimPosition = new Vector2(Gdx.input.getX(),Gdx.input.getY());
		Vector3 unprojectedPos = Core.mainCam.unproject(new Vector3(aimPosition,0));
		aimPosition = new Vector2(unprojectedPos.x, unprojectedPos.y);
		
		// Get delta x/y for velocity impulse
		float angle = MathUtils.atan2(aimPosition.y - localPlayer.GetPosition().y, aimPosition.x - localPlayer.GetPosition().x);
		float dx = MathUtils.cos(angle)*20*delta;
		float dy = MathUtils.sin(angle)*20*delta;
		
		// player movement	
		Vector2 vel = localPlayer.GetVelocity();
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			localPlayer.body.applyLinearImpulse(dx, dy, localPlayer.GetPosition().x, localPlayer.GetPosition().y, true);
		}
		if (Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()) {
			Vector2 infinite = new Vector2();
			infinite.x = localPlayer.GetPosition().x+dx*2000;
			infinite.y = localPlayer.GetPosition().y+dy*2000;
			Core.world.rayCast(callback, new Vector2(localPlayer.GetPosition().x, localPlayer.GetPosition().y), infinite);
			
			if (closestFixture.getBody().getUserData() instanceof Player)
				client.Send(new PlayerShootPacket(client.GetID(), ((Player)closestFixture.getBody().getUserData()).uid_local, new Vector2(dx*10, dy*10), hitPoint));
			
		}
		
		localPlayer.body.setTransform(localPlayer.GetPosition().x, localPlayer.GetPosition().y, angle);
		client.SendPosition(localPlayer.GetPosition().x, localPlayer.GetPosition().y, localPlayer.GetPosition().z);
		
		// render map
		if (Core.map != null) {
			Core.map.render();
		}
		
		bloom.render();
		
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
