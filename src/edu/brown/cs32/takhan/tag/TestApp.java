package edu.brown.cs32.takhan.tag;

import java.io.IOException;

public class TestApp {

	
	public TestApp(){
		try {
			Server server = new Server(2000);
			server.run();
		} catch (IOException e) {
			System.err.println("Problem");
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestApp();

	}

}
