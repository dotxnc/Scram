package com.lum.scram.net.packets.master;

public class AddServerPacket {
	public String ip;
	public int port;
	
	public AddServerPacket() {}
	public AddServerPacket(String _ip, int _port) {
		ip = _ip;
		port = _port;
	}
}
