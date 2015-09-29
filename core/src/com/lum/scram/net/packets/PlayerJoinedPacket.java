package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.Player;

public class PlayerJoinedPacket extends Packet {
	
	public PlayerJoinedPacket() {}
	
	public PlayerJoinedPacket(int uid) {
		super(uid);
	}
	
	@Override
	public void HandlePacket() {
		Core.players.put(uid_sender, new Player(100, 100));
		Core.toCreate.add(uid_sender);
	}
	
	@Override
	public void HandlePacketServer(Server server) {
		server.sendToAllUDP(this);
	}
	
	
}
