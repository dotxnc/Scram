package com.lum.scram.net.packets;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.LaserBeam;

public class PlayerShootPacket extends Packet {
	public int uid_hit;
	public Vector2 direction;
	public Vector2 hitPoint;
	
	public PlayerShootPacket() {}
	public PlayerShootPacket(int uid, int uid_hit, Vector2 direction, Vector2 hitPoint) {
		super(uid);
		this.uid_hit = uid_hit;
		this.direction = direction;
		this.hitPoint = hitPoint;
	}
	
	public void HandlePacket() {
		Core.players.get(uid_hit).body.applyLinearImpulse(direction.x, direction.y, hitPoint.x, hitPoint.y, true);
		Vector2 pos = Core.players.get(uid_sender).body.getPosition().cpy();
		Core.beams.add(new LaserBeam(pos, hitPoint, Core.beams.size));
		
	}
	
	public void HandlePacketServer(Server server) {
		server.sendToAllUDP(this);
	}
	
}
