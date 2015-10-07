package com.lum.scram.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.Player;
import com.lum.scram.net.packets.LoadMapPacket;
import com.lum.scram.net.packets.Packet;
import com.lum.scram.net.packets.PlayerJoinedPacket;
import com.lum.scram.net.packets.PlayerLeftPacket;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
	private final Server server;
	
	public GameServer() {
		server = new Server();
		server.getKryo().setRegistrationRequired(false);
	}
	
	public void Listen() {
		Preferences prefs = Gdx.app.getPreferences("Scram");
		try {
			server.start();
			server.bind(prefs.getInteger("port"), prefs.getInteger("port"));
		} catch (IOException ex) {
			Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void ConnectListeners() {
		server.addListener(new Listener() {
			public void connected(Connection conn) {
				System.out.println("CONNECTION FROM " + conn.getID());
				server.sendToTCP(conn.getID(), new LoadMapPacket("maps/map2.tmx"));
				server.sendToAllTCP(new PlayerJoinedPacket(conn.getID(), 192*Core.PIM, 50, ""));
				
				// Give new client info on all previous clients
				for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
					Player p = (Player) playerEntry.getValue();
					if (p.body == null) continue;
					float x = p.body.getPosition().x;
					float y = p.body.getPosition().y;
					server.sendToTCP(conn.getID(), new PlayerJoinedPacket(playerEntry.getKey(), x, y, p.name));
				}
				
			}
			
			public void disconnected(Connection conn) {
				System.out.println("DISCONNECT FROM " + conn.getID());
				server.sendToAllTCP(new PlayerLeftPacket(conn.getID()));
			}
			
			public void received(Connection conn, final Object obj) {
				if (obj instanceof Packet) {
					Gdx.app.postRunnable(new Runnable() {
						public void run () {
							((Packet)obj).HandlePacketServer(server);
						}
					});
				}
			}
		});
	}
	
	public int GetConnections() {
		return server.getConnections().length;
	}
	
	public void Stop() {
		server.stop();
	}
}
