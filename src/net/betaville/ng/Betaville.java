package net.betaville.ng;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.betaville.ng.net.NetModelLoader;
import net.betaville.ng.net.NetModelLoader.LookupRoutine;
import net.betaville.ng.net.NetPool;
import net.betaville.ng.net.ProtectedManager;
import net.betaville.ng.net.UnprotectedManager;
import net.betaville.ng.util.DriveFinder;
import net.betaville.ng.xml.PreferenceReader;
import net.betaville.ng.xml.UpdatedPreferenceWriter;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jdom.JDOMException;

import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.IUser.UserType;
import edu.poly.bxmc.betaville.model.Wormhole;


/**
 * Betaville's main class
 * Be sure to run this with the VM Argument -Xmx512M
 * 
 * @author Jannes Meyer
 */
public class Betaville {
	private static Logger logger = Logger.getLogger(Betaville.class);
	
	// Static attributes
	public static final boolean DEBUG = false;
	
	// Class attributes
	private ProtectedManager protectedManager;
	private LoginManager loginManager;

	// Main
	public static void main(String[] args) {
		try {
			new Betaville();
		} catch (UnknownHostException e) {
			logger.error("Unknown host", e);
		} catch (IOException e) {
			logger.error("Connection error", e);
		}
	}
	
	/**
	 * Constructor
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public Betaville() throws UnknownHostException, IOException {
		// Setup logger
		Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%-4r [%t] %-5p %c %x - %m%n")));
		
		if (!DEBUG) {
			// Betaville logger
			java.util.logging.Logger.getLogger("net.betaville.ng").setLevel(java.util.logging.Level.WARNING);
			Logger.getRootLogger().setLevel(Level.WARN);
		}
		// JME logger
		java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.WARNING);
		
		// Try to load the preferences
		try {
			File preferencesFile = new File(DriveFinder.getHomeDir().toString() + "/.betaville/preferences.xml");
			PreferenceReader preferenceReader = new PreferenceReader(preferencesFile);
			if (preferenceReader.isXMLLoaded()) {
				preferenceReader.parse();
				UpdatedPreferenceWriter.writeDefaultPreferences();
			} else {
				logger.info("Preferences file not found");
			}
		} catch (JDOMException e) {
			logger.error("JDOM error", e);
		} catch (IOException e) {
			try {
				logger.info("Preferences file could not be found, writing one.");
				UpdatedPreferenceWriter.writeDefaultPreferences();
			} catch (IOException e1) {
				logger.error("Preferences file could not be written", e1);
			}
		}


		if (!SettingsPreferences.guestMode()) {
			String username;
			String password;
			// TODO: Replace this with a better interface
			username = Console.read("Username: ");
			password = Console.read("Password: ");
			login(username, password);
		} else {
			SettingsPreferences.setUserType(UserType.GUEST);
		}


		// Select start city
		int selectedWormhole;
		final List<Wormhole> locations = NetPool.getPool().getConnection().getAllWormholes();
		
		if (!DEBUG) {
			// Normal mode: selection
			UnprotectedManager net = NetPool.getPool().getConnection();
			for (int i = 0; i < locations.size(); ++i) {
				Wormhole location = locations.get(i);
				String[] cityInfo = net.findCityByID(location.getCityID());
				// TODO: Give this a better interface
				System.out.println(i + ": " + cityInfo[0] + " - " + location.getName());
			}
			
			selectedWormhole = Console.readInt("Select a location: ");
		} else {
			// Debug mode: automatic NYC
			selectedWormhole = 1;
		}
		
		Wormhole wormhole = locations.get(selectedWormhole);
		logger.info("Selected wormhole: " + wormhole.getName());
		
		int startupCity = wormhole.getCityID();
		SettingsPreferences.setStartupCity(startupCity);
		
		// Load city
		List<City> cities = NetPool.getPool().getConnection().findAllCities();
		for (City c : cities) {
			if (c.getCityID() == startupCity) {
				SettingsPreferences.addCityAndSetToCurrent(c);
			} else {
				SettingsPreferences.addCity(c);
			}
		}
		
		// Load models
		long startTime = System.currentTimeMillis();
		List<Design> terrain = null;
		List<Design> base = null;
		try {
			// Load terrain
			terrain = NetModelLoader.loadCurrentCityTerrain();
			// Load the city
			base = NetModelLoader.loadCurrentCity(LookupRoutine.ALL_IN_CITY);
		} catch (UnknownHostException e) {
			logger.fatal(e);
		} catch (IOException e) {
			logger.fatal(e);
		}
		logger.info("Done loading models, took: " + (System.currentTimeMillis() - startTime));
		
//		// Load proposals
//		FlagProducer testFlagger = new FlagProducer(cameraStartPosition.getUTM(), new DesktopFlagPositionStrategy());
//		testFlagger.getProposals(30000);
//		testFlagger.placeFlags();

		//betavilleUpdater.addTask(new BetavilleTask(new BaseUpdater(30000)));
		
		// Start app
		List<Design> allDesigns = new ArrayList<Design>();
		allDesigns.addAll(terrain);
		allDesigns.addAll(base);
		logger.debug("Number of designs: " + allDesigns.size());
		BetavilleJMEGame game = new BetavilleJMEGame(wormhole, allDesigns);
		game.start();
	}

	/**
	 * Checks if the client is on the right version
	 * 
	 * @return 0 if this is acceptable<br>
	 * 1 if the client is newer than the server<br>
	 * -1 if the client is older than the server<br>
	 * -2 if a network connection couldn't be made
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private int checkVersion() throws UnknownHostException, IOException {
		logger.info("Checking version");
		try {
			long serverVersion = NetPool.getPool().getSecureConnection().getDesignVersion();
			logger.info("Server is version " + serverVersion);
			
			if(Design.serialVersionUID == serverVersion) {
				return 0;
			} else if(Design.serialVersionUID > serverVersion) {
				return 1;
			} else if(Design.serialVersionUID < serverVersion) {
				return -1;
			} else if(serverVersion == -2){
				logger.error("server error");
			}
			return -1;
		} catch (NullPointerException e) {
			// can't connect? Throw a special error
			return -2;
		}
	}
	
	/**
	 * Extracted from file {@link #SwingLoginWindow.java}
	 * 
	 * @param username
	 * @param password
	 */
	private void login(String username, String password) {
		try {
			int versionCheck = checkVersion();
			if (versionCheck == -2) {
				throw new RuntimeException("Can't connect");
			} else if(versionCheck < 0){
				// TODO: change this to a normal Exception
				throw new RuntimeException("Update your Betaville Client!");
			} else if(versionCheck > 0){
				// TODO: change this to a normal Exception
				throw new RuntimeException("Your Client is to new for your server!");
			}

			// TODO: Do some sanity checks
			//username != null
			//password != null
			//StringVerifier.isValidUsername(username)
			//StringVerifier.isValidEmail(username)
			
			// Establish a secure connection
			if (protectedManager == null){
				protectedManager = NetPool.getPool().getSecureConnection();
			}
			
			
		} catch (UnknownHostException e) {
			logger.fatal("Could not connect to server at " + SettingsPreferences.getServerIP(), e);
		} catch (IOException e) {
			logger.fatal("Could not connect to server at "+SettingsPreferences.getServerIP(), e);
		}

		// Authenticate
		boolean response = protectedManager.startSession(username, password);
		
		if (response) {
			logger.info(username + " logged in");
			
			// TODO: make savePassword configurable
			boolean savePassword = true;
			
			if (savePassword) {
				saveCookie(username, password);
			} else {
				deleteCookie();
			}
		} else {
			// TODO: replace with normal exception
			throw new RuntimeException("Wrong username or password");
		}
	}
	
	/**
	 * Remember username and password
	 * 
	 * @param username
	 * @param password
	 */
	private void saveCookie(String username, String password) {
		loginManager = new LoginManager();
		
		String[] loginData = new String[2];
		loginData[0] = username;
		loginData[1] = password;
		
		try {
			loginManager.saveCookie(loginData);
		} catch (IOException e1) {
			logger.error("Could not save login data");
		}
	}
	
	/**
	 * Delete username and password
	 */
	private void deleteCookie() {
		loginManager = new LoginManager();
		try {
			loginManager.deleteLogin();
		} catch (IOException e) {
			 logger.error("Could not delete login data");
		}
	}
}