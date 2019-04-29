package com.quad.core.components;

import java.net.InetAddress;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.net.entity.PlayerMP;

public abstract class State
{
	protected TileMap tm;
	protected ArrayList<PlayerMP> connectedPlayers = new ArrayList<PlayerMP>();
	
	protected ObjectManager manager = new ObjectManager();
	public abstract void init(GameContainer gc);
	public abstract void update(GameContainer gc, float dt);
	public abstract void render(GameContainer gc, Renderer r);
	public abstract void dipose();
	
	public ObjectManager getManager()
	{
		return manager;
	}
	public void setManager(ObjectManager manager)
	{
		this.manager = manager;
	}
	
	public TileMap getTileMap() {
		return tm;
	}
	
	public abstract void addPlayer(InetAddress address,int x, int y, int port, String username);
	public abstract void removePlayer(String username);
	public abstract void movePlayer(GameContainer gc, String username, int x, int y, boolean[] m);
}
