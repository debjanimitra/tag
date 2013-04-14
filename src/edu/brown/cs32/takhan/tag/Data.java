package edu.brown.cs32.takhan.tag;

public class Data implements TagData{
	
	private String _start;
	private String _end;
	private String _url;
	private String _id;
	private String _class;
	private String _text;
	
	public Data(String text, String url){
		_text = text;
		_url = url;
	}
	
	public String getStart(){
		return _start;
	}
	
	public String getEnd(){
		return _end;
	}
	
	public void setStart(String start){
		_start = start;
	}
	
	public void setEnd(String end){
		_end = end;
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
