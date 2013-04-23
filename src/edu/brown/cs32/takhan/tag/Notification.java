package edu.brown.cs32.takhan.tag;

/**
 * This class represents a notification object in
 * the program
 * @author takhan
 *
 */
public class Notification { //might need to implement Serializable
	
	private String _url;
	private String _user;
	
	/**
	 * Initializes a new notification that stores strings
	 * representing the url of the notification and the user
	 * it is for.
	 * @param url
	 * @param user
	 */
	public Notification(String url, String user){
		_user = user;
		_url = url;
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

}
