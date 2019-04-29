package com.quad.net.packets;

import com.quad.net.GameClient;
import com.quad.net.GameServer;

public abstract class Packet {
	
	public static enum PacketTypes{
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02);
		
		private int packetId;
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}
		
		public int getID() {
			return packetId;
		}
	}
	
	public byte packetId;
	
	public Packet(int packetId) {
		this.packetId = (byte)packetId;
	}

	public abstract void writeData(GameServer server);
	public abstract void writeData(GameClient client);
	
	public String readData(byte[] data) {
		String message = new String(data).trim();
		return message.substring(2);
	}
	
	public abstract byte[] getData();
	
	public static PacketTypes lookupPacket(String id) {
		try {
			return lookupPacket(Integer.parseInt(id));
		}catch(NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
	
	public static PacketTypes lookupPacket(int id) {
		for(PacketTypes p : PacketTypes.values()) {
			if(p.getID() == id) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}
}
