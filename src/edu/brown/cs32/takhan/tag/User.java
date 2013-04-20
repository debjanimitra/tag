package edu.brown.cs32.takhan.tag;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class User {
	
	private String _userID;
	private String _password;
	private ListMultimap<String,TagData> _dataMap;
	
	
	public User(String id){
		_dataMap = ArrayListMultimap.create();
		_userID = id;
	}
	
	/**
	 * Sets a users' secure password to login in to the application
	 * @param string
	 */
	public void setPassword(String string){
		_password = string;
	}
	
	/**
	 * Returns a user's password
	 * @return
	 */
	public String getPassword(){
		return _password;
	}
	
	/**
	 * Returns whether the user's password is equal to the word
	 * passed in. 
	 * @param word
	 * @return
	 */
	public boolean checkPassword(String word){
		if(_password.equals(word)){
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Returns the user's ID 
	 * @return
	 */
	public String getID(){
		return _userID;
	}
	
	/**
	 * Adds data to the user's current hashmap of data.
	 * @param url
	 * @param data
	 */
	public void addData(String url, TagData data){
		if(!_dataMap.containsEntry(url, data)){
			_dataMap.put(url, data);
		}
	}
	
	/**
	 * Returns all the data items that are currently stored. 
	 * @return
	 */
	public Collection<TagData> getAllData(){
		return _dataMap.values();
	}

}
