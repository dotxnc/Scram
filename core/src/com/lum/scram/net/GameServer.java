package com.lum.scram.net;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.net.packets.Packet;
import java.io.IOException;

public class GameServer {
	private final Server server;
	
	public GameServer() {
		server = new Server();
	}
	
	public void Listen() throws IOException {
		server.start();
		server.bind(9696, 9697);
	}
	
	public void ConnectListeners() {
		server.addListener(new Listener() {
			public void connected(Connection conn) {
				System.out.println("CONNECTION FROM " + conn.getID());
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
