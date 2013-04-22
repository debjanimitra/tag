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
	public String findElement(String enteredText) {
		Elements links = _doc.select("a");
		Elements divs = _doc.select("div");
		Elements listEls = _doc.select("li");
		Elements paragraphs = _doc.select("p");
		//The most likely thing is that it will be a link that leads to some article
		for (Element e : links) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				Data dStore = new Data(text, _url);
			}
			return text;
		}
		//divs
		for (Element e : divs) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				Data dStore = new Data(text, _url);
			}
			return text;
		}
		//list elements
		for (Element e : listEls) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				Data dStore = new Data(text, _url);
			}
			return text;
		}
		//paragraphs
		for (Element e : paragraphs) {
			String text = e.text();
			//Storing process
			if (text.contains(enteredText)) {
				Data dStore = new Data(text, _url);
			}
			return text;
		}
		return null;
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
	public void checkUpdate(ArrayList<String> eAttr) {
		try {
			Document doc = Jsoup.connect(eAttr.get(0)).get();
			//check the id
			if (eAttr.get(1) != null) {
				Elements id = doc.select(eAttr.get(1));
				for (Element e : id) {
					String text = e.text();
					if (text.contains(eAttr.get(3))) { 
						//what should we return?
					}
				}
			}
			//check the class
			else if (eAttr.get(2) != null) {
				Elements classes = doc.select(eAttr.get(1));
				for (Element e : classes) {
					String text = e.text();
					if (text.contains(eAttr.get(3))) { 
						//what should we return?
					}
				}
			}
			//check everything
			else {
				Elements all = doc.select("*");
				for (Element e : all) {
					String text = e.text();
					if (text.contains(eAttr.get(3))) { 
						//what should we return?
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
