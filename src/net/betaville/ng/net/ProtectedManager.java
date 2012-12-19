package net.betaville.ng.net;

import edu.poly.bxmc.betaville.jme.map.ILocation;
import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Comment;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.model.EmptyDesign;
import edu.poly.bxmc.betaville.model.ProposalPermission;
import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;

/**
 * Outline of the authenticated network functionality in Betaville
 * @author Skye Book
 *
 */
public interface ProtectedManager extends UnprotectedManager{

	public abstract boolean startSession(String name, String pass);

	public abstract boolean endSession(String sessionToken);

	/**
	 * Adds a user to the server.
	 * @param name The username of the new user.
	 * @param pass The password for the new user.
	 * @param email The email address as provided by the new user.
	 * @param twitter The twitter account/name as provided by the new user.
	 * @param bio The biography as provided by the new user.
	 * @return Whether or not the operation succeeded.
	 */
	public abstract boolean addUser(String name, String pass, String email,
			String twitter, String bio);

	/**
	 * Changes the password for a user.
	 * @param name The user proposing the change.
	 * @param pass The password supplied by the user.
	 * @param newPass The new password to be used.
	 * @return Whether or not the operation succeeded.
	 */
	public abstract boolean changePassword(String name, String pass,
			String newPass);

	/**
	 * Changes the biographical information of a user.
	 * @param newBio The new biographic information to be used.
	 * @return Whether or not the operation succeeded.
	 */
	public abstract boolean changeBio(String newBio);

	/**
	 * Adds an EmptyDesign to the server.
	 * @param design The <code>Design</code> object describing the new entity.
	 * @param sourceID The ID of the design that this <code>Design</code>
	 * is improving on.  Using the value 0 here signifies that this is not an
	 * iteration on a previously existing <code>Design</code>.
	 * @param pft A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @return The ID of the new <code>Design</code>.
	 * @see Design
	 * @see PhysicalFileTransporter
	 */
	public abstract int addEmptyDesign(EmptyDesign design);

	/**
	 * Adds a design to the server.
	 * @param design The <code>Design</code> object describing the new entity.
	 * @param sourceID The ID of the design that this <code>Design</code>
	 * is improving on.  Using the value 0 here signifies that this is not an
	 * iteration on a previously existing <code>Design</code>.
	 * @param pft A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param thumbTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param sourceMediaTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param permission A set of proposal permissions
	 * @return The ID of the new <code>Design</code>.
	 * @see Design
	 * @see PhysicalFileTransporter
	 */
	public abstract int addProposal(Design design, String removables, PhysicalFileTransporter pft,
			PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter,
			ProposalPermission permission);

	/**
	 * Adds a version of the proposal to the server.
	 * @param design The <code>Design</code> object describing the new entity.
	 * @param sourceID The ID of the design that this <code>Design</code>
	 * is improving on.  Using the value 0 here signifies that this is not an
	 * iteration on a previously existing <code>Design</code>.
	 * @param pft A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param thumbTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param sourceMediaTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @return The ID of the new <code>Design</code>.
	 * @see Design
	 * @see PhysicalFileTransporter
	 */
	public abstract int addVersion(Design design, String removables, PhysicalFileTransporter pft,
			PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter);

	/**
	 * Adds a design to the server.
	 * @param design The <code>Design</code> object describing the new entity.
	 * @param sourceID The ID of the design that this <code>Design</code>
	 * is improving on.  Using the value 0 here signifies that this is not an
	 * iteration on a previously existing <code>Design</code>.
	 * @param pft A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param thumbTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @param sourceMediaTransporter A <code>PhysicalFileTransporter</code> to carry relevant media data.
	 * @return The ID of the new <code>Design</code>.
	 * @see Design
	 * @see PhysicalFileTransporter
	 */
	public abstract int addBase(Design design, PhysicalFileTransporter pft,
			PhysicalFileTransporter thumbTransporter, PhysicalFileTransporter sourceMediaTransporter);
	
	/**
	 * Sets a thumbnail for an object
	 * @param designID The {@link Design} to set the thumbnail for.
	 * @param pft The data containing the thumbnail image
	 * @return
	 */
	public abstract int setThumbnailForObject(int designID, PhysicalFileTransporter pft);

	/**
	 * Removes a <code>Design</code> from the database.  Must be performed by the user who
	 * originally added the design.  
	 * @param designID The ID of the <code>Design</code> to remove.
	 * @return 0 for success, -2 for networking error, -3 for failed authentication
	 */
	public abstract int removeDesign(int designID);

	/**
	 * Changes the stored name of a <code>Design</code>
	 * @param designID The ID of the <code>Design</code> to change the name for.
	 * @param newName The new name to be applied.
	 * @return Whether or not the name change operation succeeded.
	 */
	public abstract boolean changeDesignName(int designID, String newName);

	/**
	 * 
	 * @param designID
	 * @param pft
	 * @param sourceMedia
	 * @param textureOnOff
	 * @return
	 */
	public abstract boolean changeDesignFile(int designID, PhysicalFileTransporter pft,
			PhysicalFileTransporter sourceMedia, boolean textureOnOff);

	/**
	 * Changes the stored description of a <code>Design</code>
	 * @param designID The ID of the <code>Design</code> to change the description for.
	 * @param newDescription The new description to be applied.
	 * @return Whether or not the description change operation succeeded.
	 */
	public abstract boolean changeDesignDescription(int designID, String newDescription);

	/**
	 * Changes the stored address of a <code>Design</code>
	 * @param designID The ID of the <code>Design</code> to change the address for.
	 * @param newAddress The new address to be applied.
	 * @return Whether or not the address change operation succeeded.
	 */
	public abstract boolean changeDesignAddress(int designID, String newAddress);

	/**
	 * Changes the stored url of a <code>Design</code>
	 * @param designID The ID of the <code>Design</code> to change the address for.
	 * @param url The new URL to be applied.
	 * @return Whether or not the URL change operation succeeded.
	 */
	public abstract boolean changeDesignURL(int designID, String newURL);

	/**
	 * Changes the location of a <code>ModeledDesign</code>
	 * @param designID The ID of the <code>ModeledDesign</code> to change.
	 * @param rotY The new rotation of the object about the Y-Xxis.
	 * @param newLocation The new location in the form of a <code>UTMCoordinate</code>
	 * @return Whether or not the location was changed.
	 * @see UTMCoordinate
	 */
	public abstract boolean changeModeledDesignLocation(int designID, float rotY,
			UTMCoordinate newLocation);

	/**
	 * Favorites a design on the server on behalf of the user
	 * @param designID
	 * @return 0 for success, -1 for SQL error, -2 if its already faved, -3 for failed authentication, or -4 for a network error
	 */
	public abstract int faveDesign(int designID);

	/**
	 * Adds a comment in reference to a specific design.
	 * @param comment The <code>Comment</code> to add.
	 * @return Whether or not the comment was added.
	 * @see Comment
	 */
	public abstract boolean addComment(Comment comment);

	/**
	 * Deletes a comment on the server.  This can only be done by the user who posted
	 * the comment.  This needs to expand on the server-side to include moderator rights.
	 * @param commentID The ID of the comment to delete.
	 * @return Whether or not the comment was deleted.
	 */
	public abstract boolean deleteComment(int commentID);

	/**
	 * Adds a new wormhole on the server.
	 * @param location The location for this wormhole
	 * @param name The name of this wormhole
	 * @param cityID The ID of the city in which this wormhole is located
	 * @return The ID of the new wormhole or a server error code.
	 */
	public abstract int addWormhole(ILocation location, String name, int cityID);

	public abstract int deleteWormhole(int wormholeID);

	public abstract int editWormholeName(int wormholeID, String newName);

	public abstract int editWormholeLocation(int wormholeID,
			UTMCoordinate newLocation);

}