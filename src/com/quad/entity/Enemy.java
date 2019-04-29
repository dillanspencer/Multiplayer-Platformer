package com.quad.entity;



import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;


public class Enemy extends GameObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	protected boolean knockback;
	protected double exp;
	protected int level;
	
	protected boolean flinching;
	protected long flinchCount;
	protected int type;
	
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}
	
	public boolean isFlinching(){return flinching;}
	public boolean isDead() { return dead; }
	public boolean shouldRemove() { return remove; }
	
	public void setType(int i){
		type = i;
	}
	
	public int getType(){return type;}
	public int getDamage() { return damage; }
	public int getHealth(){return health;}
	public int getMaxHealth(){return maxHealth;}
	
	
	public double getExp() {
		return exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}

	public int isLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
		knockback = true;
		flinching = true;
		flinchCount = 0;
	}
	
	public void respawn() {
		dead = false;
		remove = false;
		health = maxHealth;
		knockback = false;
		flinchCount = 0;
	}
	
	public void tileMapCollision() {
		rowTile = (int) (y / tileSize);
		colTile = (int) (x / tileSize);

		currCol = (int) x / tileSize;
		currRow = (int) y / tileSize;

		xdest = x + dx;
		ydest = y + dy;

		xtemp = x;
		ytemp = y;

		calculateCorners(x, ydest);
		if (dy < 0) {
			if (topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			} else {
				ytemp += dy;
			}
		}
		if (dy > 0) {
			if (bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
			} else {
				ytemp += dy;
			}
		}

		calculateCorners(xdest, y);
		if (dx < 0) {
			if (topLeft || bottomLeft) {
				dx = 0;
				 xtemp = currCol * tileSize + cwidth / 2;
			} else {
				xtemp += dx;
			}
		}
		if (dx > 0) {
			if (topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			} else {
				xtemp += dx;
			}
		}

		if (!falling) {
			calculateCorners(x, ydest + 1);
			if (!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);
		
	}

	
	public void render(GameContainer gc, Renderer r) {
		super.renderComponents(gc, r);
		
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














