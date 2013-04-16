package edu.brown.cs32.vgavriel.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.brown.cs32.takhan.tag.Data;
import edu.brown.cs32.takhan.tag.User;

public class Message implements Serializable {
	private static final long serialVersionUID = 938459082390485345L;
	private User _user = null;
	private Data _data = null;
	private String _userID = null;
	
	public Message(User user){
		super();
		_user = user;
	}
	public Message(Data data){
		_data = data;
	}
	public Message(String userID){
		_userID = userID;
	}
	public Message(User user, Data data){
		_user = user;
		_data = data;
	}
	
	User getUser(){
		return _user;
	}
	
	Data getData(){
		return _data;
	}
	
	String getUserID(){
		return _userID;
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		ois.defaultReadObject();
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.defaultWriteObject();
	}
}
