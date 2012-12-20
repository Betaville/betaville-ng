package net.betaville.ng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Helps with reading input from the command line.
 * This class should go away in the future, once we have a new user interface.
 * 
 * @author Jannes Meyer
 */
public class Console {
	
	public static String read(String message) {
		System.out.print(message);
		try {
		    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		    return bufferRead.readLine();
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int readInt(String message) {
		return Integer.parseInt(read(message));
	}
	
}
