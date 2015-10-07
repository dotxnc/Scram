package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;

public class PlayerNamePacket extends Packet {
	
	public String name;
	
	public PlayerNamePacket() {}
	public PlayerNamePacket(int uid, String name) {
		super(uid);
		this.name = name;
	}
	
	public void HandlePacket() {
		Core.players.get(uid_sender).name = name;
	}
	
	public void HandlePacketServer(Server server) {
		server.sendToAllTCP(this);
	}
	
}
