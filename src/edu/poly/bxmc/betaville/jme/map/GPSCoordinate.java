package edu.poly.bxmc.betaville.jme.map;

import java.io.Serializable;


/**
 * Class <GPSCoordinate> - GPS coordinate: Encapsulation of a single gps coordinate.
 *
 * @author Jonas Panten
 * @author Skye Book
 */
public class GPSCoordinate implements ILocation, Serializable{
	private static final long serialVersionUID = 1L;

	/**
	 * Attribute <latitude> - Latitude of the coordinate
	 */
	private double latitude;
	
	/**
	 * Attribute <longitude> - Longitude of the coordinate
	 */
	private double longitude;
	
	/**
	 * Attribute <altitude> - Altitude of the coordinate
	 */
	private double altitude;

	/**
	 * Creates a Coordinate by taking in DD values
	 *
	 * @param altitude Altitude of the coordinate
	 * @param latitude Latitude of the coordinate
	 * @param longitude Longitude of the coordinate
	 */
	public GPSCoordinate(double altitude, double latitude, double longitude) {
		super();
		this.altitude = altitude;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * Creates a Coordinate by converting a DMS number into a DD number.
	 * @param altitude Altitude of the coordinate
	 * @param latDegrees value for Latitude degrees
	 * @param latMinutes value for Latitude minutes
	 * @param latSeconds value for Latitude seconds
	 * @param lonDegrees value for Longitude degrees
	 * @param lonMinutes value for Longitude minutes
	 * @param lonSeconds value for Longitude seconds
	 */
	public GPSCoordinate(double altitude, int latDegrees, int latMinutes, double latSeconds,
			int lonDegrees, int lonMinutes, double lonSeconds){
		this(altitude,DecimalDegreeConverter.dmsToDD(latDegrees, latMinutes, latSeconds),
				DecimalDegreeConverter.dmsToDD(lonDegrees, lonMinutes, lonSeconds));		
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#getLatitude()
	 */
	public double getLatitude() {
		return latitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#setLatitude(double)
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#getLongitude()
	 */
	public double getLongitude() {
		return longitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#setLongitude(double)
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#getAltitude()
	 */
	public double getAltitude() {
		return altitude;
	}
	
	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betavile.model.IGPSCoordinate#setAltitude(double)
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public UTMCoordinate getUTM() {
		// TODO: Repair getUTM()
		//return MapManager.latLonToUTM(this);
		throw new RuntimeException("getUTM() stub reached");
	}
	
	public String toString(){
		return getClass().getName()+":  Latitude: " + latitude + "  Longitude: " + longitude + "  Altitude: " + altitude;
	}

	public GPSCoordinate getGPS() {
		return this;
	}
	
	@Override
	public GPSCoordinate clone(){
		// TODO Auto-generated method stub
		return new GPSCoordinate(altitude, latitude, longitude);
	}
}
