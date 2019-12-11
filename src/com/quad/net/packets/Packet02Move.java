package com.quad.net.packets;

import com.quad.net.GameClient;

public class Packet02Move extends Packet {

	private String username;
	private int x, y;

	// attributes
	public final int NUM_MOVES = 10;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;
	public static final int ATTACKING = 4;
	public static final int MIDATTACKING = 5;
	public static final int TOPATTACKING = 6;

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
		movement[UP] = Integer.parseInt(dataArray[5]) == 1;
		movement[DOWN] = Integer.parseInt(dataArray[6]) == 1;
		movement[ATTACKING] = Integer.parseInt(dataArray[7]) == 1;
		movement[MIDATTACKING] = Integer.parseInt(dataArray[8]) == 1;
		movement[TOPATTACKING] = Integer.parseInt(dataArray[9]) == 1;
	}

	public Packet02Move(String username, int x, int y, boolean[] m) {
		super(02);
		movement = new boolean[NUM_MOVES];
		this.username = username;
		this.x = x;
		this.y = y;
		movement[LEFT] = m[LEFT];
		movement[RIGHT] = m[RIGHT];
		movement[UP] = m[UP];
		movement[DOWN] = m[DOWN];
		movement[ATTACKING] = m[ATTACKING];
		movement[MIDATTACKING] = m[MIDATTACKING];
		movement[TOPATTACKING] = m[TOPATTACKING];
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());
	}

	@Override
	public byte[] getData() {
		return ("02" + this.username + "," + this.x + "," + this.y + "," + (movement[LEFT] ? 1 : 0) + ","
				+ (movement[RIGHT] ? 1 : 0) + "," + (movement[UP] ? 1 : 0) + "," + (movement[DOWN] ? 1 : 0) + ","
				+ (movement[ATTACKING] ? 1 : 0) + "," + (movement[MIDATTACKING] ? 1 : 0) + ","
				+ (movement[TOPATTACKING] ? 1 : 0)).getBytes();

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

	public boolean isUp() {
		return movement[UP];
	}

	public boolean isDown() {
		return movement[DOWN];
	}

	public boolean isAttacking() {
		return movement[ATTACKING];
	}

	public boolean isMidAttacking() {
		return movement[MIDATTACKING];
	}

	public boolean isTopAttacking() {
		return movement[TOPATTACKING];
	}
}