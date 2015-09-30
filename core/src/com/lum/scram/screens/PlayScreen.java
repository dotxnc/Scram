package com.lum.scram.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lum.scram.Core;
import static com.lum.scram.Core.MIP;
import static com.lum.scram.Core.PIM;
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
			
			Core.font.draw(Core.batch, p.GetPosition().x + " " + p.GetPosition().y, p.GetPosition().x+9*MIP, -p.GetPosition().y+8*MIP);
		}
		
		Core.batch.end();
		
		/***************************
		 * 
		 * TODO: MOVE ALL THIS OVER TO THE PLAYER CLASS
		 * 
		 ***************************/
		
		// Player rendering
		Core.srend.setProjectionMatrix(Core.mainCam.combined);
		Core.srend.begin(ShapeType.Line);
		
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			if (((Player)playerEntry.getValue()).body != null) {
				Vector2 pos = ((Player)playerEntry.getValue()).body.getPosition();
				float rot = ((Player)playerEntry.getValue()).body.getAngle();
				//Core.srend.circle(pos.x*PIM, pos.y*PIM, 0.5f, 100);
				Core.srend.rect(pos.x*PIM, pos.y*PIM, 0.25f, 0.25f, 0.5f, 0.5f, 1, 1, rot);
				Core.srend.circle(pos.x*PIM+0.25f, pos.y*PIM+0.25f, 0.15f, 100);
			}
			
		}
		
		Core.srend.end();
		
		if (Core.toCreate.contains(client.GetID(), true))
			return;
		
		Player p = (Player) Core.players.get(client.GetID());
		
		if (p == null || p.body == null)
			return;
		
		client.SendPosition(p.GetPosition().x, p.GetPosition().y, p.GetPosition().z);
		
		/***************************
		 * 
		 * TODO: MOVE ALL THIS OVER TO THE PLAYER CLASS
		 * 
		 ***************************/
		
		if (Gdx.input.isKeyPressed(Keys.W)) {
			p.body.applyLinearImpulse(0, -20, p.GetPosition().x, p.GetPosition().y, true);
		}
		
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
