package com.quad.net.packets;

import com.quad.net.GameClient;
import com.quad.net.GameServer;

public class Packet02Move extends Packet {

	private String username;
	private int x, y;

	// attributes
	public final int NUM_MOVES = 6;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int JUMPING = 2;
	public static final int FALLING = 3;
	public static final int CROUCHING = 4;
	public static final int SLIDING = 5;

	private boolean[] movement;

	public Packet02Move(byte[] data) {
		super(02);
		movement = new boolean[NUM_MOVES];
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		movement[LEFT] = Integer.parseInt(dataArray[3]) == 1;
		movement[RIGHT] = Integer.parseInt(dataArray[4]) == 1;
		movement[JUMPING] = Integer.parseInt(dataArray[5]) == 1;
		movement[FALLING] = Integer.parseInt(dataArray[6]) == 1;
		movement[CROUCHING] = Integer.parseInt(dataArray[7]) == 1;
		movement[SLIDING] = Integer.parseInt(dataArray[8]) == 1;
	}

	public Packet02Move(String username, int x, int y, boolean[] m) {
		super(02);
		movement = new boolean[NUM_MOVES];
		this.username = username;
		this.x = x;
		this.y = y;
		movement[LEFT] = m[LEFT];
		movement[RIGHT] = m[RIGHT];
		movement[JUMPING] = m[JUMPING];
		movement[FALLING] = m[FALLING];
		movement[CROUCHING] = m[CROUCHING];
		movement[SLIDING] = m[SLIDING];
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.y + "," + (movement[LEFT] ? 1 : 0) + ","
				+ (movement[RIGHT] ? 1 : 0) + "," + (movement[JUMPING] ? 1 : 0) + "," + (movement[FALLING] ? 1 : 0)
				+ "," + (movement[CROUCHING] ? 1 : 0) + "," + (movement[SLIDING] ? 1 : 0)).getBytes();

	}

	public String getUsername() {
		return username;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public boolean isLeft() {
		return movement[LEFT];
	}

	public boolean isRight() {
		return movement[RIGHT];
	}

	public boolean isJumping() {
		return movement[JUMPING];
	}

	public boolean isFalling() {
		return movement[FALLING];
	}
	
	public boolean isCrouching() {
		return movement[CROUCHING];
	}
	
	public boolean isSliding() {
		return movement[SLIDING];
	}
}