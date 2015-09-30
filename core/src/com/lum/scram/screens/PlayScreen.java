package com.lum.scram.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.lum.scram.Core;
import com.lum.scram.Player;
import com.lum.scram.Scram;
import com.lum.scram.net.GameClient;
import com.lum.scram.net.GameServer;
import java.io.IOException;
import java.util.HashMap;

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
		
		server.ConnectListeners();
		server.Listen();
		
		
		client.ConnectListeners();
		client.Connect();
		
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Core.batch.begin();
		if (Core.isServer)
			Core.font.draw(Core.batch, "THIS IS A SERVER", 10, 10);
		else
			Core.font.draw(Core.batch, "THIS IS A CLIENT", 10, 10);
		
		//Core.font.draw(Core.batch, "# Players = " + Integer.toString(Core.players.size()), 10, 25);
		Core.font.draw(Core.batch, "# Players = " + server.GetConnections(), 10, 25);
		Core.font.draw(Core.batch, "Connected = " + client.IsConnected(), 10, 40);
		Core.batch.end();
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
