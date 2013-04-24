package edu.brown.cs32.dcorrea.htmlparsing;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import edu.brown.cs32.takhan.tag.*;

public class HTMLParsing {
	private Document _doc;
	private String _url;
	
	public HTMLParsing(String url) {
		try {
			_url = url;
			_doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * This method takes in the text that the user entered and uses it
	 * to compare it with the text contained in the html documents. This function
	 * will begin with parsing the text in the links, then in the divs and then finally in the
	 * paragraph fields. If we find a match we return the complete text and if not
	 * then we return null.
	 */
	public ArrayList<Element> findElement(String enteredText) {
		Elements links = _doc.select("a");
		Elements divs = _doc.select("div");
		Elements listEls = _doc.select("li");
		Elements paragraphs = _doc.select("p");
		ArrayList<Element> _elements = new ArrayList<Element>();
		//The most likely thing is that it will be a link that leads to some article
		for (Element e : links) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				_elements.add(e);
			}
		}
		//divs
		for (Element e : divs) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				_elements.add(e);
			}
		}
		//list elements
		for (Element e : listEls) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				_elements.add(e);
			}
		}
		//paragraphs
		for (Element e : paragraphs) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				_elements.add(e);
			}
		}
		return _elements;
	}
	/*
	 * This element will take in all the attributes in an ArrayList, this 
	 * arraylist will contain the specific text that was saved at the time of tagging
	 * along with the various identifier that we were able to fetch from the html element
	 * [0] = url
	 * [1] = id
	 * [2] = class
	 * [3] = text
	 */
	public String checkUpdate(Data dObj) {
		try {
			Document doc = Jsoup.connect(dObj.getURL()).get();
			String change = "true";
			//check the id
			if (dObj.getID() != null) {
				Elements id = doc.select(dObj.getID());
				for (Element e : id) {
					String text = e.text();
					if (text.equals(dObj.getText())) {
						change = "false";
						//what should we return?
					}
				}
			}
			//check the class
			else if (dObj.getClassObject() != null && change.equals("true")) {
				Elements classes = doc.select(dObj.getID());
				if (classes.size() > 1) {
					return "lost";
				}
				for (Element e : classes) {
					String text = e.text();
					if (text.equals(dObj.getText())) {
						change = "false"; 
						//what should we return? 
					}
				}
			}
			//check everything
			else if (dObj.getClassObject() == null && dObj.getID() == null) {
				Elements all = doc.select("*");
				for (Element e : all) {
					String text = e.text();
					if (text.equals(dObj.getText())) { 
						change = "false";
						//what should we return?
					}
				}
				if (change.equals("false")) {
					change = this.checkForAddition(dObj);
				}
			}
			//If the element is not there we should notify the user
			if (change.equals("true")) {
				return "true";
			} else {
				return "false";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lost";
	}
	
	public String checkForAddition(Data dObj) {
		Document prevDoc = dObj.getDocument();
		Element prevBody = prevDoc.body();
		Element currBody = _doc.body();
		String pBText = prevBody.text();
		String cBText = currBody.text();
		if (pBText.length() > cBText.length()) {
			return "true";
		} else {
			return "false";
		}
	}
}
