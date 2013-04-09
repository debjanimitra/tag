package edu.brown.cs32.takhan.tag;

import java.util.Collection;
import java.util.HashMap;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class User {
	
	private String _userID;
	private ListMultimap<String,TagData> _dataMap;
	
	
	public User(){
		_dataMap = ArrayListMultimap.create();
		
		
	}
	
	
	public String getID(){
		return _userID;
	}
	
	public void addData(String website, TagData data){
		if(!_dataMap.containsEntry(website, data)){
			_dataMap.put(website, data);
		}
	}
	
	public Collection<TagData> getAllData(){
		return _dataMap.values();
	}

}
