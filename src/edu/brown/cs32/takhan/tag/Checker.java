package edu.brown.cs32.takhan.tag;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jsoup.HttpStatusException;

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
			Collection<User> userC = _database.getAllUsers();
			List<User> users = new ArrayList<>();
			for(User u:userC){
				users.add(u);
			}
			List<Data> dataList = new ArrayList<>();
			for(User user:users){
				Collection<Data> dataC = user.getAllData();
				List<Data> data = new ArrayList<>();
				for(Data d:dataC) {
					data.add(d);
				}
				for(Data item:data){
					//boolean update = false;
					String update = "";
					// Add code here to check for an update

					HTMLParsing htmlP;
					try {
						htmlP = new HTMLParsing(item.getURL());
						update = htmlP.checkUpdate(item);
						switch(update){
							case "true":
								Notification message = new Notification(item.getURL(),item.getUser(), false);
								_list.add(message);
								if(!item.getPerm()){
									dataList.add(item);
									_database.updateFile();
									//user.removeData(item);
								}
								break;
							case "false":
								break;
							case "lost":
								Notification lostMessage = new Notification(item.getURL(),item.getUser(),true);
								_list.add(lostMessage);
								dataList.add(item);
								_database.updateFile();
								//user.removeData(item);
								break;
						}
						//if(update){
							//Notification message = new Notification(item.getURL(),item.getUser());
							//_list.add(message);
						//}
					} catch (UnknownHostException | HttpStatusException e) {
						// this should never happen except the website goes off

					}
					
				}
				
			}
			for(Data tag:dataList){
				String user = tag.getUser();
				User person = _database.getUser(user);
				person.removeData(tag);
				System.out.println("hi");
			}
			_database.updateFile();
			if(!_list.isEmpty()){
				_list = _server.pushNotifications(_list);
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
