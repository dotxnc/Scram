package com.lum.scram.net.packets;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;

public class PlayerInfoPacket extends Packet {
	
	public float x,y,rot,normal,health;
	
	public PlayerInfoPacket() {}
	public PlayerInfoPacket(int uid, float x, float y, float rot, float normal, float health) {
		super(uid);
		this.x = x;
		this.y = y;
		this.rot = rot;
		this.normal = normal;
		this.health = health;
	}
	
	public void HandlePacket() {
		if (Core.players.get(uid_sender) == null || Core.players.get(uid_sender).body == null)
			return;
		
		Core.players.get(uid_sender).body.setTransform(new Vector2(x,y), rot);
		Core.players.get(uid_sender).normal = normal;
		Core.players.get(uid_sender).health = health;
	}
	
	public void HandlePacketServer(Server server) {
		//server.sendToAllUDP(this);
		server.sendToAllExceptUDP(uid_sender, this);
	}
	
}
