package edu.brown.cs32.vgavriel.connector;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class ClientPool {
private HashMap<String, ClientHandler> _clientPool;
	
	/**
	 * Initialize a new {@link ClientPool}.
	 */
	public ClientPool() {
		_clientPool = new HashMap<>();
	}
	
	/**
	 * Add a new client to the chat room.
	 * 
	 * @param client to add
	 */
	public synchronized void add(String userID, ClientHandler client) {
		_clientPool.put(userID, client);		
	}
	
	/**
	 * Remove a client from the pool. Only do this if you intend to clean up
	 * that client later.
	 * 
	 * @param username of the client to remove
	 * @return true if the client was removed, false if the username was not there.
	 */
	public synchronized boolean remove(String userID) {
		return _clientPool.remove(userID) == null ? false : true;		
	}

	public void killall() {
		for (ClientHandler clientHandler : _clientPool.values()) {
				clientHandler.kill();
		}
		_clientPool.clear();		
	}
	
}
