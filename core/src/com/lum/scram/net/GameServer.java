package com.lum.scram.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.net.packets.Packet;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameServer {
	private final Server server;
	
	public GameServer() {
		server = new Server();
		server.getKryo().setRegistrationRequired(false);
	}
	
	public void Listen() {
		try {
			server.start();
			server.bind(9696, 9696);
		} catch (IOException ex) {
			Logger.getLogger(GameServer.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void ConnectListeners() {
		server.addListener(new Listener() {
			public void connected(Connection conn) {
				System.out.println("CONNECTION FROM " + conn.getID());
			}
			
			public void disconnected(Connection conn) {
				System.out.println("DISCONNECT FROM " + conn.getID());
			}
			
			public void received(Connection conn, Object obj) {
				System.out.println("Packet");
				if (obj instanceof Packet) {
					((Packet)obj).HandlePacketServer(server);
				}
			}
		});
	}
	
	public int GetConnections() {
		return server.getConnections().length;
	}
}
