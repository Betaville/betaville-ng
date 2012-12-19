package net.betaville.ng.net;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.DataFormatException;

import net.betaville.ng.CacheManager;
import net.betaville.ng.util.StringZipper;
import net.betaville.ng.xml.DataReader;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.City;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.ProposalPermission;
import edu.poly.bxmc.betaville.model.Wormhole;
import edu.poly.bxmc.betaville.model.IUser.UserType;
import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;

/**
 * @author Skye Book
 *
 */
public abstract class ClientManager extends NetworkConnection implements UnprotectedManager {
	private static final Logger logger = Logger.getLogger(ClientManager.class);
	
	protected long lastUsed=-1;
	
	/**
	 * Sets the <code>lastUsed</code> value to the current system time
	 */
	public void touchLastUsed(){
		lastUsed=System.currentTimeMillis();
	}
	
	/**
	 * Gets the last time that this manager was used
	 * @return The long time
	 */
	public long getLastUsed(){
		return lastUsed;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#checkNameAvailability(java.lang.String)
	 */
	public boolean checkNameAvailability(String name){
		busy.getAndSet(true);
		try {
			logger.info("Checking if a name is available");
			writeToStream(new Object[]{"user", "available", name});
			String response = (String)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean(response);
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getUserEmail(java.lang.String)
	 */
	public String getUserEmail(String user){
		busy.getAndSet(true);
		try {
			logger.info("Getting user email");
			writeToStream(new Object[]{"user", "getmail", user});
			String response = (String)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignByID(int)
	 */
	public Design findDesignByID(int designID){
		busy.getAndSet(true);
		try {
			logger.debug("Finding design " + designID);
			writeToStream(new Object[]{"design", "findbyid", designID});
			Object response = readResponse();
			if(response instanceof Design){
				CacheManager.getCacheManager().requestFile(((Design)response).getID(), ((Design)response).getFilepath());
				CacheManager.getCacheManager().requestThumbnail(designID);
				
				busy.getAndSet(false);
				touchLastUsed();
				return (Design)response;
			}
			else{
				busy.getAndSet(false);
				touchLastUsed();
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findDesignsByName(String name){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs named \""+name+"\"");
			writeToStream(new Object[]{"design", "findbyname", name});
			Object response = readResponse();
			if(response instanceof List<?>){
				if(((List<?>)(response)).get(0) instanceof Design){
					busy.getAndSet(false);
					touchLastUsed();
					return (List<Design>)response;
				}
				else{
					busy.getAndSet(false);
					touchLastUsed();
					return null;
				}
			}
			else{
				busy.getAndSet(false);
				touchLastUsed();
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByUser(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findDesignsByUser(String user){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs by \""+user+"\"");
			writeToStream(new Object[]{"design", "findbyuser", user});
			Object response = readResponse();
			if(response instanceof List<?>){
				logger.info("returning");
				busy.getAndSet(false);
				touchLastUsed();
				return (List<Design>)response;
			}
			else{
				logger.error("Not a list!");
				busy.getAndSet(false);
				touchLastUsed();
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findDesignsByDate(java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findDesignsByDate(Date date){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs dates \""+date+"\"");
			writeToStream(new Object[]{"design", "findbydate", date.getTime()});
			Object response = readResponse();
			if(response instanceof List<?>){
				logger.info("returning");
				busy.getAndSet(false);
				touchLastUsed();
				return (List<Design>)response;
			}
			else{
				logger.error("Not a list!");
				busy.getAndSet(false);
				touchLastUsed();
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllDesignsByCity(int)
	 */
	public List<Design> findAllDesignsByCity(int cityID){
		return findDesignsByCity(cityID, false);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findBaseDesignsByCity(int)
	 */
	public List<Design> findBaseDesignsByCity(int cityID){
		return findDesignsByCity(cityID, true);
	}

	/**
	 * Gets the designs located in a city.
	 * @param cityID The ID of the city that we are looking in.
	 * @param onlyBase Whether to retrieve only the base designs
	 * (true) or all of them (false).
	 * @return A Vector of <code>Design</code> objects.
	 * @see Design
	 */
	@SuppressWarnings("unchecked")
	protected List<Design> findDesignsByCity(int cityID, boolean onlyBase){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs from city " + cityID);
			writeToStream(new Object[]{"design", "findbycity", cityID, onlyBase});
			Object response = readResponse();
			if(response instanceof List<?>){
				if(((List<?>)response).size()>0){
					if(((List<?>)(response)).get(0) instanceof Design){
						busy.getAndSet(false);
						touchLastUsed();
						return (List<Design>)response;
					}
					else{
						busy.getAndSet(false);
						touchLastUsed();
						return null;
					}
				}
				else{
					busy.getAndSet(false);
					touchLastUsed();
					return null;
				}
			}
			else{
				busy.getAndSet(false);
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}
	
	/**
	 * Gets the designs located in a city.
	 * @param cityID The ID of the city that we are looking in.
	 * @param onlyBase Whether to retrieve only the base designs
	 * (true) or all of them (false).
	 * @param start
	 * @param end
	 * @return A List of <code>Design</code> objects.
	 * @see Design
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findDesignsByCitySetStartEnd(int cityID, boolean onlyBase, int start, int end){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs from city " + cityID);
			writeToStream(new Object[]{"design", "findbycitysetstartend", cityID, onlyBase, start, end});
			Object response = readResponse();
			if(response instanceof List<?>){
				if(((List<?>)response).size()>0){
					if(((List<?>)(response)).get(0) instanceof Design){
						busy.getAndSet(false);
						touchLastUsed();
						return (List<Design>)response;
					}
					else{
						busy.getAndSet(false);
						touchLastUsed();
						return null;
					}
				}
				else{
					busy.getAndSet(false);
					touchLastUsed();
					return null;
				}
			}
			else{
				busy.getAndSet(false);
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}
	
	public List<Design> findModelDesignsByCityLimit(int cityID, boolean onlyBase, int limit){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs from city " + cityID);
			writeToStream(new Object[]{"design", "findbymodellimitedcity", cityID, onlyBase, limit});
			byte[] response = (byte[])readResponse();
			busy.set(false);
			touchLastUsed();
			return DataReader.readDesigns(StringZipper.uncompress(response));
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		} catch (JDOMException e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		} catch (DataFormatException e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findTerrainByCity(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findTerrainByCity(int cityID){
		busy.getAndSet(true);
		try {
			logger.info("Finding designs from city " + cityID);
			writeToStream(new Object[]{"design", "terrainbycity", cityID});
			Object response = readResponse();
			if(response instanceof List<?>){
				if(((List<?>)response).size()>0){
					if(((List<?>)(response)).get(0) instanceof Design){
						busy.getAndSet(false);
						touchLastUsed();
						return (List<Design>)response;
					}
					else{
						busy.getAndSet(false);
						touchLastUsed();
						return null;
					}
				}
				else{
					busy.getAndSet(false);
					touchLastUsed();
					return null;
				}
			}
			else{
				busy.getAndSet(false);
				touchLastUsed();
				return null;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findVersionsOfProposal(int)
	 */
	public int[] findVersionsOfProposal(int proposalDesignID){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"version", "versionsofproposal", proposalDesignID});
			Object response = readResponse();
			if(response!=null){
				if(response instanceof int[]){
					busy.getAndSet(false);
					touchLastUsed();
					return (int[])response;
				}
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<Design> getVersionsOfProposal(int proposalRootDesignID){
		busy.getAndSet(true);
		try {
			logger.info("Getting versions of Proposal "+proposalRootDesignID);
			writeToStream(new Object[]{"version", "getversionsofproposal", proposalRootDesignID});
			Object response = readResponse();
			if(response instanceof List){
				busy.getAndSet(false);
				touchLastUsed();
				return (List<Design>)response;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getProposalPermissions(int)
	 */
	public ProposalPermission getProposalPermissions(int designID){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"proposal", "getpermissions", designID});
			ProposalPermission response = (ProposalPermission) readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
		busy.getAndSet(false);
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllProposalsInCity(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findAllProposalsInCity(int cityID){
		busy.getAndSet(true);
		try {
			logger.info("Finding proposals in city "+cityID);
			writeToStream(new Object[]{"design", "findproposalsbycity", cityID});
			Object response = readResponse();
			if(response instanceof List){
				busy.getAndSet(false);
				touchLastUsed();
				return (List<Design>)response;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllProposals(int)
	 */
	public int[] findAllProposals(int designID){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"design", "allproposals", designID});
			int[] response = (int[]) readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllProposalsNearLocation(edu.poly.bxmc.betaville.jme.map.UTMCoordinate, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> findAllProposalsNearLocation(UTMCoordinate coordinate, int meterRadius){
		busy.getAndSet(true);
		try {
			logger.info("Finding proposals within " + meterRadius + " of " + coordinate.toString());
			writeToStream(new Object[]{"proposal", "findinradius", coordinate, meterRadius});
			Object response = readResponse();
			if(response instanceof List){
				busy.getAndSet(false);
				touchLastUsed();
				return (List<Design>)response;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestThumbnail(int)
	 */
	public PhysicalFileTransporter requestThumbnail(int designID){
		busy.getAndSet(true);
		try{
			logger.debug("Requesting thumbnail for design " + designID);
			writeToStream(new Object[]{"design", "requestthumb", designID});
			PhysicalFileTransporter response = (PhysicalFileTransporter)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestFile(edu.poly.bxmc.betaville.model.Design)
	 */
	public PhysicalFileTransporter requestFile(Design design){
		return requestFile(design.getID());
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#requestFile(int)
	 */
	public PhysicalFileTransporter requestFile(int designID){
		busy.getAndSet(true);
		try {
			logger.debug("Requesting file for design " + designID);
			writeToStream(new Object[]{"design", "requestfile", designID});
			Object object = readResponse();
			PhysicalFileTransporter response = null;
			if(object instanceof PhysicalFileTransporter){
				response = (PhysicalFileTransporter)object;
			}
			else{
				// use the URL
			}
			
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#addCity(java.lang.String, java.lang.String, java.lang.String)
	 */
	public int addCity(String name, String state, String country){
		busy.getAndSet(true);
		try {
			logger.info("Adding a city [if it doesn't already exist]: " + name + ", " + state + ", " + country);
			writeToStream(new Object[]{"city", "add", name, state, country});
			int response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return -3;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findAllCities()
	 */
	@SuppressWarnings("unchecked")
	public List<City> findAllCities(){
		try {
			busy.getAndSet(true);
			writeToStream(new String[]{"city", "getall"});
			List<City> response = (List<City>)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCitiesByName(String name){
		busy.getAndSet(true);
		try {
			logger.info("Finding cities named \""+name+"\"");
			writeToStream(new Object[]{"city", "findbyname", name});
			List<Integer> response = (List<Integer>)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByState(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCitiesByState(String state){
		busy.getAndSet(true);
		try {
			logger.info("Finding cities located in \""+state+"\"");
			writeToStream(new Object[]{"city", "findbystate", state});
			List<Integer> response = (List<Integer>)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCitiesByCountry(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> findCitiesByCountry(String country){
		busy.getAndSet(true);
		try {
			logger.info("Finding cities located in \""+country+"\"");
			writeToStream(new Object[]{"city", "findbycountry", country});
			List<Integer> response = (List<Integer>)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCityByID(int)
	 */
	public String[] findCityByID(int cityID){
		busy.getAndSet(true);
		try {
//			logger.info("Finding city with ID: "+cityID);
			writeToStream(new Object[]{"city", "findbyid", cityID});
			String[] response = (String[])readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#findCityByAll(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String[] findCityByAll(String name, String state, String country){
		busy.getAndSet(true);
		try {
//			logger.info("Finding city: \""+name+"\", \""+state+"\", \""+country+"\"");
			writeToStream(new Object[]{"city", "findbyall", name, state, country});
			String[] response = (String[])readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
			return null;
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#reportSpamComment(int)
	 */
	public void reportSpamComment(int commentID){
		busy.getAndSet(true);
		try {
			logger.info("Reporting comment " + commentID + " as spam");
			writeToStream(new Object[]{"comment", "reportspam", commentID});
			busy.getAndSet(false);
			touchLastUsed();
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		}
		busy.getAndSet(false);
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getComments(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Comment> getComments(int designID){
		busy.getAndSet(true);
		try {
			logger.info("Retrieving comments for design " + designID);
			writeToStream(new Object[]{"comment", "getforid", designID});
			List<Comment> response = (List<Comment>)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#checkUserLevel(java.lang.String, edu.poly.bxmc.betaville.model.IUser.UserType)
	 */
	public int checkUserLevel(String user, 	UserType userType){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"user", "checklevel", user, userType});
			int response =  Integer.parseInt((String) readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return -2;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getDesignVersion()
	 */
	public long getDesignVersion(){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"softwareversion", "getdesign"});
			logger.info("Wrote softwareversion and getdesign");
			long response =  Long.parseLong((String) readResponse());
			logger.info("Read response");
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return -2;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getUserLevel(java.lang.String)
	 */
	public UserType getUserLevel(String user){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"user", "getlevel", user});
			UserType response = (UserType)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e){
			logger.error("Network issue detected", e);
			busy.getAndSet(false);
		} catch (UnexpectedServerResponse e){
			e.printStackTrace();
			busy.getAndSet(false);
		}
		busy.getAndSet(false);
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getWormholesWithin(edu.poly.bxmc.betaville.jme.map.UTMCoordinate, int, int)
	 */
	@SuppressWarnings("unchecked")
	public List<Wormhole> getWormholesWithin(UTMCoordinate location, int extentNorth, int extentEast){
		List<Wormhole> wormholes = null;
		busy.getAndSet(true);
		try{
			writeToStream(new Object[]{"wormhole", "getwithin", location, extentNorth, extentEast});
			wormholes = (List<Wormhole>)readResponse();
		} catch (IOException e){
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e){
			logger.error("The server has returned an unexpected type!", e);
		}
		busy.getAndSet(false);
		touchLastUsed();
		return wormholes;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getAllWormholes()
	 */
	@SuppressWarnings("unchecked")
	public List<Wormhole> getAllWormholes(){
		List<Wormhole> wormholes = null;
		busy.getAndSet(true);
		try{
			writeToStream(new Object[]{"wormhole", "getall"});
			wormholes = (List<Wormhole>)readResponse();
		} catch (IOException e){
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e){
			logger.error("The server has returned an unexpected type!", e);
		}
		busy.getAndSet(false);
		touchLastUsed();
		return wormholes;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#getAllWormholesInCity(int)
	 */
	@SuppressWarnings("unchecked")
	public List<Wormhole> getAllWormholesInCity(int cityID){
		logger.info("looking for all wormholes in city");
		List<Wormhole> wormholes = null;
		busy.getAndSet(true);
		try{
			writeToStream(new Object[]{"wormhole", "getallincity", cityID});
			wormholes = (List<Wormhole>)readResponse();
		} catch (IOException e){
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e){
			logger.error("The server has returned an unexpected type!", e);
		}
		busy.getAndSet(false);
		touchLastUsed();
		return wormholes;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.UnprotectedManager#synchronizeData(java.util.HashMap)
	 */
	@SuppressWarnings("unchecked")
	public List<Design> synchronizeData(HashMap<Integer, Integer> hashMap){
		List<Design> designs = null;
		busy.getAndSet(true);
		try{
			writeToStream(new Object[]{"design", "synchronizedata", hashMap});
			designs = (List<Design>)readResponse();
		} catch (IOException e){
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e){
			logger.error("The server has returned an unexpected type!", e);
		}
		busy.getAndSet(false);
		touchLastUsed();
		return designs;
	}
}
