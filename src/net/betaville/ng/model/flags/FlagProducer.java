package net.betaville.ng.model.flags;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.betaville.ng.SettingsPreferences;

import org.apache.log4j.Logger;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.Design.Classification;

/**
 * @author Skye Book
 *
 */
@SuppressWarnings("unused")
public class FlagProducer {
	private static Logger logger = Logger.getLogger(FlagProducer.class);
	private UTMCoordinate currentLocation;
	private AbstractFlagPositionStrategy flagPositionStrategy;

	// all of the proposals found in an area
	private List<Design>proposalDesigns = null;


	ArrayList<Integer> bases = new ArrayList<Integer>();


	/**
	 * 
	 */
	public FlagProducer(UTMCoordinate currentLocation, AbstractFlagPositionStrategy flagPositionStrategy){
		this.currentLocation=currentLocation;
		this.flagPositionStrategy=flagPositionStrategy;
	}

	public void getProposals(int meterRadius) throws UnknownHostException, IOException{
		// TODO: Don't do networking in here
//		proposalDesigns = NetPool.getPool().getConnection().findAllProposalsNearLocation(currentLocation, meterRadius);
//		logger.info(proposalDesigns.size() + " proposals retrieved");
//
//		Iterator<Design> proposalIt = proposalDesigns.iterator();
//		while(proposalIt.hasNext()){
//			Design design = proposalIt.next();
//			if(design.getClassification().equals(Classification.PROPOSAL)){
//				SettingsPreferences.getCity().addDesign(design);
//				bases.add(design.getSourceID());
//			}
//		}
//		logger.debug(proposalDesigns.size() + " proposal(s) based on " +bases.size() + " designs found within the specified area");
	}

	public void placeFlags(){
		// TODO: Repair placeFlags()
		logger.warn("Reached placeFlags() stub");
//		double groupingProximityThreshold = 50d;
//
//		HashMap<ArrayList<Design>, UTMCoordinate> groupings = new HashMap<ArrayList<Design>, UTMCoordinate>();
//
//		for(int i=0; i<proposalDesigns.size(); i++){
//			Design base = proposalDesigns.get(i);
//			// skip this design if it isn't a proposal root
//			if(!base.getClassification().equals(Classification.PROPOSAL)) continue;
//
//			boolean hasBeenPutInGroup = false;
//
//			// go through each grouping and look for a match based on proximity
//			for(Entry<ArrayList<Design>, UTMCoordinate> entry : groupings.entrySet()){
//				// calculate the distance between this proposal and the average of the proposals in the group
//				/*
//				 * Note: This is a slightly skewed calculation as it means that proposals put in a fresh group
//				 * at the beginning of the list could potentially be eligible for inclusion in a group in a later
//				 * part of the list as the coordinate changes due to averaging.  I think this is something we can
//				 * live with for the time being (and the odds are that this will probably never be an issue, presuming
//				 * that the proximity threshold for inclusion in a group is kept fairly small)
//				 */
//				double distance = MapManager.greatCircleDistanced(entry.getValue().getGPS(), base.getCoordinate().getGPS());
//
//				if(distance<groupingProximityThreshold){
//					logger.debug("Found a proposal to be grouped!");
//					ArrayList<Design> group = entry.getKey();
//					group.add(base);
//					hasBeenPutInGroup=true;
//
//					// average the coordinates
//					ArrayList<ILocation> coordinates = new ArrayList<ILocation>();
//					for(Design design : group){
//						coordinates.add(design.getCoordinate());
//					}
//
//					// assign the new average coordinate
//					entry.setValue(MapManager.averageLocations(coordinates).getUTM());
//				}
//			}
//
//			// if, after scanning the groups, we haven't found a match it is time to make a new group of proposals
//			if(!hasBeenPutInGroup){
//				ArrayList<Design> group = new ArrayList<Design>();
//				group.add(base);
//				groupings.put(group, base.getCoordinate().clone());
//			}
//		}
//
//		logger.info("There will now be " + groupings.size() + " groupings rather than " + bases.size()+" indvidual flags");
//
//
//		// now let's place those flags!
//		for(Entry<ArrayList<Design>, UTMCoordinate> entry : groupings.entrySet()){
//			ArrayList<Integer> baseIDs = new ArrayList<Integer>();
//			for(Design proposalBase : entry.getKey()){
//				baseIDs.add(proposalBase.getID());
//			}
//
//			entry.getValue().setAltitude(50);
//
//			flagPositionStrategy.placeFlag(entry.getValue(), baseIDs);
//		}
		
	}
}