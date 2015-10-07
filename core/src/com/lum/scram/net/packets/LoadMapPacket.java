package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.GameMap;

public class LoadMapPacket extends Packet {
	
	private String name;
	
	public LoadMapPacket() {}
	public LoadMapPacket(String name) {
		this.name = name;
	}
	
	public void HandlePacket() {
		Core.map = new GameMap(name);
	}
	
	public void HandlePacketServer(Server server) {
		server.sendToAllTCP(this);
	}

}
