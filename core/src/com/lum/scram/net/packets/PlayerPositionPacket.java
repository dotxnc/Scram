package com.lum.scram.net.packets;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;

public class PlayerPositionPacket extends Packet {
	
	public float x,y,rot;
	
	public PlayerPositionPacket() {}
	public PlayerPositionPacket(int uid, float x, float y, float rot) {
		super(uid);
		this.x = x;
		this.y = y;
		this.rot = rot;
	}
	
	public void HandlePacket() {
		Core.players.get(uid_sender).body.getTransform().setPosition(new Vector2(x, y));
		Core.players.get(uid_sender).body.getTransform().setRotation(rot);
	}
	
	public void HandlePacketServer(Server server) {
		server.sendToAllUDP(this);
	}
	
}
