package com.quad.core;

public class Settings {
	
	public static final int WIDTH = 1080/3;
	public static final int HEIGHT = 640/3;
	public static final int SCALE = 3;	
	
	//multiplayer
	public static String USERNAME = "DEFAULT";
	
	public static int FPS = 60;
	
	public static boolean LIGHT = false;
	
	public static final boolean RAINING = false;
	
	public static void setLight(GameContainer gc, boolean i){
		Settings.LIGHT = i;
		gc.setLightEnable(i);
		gc.setDynamicLights(i);
	}
	
	public static void changeFps(GameContainer gc, int i){
		gc.setFrameCap(i);
	}
	
	public static void setUsername(String s) {
		Settings.USERNAME = s;
	}

}
