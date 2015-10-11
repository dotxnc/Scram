package com.lum.scram.net.packets;

import com.esotericsoftware.kryonet.Server;
import com.lum.scram.Core;
import com.lum.scram.Player;
import java.util.Map;

public class PlayerLoginPacket extends Packet {
	
	public PlayerLoginPacket() {}
	public PlayerLoginPacket(int uid) {
		super(uid);
	}
	
	public void HandlePacket() {}
	public void HandlePacketServer(Server server) {
		System.out.println("PLAYER LOGIN FROM " + uid_sender);
		server.sendToTCP(uid_sender, new LoadMapPacket("maps/map2.tmx"));
		server.sendToAllTCP(new PlayerJoinedPacket(uid_sender, 192*Core.PIM, 50, ""));
		
		// Give new client info on all previous clients
		for (Map.Entry<Integer, Player> playerEntry : Core.players.entrySet()) {
			Player p = (Player) playerEntry.getValue();
			if (p.body == null) continue;
			float x = p.body.getPosition().x;
			float y = p.body.getPosition().y;
			server.sendToTCP(uid_sender, new PlayerJoinedPacket(playerEntry.getKey(), x, y, p.name));
		}
	}
	
}
