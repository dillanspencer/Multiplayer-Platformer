package com.quad.net.packets;

import com.quad.net.GameClient;

public class Packet10Start extends Packet{
	
	private String username;
	
	public Packet10Start(byte[] data) {
		super(10);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
	}
	
	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("10" + this.username).getBytes();
	}

	public String getUsername() {
		return username;
	}

}
