package com.lum.scram.net.packets;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.Player;

public class PlayerJoinedPacket extends Packet {
	
	private float x,y;
	
	public PlayerJoinedPacket() {}
	
	public PlayerJoinedPacket(int uid, float x, float y) {
		super(uid);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void HandlePacket() {
		if (Core.players.containsKey(uid_sender))
			return;
		
		Core.players.put(uid_sender, new Player(x, y));
		Core.toCreate.add(uid_sender);
	}
	
	@Override
	public void HandlePacketServer(Server server) {
		server.sendToAllUDP(this);
	}
	
	
}
