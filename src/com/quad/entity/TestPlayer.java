package com.quad.entity;

import java.awt.Rectangle;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.ShadowType;
import com.quad.net.packets.Packet02Move;

public class TestPlayer extends GameObject {

	public TestPlayer(TileMap tm) {
		super(tm);
		moveSpeed = 0.5f;
		maxSpeed = 1;

		moveSpeed = 0.5;
		maxSpeed = 1;
		stopSpeed = 0.1;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStart = -4.2;
		stopJumpSpeed = 0.1;

		cwidth = 24;
		cheight = 16;
		coffx = 12;
		coffy = 8;
	}

	private void getNextPosition() {

		double maxSpeed = this.maxSpeed;

		// movement
		if (left) {
			dx -= moveSpeed;
			if (dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		} else if (right) {
			dx += moveSpeed;
			if (dx > maxSpeed) {
				dx = maxSpeed;
			}
		} else if (up) {
			dy -= moveSpeed;
			if (dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		} else if (down) {
			dy += moveSpeed;
			if (dy > maxSpeed) {
				dy = maxSpeed;
			}
		} else {
			if (dx > 0) {
				dx -= stopSpeed;
				if (dx < 0) {
					dx = 0;
				}
			} else if (dx < 0) {
				dx += stopSpeed;
				if (dx > 0) {
					dx = 0;
				}
			}
			else if(dy > 0) {
				dy -= stopSpeed;
				if(dy < 0) {
					dy = 0;
				}
			}
			else if(dy < 0) {
				dy += stopSpeed;
				if(dy > 0) {
					dy = 0;
				}
			}
		}
	}

	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);

		checkTileMapCollision();
		getNextPosition();
		setPosition(xtemp, ytemp);

		boolean[] m = { left, right, jumping, falling };
		Packet02Move movePacket = new Packet02Move(gc.getUser().getUserName(), (int) x, (int) y, m);
		movePacket.writeData(gc.getClient());
	}

	public void render(GameContainer gc, Renderer r) {
		r.drawFillRect((int) x, (int) y, 16, cheight, 0xffffff);
		Rectangle re = getRectangle();
		re.x += xmap;
		re.y += ymap;
		//r.drawRect(re.x, re.y,re.width , re.height, 0xfc0317, ShadowType.NONE);
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
