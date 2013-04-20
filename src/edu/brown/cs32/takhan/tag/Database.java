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
import java.util.Collection;
import java.util.HashMap;

public class Database{
	
	private HashMap<String,User> _dataMap;
	private final String fileName = "data.ser";
	private ObjectOutput _output;
	
	
	public Database(){
		try{
			OutputStream file = new FileOutputStream(fileName);
		    OutputStream outputBuffer = new BufferedOutputStream(file);
		    ObjectOutput output = new ObjectOutputStream(outputBuffer);
		    _output = output;
			InputStream inputFile;
			try {
			inputFile = new FileInputStream(fileName);
			InputStream buffer = new BufferedInputStream(inputFile);
			ObjectInput input = new ObjectInputStream(buffer);
			_dataMap = (HashMap<String,User>) input.readObject();
			}
			catch(IOException | ClassNotFoundException e){
				_dataMap = new HashMap<>();
			}
			
		}
		catch(IOException e){
			
		}
	}
	
	
	public synchronized void addUser(User user, String id){
		if(!_dataMap.containsKey(id)){
			_dataMap.put(id, user);
		}
	}
	
	public synchronized Collection<User> getAllUsers(){
		return _dataMap.values();
	}
	
	public synchronized void updateFile(){
		try {
			_output.writeObject(_dataMap);
		} catch (IOException e) {
	
		}
	}
}
