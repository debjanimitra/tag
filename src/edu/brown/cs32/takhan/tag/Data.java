package edu.brown.cs32.takhan.tag;

public class Data implements TagData{ // might need to implement Serializable

	private String _url;
	private String _id;
	private String _class;
	private String _text;
	private final String _username;
	
	public Data(String text, String url, String id, String username){
		_text = text;
		_url = url;
		_username = username;
		_id = id;
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

}
