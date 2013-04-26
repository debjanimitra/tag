package edu.brown.cs32.dcorrea.htmlparsing;

public class ElementInfo {
	private String _ID;
	private String _class;
	private boolean _perm;
	
	public ElementInfo() {
		_perm = false;
	}
	
	public String getID() {
		return _ID;
	}
	public String getElementClass() {
		return _class;
	}
	public boolean getPerm() {
		return _perm;
	}
	
	public void setID(String d) {
		_ID = d;
	}
	public void setElementClass(String c) {
		_class = c;
	}
	public void setPerm(boolean p) {
		_perm = p;
	}
}
