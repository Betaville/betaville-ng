package edu.poly.bxmc.betaville.model;

/**
 * @author Skye Book
 *
 */
public class StringVerifier {
	
	/**
	 * Tests a string to see if it is a valid username.
	 * @param user The string to check for username validity.
	 * @return true if the supplied string is a viable
	 * username; false if it is not.
	 */
	public static boolean isValidUsername(String user){
		return user.matches("[A-Za-z0-9]{1,16}");
	}
	
	public static boolean isValidEmail(String email){
		//return email.matches("[-._A-Za-z0-9]@[-.A-Za-z0-9]\\.[A-Za-z]");
		return (email.contains("@") && email.contains("."));
	}
	
}
