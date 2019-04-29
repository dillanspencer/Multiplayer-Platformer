package com.quad.net.entity;

import java.net.InetAddress;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.entity.players.Adventurer;

public class PlayerMP extends Adventurer{
	
	public InetAddress ipAddress;
	public int port;

	public PlayerMP(TileMap tm,int x, int y, InetAddress ipAddress, int port) {
		super(tm);
		setPosition(x, y);
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public void update(GameContainer gc, float dt) {
		super.update(gc, dt);
	}

}
