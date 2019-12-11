package com.quad.entity;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.components.GameObject;
import com.quad.core.fx.Image;
import com.quad.net.packets.Packet02Move;

public abstract class GamePlayer extends GameObject {

	// references
	protected ArrayList<GamePlayer> players;

	// player stuff
	protected int health;
	protected int maxHealth;
	protected int damage;
	protected boolean knockback;
	protected boolean flinching;
	protected long time;
	protected boolean collision;

	// player values
	protected int level;
	protected double exp;
	protected double maxExp;

	// actions
	protected boolean attacking;
	protected boolean topAttacking;
	protected boolean midAttacking;
	protected int chargingTick;
	protected boolean teleporting;
	protected boolean firing;

	protected Rectangle ar;
	protected Rectangle aur;
	protected Rectangle cr;

	// emotes
	public Image confused;
	public Image surprised;
	public static final int NONE = 0;
	public static final int CONFUSED = 1;
	public static final int SURPRISED = 2;
	private int emote = NONE;

	public GamePlayer(TileMap tm) {
		super(tm);
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 10;
		ar.height = 20;
		aur = new Rectangle((int) x - 30, (int) y - 45, 16, 16);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 50;
		cr.height = 40;

		width = 16;
		height = 16;
		cwidth = 10;
		cheight = 16;
		coffy = 0;
		coffx = 0;
		collision = true;

		moveSpeed = 1.5;
		maxSpeed = 2;
		stopSpeed = 0.15;
		fallSpeed = 0.15;
		maxFallSpeed = 10.0;
		jumpStart = -4.2;
		stopJumpSpeed = 0.1;

		damage = 1;

		facingRight = true;

		health = maxHealth = 5;

	}

	public void init(ArrayList<GamePlayer> players) {
		this.players = players;
	}

	public int getHealth() {
		return health;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void setEmote(int i) {
		emote = i;
	}

	public void setTeleporting(boolean b) {
		teleporting = b;
	}

	public void setAttacking() {
		if (knockback)
			return;
		if (midAttacking || topAttacking)
			return;
		else
			attacking = true;
	}

	public void setMidAttacking() {
		if (knockback)
			return;
		if (attacking || topAttacking)
			return;
		else
			midAttacking = true;
	}

	public void setTopAttacking() {
		if (knockback)
			return;
		if (midAttacking || attacking)
			return;
		else
			topAttacking = true;
	}

	public void setDx(double i) {
		dx = i;
	}

	public void setDy(double i) {
		dy = i;
	}

	public void setDead() {
		health = 0;
		stop();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public double getExp() {
		return exp;
	}

	public void addExp(double exp) {
		this.exp += exp;
	}

	public void setExp(double exp) {
		this.exp = exp;
	}

	public double getMaxExp() {
		return maxExp;
	}

	public void setMaxExp(double maxExp) {
		this.maxExp = maxExp;
	}

	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long t) {
		time = t;
	}

	public void setHealth(int i) {
		health = i;
	}

	public void hit(int damage) {
		if (flinching)
			return;
		health -= damage;
		if (health < 0)
			health = 0;
		flinching = true;
		if (facingRight)
			dx = -1;
		else
			dx = 1;
		dy = -3;
		knockback = true;
	}

	public int getDamage() {
		return damage;
	}

	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}

	public void stop() {
		left = right = up = down = flinching = jumping = attacking = midAttacking = firing = false;
	}

	public void update(GameContainer gc, float dt) {
		super.updateComponents(gc, dt);
	}

	public void render(GameContainer gc, Renderer r) {
		super.renderComponents(gc, r);
	}

	public void postRender(GameContainer gc, Renderer r) {

	}

	@Override
	public void componentEvent(String name, GameObject object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	public boolean isUp() { return up;}
	public boolean isDown() { return down;}
	public boolean isLeft() { return left;}
	public boolean isRight() { return right;}
	
	public void setRight(GameContainer gc, boolean b) {
		super.setRight(b);
	}

	public void setLeft(GameContainer gc, boolean b) {
		super.setLeft(b);
	}

	public void setUp(GameContainer gc, boolean b) {
		super.setUp(b);
	}

	public void setDown(GameContainer gc, boolean b) {
		super.setDown(b);
	}

	public boolean isAttacking() {
		return attacking;
	}

	public boolean isMidAttacking() {
		return midAttacking;
	}

	public boolean isTopAttacking() {
		return topAttacking;
	}

}