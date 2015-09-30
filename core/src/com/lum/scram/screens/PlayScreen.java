package com.lum.scram.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lum.scram.Core;
import static com.lum.scram.Core.MIP;
import com.lum.scram.Player;
import com.lum.scram.Scram;
import com.lum.scram.net.GameClient;
import com.lum.scram.net.GameServer;
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
	
	public PlayScreen(Scram game) {
		this.game = game;
		
		Core.world = new World(new Vector2(0, 0), true);
		Core.players = new HashMap<Integer, Player>();
		Core.toCreate = new Array<Integer>();
		
		server = new GameServer();
		client = new GameClient();
		
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
		
		// Update world
		Core.world.step(1/60.f, 8, 6);
		
		// Generate player bodies
		for (int i = 0; i < Core.toCreate.size; i++) {
			Core.players.get(Core.toCreate.get(i)).Create();
			Core.toCreate.removeIndex(i);
		}
		
		// Debug text rendering
		Core.batch.begin();
		if (Core.isServer)
			Core.font.draw(Core.batch, "THIS IS A SERVER", 10, 10);
		else
			Core.font.draw(Core.batch, "THIS IS A CLIENT", 10, 10);
		
		Core.font.draw(Core.batch, "# Players = " + server.GetConnections(), 10, 25);
		Core.font.draw(Core.batch, "# Network Bodies = " + Core.players.size(), 10, 40);
		Core.font.draw(Core.batch, "Connected = " + client.IsConnected(), 10, 65);
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			if (playerEntry.getValue().body == null)
				continue;
			
			Player p = (Player)playerEntry.getValue();
			
			Core.font.draw(Core.batch, p.GetPosition().x + " " + p.GetPosition().y, p.GetPosition().x, -p.GetPosition().y);
		}
		
		Core.batch.end();
		
		
		/* UPDATE AND RENDER PLAYERS */
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet())
			((Player)playerEntry.getValue()).Render(Core.srend);
		
		if (Core.toCreate.contains(client.GetID(), true))
			return;
		
		Player localPlayer = (Player) Core.players.get(client.GetID());
		
		if (localPlayer == null || localPlayer.body == null)
			return;
		
		localPlayer.HandleInput();
		client.SendPosition(localPlayer.GetPosition().x, localPlayer.GetPosition().y, localPlayer.GetPosition().z);
		
		float lerpAmt = 0.01f; //framerate independent lerping
		Vector3 newPos = Core.mainCam.position.lerp(new Vector3(localPlayer.GetPosition().x,localPlayer.GetPosition().y, 0) , (float) (1 - Math.pow(lerpAmt, delta)));
		Core.mainCam.position.set(newPos);
		
		Core.mainCam.update();

		
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
