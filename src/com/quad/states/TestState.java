package com.quad.states;

import java.net.InetAddress;
import java.util.ArrayList;

import com.quad.Tile.TileMap;
import com.quad.core.GameContainer;
import com.quad.core.Renderer;
import com.quad.core.Settings;
import com.quad.core.components.State;
import com.quad.core.fx.Light;
import com.quad.entity.GamePlayer;
import com.quad.entity.characters.Dwarf;
import com.quad.net.packets.Packet00Login;
import com.quad.net.packets.Packet02Move;
import com.quad.net.users.User;
import com.sun.glass.events.KeyEvent;

public class TestState extends State {

	private ArrayList<User> users;
	private ArrayList<GamePlayer> players;

	private Dwarf player;

	private TileMap tm;

	@Override
	public void init(GameContainer gc) {
		tm = new TileMap(gc, 16);
		// gc.setLightEnable(true);
		// login to server TEMPORARY
		Packet00Login login = new Packet00Login(gc.getUser().getUserName(), 50, 50);
		login.writeData(gc.getClient());
		users = gc.getOnlineUsers();
		players = new ArrayList<GamePlayer>();

		tm.loadTiles("/tiles/dungeon.gif");
		tm.loadMap("/maps/bigmap.map");

		player = new Dwarf(tm);
		player.init(players);
		player.setPosition(100, 50);
	}

	@Override
	public void update(GameContainer gc, float dt) {
		tm.update(dt);

		// input
		player.setRight(gc, gc.getInput().isKey(KeyEvent.VK_RIGHT));
		player.setLeft(gc, gc.getInput().isKey(KeyEvent.VK_LEFT));
		player.setUp(gc, gc.getInput().isKey(KeyEvent.VK_UP));
		player.setDown(gc, gc.getInput().isKey(KeyEvent.VK_DOWN));
		if (gc.getInput().isKeyPressed(KeyEvent.VK_Z))
			player.setAttacking();
		if (gc.getInput().isKeyPressed(KeyEvent.VK_X))
			player.setMidAttacking();

		if (gc.getInput().isKeyPressed(KeyEvent.VK_ESCAPE))
			System.exit(0);

		for (GamePlayer p : players) {
			p.update(gc, dt);
		}

		// movement packet
		boolean[] m = {player.isLeft(), player.isRight(), player.isUp(), player.isDown(), 
						player.isAttacking(), player.isMidAttacking(), player.isTopAttacking()};
		Packet02Move movePacket = new Packet02Move(gc.getUser().getUserName(), (int) player.getX(), (int) player.getY(), m);
		movePacket.writeData(gc.getClient());

		player.update(gc, dt);
		tm.setPosition(Settings.WIDTH / 2 - player.getX(), Settings.HEIGHT / 2 - player.getY());
	}

	@Override
	public void render(GameContainer gc, Renderer r) {
		// TODO Auto-generated method stub
		// r.drawFillRect(0, 0, Settings.WIDTH, Settings.HEIGHT + 100, 0x25131A);
		// r.drawFillRect(x, y, 50, 50, 0xffffff);
		tm.draw(r);
		for (int i = 0; i < gc.getOnlineUsers().size(); i++) {
			r.drawString(users.get(i).getUserName(), 0xffffff, 10, 20 + (10 * i));
		}
		for (GamePlayer p : players) {
			if (((Dwarf) p).getUserName() == gc.getUser().getUserName() || players.isEmpty()) {
				return;
			}
			p.render(gc, r);
		}

		//r.drawLight(new Light(0xfcca03, 250), (int) (player.getx() + tm.getx()), (int) (player.gety() + tm.gety()));

		player.render(gc, r);
	}

	@Override
	public void dipose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addPlayer(InetAddress address, int x, int y, int port, String username) {
		Dwarf g = new Dwarf(tm);
		g.init(players);
		g.setUserName(username);
		g.setPosition(x, y);
		players.add(g);
		System.out.println("ADDED " + username);
	}

	@Override
	public void removePlayer(String username) {
		int index = 0;
		for (User e : users) {
			if (e instanceof User && ((User) e).getUserName().equals(username)) {
				break;
			}
			index++;
		}

		users.remove(index);
		players.remove(index);
	}

	@Override
	public void movePlayer(GameContainer gc, String username, int x, int y, boolean[] m) {
		if (username == gc.getUser().getUserName() || players.isEmpty()) {
			return;
		}
		int index = 0;
		for (GamePlayer e : players) {
			if (e instanceof Dwarf && ((Dwarf) e).getUserName().equals(username)) {
				break;
			}
			index++;
		}
		if (index > players.size() - 1)
			return;
		Dwarf p = (Dwarf) this.players.get(index);
		//p.setPosition(x, y);
		p.setLeft(m[Packet02Move.LEFT]);
		p.setRight(m[Packet02Move.RIGHT]);
		p.setUp(m[Packet02Move.UP]);
		p.setDown(m[Packet02Move.DOWN]);

		// attacks
		if (m[Packet02Move.ATTACKING] && !p.isAttacking()) {
			p.setAttacking();
		}
		if (m[Packet02Move.MIDATTACKING] && !p.isMidAttacking())
			p.setMidAttacking();
		if (m[Packet02Move.TOPATTACKING] && !p.isTopAttacking())
			p.setAttacking();
	}

}
