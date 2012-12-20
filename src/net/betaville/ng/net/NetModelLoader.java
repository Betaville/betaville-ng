package net.betaville.ng.net;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import net.betaville.ng.CacheManager;
import net.betaville.ng.SettingsPreferences;

import org.apache.log4j.Logger;

import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.Design.Classification;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ModeledDesign;

/**
 * Loads models from the network.  Different options for what is loaded
 * can be configured by using the constructor's {@link LookupRoutine} parameter.
 * @author Skye Book, modified by Jannes Meyer
 * @see LookupRoutine
 */
public class NetModelLoader{
	
	private static Logger logger = Logger.getLogger(NetModelLoader.class);

	/** Options for what kind of models to load from the network.
	 * @author Skye Book
	 */
	public static enum LookupRoutine{
		/**
		 * Loads all designs in the set city
		 */
		ALL_IN_CITY,
		/**
		 * Loads all designs by the current user
		 */
		ALL_BY_USER
	};

	/**
	 * Use this value for implying no limit on the
	 * number of models to load.
	 */
	public static final int NO_LIMIT = -1;



	/**
	 * 
	 * @param lookupRoutine
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static List<Design> loadCurrentCity(LookupRoutine lookupRoutine) throws UnknownHostException, IOException{
		return loadCity(lookupRoutine, NO_LIMIT, SettingsPreferences.getCity().getCityID());
	}

	/**
	 * 
	 * @param lookupRoutine The {@link LookupRoutine} to use when loading models.
	 * @param limit The maximum number of models to be loaded
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static List<Design> loadCurrentCity(LookupRoutine lookupRoutine, int limit) throws UnknownHostException, IOException{
		return loadCity(lookupRoutine, limit, SettingsPreferences.getCity().getCityID());
	}

	/**
	 * 
	 * @param lookupRoutine
	 * @param limit Sets a numeric limit on how many models to load
	 * from the network.  Helpful for testing purposes. {@link NetModelLoader#NO_LIMIT} for no limit,
	 * otherwise, use the number of models desired.
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @see NetModelLoader#NO_LIMIT
	 */
	public static List<Design> loadCity(LookupRoutine lookupRoutine, int limit, final int cityID) throws UnknownHostException, IOException {		
		logger.info("Loading City " + cityID);
		
		List<Design> designs = null;

		final AtomicBoolean allDesignsProcessed = new AtomicBoolean(false);

		UnprotectedManager unprotectedManager = NetPool.getPool().getConnection();
		if(lookupRoutine.equals(LookupRoutine.ALL_IN_CITY)){
			designs = unprotectedManager.findBaseDesignsByCity(cityID);
		} else if (lookupRoutine.equals(LookupRoutine.ALL_BY_USER)){
			try {
				String user = SettingsPreferences.getUser();
				designs = unprotectedManager.findDesignsByUser(user);
			} catch(NullPointerException e){
				new RuntimeException("User was not set, not loading models");
			}
		}

		if (designs == null) {
			logger.error("No base models received!");
			designs = new ArrayList<Design>();
		}
		
		List<Design> base = new ArrayList<Design>();
		for(int i = 0; i < designs.size(); i++){
			if(!(limit == NO_LIMIT || i < limit)) {
				break;
			}
			
			Design design = designs.get(i);
			
			if(design instanceof ModeledDesign){
				boolean fileResponse = false;
				fileResponse = CacheManager.getCacheManager2().requestFile(design.getID(), design.getFilepath());
				
				if (fileResponse) {
					if (design.getClassification().equals(Classification.BASE) && !design.isProposal() && design.isPublic()) {
						logger.info("(Base) Adding: " + design.getName() + " | ID: " + design.getID());
						base.add(design);
						SettingsPreferences.getCity(cityID).addDesign(design);
					}
				}
			} else if(design instanceof EmptyDesign) {
				// What the heck might these things be good for?
			}
		}
		allDesignsProcessed.set(true);
		return base;
	}

	/**
	 * Loads the terrain for the current city
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static List<Design> loadCurrentCityTerrain() throws UnknownHostException, IOException{
		return loadCityTerrain(SettingsPreferences.getCurrentCityID());
	}

	/**
	 * Loads the terrain for the current city
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static List<Design> loadCityTerrain(int cityID) throws UnknownHostException, IOException{
		logger.info("Loading Terrain for city " + cityID);
		List<Design> designs = NetPool.getPool().getConnection().findTerrainByCity(cityID);
		
		if(designs == null){
			logger.error("No terrain received!");
			designs = new ArrayList<Design>();
		}

		List<Design> terrain = new ArrayList<Design>();
		for(int i = 0; i < designs.size(); i++) {
			Design design = designs.get(i);
			
			if(design instanceof ModeledDesign){
				boolean fileResponse = false;
				fileResponse = CacheManager.getCacheManager2().requestFile(design.getID(), design.getFilepath());
				
				if (fileResponse) {
					if(design.getName().contains("$TERRAIN")) {
						// Terrain
						logger.info("(Terrain) Adding: " + design.getName() + " | ID: " + design.getID());
						terrain.add(design);
					} else {
						// Design
					}	
				}
			} else if(design instanceof EmptyDesign){
				// What the heck might these things be good for?
			}
		}
		return terrain;
	}

}
