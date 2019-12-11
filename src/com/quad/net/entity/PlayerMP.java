package com.quad.net.entity;

import java.net.InetAddress;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.components.GameObject;

public class PlayerMP extends GameObject{
	
	public InetAddress ipAddress;
	public int port;
	private String userName;
	
	public int x;
	public int y;

	public PlayerMP(TileMap tm,int x, int y, InetAddress ipAddress, int port) {
		super(tm);
		this.ipAddress = ipAddress;
		this.port = port;
		this.x = x;
		this.y = y;
	}
	
	public void update(GameContainer gc, float dt) {
		
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}


	@Override
	public void componentEvent(String name, GameObject object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	

}
