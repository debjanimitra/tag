package edu.brown.cs32.takhan.tag;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

import org.jsoup.HttpStatusException;

import edu.brown.cs32.dcorrea.htmlparsing.HTMLParsing;

public class Checker extends Thread {
	
	private Database _database;
	private Hashtable<String,List<Notification>> _notifMap;
	private Server _server;

	public Checker(Database database, Server server){
		_database = database;
		_notifMap = new Hashtable<>();
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
			boolean hasNotifChanged;
			for(User user:users){
				hasNotifChanged = false;
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
								Notification message = new Notification(item.getURL(),item.getUser(), false,item.getDataID(), item.getTitle());
								if(item.getEmail()){
									if (item.getTitle().trim().length()==0){
									Email.sendEmail(user.getEmail(), "Trakr has found a notification for the website: "+item.getURL());
									}
									else{
										Email.sendEmail(user.getEmail(), "Trakr has found a notification for "+item.getTitle()+": "+item.getURL());
									}
								}
								hasNotifChanged = true;
								if(!_notifMap.contains(item.getUser())){
									List<Notification> notifList = new ArrayList<>();
									notifList.add(message);
									_notifMap.put(item.getUser(), notifList);
								}
								else{
									List<Notification> list = _notifMap.get(item.getUser());
									List<Notification> duplicates = new ArrayList<>();
									for(Notification notif:list){
										if(notif.getID().equals(message.getID())){
											duplicates.add(notif);
										}
									}
									for(Notification dup:duplicates){
										list.remove(dup);
									}
									list.add(message);
								}
								if(!item.getPerm()){
									dataList.add(item);
									_database.updateFile();
									//user.removeData(item);
								}
								break;
							case "false":
								break;
							case "lost":
								Notification lostMessage = new Notification(item.getURL(),item.getUser(),true,item.getDataID(), item.getTitle());
								if(item.getEmail()){
									if (item.getTitle().trim().length()==0){
										Email.sendEmail(user.getEmail(), "Trakr has found a notification for the website: "+item.getURL());
										}
										else{
											Email.sendEmail(user.getEmail(), "Trakr has found a notification for "+item.getTitle()+": "+item.getURL());
										}	
								}
								hasNotifChanged = true;
								if(!_notifMap.contains(item.getUser())){
									List<Notification> notifList = new ArrayList<>();
									notifList.add(lostMessage);
									_notifMap.put(item.getUser(), notifList);
								}
								else{
									List<Notification> list = _notifMap.get(item.getUser());
									List<Notification> duplicates = new ArrayList<>();
									for(Notification notif:list){
										if(notif.getID().equals(lostMessage.getID())){
											duplicates.add(notif);
										}
									}
									for(Notification dup:duplicates){
										list.remove(dup);
									}
									list.add(lostMessage);
								}
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

					} catch (IOException e) {// TODO David: HTMLParing constructor throws this! (Viktor)
						
					}
					
				}
				
				if(hasNotifChanged){
					_server.pushNotifications(_notifMap, user.getID());
				}
			}
			for(Data tag:dataList){
				String user = tag.getUser();
				User person = _database.getUser(user);
				person.removeData(tag);
			}
			_database.updateFile();
			
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
		}
	}
	
	public synchronized List<Notification> deleteNotification(String notifID, String userID){
		List<Notification> notifList = _notifMap.get(userID);
		Notification toRemove = null;
		for(Notification notif:notifList){
			if(notif.getID().equals(notifID)){
				toRemove = notif;
				break;
			}
		}
		if(toRemove != null){
			notifList.remove(toRemove);
		} else {
			System.err.println("The client wants to remove a notification with");
			System.err.println("the ID: " + notifID + " but it was not found on the server");
		}
		return notifList;
	}
	
	public synchronized List<Notification> getNotifications(String userID){
		return _notifMap.get(userID);
	}
}
