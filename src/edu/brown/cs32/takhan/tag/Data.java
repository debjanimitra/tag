package edu.brown.cs32.takhan.tag;
import java.io.Serializable;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Data implements TagData, Serializable{ // might need to implement Serializable

	private static final long serialVersionUID = 1L;
	private String _url;
	private String _id;
	private String _text;
	private final String _username;
	private boolean _permanent; 
	private String _docBody;
	private String _classname;
	private String _title;
	
	public Data(String text, String url, String id, String classname, String username, String docBody, String title, boolean perm){
		_text = text;
		_url = url;
		_username = username;
		_id = id;
		_classname = classname;
		_permanent = perm;
		_docBody = docBody;
		_title = title;
	}
	
	public String getDocBody() {
		return _docBody;
	}
	
	public void setClass(String objClass){
		_classname = objClass;
	}
	
	public boolean getPerm(){
		return _permanent;
	}
	
	public String getClassObject(){
		return _classname;
	}
	
	public void setID(String id){
		_id = id;
	}
	
	public String getID(){
		return _id;
	}

	public String getUser(){
		return _username;
	}

	public String getURL(){
		return _url;
	}

	public String getText(){
		return _text;
	}
	
	public void setPerm(boolean perm){
		_permanent=perm;
	}
	
	public String getTitle(){
		return _title;
	}
	
	public void setTitle(String t){
		_title = t;
	}
}
