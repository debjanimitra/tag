package edu.brown.cs32.takhan.tag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class Database extends Thread {
	
	private HashMap<String,User> _dataMap;
	private final String fileName = "data.ser";
	private ObjectOutput _output;
	
	
	public Database(){
		try{
			OutputStream file = new FileOutputStream(fileName);
		    OutputStream buffer = new BufferedOutputStream(file);
		    ObjectOutput output = new ObjectOutputStream(buffer);
		    _output = output;
		}
		catch(IOException e){
			
		}
	}
	
	public void run(){
		InputStream file;
		try {
		file = new FileInputStream(fileName);
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
	
	public synchronized void updateFile(){
		try {
			_output.writeObject(_dataMap);
		} catch (IOException e) {
	
		}
	}
}
