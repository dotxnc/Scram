package com.lum.scram.net.packets;

import com.lum.scram.Core;

public class PlayerLeftPacket extends Packet {
	
	public PlayerLeftPacket() {}
	public PlayerLeftPacket(int uid) {
		super(uid);
	}
	
	public void HandlePacket() {
		if (Core.players.containsKey(uid_sender)) {
			Core.players.get(uid_sender).dispose();
			Core.players.remove(uid_sender);
			
			System.out.println("PLAYER REMOVED");
		}
	}
	
}
