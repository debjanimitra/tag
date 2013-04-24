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
	private boolean _lost;
	
	/**
	 * Initializes a new notification that stores strings
	 * representing the url of the notification and the user
	 * it is for.
	 * @param url
	 * @param user
	 */
	public Notification(String url, String user, boolean lost){
		_lost = lost;
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

}
