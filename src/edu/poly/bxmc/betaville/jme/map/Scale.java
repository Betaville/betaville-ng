package edu.poly.bxmc.betaville.jme.map;

/**
 * @author Skye Book
 *
 */
public class Scale {
	
	public static final float feetToMeters = 0.3048f;
	public static final float metersToFeet = 1 / feetToMeters;
	
	/**
	 * Converts from real world meters to scaled meters in Betaville
	 * @param meters distance, in meters, to scale to Betaville
	 * @return scaled number of meters
	 */
	public static float fromMeter(float meters){
		return meters / MapManager.sceneScale;
	}
	
	/**
	 * Converts from scaled Betaville meters to real world distances
	 * @param floatUnits distance in the 3D scene
	 * @return number of real world meters
	 */
	public static float toMeter(float floatUnits){
		return floatUnits * MapManager.sceneScale;
	}
	
	/**
	 * Converts from real world feet to scaled meters in Betaville
	 * @param feet distance, in feet, to scale to Betaville
	 * @return scaled number of feet
	 */
	public static float fromFoot(float feet){
		return feet * feetToMeters / MapManager.sceneScale;
	}
	
	/**
	 * Converts from scaled Betaville meters to real world distances
	 * @param floatUnits distance in the 3D scene
	 * @return number of real world feet
	 */
	public static float toFoot(float floatUnits){
		return (floatUnits * MapManager.sceneScale) * metersToFeet;
	}
}
