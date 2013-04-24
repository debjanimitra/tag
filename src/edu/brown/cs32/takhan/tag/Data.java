package edu.brown.cs32.takhan.tag;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Data implements TagData{ // might need to implement Serializable

	private String _url;
	private String _id;
	private String _class;
	private String _text;
	private final String _username;
	private boolean _permanent; 
	private Document _doc;
	
	
	public Data(String text, String url, String id, String username, Document doc, boolean perm){
		_text = text;
		_url = url;
		_username = username;
		_id = id;
		_permanent = perm;
		_doc = doc;
	}
	
	public Document getDocument() {
		return _doc;
	}
	
	public void setClass(String objClass){
		_class = objClass;
	}
	
	public String getClassObject(){
		return _class;
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
}
