package edu.poly.bxmc.betaville.jme.map;

import java.io.Serializable;

/**
 * Provides a generic coordinate abstraction.
 * @author Skye Book
 *
 */
public interface ILocation extends Serializable{
	/**
	 * Retrieves the {@link UTMCoordinate} representation of this object
	 * @return this coordinate
	 */
	public UTMCoordinate getUTM();
	
	/**
	 * Retrieves the {@link GPSCoordinate} representation of this object
	 * @return this coordinate
	 */
	public GPSCoordinate getGPS();
	
	public ILocation clone();
}
