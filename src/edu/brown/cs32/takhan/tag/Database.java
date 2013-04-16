package edu.brown.cs32.takhan.tag;

import java.io.*;
import java.util.HashMap;

public class Database extends Thread {
	
	private HashMap<String,User> _dataMap;
	
	
	public Database(){
		
	}
	
	public void run(){
		InputStream file;
		try {
		file = new FileInputStream("data.ser");
		InputStream buffer = new BufferedInputStream(file);
		ObjectInput input = new ObjectInputStream(buffer);
		_dataMap = (HashMap<String,User>) input.readObject();
		}
		catch(IOException | ClassNotFoundException e){
			_dataMap = new HashMap<>();
		}
		while(true){
			
		}
			
	}
	
	public synchronized void addUser(User user, String id){
		if(!_dataMap.containsKey(id)){
			_dataMap.put(id, user);
		}
	}
}
