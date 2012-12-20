package net.betaville.ng.net;

/**
 * Represents codes that signal processes to or from the server
 * @author Skye Book
 */
public class ConnectionCodes {
	
	/**
	 * Signals a desired close operation
	 */
	public static final int CLOSE = -1;
	
	/**
	 * A database error on the server has occurred.
	 */
	public static final int DATABASE_ERROR = -1;
	
	/**
	 * The user does not have permission to perform the requested action.
	 */
	public static final int AUTHENTICATION_FAILED = -3;
	
	/**
	 * @see DuplicationFunction#NOT_DUPLICATED
	 */
	public static final int DUPLICATIONFUNCTION_NONE=1;
	
	/**
	 * @see DuplicationFunction#ONE_TIME_DUPLICATION
	 */
	public static final int DUPLICATIONFUNCTION_ONCE=1;
	
	/**
	 * @see DuplicationFunction#TRACKED_DUPLICATION
	 */
	public static final int DUPLICATIONFUNCTION_TRACKED=1;
}
