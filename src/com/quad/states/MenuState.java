package com.quad.states;

import java.net.InetAddress;

import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.core.fx.Image;
import com.quad.core.swing.TextField;
import com.sun.glass.events.KeyEvent;

public class MenuState extends State{
	
	//background image
	private Image bgImage;
	private Image register;
	
	private TextField text;

	@Override
	public void init(GameContainer gc) {
		bgImage = new Image("/Hud/requestui.png");
		register = new Image("/Hud/registerui.png");
		
		text = new TextField(100, 100);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		if(gc.getInput().isKeyPressed(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
		if(gc.getInput().isKeyPressed(KeyEvent.VK_ENTER)) {
			gc.getGame().setState(gc, AbstractGame.LEVELONE);
		}
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		r.drawImage(bgImage, Settings.WIDTH/2 - (bgImage.width/2), Settings.HEIGHT/2 - (bgImage.height/2));
		r.drawImage(register, Settings.WIDTH/2 - (register.width/2), Settings.HEIGHT/2 - (register.height/2));
		
		text.render(gc, r);
	}

	@Override
	public void dipose() {
		
	}

	@Override
	public void addPlayer(InetAddress address, int x, int y, int port, String username) {
		
	}

	@Override
	public void removePlayer(String username) {
		
	}

	@Override
	public void movePlayer(GameContainer gc, String username, int x, int y, boolean[] m) {
		
	}

}
