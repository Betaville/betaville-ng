package edu.poly.bxmc.betaville.jme.map;

/**<code>DecimalDegreeConverter</code> contains utility methods
 * for converting from Degree Minute Second (DMS) to Decimal
 * Degrees (DD)
 * @author Skye Book
 *
 */
public class DecimalDegreeConverter {
	public static enum Hemisphere{NORTH, SOUTH, EAST, WEST};
	
	/**
	 * Converts a Degree Minute Second (DMS) into a Decimal Degree.
	 * @param degree
	 * @param minute
	 * @param second
	 * @return
	 */
	public static double dmsToDD(int degree, double minute, double second){
		// Decimal degrees = whole number of degrees, plus minutes divided by 60, plus seconds divided by 3600
		if(degree>0){
			return degree+((minute/60)+(second/3600));
		}
		else{
			return degree-((minute/60)+(second/3600));
		}
	}
	
	/**
	 * Converts Decimal Degrees into a Degree Minute Second (DMS) format.
	 * @param ddValue
	 * @return float array with a capacity of 3 ordered: degrees, minutes, seconds
	 */
	public static float[] ddToDMS(double ddValue){
		int degrees = Integer.parseInt(Double.toString(ddValue).substring(0, Double.toString(ddValue).indexOf(".")));
		Double decimalValue = Double.parseDouble(Double.toString(ddValue).substring(Double.toString(ddValue).lastIndexOf("."), (Double.toString(ddValue).length())));
		Double minutesSeconds = decimalValue*60;
		int minutes = Integer.parseInt(Double.toString(minutesSeconds).substring(0, Double.toString(minutesSeconds).indexOf(".")));
		
		decimalValue = Double.parseDouble(Double.toString(minutesSeconds).substring(Double.toString(minutesSeconds).lastIndexOf("."), (Double.toString(minutesSeconds).length())));
		Double seconds = decimalValue*60;
		return new float[]{degrees,minutes, seconds.floatValue()};
	}
	
	/**
	 * NOT FINISHED YET
	 * @param degree
	 * @param minute
	 * @param second
	 * @param quadrant
	 * @return
	 */
	public static double DegToDec(int degree, double minute, double second, Hemisphere quadrant){
		// Decimal degrees = whole number of degrees, plus minutes divided by 60, plus seconds divided by 3600
		switch(quadrant){
		case NORTH:
			break;
		case SOUTH:
			break;
		case EAST:
			break;
		case WEST:
			break;
		}
		return (degree+(minute/60)+(second/3600));
	}
}