package com.quad.states;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.InetAddress;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.quad.Tile.Outdoor;
import com.quad.Tile.TileMap;
import com.quad.core.AbstractGame;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.GameObject;
import com.quad.core.components.State;
import com.quad.core.fx.Light;
import com.quad.entity.Enemy;
import com.quad.entity.Fly;
import com.quad.entity.InventoryItem;
import com.quad.entity.Item;
import com.quad.entity.Player;
import com.quad.entity.enemies.Blob;
import com.quad.entity.enemies.Slime;
import com.quad.entity.objects.Block;
import com.quad.entity.objects.Switch;
import com.quad.entity.objects.Teleport;
import com.quad.entity.objects.Waterfall;
import com.quad.entity.players.Adventurer;
import com.quad.hud.HUD;
import com.quad.inventory.Axe;
import com.quad.inventory.Potion;
import com.quad.net.entity.PlayerMP;
import com.quad.net.packets.Packet00Login;
import com.quad.net.packets.Packet02Move;

public class LevelOne extends State {

	// player
	private Adventurer player;

	// bg
	private Outdoor bg;

	// items
	private ArrayList<Item> items;

	// fly
	private ArrayList<Fly> flies;

	// waterfall
	private Waterfall fall;

	// enemies
	private ArrayList<Enemy> enemies;
	private ArrayList<Enemy> dead;

	// blackout screen
	private ArrayList<Rectangle> tb;

	// teleport
	private Teleport port;
	private Teleport endPort;

	// switches
	private Switch s1;

	// block
	private Block b;

	// HUD
	private HUD hud;

	// positions
	private int spawnX;
	private int spawnY;

	// enemy re-loader
	private int respawnCount;
	private int maxRespawn;

	private int numPlayers;

	// events
	private int step[];
	private boolean eventFinish;
	private boolean eventComplete;
	private boolean mouseMode;

	@Override
	public void init(GameContainer gc) {
		// light

		// Settings.setLight(gc, true);

		tm = new TileMap(gc, 16);
		tm.loadTiles("/tiles/forest.gif");
		tm.loadMap("/maps/forest.map");

		// enemies
		enemies = new ArrayList<Enemy>();
		dead = new ArrayList<Enemy>();

		// bg
		bg = new Outdoor();
		bg.init();

		// waterfall
		fall = new Waterfall(tm);
		fall.init();
		fall.setPosition(640, 305);

		// spawn
		spawnX = 10;
		spawnY = 10;

		// player
		player = new PlayerMP(tm, spawnX, spawnY, null, -1);
		player.init(enemies);
		player.setUserName(JOptionPane.showInputDialog("Enter username"));

		Settings.setUsername(player.getUserName());

		Packet00Login login = new Packet00Login(player.getUserName(), player.getx(), player.gety());

		if (gc.getServer() != null) {
			gc.getServer().addConnection((PlayerMP) player, login);
		}
		login.writeData(gc.getClient());

		// teleport
		port = new Teleport(tm);
		port.init();
		port.setPosition(30, 64 - 36);

		endPort = new Teleport(tm);
		endPort.init();
		endPort.setPosition(870, 380);

		// switches
		s1 = new Switch(tm);
		s1.init();
		s1.setPosition(1080, 138);

		// items
		items = new ArrayList<Item>();

		// HUD
		hud = new HUD(player);

		// blackout screen
		tb = new ArrayList<Rectangle>();

		flies = new ArrayList<Fly>();
		populateNPC();
		populateEnemies();
		populateItems();

		b = new Block(tm, player);
		b.init();

		respawnCount = 0;
		maxRespawn = 500;

		step = new int[5];

	}

	private void populateNPC() {
		Fly f;

		f = new Fly(tm);
		f.init();
		f.setPosition(130, 300);
		flies.add(f);

		f = new Fly(tm);
		f.init();
		f.setPosition(135, 300);
		flies.add(f);

		f = new Fly(tm);
		f.init();
		f.setPosition(700, 245);
		flies.add(f);

		f = new Fly(tm);
		f.init();
		f.setPosition(90, 16);
		flies.add(f);

		f = new Fly(tm);
		f.init();
		f.setPosition(100, 16);
		flies.add(f);

		f = new Fly(tm);
		f.init();
		f.setPosition(330, 90);
		flies.add(f);

	}

	private void populateEnemies() {
		enemies.clear();

		Slime s;

		s = new Slime(tm, player);
		s.setPosition(spawnX + 200, 200);
		s.setWalking(true);
		enemies.add(s);

		Blob b;

		b = new Blob(tm, player);
		b.setPosition(400, 0);
		enemies.add(b);

	}

	private void populateItems() {
		Potion p;

		p = new Potion(tm, player);
		p.setPosition(400, 300);
		items.add(p);

		Axe a;

		a = new Axe(tm, player);
		a.setPosition(990, 90);
		items.add(a);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		// use mouse mode---------------------------------------------
		if (mouseMode)
			tm.setPosition(Settings.WIDTH / 2 - ((gc.getInput().getMouseX()) - tm.getx()),
					Settings.HEIGHT / 2 - (gc.getInput().getMouseY() - tm.gety()));
		else {
			tm.setPosition(Settings.WIDTH / 2 - player.getx(), Settings.HEIGHT / 2 - player.gety());
		}

		if (mouseMode) {
			player.setPosition((gc.getInput().getMouseX() - tm.getx()), (gc.getInput().getMouseY() - tm.gety()));
			player.setFalling(false);
		}
		// input
		input(gc);
		// ---------------------------------
		tm.update(dt);
		tm.fixBounds();

		bg.update(tm);

		// events
		if (eventFinish)
			eventFinish();
		if (eventComplete)
			eventComplete(gc);

		// System.out.println(connectedPlayers.size());

		// update respawn
		respawnCount++;
		if (respawnCount > maxRespawn)
			respawn();

		// player update
		player.update(gc, dt);

		// update connected players
		for (PlayerMP p : connectedPlayers) {
			p.update(gc, dt);
		}

		if (player.getInventory().isActive())
			return;

		for (Fly f : flies) {
			f.update(gc, dt);
		}

		// enemies
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update(gc, dt);
			if (e.shouldRemove()) {
				enemies.remove(i);
				dead.add(e);
				i--;
			}
		}

		// items
		for (int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			it.update(gc, dt);
			if (it.shouldRemove()) {
				items.remove(i);
				i--;
			}
		}

		// teleport
		if (port.contains(player) && gc.getInput().isKeyPressed(KeyEvent.VK_DOWN)) {
			player.setMovement(false);
			player.setEmote(Player.SURPRISED);
			spawnX = 930;
			spawnY = 390;
			eventFinish = true;
		}

		if (endPort.contains(player) && gc.getInput().isKeyPressed(KeyEvent.VK_DOWN)) {
			player.setMovement(false);
			player.setEmote(Player.SURPRISED);
			eventComplete = true;
		}

		// check switches
		if (s1.contains(player) && gc.getInput().isKeyPressed(KeyEvent.VK_Z)) {
			s1.setHit();
			tm.setTile(7, 66, 5);
			tm.setTile(6, 66, 5);
			tm.setTile(7, 65, 5);
			tm.setTile(6, 65, 5);
			tm.setTile(7, 64, 5);
			tm.setTile(6, 64, 5);
			tm.setTile(7, 63, 5);
			tm.setTile(6, 63, 5);
		}
		// check spikes
		if (player.isDead() || player.getHealth() <= 0)
			eventFinish = true;

		// waterfall
		fall.update(gc, dt);

		// teleport
		port.update(gc, dt);
		endPort.update(gc, dt);

		// switches
		s1.update(gc, dt);

	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// bg
		bg.render(r);

		// render map
		tm.draw(r);

		// teleport
		// port.render(gc, r);
		// endPort.render(gc, r);

		// switches
		s1.render(gc, r);

		// render player
		player.render(gc, r);

		// render connedcted players
		for (PlayerMP p : connectedPlayers) {
			p.render(gc, r);
		}

		r.setAmbientLight(0x1654bf);
		r.drawLight(new Light(0xadbc34, 75), player.getx() + (int) tm.getx(), (player.gety() + 15) + (int) tm.gety());

		for (Enemy e : enemies) {
			e.render(gc, r);
		}

		// hud
		// hud.render(gc, r);

		// blackout screen
		for (int i = 0; i < tb.size(); i++) {
			Rectangle re = tb.get(i);
			r.drawFillRect((int) re.getX(), (int) re.getY(), (int) re.getWidth(), (int) re.getHeight(), 0x000000);
		}
	}

	@Override
	public void dipose() {

	}

	private void respawn() {
		if (dead.size() < 1)
			return;
		for (int i = 0; i < dead.size(); i++) {
			Enemy e = dead.get(i);
			e.respawn();
			enemies.add(e);
			dead.remove(i);
			respawnCount = 0;
			i--;
		}
	}

	private void input(GameContainer gc) {

		// player
		if (player.isDead() || !player.getMovement())
			return;
		player.setRight(gc, gc.getInput().isKey(KeyEvent.VK_RIGHT));
		player.setLeft(gc, gc.getInput().isKey(KeyEvent.VK_LEFT));
		player.setJumping(gc.getInput().isKeyPressed(KeyEvent.VK_UP));
		if (gc.getInput().isKeyPressed(KeyEvent.VK_Z)) {
			player.setAttacking();
			player.setAction(5);
		}

//		if(gc.getInput().isKeyPressed(KeyEvent.VK_X) && gc.getInput().isKey(KeyEvent.VK_RIGHT)) {
//			player.setTeleportRight(true);
//		}
//		if(gc.getInput().isKeyPressed(KeyEvent.VK_X) && gc.getInput().isKey(KeyEvent.VK_LEFT)) {
//			player.setTeleportLeft(true);
//		}
//		if(gc.getInput().isKeyPressed(KeyEvent.VK_X) && gc.getInput().isKey(KeyEvent.VK_UP)) {
//			player.setTeleportUp(true);
//		}
//		if(gc.getInput().isKeyPressed(KeyEvent.VK_X) && gc.getInput().isKey(KeyEvent.VK_DOWN)) {
//			player.setTeleportDown(true);
//		}

		// exit
		if (gc.getInput().isKeyPressed(KeyEvent.VK_ESCAPE))
			System.exit(0);

		// mouse mode3
		if (gc.getInput().isKeyPressed(KeyEvent.VK_C)) {
			mouseMode = !mouseMode;
		}
		if (gc.getInput().isKeyPressed(KeyEvent.VK_DOWN)) {
			player.setSliding();
		}
		player.setCrouching(gc.getInput().isKey(KeyEvent.VK_DOWN));
		if (gc.getInput().isKeyPressed(KeyEvent.VK_ENTER)) {
			Settings.setLight(gc, !Settings.LIGHT);
		}

	}

	/////////////////////////////////////////////
	// events
	private void eventFinish() {
		step[0]++;

		if (step[0] == 100) {
			player.setPosition(spawnX, spawnY);
			player.setMovement(true);
			player.setEmote(Player.NONE);
			player.setDead(false);

			step[0] = 0;
			eventFinish = false;
		}

	}

	private void eventComplete(GameContainer gc) {
		step[1]++;

		if (step[1] == 100) {
			gc.getGame().setState(gc, AbstractGame.MENU);
		}
	}

	@Override
	public void addPlayer(InetAddress address, int x, int y, int port, String username) {
		PlayerMP p = new PlayerMP(tm, x, y, address, port);
		p.setUserName(username);
		p.init(enemies);
		connectedPlayers.add(p);
	}

	@Override
	public void removePlayer(String username) {
		int index = 0;
		for (GameObject e : connectedPlayers) {
			if (e instanceof PlayerMP && ((PlayerMP) e).getUserName().equals(username)) {
				break;
			}
			index++;
		}
		connectedPlayers.remove(index);
	}

	@Override
	public void movePlayer(GameContainer gc, String username, int x, int y, boolean[] m) {
		int index = 0;
		for (GameObject e : connectedPlayers) {
			if (e instanceof PlayerMP && ((PlayerMP) e).getUserName().equals(username)) {
				break;
			}
			index++;
		}
		try {
			PlayerMP player = (PlayerMP) this.connectedPlayers.get(index);
			player.setLeft(gc, m[Packet02Move.LEFT]);
			player.setRight(gc, m[Packet02Move.RIGHT]);
			player.setJumping(m[Packet02Move.JUMPING]);
			player.setCrouching(m[Packet02Move.CROUCHING]);
			if(m[Packet02Move.SLIDING])player.setSliding();
			player.setPosition(x, y);
		} catch (IndexOutOfBoundsException e) {
			
		}
	}

//	private void reset(){
//		//clear rect
//		tb.clear();
//		
//		//reset player
//		player.setMovement(true);
//		player.setPosition(105, 320);
//		player.setEmote(Player.NONE);
//		player.setDead(false);
//		if(player.getHealth() <= 0) player.setHealth(player.getMaxHealth());
//		
//		//change event
//		eventFinish = false;
//		step[0] = 0;
//		
//	}

}
