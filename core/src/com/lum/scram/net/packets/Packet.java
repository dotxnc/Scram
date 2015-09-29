package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;

public class Packet {
	public int uid_sender;
	
	public Packet() {}
	public Packet(int uid) {
		uid_sender = uid;
	}
	
	public void HandlePacket() {}
	public void HandlePacketServer(Server server) {}
}
