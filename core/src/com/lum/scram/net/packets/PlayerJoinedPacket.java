package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.Player;

public class PlayerJoinedPacket extends Packet {
	
	private float x,y;
	private String name;
	
	public PlayerJoinedPacket() {}
	
	public PlayerJoinedPacket(int uid, float x, float y, String name) {
		super(uid);
		this.x = x;
		this.y = y;
		this.name = name;
	}
	
	@Override
	public void HandlePacket() {
		if (Core.players.containsKey(uid_sender))
			return;
		
		Core.players.put(uid_sender, new Player(x, y, uid_sender, name));
		Core.toCreate.add(uid_sender);
	}
	
	@Override
	public void HandlePacketServer(Server server) {
		server.sendToAllTCP(this);
	}
	
	
}
