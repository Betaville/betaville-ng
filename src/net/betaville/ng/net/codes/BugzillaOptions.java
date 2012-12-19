package net.betaville.ng.net.codes;

/**
 * Tools for building a Bugzilla bug entry URL
 * @author Skye Book
 *
 */
public class BugzillaOptions {
	
	public static final String baseURL = "http://bugzilla.betaville.net/";
	public static final String enterBug = "enter_bug.cgi";
	
	public static final String component = "component";
	
	public static final String component_3dContent = "Client:%203D%20Content";
	public static final String component_CoordinateSystem = "Client:%20Coordinate%20System";
	public static final String component_Graphics = "Client:%20Graphics";
	public static final String component_GUI = "Client:%20GUI";
	public static final String component_Interaction = "Client:%20Interaction";
	public static final String component_ReadFromServer = "Client:%20Read%20From%20Server";
	public static final String component_WriteToServer = "Client:%20Write%20To%20Server";
	public static final String component_DataModeling = "Data%20Modeling";
	public static final String component_Deployment = "Deployment";
	public static final String component_DatabaseTransactions = "Server:%20Database%20Transactions";
	public static final String component_ServerReadFromClient = "Server:%20Read%20From%20Client";
	public static final String component_ServerWriteToClient = "Server:%20Write%20To%20Client";
	
	public static String constructURL(String componentName){
		return baseURL+enterBug+"?"+"product=Betaville&"+component+"="+componentName;
	}
}
