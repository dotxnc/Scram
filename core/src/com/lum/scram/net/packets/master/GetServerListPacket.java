package com.lum.scram.net.packets.master;

import java.util.ArrayList;

public class GetServerListPacket {
	public ArrayList<Data> list;
	public GetServerListPacket() {}
	public GetServerListPacket(ArrayList<Data> list) {
		this.list = list;
	}
}
