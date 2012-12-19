package net.betaville.ng;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.betaville.ng.util.DriveFinder;
import net.betaville.ng.xml.UpdatedPreferenceWriter;

import org.apache.log4j.Logger;

import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.IUser.UserType;



/**
 * Singleton class for keeping track of user preferences as well as
 * for interfacing with the stored file: <i>home</i>/.betaville/preferences.xml
 * @author Skye Book
 *
 */
public class SettingsPreferences {
	private static final Logger logger = Logger.getLogger(SettingsPreferences.class);

	private static int sessionID=0;
	private static String sessionToken="";

	private static int currentCity;
	private static ArrayList<City> cities = new ArrayList<City>();

	/**
	 * Adds a city to the city list and sets it as the current city.
	 * @param newCity The city to add.
	 */
	public static void addCityAndSetToCurrent(City newCity){
		cities.add(newCity);
		try {
			setCurrentCity(newCity.getCityID());
		} catch (Exception e) {
			logger.error("Adding city apparently failed as it could not be found in the city list", e);
		}
	}

	/**
	 * Adds a city to the city list
	 * @param newCity The city to add.
	 */
	public static void addCity(City newCity){
		cities.add(newCity);
	}

	public static void setCurrentCity(int cityID) throws Exception{
		boolean cityFound=false;
		for(City c : cities){
			if(c.getCityID()==cityID) cityFound=true;
		}
		if(!cityFound) logger.warn("City not found in local list of cities");
		currentCity=cityID;
		logger.info("Current City ID Set To: " + cityID);
	}

	public static int getCurrentCityID(){
		return currentCity;
	}

	public static City getCity(){
		for(City c : cities){
			if(c.getCityID()==currentCity) return c;
		}
		return null;
	}

	public static City getCity(int cityID){
		for(City c : cities){
			if(c.getCityID()==cityID) return c;
		}
		return null;
	}

	public static void setSessionID(int id){
		sessionID=id;
	}

	public static int getSessionID(){
		return sessionID;
	}

	public static void setSessionToken(String token){
		sessionToken=token;
	}

	public static String getSessionToken(){
		return sessionToken;
	}

	public static boolean guestMode(){
		return readTrueFalseValue("betaville.guestmode.enabled");
	}


	private static ExecutorService threadPool = Executors.newCachedThreadPool(new BetavilleThreadFactory("BetavilleMainThreadPool"));
	private static ExecutorService guiThreadPool = Executors.newCachedThreadPool(new BetavilleThreadFactory("BetavilleGUIThreadPool"));

	public static String getServerIP(){
		String ip = System.getProperty("betaville.server");
		if(ip.equals("devstreak.net")) ip = "master.betaville.net";
		return ip;
	}

	/**
	 * Options for the visual characteristics of the selected spatial.
	 * @author Skye Book
	 *
	 */
	public enum SelectionVisuals{WIREFRAME, GLOW_ORANGE, BOUNDING};
	public static SelectionVisuals SELECTION_VISUAL = SelectionVisuals.GLOW_ORANGE;

	private static String userName;
	private static String userPass;
	private static UserType userType;
	private static boolean authenticated = false;

	private static URL DATA_FOLDER = null;

	public synchronized static URL getDataFolder(){
		if(DATA_FOLDER==null){
			try {
				DATA_FOLDER = new URL(System.getProperty("betaville.cache.location")+"/");
				File cacheFile = new File(DATA_FOLDER.toURI());
				if(!cacheFile.exists()){
					cacheFile.mkdirs();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		return DATA_FOLDER;
	}

	/**
	 * The last accessed location when using a file browser within
	 * the application.  Initialized to the user's home folder on
	 * startup.
	 */
	public static File BROWSER_LOCATION = DriveFinder.getHomeDir();

	public static File getLocalServerFolder(){
		File file = new File(DriveFinder.getHomeDir().toString()+"/.betaville/"+getServerIP()+"/");
		if(!file.exists()) file.mkdirs();
		return new File(DriveFinder.getHomeDir().toString()+"/.betaville/"+getServerIP()+"/");
	}

	private static boolean shadowsOn=false;
	private static boolean waterOn=false;

	public static void setAuthenticated(boolean value){
		authenticated=value;
	}

	public static boolean isAuthenticated(){
		return authenticated;
	}

	public static void setUserPass(String user, String pass){
		userName=user;
		userPass=pass;
	}

	/**
	 * @return the userType
	 */
	public static UserType getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public static void setUserType(UserType userType) {
		SettingsPreferences.userType = userType;
	}

	public static String getPass(){
		return userPass;
	}

	public static String getUser(){
		return userName;
	}

	public static boolean isShadowsOn() {
		return shadowsOn;
	}

	public static boolean isWaterOn() {
		return waterOn;
	}

	public static ExecutorService getThreadPool(){
		return threadPool;
	}

	public static ExecutorService getGUIThreadPool(){
		return guiThreadPool;
	}

	public static boolean isTextured(){
		return Boolean.parseBoolean(System.getProperty("betaville.display.textured"));
	}

	public static boolean isFullscreen(){
		return Boolean.parseBoolean(System.getProperty("betaville.display.fullscreen"));
	}

	public static String getResolution(){
		return System.getProperty("betaville.display.resolution");
	}

	public static Boolean isFogEnabled(){
		return Boolean.parseBoolean(System.getProperty("betaville.display.fog.enabled"));
	}

	public static boolean showTutorialWindow(){
		String value = System.getProperty("betaville.tutorial.show");
		if(value!=null) return Boolean.parseBoolean(value);
		else return true;
	}

	public static boolean alwaysShowSettings(){
		String value = System.getProperty("betaville.startup.showsettings");
		if(value!=null) return Boolean.parseBoolean(value);
		else return true; 
	}

	public static boolean loadModelsOnStart(){
		String value = System.getProperty("betaville.display.model.loadonstart");
		if(value!=null) return Boolean.parseBoolean(value);
		else return true;
	}

	public static boolean useGeneratedTerrainEnabled(){
		return readTrueFalseValue("betaville.display.terrain.usegenerated");
	}

	public static boolean useSSL(){
		return readTrueFalseValue("betaville.net.usessl");
	}

	/**
	 * 
	 * @param property The System property to read the true/false value from
	 * @return The true/false value, or true if there is no value found
	 */
	public static boolean readTrueFalseValue(String property){
		String value = System.getProperty(property);
		if(value!=null) return Boolean.parseBoolean(value);
		else return true;
	}

	/**
	 * Gets the startup city described in the user's xml file on startup.
	 * @return The ID of the city, or 2 if the value was invalid.
	 */
	public static int getStartupCity(){
		String value = System.getProperty("betaville.startup.city");
		if(value==null) return 2;
		try{
			return Integer.parseInt(value);
		}catch (NumberFormatException e) {
			return 2;
		}
	}

	/**
	 * Gets the startup city described in the user's xml file on startup.
	 * @param cityID The ID of the city, or 2 if the value was invalid.
	 */
	public static void setStartupCity(int cityID){
		System.setProperty("betaville.startup.city", Integer.toString(cityID));
		try {
			UpdatedPreferenceWriter.writeDefaultPreferences();
		} catch (IOException e) {
			logger.error("Preferences could not be written", e);
		}
	}
}
