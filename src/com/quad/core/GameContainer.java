package com.quad.core;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import com.quad.core.components.Physics;
import com.quad.net.GameClient;
import com.quad.net.packets.Packet00Login;
import com.quad.net.users.User;

public class GameContainer implements Runnable
{
	private Thread thread;
	private AbstractGame game;
	private Window window;
	private Renderer renderer;
	private Input input;
	private Physics physics;
	private GameClient socketClient;
	private User user;
	private ArrayList<User> onlineUsers;
	
	public static int width = 320, height = 240;
	private float scale = 2.0f;
	private String title = "The Hollow";
	private double frameCap = 1.0 / Settings.FPS;
	private boolean isRunning = false;
	
	private boolean lockFrameRate = false;
	private boolean lightEnable = false;
	private boolean dynamicLights = false;
	private boolean clearScreen = false;
	private boolean debug = false;
	private int fullscreen = 0;
	private boolean isServer = false;
	
	public GameContainer(AbstractGame game)
	{
		this.game = game;
	}
	
	public void start()
	{
		if(isRunning)
			return;
	
		
		game = new AbstractGame();
		
		if(fullscreen == 1){
			window = new Window(this,true);
		}else{
			window = new Window(this);
		}
		renderer = new Renderer(this);
		input = new Input(this);
		physics = new Physics();
		
		socketClient = new GameClient(this, "192.168.0.17");
		socketClient.start();
		
		user = new User("Dillan " + new Random().nextInt(100));
		onlineUsers = new ArrayList<User>();
		
		
		thread = new Thread(this);
		thread.start();
		
	}
	
	public boolean isServer() {
		return isServer;
	}

	public void setServer(boolean isServer) {
		this.isServer = isServer;
	}

	public void stop()
	{
		if(!isRunning)
			return;
		
		isRunning = false;
	}
	
	public void run()
	{
		isRunning = true;
		
		double firstTime = 0;
		double lastTime = System.nanoTime() / 1000000000.0;
		double passedTime = 0;
		double unprocessedTime = 0;
		double frameTime = 0;
		int frames = 0;
		int fps = 0;
		
		game.init(this);	
		
		while(isRunning)
		{
			boolean render = !lockFrameRate;
			
			firstTime = System.nanoTime() / 1000000000.0;
			passedTime = firstTime - lastTime;
			lastTime = firstTime;
			
			unprocessedTime += passedTime;
			frameTime += passedTime;
			
			while(unprocessedTime >= frameCap)
			{
				if(input.isKeyPressed(KeyEvent.VK_F2)) debug = !debug;
				
				game.update(this, (float)frameCap);
				physics.update();
				input.update();
				unprocessedTime -= frameCap;
				render = true;
				
				if(frameTime >= 1)
				{
					frameTime = 0;
					fps = frames;
					frames = 0;
				}
			}
			
			if(render)
			{
				if(clearScreen) renderer.clear();
				
				game.render(this, renderer);
				if(lightEnable || dynamicLights){ renderer.drawLightArray(); renderer.flushMaps();}
				renderer.setTranslate(false);
				if(debug) renderer.drawString("FPS-" + fps, 0xffffffff, 0, 0);
				renderer.setTranslate(true);
				
				window.update();
				frames++;
			}
			else
			{
				try
				{
					Thread.sleep(1);
				} 
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
		
		cleanUp();
	}
	
	private void cleanUp()
	{
		window.cleanUp();
	}

	public void setFrameCap(int number)
	{
		frameCap = 1.0 / number;
	}
	
	public double getFrameCap()
	{
		return frameCap;
	}
	
	public GameClient getClient() {
		return socketClient;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public float getScale()
	{
		return scale;
	}

	public void setScale(float scale)
	{
		this.scale = scale;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Window getWindow()
	{
		return window;
	}

	public boolean isDynamicLights()
	{
		return dynamicLights;
	}

	public void setDynamicLights(boolean dynamicLights)
	{
		this.dynamicLights = dynamicLights;
	}

	public boolean isLightEnable()
	{
		return lightEnable;
	}

	public void setLightEnable(boolean lightEnable)
	{
		this.lightEnable = lightEnable;
	}

	public boolean isClearScreen()
	{
		return clearScreen;
	}

	public void setClearScreen(boolean clearScreen)
	{
		this.clearScreen = clearScreen;
	}

	public AbstractGame getGame()
	{
		return game;
	}

	public void setGame(AbstractGame game)
	{
		this.game = game;
	}

	public Input getInput()
	{
		return input;
	}

	public void setInput(Input input)
	{
		this.input = input;
	}

	public boolean isDebug()
	{
		return debug;
	}
	

	public ArrayList<User> getOnlineUsers() {
		return onlineUsers;
	}

	public int getFullscreen() {
		return fullscreen;
	}

	public void setFullscreen(int fullscreen) {
		this.fullscreen = fullscreen;
	}

	public void setDebug(boolean debug)
	{
		this.debug = debug;
	}

	public boolean isLockFrameRate()
	{
		return lockFrameRate;
	}

	public void setLockFrameRate(boolean lockFrameRate)
	{
		this.lockFrameRate = lockFrameRate;
	}

	public Physics getPhysics()
	{
		return physics;
	}

	public void setPhysics(Physics physics)
	{
		this.physics = physics;
	}
}
