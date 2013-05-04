package edu.brown.cs32.vgavriel.connectorOnServer;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.net.util.Base64;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import edu.brown.cs32.takhan.tag.Checker;
import edu.brown.cs32.takhan.tag.Data;
import edu.brown.cs32.takhan.tag.Database;
import edu.brown.cs32.takhan.tag.Notification;
import edu.brown.cs32.takhan.tag.User;

public class ClientHandler extends Thread {
	private Socket _clientSocket;
	private Socket _pushSocket;
	private ClientPool _clientPool;
	private String _userID;
	private ObjectInputStream _standardInput;
	private ObjectOutputStream _standardOutput;
	private ObjectInputStream _pushInput;
	private ObjectOutputStream _pushOutput;
	private boolean _running;
	private Database _database;
	private Checker _checker;

	/**
	 * Constructs a {@link ClientHandler} on the given client with the given pool.
	 * @param database 
	 * 
	 * @param pool a group of other clients to chat with
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if pool or client is null
	 */
	public ClientHandler(Socket clientSocket, Socket pushSocket, ClientPool clientPool, Database database, Checker checker) throws IOException {
		if (clientSocket == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}
		_clientSocket = clientSocket;
		_pushSocket = pushSocket;
		_clientPool = clientPool;

		_standardOutput = new ObjectOutputStream(_clientSocket.getOutputStream());	
		_standardOutput.flush();
		_standardInput = new ObjectInputStream(_clientSocket.getInputStream());		

		_pushOutput = new ObjectOutputStream(_pushSocket.getOutputStream());	
		_pushOutput.flush();
		_pushInput = new ObjectInputStream(_pushSocket.getInputStream());		

		_database = database;
		_checker = checker;
		_running = true;
	}

	/**
	 * is the first thing executed upon the start of this thread.
	 * This method waits for the Client to send its credentials,
	 * so that the server can add the credentials to the ClientPool
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public boolean handShake() throws ClassNotFoundException, IOException{
		Message message = (Message) _standardInput.readObject();
		String userID;
		// normal login:
		if(message != null && message.getContent() == MessageContent.USERID && (userID = (((String) message.getObject()).split("\t"))[0]) != null){
			_userID = userID;
			System.out.println(("User logging in is: "+(((String) message.getObject()).split("\t"))[0]));
			Collection<User> log = _database.getAllUsers();
			for(User person:log){
				System.out.println("Guy: "+person.getID());
			}
			if(_database.hasUser(_userID)){
				String encodedPassword = (((String) message.getObject()).split("\t"))[1];
				if(_clientPool.isClientConnected(_userID)){
					this.normalSend(new Message(MessageContent.ERRORHANDSHAKE_MULTIPLELOGINS, null));
					return false;
				}
				else if(!_database.getUser(_userID).getPassword().equals(new String(Base64.decodeBase64(encodedPassword)))){
					this.normalSend(new Message(MessageContent.ERRORHANDSHAKE_WRONGPASSWORD,null));
				}
				_clientPool.add(_userID, this);
				this.normalSend(new Message(MessageContent.DONE, null));
			} else {
				this.normalSend(new Message(MessageContent.ERRORHANDSHAKE_UNKNOWNUSER, null));
				return false;
			}			
		}
		// registration:
		else if(message != null && message.getContent() == MessageContent.NEWUSERID && (userID = (((String) message.getObject()).split("\t"))[0]) != null){
			System.out.println((String) message.getObject());
			_userID = userID;
			if(!_database.hasUser(_userID)){
				User newUser = new User(_userID);
				String encodedPassword = (((String) message.getObject()).split("\t"))[1];
				String email = (((String) message.getObject()).split("\t"))[2];
				newUser.setPassword(new String(Base64.decodeBase64(encodedPassword)));
				newUser.setEmail(email);
				System.out.println("Password being set to: "+newUser.getPassword());
				_database.addUser(newUser, _userID);
				_clientPool.add(_userID, this);
				this.normalSend(new Message(MessageContent.DONE, null));
			} else {
				this.normalSend(new Message(MessageContent.ERRORHANDSHAKE_NONUNIQUEUSER, null));
				return false;
			}			
		} else { // not a valid handshake message!
			System.err.println("The first message HAS to contain ONLY the user credentials");			
			this.normalSend(new Message(MessageContent.ERRORHANDSHAKE_NOUSER, null));
			return false;
		}
		return true;
	}

	/**
	 * Send and receive data from the client. The first line received will be
	 * interpreted as the cleint's user-name.
	 */
	public void run() {
		try {
			// HANDSHAKE:
			while(!handShake()){}		
			// POST HANDSHAKE:
			Message message = (Message) _standardInput.readObject();
			while(message != null){
				Message toSend = processMessage(message);
				if(toSend.getContent() == MessageContent.DONE_GETWEBTAGS){
					ArrayListMultimap<String, Data> d = (ArrayListMultimap<String, Data>) toSend.getObject();
					System.out.println("about to send size: " + d.size());
				}
				normalSend(toSend);
				message = (Message) _standardInput.readObject();
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
	@SuppressWarnings("unchecked")
	private Message processMessage(Message message){

		if(message == null){
			return null;
		}

		switch(message.getContent()){
		case DATA:
			Data data = (Data) message.getObject();
			User user = _database.getUser(data.getUser());
			if(user.addData(data.getURL(), data)){
				System.out.println("data added!");
				_database.updateFile();
				return new Message(MessageContent.DONE, null);
			} else {
				return new Message(MessageContent.ERROR_RECEIVE_TAGALREADYEXISTS, null);
			}
			/**
			 * TODO: message has an instance of Data, so process it and return a confirmation
			 */
			//break; //never reached?
		case GET_WEBTAGS:
			ListMultimap<String, Data> toSendWebtags = _database.getUser(_userID).getDataMap();
			if(toSendWebtags != null){
				return new Message(MessageContent.DONE_GETWEBTAGS, (Object) toSendWebtags);
			} else {
				return new Message(MessageContent.ERROR_GETWEBTAGS_UNKNOWNUSER, null);
			}
			//break;
		case UPDATE_WEBTAGS:
			if(message.getObject() != null){
				_database.getUser(_userID).setDataMap((ListMultimap<String,Data>) message.getObject());
				_database.updateFile();
				return new Message(MessageContent.DONE_UPDATEWEBTAGS, null);
			} else {
				return new Message(MessageContent.ERROR_UPDATINGWEBTAGS, null);
			}
			//break;
		case GET_NOTIFICATIONS:
			List<Notification> toSendNotifications = _checker.getNotifications(_userID);
			if(toSendNotifications != null){
				return new Message(MessageContent.DONE_GETNOTIFICATIONS, (Object) toSendNotifications);
			} else {
				return new Message(MessageContent.ERROR_GETNOTIFICATIONS_UNKNOWNUSER, null);
			}
			//break;
		case DELETE_NOTIFICATION:
			if(message.getObject() != null){
				String toDeleteID = (String) message.getObject();
				List<Notification> toReSendNotifications = _checker.deleteNotification(toDeleteID, _userID);
				if(toReSendNotifications != null){
					return new Message(MessageContent.DONE_DELETENOTIFICATION, (Object) toReSendNotifications);
				}
			}
			else {
				return new Message(MessageContent.ERROR_DELETENOTIFICATIONS_UNKNOWNUSER, null);
			}
			//break;
		default:
			return new Message(MessageContent.ERROR_RECEIVE_INVALIDDATA, null);
			/**
			 * TODO: message has an unused content, like USERID or DONE
			 * maybe return an error String containing Message here
			 */
			// break; never reached
		}

		// return result; never reached
	}

	/**
	 * Send an instance of Message to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void normalSend(Message message) {
		try {
			_standardOutput.writeObject(message);
			_standardOutput.flush();
			_standardOutput.reset();
		} catch (IOException e) {
			System.out.println("A client disconnected!");
			kill();
		}

	}

	public void pushSend(Message message) {
		try {
			_pushOutput.writeObject(message);
			_pushOutput.flush();
			_pushOutput.reset();
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
			_standardInput.close();
			_standardOutput.close();		
			_pushInput.close();
			_pushOutput.close();
		} catch (IOException e) {
			//there is really nothing we can do here.
		}
	}

	public String getUserID(){
		return _userID;
	}

}
