package edu.brown.cs32.takhan.tag;

import java.io.Serializable;

/**
 * This class represents a notification object in
 * the program
 * @author takhan
 *
 */
public class Notification implements Serializable{ //might need to implement Serializable
	
	private static final long serialVersionUID = 1L;
	private String _url;
	private String _user;
	private boolean _lost;
	private String _id;
	private String _title;
	
	/**
	 * Initializes a new notification that stores strings
	 * representing the url of the notification and the user
	 * it is for.
	 * @param url
	 * @param user
	 */
	public Notification(String url, String user, boolean lost, String id, String title){
		_lost = lost;
		_user = user;
		_url = url;
		_id = id;
		_title=title;
	}
	
	
	public String getTitle(){
		return _title;
	}
	
	public void setTitle(String title){
		_title=title;
	}
	
	
	/**
	 * Returns a user's ID;
	 * @return
	 */
	public String getUser(){
		return _user;
	}
	
	/**
	 * Returns the url
	 * @return
	 */
	public String getURL(){
		return _url;
	}
	
	/**
	 * Sets the lost boolean 
	 * @param lost
	 */
	public void setLost(boolean lost){
		_lost = lost;
	}
	
	/**
	 * Returns the lost boolean 
	 * @return
	 */
	public boolean getLost(){
		return _lost;
	}
	
	public String getID(){
		return _id;
	}

}
