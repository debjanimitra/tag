package edu.brown.cs32.takhan.tag;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import edu.brown.cs32.vgavriel.connectorOnServer.ClientHandler;
import edu.brown.cs32.vgavriel.connectorOnServer.ClientPool;
import edu.brown.cs32.vgavriel.connectorOnServer.Message;
import edu.brown.cs32.vgavriel.connectorOnServer.MessageContent;

/**
 * 
 * @author vgavriel
 *
 */
public class Server extends Thread{
	private int _portNumber;
	private ServerSocket _standardServerSocket;
	private ServerSocket _pushServerSocket;
	private ClientPool _clientPool;
	private boolean _running;

	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int portNumber) throws IOException{
		if (portNumber <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		} else if (portNumber > 65535) {
			throw new IllegalArgumentException("Ports higher than 65535 are not allowed");
		}

		_portNumber = portNumber;
		_clientPool = new ClientPool();
		_standardServerSocket = new ServerSocket(_portNumber);
		_pushServerSocket = new ServerSocket(_portNumber + 1);
	}

	/**
	 * Wait for and handle connections indefinitely.
	 */
	public void run() {
		Email.sendEmail("tak93@live.com", "Yayyy");
		_running = true;
		Database database = new Database();
		Checker checker = new Checker(database,this);
		checker.start();
		while(_running){
			try {
				Socket clientConnection = _standardServerSocket.accept();
				Socket pushConnection = _pushServerSocket.accept();
				System.out.println("Connected to a client.");
				if(clientConnection != null){
					ClientHandler ch = new ClientHandler(clientConnection, pushConnection, _clientPool, database,checker);
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
		_standardServerSocket.close();
		_pushServerSocket.close();
	}

	/**
	 * Sends notifications to the specified client if it is currently
	 * connected to the server.
	 * @param dataMap:  
	 * @param list
	 */
	public void pushNotifications(Hashtable<String,List<Notification>> dataMap, String user){


		if(_clientPool.isClientConnected(user)){
			List<Notification> notifList = dataMap.get(user);
			if(notifList == null){
				return;
			}
			ClientHandler handler = _clientPool.getClient(user);
			Message message = new Message(MessageContent.NOTIFICATIONLIST,(Object)notifList);
			handler.pushSend(message);
		}
	}
	/*
	public void pushUser(List<Notification> toSend, String user){
		ClientHandler handler = _clientPool.getClient(user);
		Message message = new Message(MessageContent.NOTIFICATION,(Object)toSend);
		handler.pushSend(message);
	}*/
}
