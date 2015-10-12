package com.lum.scram.net;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lum.scram.net.packets.master.AddServerPacket;
import com.lum.scram.net.packets.master.Data;
import com.lum.scram.net.packets.master.GetServerListPacket;
import com.lum.scram.net.packets.master.RemoveServerPacket;
import com.lum.scram.screens.MenuScreen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MasterClient {
	private final Client client;
	
	public MasterClient() {
		client = new Client();
		//client.getKryo().setRegistrationRequired(false);
		client.getKryo().register(Data.class);
		client.getKryo().register(ArrayList.class);
		client.getKryo().register(GetServerListPacket.class);
		client.getKryo().register(RemoveServerPacket.class);
		client.getKryo().register(AddServerPacket.class);
		client.getKryo().register(String.class);
		client.getKryo().register(Integer.class);
	}
	
	public void Connect() {
		try {
			client.start();
			client.connect(10000, "104.236.57.226", 7779, 7779);
		} catch (IOException ex) {
			Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	public void ConnectListeners() {
		client.addListener(new Listener() {
			public void received(Connection conn, final Object obj) {
				if (obj instanceof GetServerListPacket) {
					GetServerListPacket p = (GetServerListPacket)obj;
					Array<String> dump = new Array<String>();
					int index = 0;
					for (Data d : p.list) {
						dump.add(index + ". " + d.hoster + "'s game");
						index++;
					}
					MenuScreen.setServerList(dump.toArray());
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
