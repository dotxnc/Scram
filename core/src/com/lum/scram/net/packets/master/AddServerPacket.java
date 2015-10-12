package com.lum.scram.net.packets.master;

public class AddServerPacket {
	public String ip;
	public String hoster;
	public int port;
	
	public AddServerPacket() {}
	public AddServerPacket(String _ip, String _hoster, int _port) {
		ip = _ip;
		hoster = _hoster;
		port = _port;
	}
}
