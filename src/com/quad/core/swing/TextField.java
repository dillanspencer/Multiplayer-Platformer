package com.quad.core.swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.quad.core.GameContainer;
import com.quad.core.Renderer;

public class TextField implements KeyListener{
	
	private String string;
	private int x, y;
	
	public TextField(int x, int y) {
		string = "";
		this.x = x;
		this.y = y;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		string += e.getKeyChar();
	}
	
	public void render(GameContainer gc, Renderer r) {
		r.drawString(string, 0xffffff, x, y);
	}

}
