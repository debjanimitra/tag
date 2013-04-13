package edu.brown.cs32.vgavriel.connector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import edu.brown.cs32.takhan.tag.Data;
import edu.brown.cs32.takhan.tag.User;

public class Message implements Serializable {
	private static final long serialVersionUID = 938459082390485345L;
	private User _user;
	private Data _data;
	
	public Message(User user){
		super();
		_user = user;
		_data = null;
	}
	public Message(Data data){
		super();
		_user = null;
		_data = data;
	}
	public Message(User user, Data data){
		super();
		_user = user;
		_data = data;
	}
	
	User getUser(){
		return _user;
	}
	
	Data getData(){
		return _data;
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException{
		ois.defaultReadObject();
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException{
		oos.defaultWriteObject();
	}
}
