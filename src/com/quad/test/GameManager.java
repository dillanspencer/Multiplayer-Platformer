package com.quad.test;


import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Settings;

public class GameManager
{
	
	public static void main(String args[])
	{
		GameContainer gc = new GameContainer(new AbstractGame());
		OptionManager manager = new OptionManager(gc);
		
	}

	
}
