package edu.brown.cs32.takhan.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Checker extends Thread {
	
	private Database _database;
	private List<Notification> _list;
	private Server _server;

	public Checker(Database database, Server server){
		_database = database;
		_list = new ArrayList<>();
		_server = server;
	}
	
	/**
	 * The run method of this thread will periodically 
	 * keep checking to see if there are any updates 
	 * for any of the users tags. If there are it will
	 * create a notification to be sent to the user about
	 * it.  
	 */
	public void run(){
		while(true){
			Collection<User> users = _database.getAllUsers();
			for(User user:users){
				Collection<TagData> data = user.getAllData();
				for(TagData item:data){
					boolean update = false;
					// Add code here to check for an update
					if(update){
						Notification message = new Notification(null,null);
						_list.add(message);
					}
				}
			}
			if(!_list.isEmpty()){
				_list = _server.pushNotifications(_list);
			}
		}
	}
}
