package net.betaville.ng.util;

/**
 * A collection of static methods to 
 * @author Skye Book
 *
 */
public class OS {

	/**
	 * 
	 * @return True if this computer is running OS X
	 */
	public static boolean isMac(){
		return System.getProperty("os.name").startsWith("Mac");
	}
	
	/**
	 * 
	 * @return True if this computer is running Windows
	 */
	public static boolean isWindows(){
		return System.getProperty("os.name").startsWith("Win");
	}
	
	/**
	 * 
	 * @return True if this computer is running Linux
	 */
	public static boolean isLinux(){
		return System.getProperty("os.name").startsWith("Lin");
	}
	
}
