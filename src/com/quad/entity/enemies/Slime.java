package com.quad.entity.enemies;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.fx.Image;
import com.quad.entity.Enemy;
import com.quad.entity.players.GamePlayer;

public class Slime extends Enemy {

	// player accessor
	private GamePlayer player;

	// animations
	private ArrayList<Image[]> sprites;
	private final int[] NUMFRAMES = { 4, 4, 5, 3, 5 };
	private final int[] FRAMEWIDTHS = { 32, 32, 32, 32, 32 };
	private final int[] FRAMEHEIGHTS = { 25, 25, 25, 25, 25 };
	private final int[] SPRITEDELAYS = { 10, 10, 10, 10, 10 };

	private final int IDLE = 0;
	private final int WALKING = 1;
	private final int ATTACKING = 2;
	private final int HIT = 3;

	// actions
	private boolean walking;
	private boolean attacking;

	// active
	private boolean active;

	public Slime(TileMap tm, GamePlayer player) {
		super(tm);

		this.player = player;

		health = maxHealth = 3;

		width = 32;
		height = 25;
		cwidth = 32;
		cheight = 25;
		coffy = 0;

		damage = 0;
		moveSpeed = 0.3;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;

		attacking = walking = false;

		// load sprites
		try {

			Image spritesheet = new Image("/enemies/slime.gif");

			int count = 0;
			sprites = new ArrayList<Image[]>();
			for (int i = 0; i < NUMFRAMES.length; i++) {
				Image[] bi = new Image[NUMFRAMES[i]];
				for (int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(j * FRAMEWIDTHS[i], count, FRAMEWIDTHS[i], FRAMEHEIGHTS[i]);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		setAnimation(IDLE);
	}

	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}

	private void getNextPosition() {
		if (left)
			dx = -moveSpeed;
		else if (right)
			dx = moveSpeed;
		else
			dx = 0;
		if ((attacking && !(jumping || falling))) {
			dx = 0;
		}
		if (falling) {
			dy += fallSpeed;
			if (dy > maxFallSpeed)
				dy = maxFallSpeed;
		}
		if (jumping && !falling) {
			dy = jumpStart;
		}
		if (knockback) {
			dy -= fallSpeed * 2;
			if (dy < -1)
				knockback = false;
			return;
		}
	}

	private void moveSlime() {
		if (!walking)
			return;

		if (!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if (!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		if (topRight) {
			left = true;
			right = facingRight = false;
		}
		if (topLeft) {
			left = false;
			right = facingRight = true;
		}

		if (dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
	}

	public boolean isWalking() {
		return walking;
	}

	public void setWalking(boolean walking) {
		this.walking = walking;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);

		if (!active) {
			if (Math.abs(player.getx() - x) < Settings.WIDTH)
				active = true;
			return;
		}

		// check if done flinching
		if (flinching) {
			flinchCount++;
			if (flinchCount > 20) {
				flinching = false;
			}
		}

		if (attacking) {
			dx = 0;
			walking = false;
		}

		// movement
		getNextPosition();
		tileMapCollision();
		calculateCorners(x, ydest + 1);
		moveSlime();
		setPosition(xtemp, ytemp);

		if (gc.getInput().isKeyPressed(KeyEvent.VK_S))
			setAttacking(true);
		// end attack
		if (currentAction == ATTACKING) {
			if (animation.hasPlayedOnce()) {
				attacking = false;
				walking = true;
			}
		}

		if (currentAction == IDLE) {
			left = right = false;
		}

		// check animation
		if (flinching) {
			if (currentAction != HIT) {
				setAnimation(HIT);
			}
		} else if (attacking) {
			if (currentAction != ATTACKING) {
				setAnimation(ATTACKING);
				System.out.println("Attack");
			}
		} else if (walking) {
			if (currentAction != WALKING) {
				setAnimation(WALKING);
			}
		} else {
			if (currentAction != IDLE) {
				setAnimation(IDLE);
			}
		}

		// update animation
		animation.update();
	}

	public void render(GameContainer gc, Renderer r) {

		if (flinching && !knockback) {
			if (flinchCount % 10 < 5)
				return;
		}

		super.renderComponents(gc, r);
	}

}
