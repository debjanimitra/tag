package edu.brown.cs32.takhan.tag;

import java.io.IOException;

public class TestApp {

	
	public TestApp(){
		String hi = "i        t";
		String[] split = hi.split("\t");
		System.out.println(split.length);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestApp();

	}

}
