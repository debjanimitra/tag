package edu.brown.cs32.vgavriel.connectorOnServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


import edu.brown.cs32.takhan.tag.Data;
import edu.brown.cs32.takhan.tag.User;

public class ClientHandler extends Thread {
	private Socket _clientSocket;
	private ClientPool _clientPool;
	private String _userID;
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
	public ClientHandler(Socket clientSocket, ClientPool clientPool) throws IOException {
		if (clientSocket == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
		_clientSocket = clientSocket;
		_clientPool = clientPool;

		_output = new ObjectOutputStream(_clientSocket.getOutputStream());	
		_output.flush();
		_input = new ObjectInputStream(_clientSocket.getInputStream());		
		_running = true;
	}

	/**
	 * is the first thing executed upon the start of this thread.
	 * This method waits for the Client to send its credentials,
	 * so that the server can add the credentials to the ClientPool
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public String handShake() throws ClassNotFoundException, IOException{
		Message message = (Message) _input.readObject();
		String userID;
		if(message != null && message.getContent() == MessageContent.USERID && (userID = (String) message.getObject()) != null){
			_userID = userID;
			_clientPool.add(_userID, this);
			return userID;
		} else {
			System.err.println("The first message HAS to contain ONLY the user credentials");
			kill();
			return null;
		}
	}

	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		try {
			// HANDSHAKE:
			String userID = handShake();
			if(userID != null){
				_clientPool.add(userID, this);
			} else {
				System.out.println("The client failed the handshake (did not provide user name) and got disconnected!");
				return;
			}		
			// POST HANDSHAKE:
			Message message = (Message) _input.readObject();
			while(message != null){
				send(processMessage(message));
				message = (Message) _input.readObject();
			}			

		} catch(EOFException | SocketException e){
			System.out.println("A client disconnected.");
			kill();
		}
		catch (IOException e) {
			System.err.println("IO Problem ClientHandler");
		} catch (ClassNotFoundException e){
			System.err.println("The class should be Message which should be found.");
		}

	}

	/**
	 * this method is here for processing the incoming message from the Client, and returning
	 * a response message!
	 * @param message
	 * @return the message to be returned to the clientside
	 */
	private Message processMessage(Message message){
		Message result = null;
		if(message == null){
			return null;
		}

		switch(message.getContent()){
		case DATA:
			/**
			 * TODO: message has an instance of Data, so process it and return a confirmation
			 */
			break;
		case NOTIFICATION:
			/**
			 * TODO: message has an instance of Notification, so process it and return a confirmation
			 */
			break;
		case USER:
			/**
			 * TODO: message has an instance of User, so process it and return a confirmation
			 */
			break;
		default:
			/**
			 * TODO: message has an unused content, like USERID or DONE
			 * maybe return an error String containing Message here
			 */
			break;
		}

		return result;
	}

	/**
	 * Send an instance of Message to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(Message message) {
		try {
			_output.writeObject(message);
			_output.flush();
		} catch (IOException e) {
			System.out.println("A client disconnected!");
			kill();
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
			_clientPool.remove(_userID);			
			_clientSocket.close();
			_input.close();
			_output.close();			
		} catch (IOException e) {
			//there is really nothing we can do here.
		}
	}

	public String getUserID(){
		return _userID;
	}

}
