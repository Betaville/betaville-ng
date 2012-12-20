package net.betaville.ng;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import net.betaville.ng.net.NetModelLoader;
import net.betaville.ng.net.NetModelLoader.LookupRoutine;
import net.betaville.ng.net.NetPool;
import net.betaville.ng.net.ProtectedManager;
import net.betaville.ng.util.DriveFinder;
import net.betaville.ng.xml.PreferenceReader;
import net.betaville.ng.xml.UpdatedPreferenceWriter;
import net.skyebook.jme2loader.JME2Loader;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.jdom.JDOMException;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import edu.poly.bxmc.betaville.jme.map.MapManager;
import edu.poly.bxmc.betaville.jme.map.Rotator;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.IUser.UserType;
import edu.poly.bxmc.betaville.model.ModeledDesign;
import edu.poly.bxmc.betaville.model.Wormhole;


/**
 * Betaville
 * This app lets you select a city and download all of its models.
 * Then it launches a jME3 application to render the city.
 * 
 * Run this with the VM argument -Xmx512M
 * 
 * @author Jannes Meyer
 */
public class Betaville extends SimpleApplication {
	private static Logger logger = Logger.getLogger(Betaville.class);
	
	// Static attributes
	public static final boolean DEBUG = true;
	
	// Class attributes
	private ProtectedManager protectedManager;
	private LoginManager loginManager;
	
//	ColorRGBA backgroundColor = ColorRGBA.White;
	ColorRGBA backgroundColor = new ColorRGBA(0.627f, 0.761f, 0.988f, 1);
	private FilterPostProcessor fpp;
    private FogFilter fog;
	private boolean fogEnabled = false;

	// Main
	public static void main(String[] args) {
		Betaville game = new Betaville();
		game.start();
	}
	

	/**
	 * Constructor
	 */
	public Betaville() {
		// Setup logger
		Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%-4r [%t] %-5p %c %x - %m%n")));
		
		if (!DEBUG) {
			// Betaville logger
			Logger.getRootLogger().setLevel(Level.WARN);
		}
		// JME logger. Sorry, I don't care about you
		java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.WARNING);
		Logger.getLogger("net.skyebook.jme2loader.JME2Loader").setLevel(Level.WARN);
		
		
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
	}
	
	/**
	 * App setup
	 */	
	@Override
	public void simpleInitApp() {
		if (!SettingsPreferences.guestMode()) {
			// TODO: Replace this with a better interface
			String username = Console.read("Username: ");
			String password = Console.read("Password: ");
			login(username, password);
		} else {
			SettingsPreferences.setUserType(UserType.GUEST);
		}

		// Select start city
		List<Wormhole> locations = null;
		try {
			locations = NetPool.getPool().getConnection().getAllWormholes();
		} catch (UnknownHostException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		
		int i = 0;
		for (Wormhole location : locations) {
			String[] cityInfo = null;
			try {
				cityInfo = NetPool.getPool().getConnection().findCityByID(location.getCityID());
			} catch (UnknownHostException e) {
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
			}
			// TODO: Give this a better interface
			System.out.println((i++) + ": " + cityInfo[0] + " - " + location.getName());
		}
		
		// TODO: Give this a better interface
		int selectedWormhole = Console.readInt("Select a location (0-" + (locations.size() - 1) + ", 1 for NYC): ");
		Wormhole wormhole = locations.get(selectedWormhole);
		logger.info("Selected location: " + wormhole.getName());
		
		int startupCity = wormhole.getCityID();
		SettingsPreferences.setStartupCity(startupCity);
		
		// Load city specs
		List<City> cities = null;
		try {
			cities = NetPool.getPool().getConnection().findAllCities();
		} catch (UnknownHostException e) {
			logger.error(e);
		} catch (IOException e) {
			logger.error(e);
		}
		for (City c : cities) {
			if (c.getCityID() == startupCity) {
				SettingsPreferences.addCityAndSetToCurrent(c);
			} else {
				SettingsPreferences.addCity(c);
			}
		}
		
		// Activate legacy model support for jME2 -> jME3 model conversion
		assetManager.registerLocator(SettingsPreferences.getDataFolder().getPath(), FileLocator.class);
		assetManager.registerLoader(JME2Loader.class, "jme");
		
		// Download models
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
//
//		// Stream updates
//		betavilleUpdater.addTask(new BetavilleTask(new BaseUpdater(30000)));
		
		logger.info("Number of terrain models: " + terrain.size());
		logger.info("Number of base models: " + base.size());
		
		// Start app
		initCity(wormhole, terrain, base);
	}

	/**
	 * Converts a jME2 model into a jME3 model
	 * @param oldFile
	 * @throws IOException
	 */
	private void convert2to3(File oldFile) throws IOException {
		// Load old file
		String oldFilename = oldFile.getName();
		Spatial model = assetManager.loadModel(oldFilename);
		// TODO: model.setName()
		logger.info("Load " + oldFilename);
		
		// Save as j3o file
		try {
			File newFile = new File(SettingsPreferences.getDataFolder3().getPath(), CacheManager.removeExtension(oldFilename) + ".j3o");
			BinaryExporter exporter = BinaryExporter.getInstance();
			exporter.save(model, newFile);
			
			if (newFile.exists()) {
				logger.info("Save as " + newFile);
			} else {
				logger.error("Conversion failed. File does not exist: " + newFile);
				throw new IOException();
			}
		} catch (IOException e) {
			logger.error("Couldn't save the converted j3o file");
			throw e;
		}
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
	
	
	/**
	 * Actually puts the scene together
	 * @param base 
	 * @param terrain 
	 * @param wormhole 
	 */
	private void initCity(Wormhole wormhole, List<Design> terrain, List<Design> base) {
		// Don't show debugging information
//		setDisplayStatView(false);
//		setDisplayFps(false);

		// Set frustum so that we don't clip too soon near the camera 
		cam.setFrustumPerspective(45, (float)cam.getWidth() / cam.getHeight(), 0.01f, 1000);
		
		// Lighting
		initLighting();
		initFog();
		viewPort.setBackgroundColor(backgroundColor);
		
		// Events
		initInput();
		
		// Set camera position
		UTMCoordinate startPosition = wormhole.getLocation();
		MapManager.setUTMZone(startPosition.getLonZone(), startPosition.getLatZone());
		MapManager.adjustOffsets(startPosition);
		
		// Add the .betaville folder as a location to load models from
		String dataFolder = SettingsPreferences.getDataFolder3().getPath();
		assetManager.registerLocator(dataFolder, FileLocator.class);

		// Create wireframe material
//		Material wire = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//		wire.setColor("Color", ColorRGBA.Gray);
//		wire.getAdditionalRenderState().setWireframe(true);
		
		
		// Attach terrain to the scene
		Node terrainNode = new Node("terrain");
		for (Design design : terrain) {
			loadAndAttachDesign(terrainNode, design);
		}
		rootNode.attachChild(terrainNode);

		// Attach base models to the scene
		Node baseNode = new Node("basemodels");
		for (Design design : base) {
			// Blacklist "ID 4603: NYC_Water_1"
			if (design.getID() == 4603) {
				continue;
			}
			loadAndAttachDesign(baseNode, design);
		}
		rootNode.attachChild(baseNode);
	}
	
	private void loadAndAttachDesign(Node node, Design design) {
		if(design instanceof ModeledDesign) {
			ModeledDesign md = (ModeledDesign) design;
			
			// Load the j3o model instead of the one that we got from the server
			Spatial model = null;
			try {
				model = assetManager.loadModel(CacheManager.removeExtension(md.getFilepath()) + ".j3o");
			} catch(AssetNotFoundException e) {
				// Asset not found
				try {
					convert2to3(new File(md.getFilepath()));
					model = assetManager.loadModel(CacheManager.removeExtension(md.getFilepath()) + ".j3o");
				} catch (IOException e1) {
					logger.error(e1);
				}
			}
			model.setLocalTranslation(MapManager.locationToBetaville(md.getCoordinate()));
			model.setLocalRotation(Rotator.fromThreeAngles(md.getRotationX(), md.getRotationY(), md.getRotationZ()));
//			model.setMaterial(wire);

			node.attachChild(model);
		} else if (design instanceof EmptyDesign) {
			// Why are these things even there? So useless
		} else {
			logger.warn("Not a ModeledDesign. No idea how to render this");
		}
	}
	
	private void initLighting() {
		ColorRGBA diffuseLightColor = new ColorRGBA(1f, 1f, 1f, 1f);
		ColorRGBA diffuseLightColor2 = new ColorRGBA(.3f,.4f,.45f,.3f);
		
		DirectionalLight directionalLight = new DirectionalLight();
		directionalLight.setDirection(new Vector3f(.25f, -.85f, .75f));
		directionalLight.setColor(diffuseLightColor);

		DirectionalLight directionalLight2 = new DirectionalLight();
		directionalLight2.setDirection(new Vector3f(-.25f,.85f,-.75f));
		directionalLight2.setColor(diffuseLightColor2);

		rootNode.addLight(directionalLight);
		rootNode.addLight(directionalLight2);
	}
	
	private void initFog() {
		fpp = new FilterPostProcessor(assetManager);
		fog = new FogFilter();
		fog.setFogColor(backgroundColor);
		fog.setFogDistance(3000);
		fog.setFogDensity(1.5f);
		fpp.addFilter(fog);
		if (fogEnabled) {
			viewPort.addProcessor(fpp);
		}
	}

	private void initInput() {
		ActionListener actionListener = new ActionListener() {
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (name.equals("ToggleFog") && keyPressed) {
					if (fogEnabled){
						fogEnabled = false;
						viewPort.removeProcessor(fpp);
					} else{
						fogEnabled = true;
						viewPort.addProcessor(fpp);
					}
				}
			}
		};

		AnalogListener analogListener = new AnalogListener() {
			public void onAnalog(String name, float isPressed, float tpf) {
				if(name.equals("DensityUp")){
					fog.setFogDensity(fog.getFogDensity() + 0.1f);
					System.out.println("Fog density : " + fog.getFogDensity());
				}
				if(name.equals("DensityDown")){
					fog.setFogDensity(fog.getFogDensity() - 0.1f);
					System.out.println("Fog density : " + fog.getFogDensity());
				}
				if(name.equals("DistanceUp")){
					fog.setFogDistance(fog.getFogDistance() + 100);
					System.out.println("Fog Distance : " + fog.getFogDistance());
				}
				if(name.equals("DistanceDown")){
					fog.setFogDistance(fog.getFogDistance() - 100);
					System.out.println("Fog Distance : " + fog.getFogDistance());
				}
			}
		};

		inputManager.addMapping("ToggleFog", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
		inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));
		inputManager.addMapping("DistanceUp", new KeyTrigger(KeyInput.KEY_U));
		inputManager.addMapping("DistanceDown", new KeyTrigger(KeyInput.KEY_J));
		
		inputManager.addListener(actionListener, "ToggleFog");
		inputManager.addListener(analogListener, "DensityUp","DensityDown","DistanceUp","DistanceDown");
	}
	
}