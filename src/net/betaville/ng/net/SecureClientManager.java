package net.betaville.ng.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import net.betaville.ng.SettingsPreferences;

import org.apache.log4j.Logger;

import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ProposalPermission;
import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;

/**
 * Provides an encrypted communications channel to the server
 * @author Skye Book
 *
 */
public class SecureClientManager extends ClientManager implements ProtectedManager{
	private static Logger logger = Logger.getLogger(SecureClientManager.class);

	/**
	 * Constant <PORT_SERVER> - Port of the server
	 */
	private final int PORT_SERVER = 14500;

	/**
	 * Constructor - Creation of the client manager
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public SecureClientManager(boolean createSocketHere) throws UnknownHostException, IOException{
		this(createSocketHere, SettingsPreferences.getServerIP());
	}

	/**
	 * Constructor - Creation of the client manager
	 */
	public SecureClientManager(boolean createSocketHere, String serverIP) throws UnknownHostException, IOException{

		if(!createSocketHere) return;

		clientSocket = new Socket(serverIP, PORT_SERVER);
		logger.info("Client application : "+ clientSocket.toString());
		progressOutput = new ProgressOutputStream(clientSocket.getOutputStream());
		output = new ObjectOutputStream(progressOutput);

		progressInput = new ProgressInputStream(clientSocket.getInputStream());
		input = new ObjectInputStream(progressInput);
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#startSession(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean startSession(String name, String pass){
		busy.getAndSet(true);
		try {
			logger.info("Starting User Session");
			writeToStream(new Object[]{"user", "startsession", name, pass});
			Object[] response = (Object[])readResponse();
			int sessionID = Integer.parseInt((String)response[0]);
			String sessionToken = (String)response[1];
			busy.getAndSet(false);
			touchLastUsed();
			if(sessionID>0){
				logger.info("Logged in as "+ name +", Session ID: "+sessionID);
				SettingsPreferences.setSessionID(sessionID);
				SettingsPreferences.setSessionToken(sessionToken);
				return true;
			}
			else{
				if(sessionID==-3){
					// authentication failed
					logger.warn("Authentication Failed");
				}
				return false;
			}
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#endSession(java.lang.String)
	 */
	@Override
	public boolean endSession(String sessionToken){
		busy.getAndSet(true);
		try {
			logger.info("Ending user session");
			writeToStream(new Object[]{"user", "endsession", sessionToken});
			int response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			if(response==0){
				SettingsPreferences.setSessionToken("");
				return true;
			}
			else return false;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addUser(String name, String pass, String email, String twitter, String bio){
		busy.getAndSet(true);
		try {
			logger.info("Adding User");
			writeToStream(new Object[]{"user", "add", name, pass, email, twitter, bio});
			String response = (String)readResponse();
			if(Boolean.parseBoolean(response)){
				System.out.println("bool read correctly");
			}
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean(response);
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changePassword(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean changePassword(String name, String pass, String newPass){
		busy.getAndSet(true);
		try {
			logger.info("Changing user password");
			writeToStream(new Object[]{"user", "changepass", name, pass, newPass});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeBio(java.lang.String)
	 */
	@Override
	public boolean changeBio(String newBio){
		busy.getAndSet(true);
		try {
			logger.info("Changing user information");
			writeToStream(new Object[]{"user", "changebio", SettingsPreferences.getSessionToken(), newBio});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addEmptyDesign(edu.poly.bxmc.betaville.model.EmptyDesign)
	 */
	@Override
	public int addEmptyDesign(EmptyDesign design){
		busy.getAndSet(true);
		try {
			logger.info("Adding an empty design");
			writeToStream(new Object[]{"design", "addempty", design, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -3;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addProposal(edu.poly.bxmc.betaville.model.Design, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.model.ProposalPermission)
	 */
	@Override
	public int addProposal(Design design, String removables, PhysicalFileTransporter pft, PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter, ProposalPermission permission){
		busy.getAndSet(true);
		try {
			logger.info("Adding a proposal");
			writeToStream(new Object[]{"design", "addproposal", design, null, SettingsPreferences.getSessionToken(), pft, removables, thumbTransporter, sourceMediaTransporter, permission});
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -3;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addVersion(edu.poly.bxmc.betaville.model.Design, java.lang.String, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter)
	 */
	@Override
	public int addVersion(Design design, String removables, PhysicalFileTransporter pft, PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter){
		busy.getAndSet(true);
		try {
			logger.info("Adding a version of a proposal");
			writeToStream(new Object[]{"proposal", "addversion", design, null, SettingsPreferences.getSessionToken(), pft, removables, thumbTransporter, sourceMediaTransporter});
			logger.info("Data transmission complete for design addition");
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -3;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addBase(edu.poly.bxmc.betaville.model.Design, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter)
	 */
	@Override
	public int addBase(Design design, PhysicalFileTransporter pft, PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter){
		busy.getAndSet(true);
		try {
			logger.info("Adding a base design");
			writeToStream(new Object[]{"design", "addbase", design, null, SettingsPreferences.getSessionToken(), pft, thumbTransporter, sourceMediaTransporter});
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -3;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#setThumbnailForObject(int, edu.poly.bxmc.betaville.net.PhysicalFileTransporter)
	 */
	@Override
	public int setThumbnailForObject(int designID, PhysicalFileTransporter pft) {
		busy.getAndSet(true);
		try {
			logger.info("Adding a base design");
			// send the thumbnail along as well if its transporter isn't null
			writeToStream(new Object[]{"design", "setthumb", designID, pft, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#removeDesign(int)
	 */
	@Override
	public int removeDesign(int designID){
		busy.getAndSet(true);
		try {
			logger.info("Removing a design");
			writeToStream(new Object[]{"design", "remove", designID, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -2;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignName(int, java.lang.String)
	 */
	@Override
	public boolean changeDesignName(int designID, String newName){
		busy.getAndSet(true);
		try {
			logger.info("Changing name of design " + designID + " to \""+newName+"\"");
			writeToStream(new Object[]{"design", "changename", designID, newName, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignFile(int, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, edu.poly.bxmc.betaville.net.PhysicalFileTransporter, boolean)
	 */
	@Override
	public boolean changeDesignFile(int designID, PhysicalFileTransporter pft, PhysicalFileTransporter sourceMedia, boolean textureOnOff){
		busy.getAndSet(true);
		try {
			logger.info("Changing the file for design " + designID);
			writeToStream(new Object[]{"design", "changefile", designID, null, SettingsPreferences.getSessionToken(), textureOnOff, pft});
			logger.info("Data transmission complete for file change");
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignDescription(int, java.lang.String)
	 */
	@Override
	public boolean changeDesignDescription(int designID, String newDescription){
		busy.getAndSet(true);
		try {
			logger.info("Changing description for design " + designID);
			writeToStream(new Object[]{"design", "changedescription", designID, newDescription, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignAddress(int, java.lang.String)
	 */
	@Override
	public boolean changeDesignAddress(int designID, String newAddress){
		busy.getAndSet(true);
		try {
			logger.info("Changing the address of design " + designID);
			writeToStream(new Object[]{"design", "changeaddress", designID, newAddress, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeDesignURL(int, java.lang.String)
	 */
	@Override
	public boolean changeDesignURL(int designID, String newURL){
		busy.getAndSet(true);
		try {
			logger.info("Changing the URL for design " + designID);
			writeToStream(new Object[]{"design", "changeurl", designID, newURL, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#changeModeledDesignLocation(int, float, edu.poly.bxmc.betaville.jme.map.UTMCoordinate)
	 */
	@Override
	public boolean changeModeledDesignLocation(int designID, float rotY, UTMCoordinate newLocation){
		busy.getAndSet(true);
		try {
			logger.info("Changing the location for design " + designID);
			writeToStream(new Object[]{"design", "changemodellocation", designID, newLocation, rotY, SettingsPreferences.getSessionToken()});
			String response = (String)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean(response);
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#faveDesign(int)
	 */
	@Override
	public int faveDesign(int designID){
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"fave", "add", SettingsPreferences.getSessionToken(), designID});
			String response = (String)readResponse();
			busy.getAndSet(false);
			touchLastUsed();
			return Integer.parseInt(response);
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return -4;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addComment(edu.poly.bxmc.betaville.model.Comment)
	 */
	@Override
	public boolean addComment(Comment comment){
		busy.getAndSet(true);
		try {
			logger.info("Inserting a comment for design " + comment.getDesignID());
			writeToStream(new Object[]{"comment", "add", comment, SettingsPreferences.getSessionToken()});
			boolean response = Boolean.parseBoolean((String)readResponse());
			System.out.println("received net response");
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#deleteComment(int)
	 */
	@Override
	public boolean deleteComment(int commentID){
		busy.getAndSet(true);
		try {
			logger.info("Deleting comment " + commentID);
			writeToStream(new Object[]{"comment", "delete", commentID, SettingsPreferences.getSessionToken()});
			busy.getAndSet(false);
			touchLastUsed();
			return Boolean.parseBoolean((String)readResponse());
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#addWormhole(edu.poly.bxmc.betaville.jme.map.ILocation, java.lang.String, int)
	 */
	@Override
	public int addWormhole(ILocation location, String name, int cityID){
		Integer response = null;
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"wormhole", "add", location.getUTM(), name, cityID, SettingsPreferences.getSessionToken()});
			response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#deleteWormhole(int)
	 */
	@Override
	public int deleteWormhole(int wormholeID){
		Integer response = null;
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"wormhole", "delete", wormholeID, SettingsPreferences.getSessionToken()});
			response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#editWormholeName(int, java.lang.String)
	 */
	@Override
	public int editWormholeName(int wormholeID, String newName){
		Integer response = null;
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"wormhole", "editname", newName, wormholeID, SettingsPreferences.getSessionToken()});
			response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return response;
	}

	/*
	 * (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.net.ProtectedManager#editWormholeLocation(int, edu.poly.bxmc.betaville.jme.map.UTMCoordinate)
	 */
	@Override
	public int editWormholeLocation(int wormholeID, UTMCoordinate newLocation){
		Integer response = null;
		busy.getAndSet(true);
		try {
			writeToStream(new Object[]{"wormhole", "editname", newLocation, wormholeID, SettingsPreferences.getSessionToken()});
			response = Integer.parseInt((String)readResponse());
			busy.getAndSet(false);
			touchLastUsed();
			return response;
		} catch (IOException e) {
			logger.error("Network issue detected", e);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (UnexpectedServerResponse e) {
			e.printStackTrace();
		}
		busy.getAndSet(false);
		return response;
	}
}
