package edu.poly.bxmc.betaville.jme.map;

/**
 * @author Skye Book
 *
 */
public class UTMInconsistentCoordinateException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UTMInconsistentCoordinateException() {
		super("Coordinate System has incorrect polarity");
	}

	/**
	 * @param arg0
	 */
	public UTMInconsistentCoordinateException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
}
