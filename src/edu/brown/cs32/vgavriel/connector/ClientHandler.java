package edu.brown.cs32.vgavriel.connector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import edu.brown.cs32.takhan.tag.User;

public class ClientHandler {
	private ClientPool _pool;
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
	public ClientHandler(ClientPool pool, Socket clientSocket) throws IOException {
		if (pool == null || clientSocket == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}

		_pool = pool;
		_clientSocket = clientSocket;

		//TODO: Set up the buffered reader and writer for the sockets to communicate with
		_input = new ObjectInputStream(_clientSocket.getInputStream());
		_output = new ObjectOutputStream(_clientSocket.getOutputStream());	
		_running = true;
	}

	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		//TODO: Get the inputs sent by client and broadcast it to the rest of the
		//clients. 
		try {					
			//TODO: The first input is the username of the client.
			User user = (User) _input.readObject();
			if(user != null){

				/*
				 * do something with the User class
				 */
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
	public void send(User user) {
		//TODO: Set up the methods, so it will send the message to the client
		try {
			_output.writeObject(user);
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
			//TODO: Close all the streams after the client disconnects.
			_running = false;
			_clientSocket.close();
			_input.close();
			_output.close();
		} catch (IOException e) {
			if(_running)
				System.err.println("IO Problem ClientHandler");

		}
	}

}
