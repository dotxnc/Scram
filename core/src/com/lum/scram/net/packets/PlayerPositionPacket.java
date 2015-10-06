package com.lum.scram.net.packets;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;

public class PlayerPositionPacket extends Packet {
	
	public float x,y,rot,normal;
	
	public PlayerPositionPacket() {}
	public PlayerPositionPacket(int uid, float x, float y, float rot, float normal) {
		super(uid);
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.normal = normal;
	}
	
	public void HandlePacket() {
		if (Core.players.get(uid_sender) == null || Core.players.get(uid_sender).body == null)
			return;
		
		Core.players.get(uid_sender).body.setTransform(new Vector2(x,y), rot);
		Core.players.get(uid_sender).normal = normal;
	}
	
	public void HandlePacketServer(Server server) {
		//server.sendToAllUDP(this);
		server.sendToAllExceptUDP(uid_sender, this);
	}
	
}
