package com.quad.Tile;

import com.quad.core.Renderer;

public class Outdoor {

	// backgrounds
	private Background mountainBack;
	private Background mountainFront;
	private Background background;
	private Background clouds;

	public void init() {
		mountainBack = new Background("/Background/BGback.gif");
		mountainFront = new Background("/Background/BGFront.gif");
		background = new Background("/Background/CloudsBack.gif");
		clouds = new Background("/Background/CloudsFront.gif");
		background.setVector(-0.1, 0);
		mountainBack.setVector(0.2, 0);
		mountainFront.setVector(0.2, 0);
		clouds.setVector(-0.2, 0);

	}

	public void update(TileMap tm) {
		mountainBack.setPosition(tm.getx(), tm.gety());
		mountainFront.setPosition(tm.getx(), tm.gety());
		//background.setPosition(tm.getx(), tm.gety());
		//clouds.setPosition(tm.getx() + clouds.getx(), tm.gety());
		
		clouds.update();
		background.update();
	}

	public void render(Renderer r) {
		background.draw(r);
		clouds.draw(r);
		mountainBack.draw(r);
		mountainFront.draw(r);
	}

}
