package net.betaville.ng.net;

/**
 * @author Skye Book
 *
 */
public class UnexpectedServerResponse extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public UnexpectedServerResponse(String message) {
		super(message);
	}
}
