package edu.brown.cs32.takhan.tag;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class User implements Serializable{ // might need to implement Serializable
	
	private static final long serialVersionUID = 1L;
	private String _userID;
	private String _password;
	private ListMultimap<String,Data> _dataMap;
	
	
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
	public boolean addData(String url, Data data){
		boolean contains = false;
		if(_dataMap.containsKey(url)){
			Collection<Data> dataCollection = _dataMap.get(url);
			for(Data item:dataCollection){
				if(item.getText().equals(data.getText())&&item.getID().equals(data.getID())&item.getClassObject().equals(data.getClassObject())){
					contains = true;
					break;
				}
			}
		}
		if(!contains){
			_dataMap.put(url, data);
			return true;
		}
		return false;
	}
	
	/**
	 * Method to remove a specific data object from the hashmap 
	 * @param toRemove
	 */
	public void removeData(Data toRemove){
		Collection<Data> dataList = _dataMap.get(toRemove.getURL());
		for(Data data:dataList){
			if(data.getID().equals(toRemove.getID())&&data.getText().equals(toRemove.getText())&&data.getClassObject().equals(toRemove.getClassObject())){
				_dataMap.remove(data.getURL(),data);
			}
		}
	}
	
	/**
	 * Returns all the data items that are currently stored. 
	 * @return
	 */
	public Collection<Data> getAllData(){
		return _dataMap.values();
	}

}
