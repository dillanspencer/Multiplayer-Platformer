package com.quad.entity.characters;

import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.fx.Image;
import com.quad.entity.Enemy;
import com.quad.entity.GamePlayer;
import com.quad.net.packets.Packet02Move;

public class Gremlin extends GamePlayer {

	// references
	private ArrayList<Enemy> enemies;

	private String userName;

	// animations
	private ArrayList<Image[]> sprites;
	private final int[] NUMFRAMES = { 7, 8, 6, 4, 6 };
	private final int[] SPRITEDELAYS = { 15, 5, 5, 5, 5 };

	// animation actions
	private static final int IDLE = 0;
	private static final int CHARGING = 0;
	private static final int WALKING = 1;
	private static final int CROUCH = 8;
	private static final int JUMPING = 9;
	private static final int DOUBLEJUMP = 10;
	private static final int FALLING = 6;
	private static final int SLIDING = 7;
	private static final int DEAD = 4;
	private static final int KNOCKBACK = 3;
	private static final int ATTACKING = 2;

	private boolean crouching;
	private boolean sliding;

	public Gremlin(TileMap tm) {
		super(tm);

		width = 32;
		height = 32;
		cwidth = 16;
		cheight = 16;
		coffy = 7;

		moveSpeed = 0.5;
		maxSpeed = 1.5;
		stopSpeed = 0.2;

		crouching = false;

		// load sprites
		try {

			Image spritesheet = new Image("/player/Gremlin.png");

			int count = 0;
			sprites = new ArrayList<Image[]>();
			for (int i = 0; i < NUMFRAMES.length; i++) {
				Image[] bi = new Image[NUMFRAMES[i]];
				for (int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * width, count, width, height);
				}
				sprites.add(bi);
				count += height;
			}

			// emotes
			spritesheet = new Image("/HUD/Emotes.gif");
			confused = spritesheet.getSubimage(0, 0, 14, 17);
			surprised = spritesheet.getSubimage(14, 0, 14, 17);

		} catch (Exception e) {
			e.printStackTrace();
		}

		setAnimation(IDLE);
	}

	private void getNextPosition() {

		if (knockback) {
			dy += fallSpeed * 2;
			if (!falling)
				knockback = false;
			return;
		}

		double maxSpeed = this.maxSpeed;
		if (sliding)
			maxSpeed *= 1.75;

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
		}

		if (up) {
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
			if (dy > 0) {
				dy -= stopSpeed;
				if (dy < 0) {
					dy = 0;
				}
			} else if (dy < 0) {
				dy += stopSpeed;
				if (dy > 0) {
					dy = 0;
				}
			}
		}

		// cannot move while attacking, except in air
		if ((attacking || midAttacking || topAttacking) && !(jumping || falling)) {
			dx = 0;
		}

		// charging
		if ((left || right) && sliding && dy == 0) {
			chargingTick++;
			if (facingRight)
				dx = moveSpeed * (3 - chargingTick * 0.07);
			else
				dx = -moveSpeed * (3 - chargingTick * 0.07);
		}

		if (dx == 0 || dy != 0) {
			sliding = false;
		}
	}

	public boolean isCrouching() {
		return crouching;
	}

	public void setCrouching(boolean crouching) {
		this.crouching = crouching;
	}

	public boolean isSliding() {
		return sliding;
	}

	public void setSliding() {
		if (!left && !right)
			return;
		sliding = true;
	}

	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
	}

	public void setAction(int tile) {

	}

	public void update(GameContainer gc, float dt) {

		// update position
		boolean isFalling = falling;
		getNextPosition();
		if (collision)
			checkTileMapCollision();
		setPosition(xtemp, ytemp);

		if (isFalling && !falling) {
			// SoundClip.play("land");
		}
		if (dx == 0)
			x = (int) x;

		// check done flinching
		if (flinching) {
			
		}
		if(attacking) {
			dx = 0;
			dy = 0;
		}
		if (midAttacking) {
			chargingTick++;
			maxSpeed = 2;
		}
		if (chargingTick >= 100) {
			chargingTick = 0;
			midAttacking = false;
			sliding = false;
			maxSpeed = 1;
		}

		// check attack finished
		if (currentAction == ATTACKING) {
			if (animation.hasPlayedOnce()) {
				attacking = false;
			}
		}

		// check enemy interaction
//		for (int i = 0; i < enemies.size(); i++) {
//
//			Enemy e = enemies.get(i);
//
//			// check attack
//			if (currentAction == ATTACKING && animation.getFrame() == 1 && animation.getCount() == 0) {
//				if (e.intersects(ar)) {
//					e.hit(damage);
//				}
//			}
//
//			// collision with enemy
//			if (!e.isDead() && intersects(e) && !charging && !e.isFlinching()) {
//				hit(e.getDamage());
//			}
//
//			if (e.isDead()) {
//				// SoundClip.play("ekill");
//			}
//		}

		// set animation, ordered by priority
		if (knockback) {
			if (currentAction != KNOCKBACK) {
				setAnimation(KNOCKBACK);
			}
		} else if (health == 0) {
			if (currentAction != DEAD) {
				setAnimation(DEAD);
				animation.setFrame(4);
				animation.setDelay(-1);
			}
		} else if (attacking) {
			if (currentAction != ATTACKING) {
				// SoundClip.play("attack");
				setAnimation(ATTACKING);
				ar.y = (int) y - 6;
				if (facingRight)
					ar.x = (int) x + 16;
				else
					ar.x = (int) x - 16;
			}
		} else if (midAttacking) {
			if (currentAction != CHARGING) {
				setAnimation(CHARGING);
			}
		} else if (left || right || up || down) {
			if (currentAction != WALKING) {
				setAnimation(WALKING);
			}
		} else if (crouching) {
			if (currentAction != CROUCH)
				setAnimation(CROUCH);
		} else if (currentAction != IDLE) {
			setAnimation(IDLE);
			animation.setNumFrames(4);
		}

		animation.update();
		// set direction
		if (!attacking && !midAttacking && !knockback) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}

	}

	public void render(GameContainer gc, Renderer r) {

		super.render(gc, r);
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
