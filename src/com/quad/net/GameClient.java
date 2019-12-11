package com.quad.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.quad.core.GameContainer;
import com.quad.net.packets.Packet;
import com.quad.net.packets.Packet.PacketTypes;
import com.quad.net.packets.Packet00Login;
import com.quad.net.packets.Packet01Disconnect;
import com.quad.net.packets.Packet02Move;
import com.quad.net.packets.Packet10Start;
import com.quad.net.users.User;

public class GameClient extends Thread {

	private InetAddress ipAddress;
	private DatagramSocket socket;
	private GameContainer game;

	public GameClient(GameContainer game, String ipAddress) {
		this.game = game;
		try {
			this.socket = new DatagramSocket();
			this.ipAddress = InetAddress.getByName(ipAddress);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
		}
	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String message = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(message.substring(0, 2));
		Packet packet = null;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			packet = new Packet00Login(data);
			handleLogin((Packet00Login) packet, address, port);
			game.getGame().getCurrentState().addPlayer(address, 100, 50, port, ((Packet00Login)packet).getUsername());
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "] "
					+ ((Packet01Disconnect) packet).getUsername() + " has left the world...");
			game.getGame().getCurrentState().removePlayer(((Packet01Disconnect) packet).getUsername());
			break;
	        case MOVE:
	            packet = new Packet02Move(data);
	            handleMove((Packet02Move) packet);
	            break;
	        case START:
	        	packet = new Packet10Start(data);
	        	handleStartGame();
	        	break;
		}
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, 1331);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void handleLogin(Packet00Login packet, InetAddress address, int port) {
		System.out.println(
				"[" + address.getHostAddress() + ":" + port + "] " + packet.getUsername() + " has joined the game...");
		game.getOnlineUsers().add(new User(packet.getUsername()));
	}
	
	private void handleStartGame() {
	}

	private void handleMove(Packet02Move packet) {
		if(packet.getUsername().equalsIgnoreCase(game.getUser().getUserName()))return;
		boolean[] m = {packet.isLeft(), packet.isRight(), packet.isUp(), packet.isDown(),
						packet.isAttacking(), packet.isMidAttacking(), packet.isTopAttacking()};
		game.getGame().getCurrentState().movePlayer(game,packet.getUsername(), packet.getX(), packet.getY(), m);
	}
	

}
