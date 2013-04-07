package edu.brown.cs32.takhan.tag;

import com.google.common.collect.*;

public class User {
	
	private String _userID;
	private ListMultimap<String,String> _dataMap;
	
	
	public User(){
		_dataMap = ArrayListMultimap.create();
		
		
	}
	
	
	public String getID(){
		return _userID;
	}
	
	public void addData(String website, String tag){
		if(!_dataMap.containsEntry(website, tag)){
			_dataMap.put(website, tag);
		}
	}

}
