package edu.brown.cs32.takhan.tag;

import java.io.IOException;

public class Main {
	public static void main(String args[]){
		int port = 6000;
		if(args.length == 1){
			try{
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Port has to be a Number!");
				System.exit(0);
			}
		}
		
		try{
			Server server = new Server(port);
			server.start();
			System.out.println("Server started");
		} catch (IOException e){
			e.printStackTrace();
			System.err.println("could not start server!");
			System.exit(0);
		}
		
		



	}
}
