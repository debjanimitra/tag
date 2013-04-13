package edu.brown.cs32.vgavriel.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import edu.brown.cs32.takhan.tag.Data;
import edu.brown.cs32.takhan.tag.User;

public class ClientHandler extends Thread {
	private Socket _clientSocket;
	private ObjectInputStream _input;
	private ObjectOutputStream _output;
	private boolean _running;

	/**
	 * Constructs a {@link ClientHandler} on the given client with the given pool.
	 * 
	 * @param pool a group of other clients to chat with
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if pool or client is null
	 */
	public ClientHandler(Socket clientSocket) throws IOException {
		if (clientSocket == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
		_clientSocket = clientSocket;

		_input = new ObjectInputStream(_clientSocket.getInputStream());
		_output = new ObjectOutputStream(_clientSocket.getOutputStream());	
		_running = true;
	}

	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		try {					
			Message message = (Message) _input.readObject();
			while(_running){
				if(message != null){
					Data data = message.getData();
					User user = message.getUser();				
					if(user != null){
						/*
						 * do something with the User class
						 */
					} else if (data != null) {
						/*
						 * do something with the Data class
						 */
					}
				}
				message = (Message) _input.readObject();
			}
			kill();

		}catch (ClassNotFoundException | IOException e) {
			System.err.println(e.getMessage());
		}

	}

	/**
	 * Send a string to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(Message message) {
		try {
			_output.writeObject(message);
			_output.flush();
		} catch (IOException e) {
			System.err.println("ERROR: Can't write to Client!");
		}

	}

	/**
	 * Close this socket and its related streams.
	 * 
	 * @throws IOException Passed up from socket
	 */
	public void kill() {
		try{
			_running = false;
			_clientSocket.close();
			_input.close();
			_output.close();
		} catch (IOException e) {
			//there is really nothing we can do here.
		}
	}
	/*
	public String getUserName(){
		return _userName;
	}*/

}