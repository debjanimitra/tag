package edu.brown.cs32.takhan.tag;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

public class Database{
	
	private ConcurrentHashMap<String,User> _dataMap;
	private final String fileName = "data.ser";
	private ObjectOutputStream _output;
	private ObjectInputStream _input;
	private OutputStream _outputFile;
	private OutputStream _outputBuffer;
	
	public Database(){
		try{
			try {
				FileInputStream inputFile;
				inputFile = new FileInputStream(fileName);
				InputStream buffer = new BufferedInputStream(inputFile);
				ObjectInputStream input = new ObjectInputStream(buffer);
				_input = input;
				_dataMap = (ConcurrentHashMap<String,User>) input.readObject();
				input.close();
				}
				catch(IOException | ClassNotFoundException e){
					_dataMap = new ConcurrentHashMap<>();
					if(_input != null){
						_input.close();
					}
				}
			OutputStream outputFile = new FileOutputStream(fileName);
		    OutputStream outputBuffer = new BufferedOutputStream(outputFile);
		    ObjectOutputStream output = new ObjectOutputStream(outputBuffer);
		    _outputFile = outputFile;
		    _outputBuffer = outputBuffer;
		    _output = output;
			
			
		}
		catch(IOException e){
			
		}
	}
	
	public synchronized boolean hasUser(String id){
		return _dataMap.containsKey(id);
	}
	
	public synchronized void addUser(User user, String id){
		if(!_dataMap.containsKey(id)){
			_dataMap.put(id, user);
		}
		this.updateFile();
	}
	
	public synchronized Collection<User> getAllUsers(){
		return _dataMap.values();
	}
	
	public synchronized User getUser(String id){
		return _dataMap.get(id);
	}
	
	public synchronized void updateFile(){
		try {
			System.out.println("File updating");
			System.out.println("Writing to file size: "+_dataMap.size());
			_output.close();
			_outputBuffer.close();
			_outputFile.close();
			_outputFile = new FileOutputStream(fileName);
		    _outputBuffer = new BufferedOutputStream(_outputFile);
		    _output = new ObjectOutputStream(_outputBuffer);
			_output.writeObject(_dataMap);
			_output.flush();
			_output.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
