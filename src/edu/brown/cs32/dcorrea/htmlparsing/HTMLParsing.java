package edu.brown.cs32.dcorrea.htmlparsing;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import edu.brown.cs32.takhan.tag.*;

public class HTMLParsing {
	private Document _doc;
	private String _url;
	
	public HTMLParsing(String url) throws UnknownHostException, HttpStatusException {
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
		enteredText = enteredText.toLowerCase();
		Elements links = _doc.select("a");
		Elements divs = _doc.select("div");
		Elements listEls = _doc.select("li");
		Elements paragraphs = _doc.select("p");
		HashMap<String, Element> hm = new HashMap<String, Element>();
		ArrayList<Element> _elements;
		//The most likely thing is that it will be a link that leads to some article
		for (Element e : links) {
			String text = e.text().toLowerCase();
			//Storing process
			if (text.contains(enteredText)) {
				if (hm.get(text)==null){
					hm.put(text, e);
				}
			}
		}
		//divs
		for (Element e : divs) {
			String text = e.text().toLowerCase();
			//Storing process
			if (text.contains(enteredText)) {
				if (hm.get(text)==null || (hm.get(text).id().length()<0 && hm.get(text).className().length()<0)){
					hm.put(text, e);
				}
			}
		}
		//list elements
		for (Element e : listEls) {
			String text = e.text().toLowerCase();
			//Storing process
			if (text.contains(enteredText)) {
				if (hm.get(text)==null || hm.get(text)==null || (hm.get(text).id().length()<0 && hm.get(text).className().length()<0)){
					hm.put(text, e);
				}
			}
		}
		//paragraphs
		for (Element e : paragraphs) {
			String text = e.text().toLowerCase();
			//Storing process
			if (text.contains(enteredText)) {
				if (hm.get(text)==null || hm.get(text)==null || (hm.get(text).id().length()<0 && hm.get(text).className().length()<0)){
					hm.put(text, e);
				}
			}
		}
		_elements = new ArrayList<Element>(hm.values());
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
			String change = "false";
			//check the id
			if (dObj.getID().length() != 0) {
				System.out.println(dObj.getID());
				Elements id = doc.select(dObj.getID());
				for (Element e : id) {
					String text = e.text();
					if (!text.equals(dObj.getText())) {
						change = "true";
						if (dObj.getPerm()) {
							dObj.setText(text);
						}
						//what should we return?
					}
				}
			}
			//check the class
			else if (dObj.getClassObject().length() != 0 && change.equals("true")) {
				Elements classes = doc.select(dObj.getID());
				if (classes.size() > 1) {
					return "lost";
				}
				for (Element e : classes) {
					String text = e.text();
					if (!text.equals(dObj.getText())) {
						change = "true"; 
						if (dObj.getPerm()) {
							dObj.setText(text);
						}
						//what should we return? 
					}
				}
			}
			//check everything
			else if (dObj.getClassObject().length() == 0 && dObj.getID().length() == 0) {
				Elements all = doc.select("*");
				boolean noMatch = false;
				for (Element e : all) {
					String text = e.text();
					if (text.equals(dObj.getText())) { 
						noMatch = true;
						//what should we return?
					}
				}
				if (!noMatch) {
					change = "true";
				}
				if (!change.equals("true")) {
					change = this.checkForAddition(dObj);
				}
			}
			//If the element is not there we should notify the user
			return change;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "lost";
	}
	
	public Document getDocument(){
		return _doc;
	}
	
	public String checkForAddition(Data dObj) {
		Element currBody = _doc.body();
		String pBText = dObj.getDocBody();
		String cBText = currBody.text();
		if (pBText.length() > cBText.length()) {
			return "true";
		} else {
			return "false";
		}
	}
	
	public ElementInfo canBePermanent(Element el) {
		ElementInfo elInfo = new ElementInfo();
		if (el.id().length() != 0) {
			elInfo.setID("#"+el.id());
			System.out.println("#"+el.id());
			elInfo.setPerm(true);
			return elInfo;
		}
		Set<String> elClass = el.classNames();
		for (String c : elClass) {
			if (c.length() == 0) {
				break;
			}
			System.out.println("."+c);
			Elements elms = _doc.select("."+c);
			System.out.println("."+c);
			if (elms.size() == 1) {
				elInfo.setElementClass("."+c);
				elInfo.setPerm(true);
				break;
			}
		}
		return elInfo;
	}
	
	private class CustomComparator implements Comparator<Element> {

		@Override
		public int compare(Element o1, Element o2) {
			// TODO Auto-generated method stub
			return o1.text().compareTo(o2.text());
		}
	}
}
