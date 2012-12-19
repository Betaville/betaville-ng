package net.betaville.ng.util;

import java.util.regex.Pattern;

/**
 * A collection of static methods to verify input
 * @author Skye Book
 *
 */
public class InputVerifier {
	
	public static boolean checkForValidEmail(String email){
		if(email.contains("..")){
			return false;
		}
		return Pattern.matches("^[-0-9a-zA-Z\\._]+@[-0-9a-zA-Z\\._]+", email);
	}
	
	/**
	 * Checks for valid usernames..  Perhaps this should be serveriside so it can
	 * be configured by individual system administrators... thoughts?
	 * -Skye -- 1/18/2011
	 * @param name
	 * @return
	 */
	public static boolean checkForValidUsername(String name){
		if(name.length()>16) return false;
		if(name.contains(" ")) return false;
		if(Pattern.matches("[Aa]dm[1Ii]n", name)) return false;
		return false;
	}
}
