package com.lum.scram.net;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lum.scram.Core;
import com.lum.scram.net.packets.Packet;
import com.lum.scram.net.packets.master.AddServerPacket;
import com.lum.scram.net.packets.master.RemoveServerPacket;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient {
	private final Client client;
	
	public MasterClient() {
		client = new Client();
		//client.getKryo().setRegistrationRequired(false);
		client.getKryo().register(RemoveServerPacket.class);
		client.getKryo().register(AddServerPacket.class);
		client.getKryo().register(String.class);
		client.getKryo().register(Integer.class);
	}
	
	public void Connect() {
		try {
			client.start();
			client.connect(5000, "localhost", 7779, 7779);
		} catch (IOException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		if (Core.isServer)
			Send(new AddServerPacket("localhost", 7777));
	}
	
	public void ConnectListeners() {
		client.addListener(new Listener() {
			public void received(Connection conn, final Object obj) {
				if (obj instanceof Packet) {
					Gdx.app.postRunnable(new Runnable() {
						public void run () {
							((Packet)obj).HandlePacket();
						}
					});
				}
			}
		});
	}
	
	public boolean IsConnected() {
		return client.isConnected();
	}
	
	public void Send(Object object) {
		client.sendTCP(object);
	}
	
	public void Disconnect() {
		Send(new RemoveServerPacket());
		client.close();
		client.stop();
	}
		
}
