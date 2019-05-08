package com.quad.entity.players;

import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.Image;
import com.quad.entity.Enemy;
import com.quad.net.packets.Packet02Move;

public class Adventurer extends GamePlayer {

	// references
	private ArrayList<Enemy> enemies;

	private String userName;

	// animations
	private ArrayList<Image[]> sprites;
	private final int[] NUMFRAMES = { 4, 3, 5, 2, 2, 4, 2, 4 };
	private final int[] SPRITEDELAYS = { 10, 7, 7, 5, 10, 4, 4, 6 };

	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 2;
	private static final int CROUCH = 1;
	private static final int PREJUMP = 3;
	private static final int JUMPING = 4;
	private static final int DOUBLEJUMP = 5;
	private static final int FALLING = 6;
	private static final int SLIDING = 7;
	private static final int DEAD = 15;
	private static final int KNOCKBACK = 4;
	private static final int ATTACKING = 7;

	private boolean crouching;
	private boolean sliding;

	public Adventurer(TileMap tm) {
		super(tm);

		width = 50;
		height = 37;
		cwidth = 10;
		cheight = 22;
		coffy = 7;

		crouching = false;

		setUserName(Settings.USERNAME);

		// load sprites
		try {

			Image spritesheet = new Image("/player/adventurer.gif");

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

		// cannot move while attacking, except in air
		if ((attacking || upattacking || charging || firing) && !(jumping || falling)) {
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

		// jumping
		if (jumping && !falling) {
			// sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
			// SoundClip.play("jump");
		}

		if (doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			// SoundClip.play("jump");

		}

		if (!falling)
			alreadyDoubleJump = false;

		// falling
		if (falling) {
			dy += fallSpeed;
			if (dy < 0 && !jumping)
				dy += stopJumpSpeed;
			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
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

	public void init(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}

	public void setAction(int tile) {

	}

	public void update(GameContainer gc, float dt) {
		inventory.update(gc, dt);
		if (inventory.isActive())
			return;

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
			flinchCount++;
			if (flinchCount > 120) {
				flinching = false;
			}
		}

		if (chargingTick >= 20) {
			chargingTick = 0;
			sliding = false;
		}

		if (!sliding) {
			cheight = 22;
			coffy = 7;
		}

		// check attack finished
		if (currentAction == ATTACKING) {
			if (animation.hasPlayedOnce()) {
				attacking = false;
				upattacking = false;
				firing = false;
			}
		}

		if (currentAction == DOUBLEJUMP) {
			if (animation.hasPlayedOnce()) {
				alreadyDoubleJump = false;
			}
		}

		// check enemy interaction
		for (int i = 0; i < enemies.size(); i++) {

			Enemy e = enemies.get(i);

			// check attack
			if (currentAction == ATTACKING && animation.getFrame() == 1 && animation.getCount() == 0) {
				if (e.intersects(ar)) {
					e.hit(damage);
				}
			}

			// collision with enemy
			if (!e.isDead() && intersects(e) && !charging && !e.isFlinching()) {
				hit(e.getDamage());
			}

			if (e.isDead()) {
				// SoundClip.play("ekill");
			}
		}

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
		} else if (dy < 0) {
			if (currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
		} else if (alreadyDoubleJump) {
			if (currentAction != DOUBLEJUMP) {
				setAnimation(DOUBLEJUMP);
			}
		} else if (dy > 0) {
			if (currentAction != FALLING) {
				if (!dead)
					setAnimation(FALLING);

			}
		} else if (sliding) {
			if (currentAction != SLIDING) {
				setAnimation(SLIDING);
				cheight = 15;
				coffy = 11;
			}
		} else if (left || right) {
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

		if (dy >= 8.0)
			fallDamage = true;
		if (bottomLeft || bottomRight) {
			if (fallDamage) {
				hit(1);
				knockback = true;
				flinching = true;
				fallDamage = false;
			}
		}

		animation.update();
		// set direction
		if (!attacking && !upattacking && !charging && !knockback) {
			if (right)
				facingRight = true;
			if (left)
				facingRight = false;
		}
		boolean[] m = {left,right,jumping, falling, crouching, sliding};
		Packet02Move movePacket = new Packet02Move(this.userName, (int) x, (int) y, m);
		movePacket.writeData(gc.getClient());
	}

	
	public void render(GameContainer gc, Renderer r) {
		// render username
		r.drawString(userName, 0xffffff, (int) (x - 10 + tileMap.getx()), (int) (y - 20 + tileMap.gety()));

		// flinch
		if (flinching && !knockback) {
			if (flinchCount % 10 < 5) {
				return;
			}
		}

		super.render(gc, r);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
