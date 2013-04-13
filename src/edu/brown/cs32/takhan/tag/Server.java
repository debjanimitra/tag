package edu.brown.cs32.takhan.tag;

import edu.brown.cs32.vgavriel.connector.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author vgavriel
 *
 */
public class Server {
	private int _portNumber;
	private ServerSocket _serverSocket;
	private ClientPool _clientPool;
	private boolean _running;

	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int portNumber) throws IOException {
		if (portNumber <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}

		_portNumber = portNumber;
		_clientPool = new ClientPool();
		_serverSocket = new ServerSocket(_portNumber);
		//TODO: Set up a server socket that will  listen to socket connection requests
	}

	/**
	 * Wait for and handle connections indefinitely.
	 */
	public void run() {
		_running = true;
		//TODO: Set up a while loop to receive all the socket connection
		//requests made by a client

		while(_running){
			try {
				Socket clientConnection = _serverSocket.accept();
				System.out.println("Connected to a client.");
				if(clientConnection != null){
					ClientHandler ch = new ClientHandler(clientConnection);
					_clientPool.add("temporary, this needs to be changed!", ch); // WE NEED TO KNOW THE USER-ID SOMEHOW!
					ch.start();
				}
			} catch (IOException e) {
				if(_running)
					System.err.println("Problem with connecting to client");
			}

		}
	}

	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill() throws IOException {
		_running = false;
		_clientPool.killall();
		_serverSocket.close();
	}
}