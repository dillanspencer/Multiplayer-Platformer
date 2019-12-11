package com.quad.net.users;

import java.net.InetAddress;

public class User {
	
	private String username;
	public InetAddress ipAddress;
	public int port;
	
	public User(String username, InetAddress ipAddress, int port) {
		this.username = username;
		this.ipAddress = ipAddress;
		this.port = port;
	}
	
	public User(String username) {
		this.username = username;
	}
	
	
	public String getUserName() {
		return username;
	}

	public void setUserName(String username) {
		this.username = username;
	}

}
