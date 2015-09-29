package com.lum.scram.net;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lum.scram.net.packets.Packet;
import com.lum.scram.net.packets.PlayerJoinedPacket;
import java.io.IOException;

public class GameClient {
	private final Client client;
	
	public GameClient() {
		client = new Client();
	}
	
	public void Connect() throws IOException {
		client.start();
		client.connect(5000, "localhost", 9696, 9697);
		
		client.sendUDP(new PlayerJoinedPacket(client.getID()));
	}
	
	public void ConnectListeners() {
		client.addListener(new Listener() {
			public void received(Connection conn, Object obj) {
				if (obj instanceof Packet) {
					((Packet)obj).HandlePacket();
				}
			}
		});
	}
	
	public boolean IsConnected() {
		return client.isConnected();
	}	
}
