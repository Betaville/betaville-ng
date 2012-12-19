package edu.poly.bxmc.betaville.model;

import java.io.Serializable;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;



/**
 * Simple data encapsulation for a wormhole's information
 * @author Skye Book
 *
 */
public class Wormhole implements Serializable{
	private static final long serialVersionUID = 3420L;
	
	private String name;
	private UTMCoordinate location;
	private int cityID;
	
	/**
	 * 
	 * @param location
	 * @param name
	 * @param cityID
	 */
	public Wormhole(UTMCoordinate location, String name,  int cityID){
		this.location=location;
		this.name=name;
		this.cityID=cityID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the location
	 */
	public UTMCoordinate getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(UTMCoordinate location) {
		this.location = location;
	}

	/**
	 * @return the cityID
	 */
	public int getCityID() {
		return cityID;
	}
	
	
}
