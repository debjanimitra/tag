package edu.brown.cs32.takhan.tag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.brown.cs32.dcorrea.htmlparsing.*;

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
				Collection<Data> data = user.getAllData();
				for(Data item:data){
					//boolean update = false;
					String update = "";
					// Add code here to check for an update
					HTMLParsing htmlP = new HTMLParsing(item.getURL());
					update = htmlP.checkUpdate(item);
					switch(update){
						case "true":
							Notification message = new Notification(item.getURL(),item.getUser(), false);
							_list.add(message);
							if(!item.getPerm()){
								user.removeData(item);
							}
							break;
						case "false":
							break;
						case "lost":
							Notification lostMessage = new Notification(item.getURL(),item.getUser(),true);
							_list.add(lostMessage);
							user.removeData(item);
							break;
					}
					//if(update){
						//Notification message = new Notification(item.getURL(),item.getUser());
						//_list.add(message);
					//}
				}
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(!_list.isEmpty()){
				_list = _server.pushNotifications(_list);
			}
		}
	}
}
