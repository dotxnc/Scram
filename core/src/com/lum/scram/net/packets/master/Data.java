package com.lum.scram.net.packets.master;

public class Data {
	public String ip;
	public String hoster;
	public int port;
	
	public Data() {}
	public Data(String ip, String hoster, int port) {
		this.ip = ip;
		this.hoster = hoster;
		this.port = port;
	}
}
