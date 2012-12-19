package edu.poly.bxmc.betaville.jme.map;

import java.io.Serializable;

import net.betaville.ng.util.Math;



/**
 * Provides a container for data using the
 * UTM projection system.  Raw values are
 * provided with data such as false eastings
 * left intact.
 * @author Skye Book
 */
public class UTMCoordinate implements ILocation, Serializable{
	private static final long serialVersionUID = 1L;
	private int easting;
	private int northing;
	// Initialize these to zero for legacy compatibility
	private short eastingCentimeters=0;
	private short northingCentimeters=0;
	private int lonZone;
	private char latZone;
	private float altitude;

	/**
	 * 
	 * @param easting
	 * @param northing
	 * @param lonZone
	 * @param latZone
	 * @param altitude
	 */
	public UTMCoordinate(int easting, int northing, int lonZone, char latZone, float altitude) {
		this(easting, northing, (short)0, (short)0, lonZone, latZone, altitude);
	}
	
	/**
	 * 
	 * @param easting
	 * @param northing
	 * @param eastingCM
	 * @param northingCM
	 * @param lonZone
	 * @param latZone
	 * @param altitude
	 */
	public UTMCoordinate(int easting, int northing, short eastingCM, short northingCM, int lonZone, char latZone, float altitude) {
		this.easting=easting;
		this.northing=northing;
		this.eastingCentimeters=eastingCM;
		this.northingCentimeters=northingCM;
		this.lonZone=lonZone;
		this.latZone=latZone;
		this.altitude=altitude;
	}
	
	/**
	 * Moves the UTM coordinate in meters.  Calculations are done internally, the coordinate returned is a reference to this object
	 * @param eastingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param northingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param altitudeDelta The amount of change in meters (can be positive or negative)
	 * @return this object (for chaining purposes)
	 */
	public UTMCoordinate move(float eastingDeltaMeters, float northingDeltaMeters, float altitudeDelta){
		int[] northing = Math.splitFraction(northingDeltaMeters);
		int[] easting = Math.splitFraction(eastingDeltaMeters);
		
		System.out.println("Moving Northing: " + northing[0]+"dot"+northing[1]);
		System.out.println("Moving Easting: " + easting[0]+"dot"+easting[1]);
		
		//int[] altitude = Math.splitFraction(altitudeDelta);
		return move(easting[0], northing[0], easting[1], northing[1], altitudeDelta);
	}
	
	/**
	 * Moves the UTM coordinate in meters.  Calculations are done internally, the coordinate returned is a reference to this object
	 * @param eastingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param northingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param altitudeDelta The amount of change in meters (can be positive or negative)
	 * @return this object (for chaining purposes)
	 */
	public UTMCoordinate move(int eastingDeltaMeters, int northingDeltaMeters, float altitudeDelta){
		return move(eastingDeltaMeters, northingDeltaMeters, 0, 0, altitudeDelta);
	}
	
	/**
	 * Moves the UTM coordinate in meters.  Calculations are done internally, the coordinate returned is a reference to this object
	 * @param eastingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param northingDeltaMeters The amount of change in meters (can be positive or negative)
	 * @param eastingDeltaCentimeters The amount of change in centimeters (can be positive or negative)
	 * @param northingDeltaCentimeters The amount of change in centimeters (can be positive or negative)
	 * @param altitudeDelta The amount of change in meters (can be positive or negative)
	 * @return this object (for chaining purposes)
	 */
	public UTMCoordinate move(int eastingDeltaMeters, int northingDeltaMeters, int eastingDeltaCentimeters, int northingDeltaCentimeters, float altitudeDelta){
		
		// go to floats for current data
		double currentEasting = (double)easting+((double) eastingCentimeters/100d);
		double currentNorthing = (double)northing+((double) northingCentimeters/100d);
		
		// go to floats to changes
		double cmEastingChange = ((double) eastingDeltaCentimeters/100d);
		double cmNorthingChange = ((double) northingDeltaCentimeters/100d);
		
		// add the whole meters
		currentEasting=currentEasting+eastingDeltaMeters;
		currentNorthing=currentNorthing+northingDeltaMeters;
		
		// add the centimeters
		currentEasting=currentEasting+cmEastingChange;
		currentNorthing=currentNorthing+cmNorthingChange;
		
		/* Return from whence you came
		 * ^ Arrested Development quote, if you didn't recognize this line
		 * stop immediately and go watch all three seasons of the series. 
		 */
		int[] eastingParts = Math.splitFraction(currentEasting);
		int[] northingParts = Math.splitFraction(currentNorthing);
		
		easting=eastingParts[0];
		eastingCentimeters=(short)eastingParts[1];
		northing=northingParts[0];
		northingCentimeters=(short)northingParts[1];
		
		// TODO: Crossing zone borders during coordinate translation needs to be addressed
		
		// deal with altitude
		altitude+=altitudeDelta;
		return this;
	}

	public int getEasting() {
		return easting;
	}

	public int getNorthing() {
		return northing;
	}
	
	public short getEastingCentimeters() {
		return eastingCentimeters;
	}

	public short getNorthingCentimeters() {
		return northingCentimeters;
	}

	public int getLonZone() {
		return lonZone;
	}

	public char getLatZone() {
		return latZone;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UTMCoordinate other = (UTMCoordinate) obj;
		if (Float.floatToIntBits(altitude) != Float
				.floatToIntBits(other.altitude))
			return false;
		if (easting != other.easting)
			return false;
		if (eastingCentimeters != other.eastingCentimeters)
			return false;
		if (latZone != other.latZone)
			return false;
		if (lonZone != other.lonZone)
			return false;
		if (northing != other.northing)
			return false;
		if (northingCentimeters != other.northingCentimeters)
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(altitude);
		result = prime * result + easting;
		result = prime * result + eastingCentimeters;
		result = prime * result + latZone;
		result = prime * result + lonZone;
		result = prime * result + northing;
		result = prime * result + northingCentimeters;
		return result;
	}
	
	public String toString(){
		// sends the UTM coordinate out in a format friendly to the IBM converter
		return lonZone+ " " + latZone + " " + easting + "." + eastingCentimeters +  " " + northing + "." + northingCentimeters;
		
	}
	
	/*
	 * @see edu.poly.bxmc.betaville.jme.map.ILocation#getGPS()
	 */
	public GPSCoordinate getGPS(){
		// TODO: Repair getGPS()
		//return MapManager.utmToLatLon(this);
		throw new RuntimeException("getGPS() stub reached");
	}
	
	public UTMCoordinate clone(){
		return new UTMCoordinate(easting, northing, lonZone, latZone, altitude);
		
	}

	/*
	 * @see edu.poly.bxmc.betaville.jme.map.ILocation#getUTM()
	 */
	public UTMCoordinate getUTM() {
		return this;
	}
}
