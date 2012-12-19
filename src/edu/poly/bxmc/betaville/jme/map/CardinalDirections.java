package edu.poly.bxmc.betaville.jme.map;

import com.jme3.math.Vector3f;

/**
 * @author Skye Book
 *
 */
public class CardinalDirections {
	public static final Vector3f NORTH = new Vector3f(1,0,0);
	public static final Vector3f SOUTH = new Vector3f(-1,0,0);
	public static final Vector3f EAST = new Vector3f(0,0,1);
	public static final Vector3f WEST = new Vector3f(0,0,-1);
	
	
	public static final Vector3f NW = new Vector3f(.75f,0,-.75f);
	public static final Vector3f NE = new Vector3f(.75f,0,.75f);
	public static final Vector3f SW = new Vector3f(-.75f,0,-.75f);
	public static final Vector3f SE = new Vector3f(-.75f,0,.75f);
	
	/**
	 * Calculates a vector's direction in degrees.
	 * @param direction The directional vector to interpret
	 * @return The degrees, between 0 and 360
	 */
	public static float getDirectionInDegrees(Vector3f direction){
		// Update Compass
		float x = direction.getX();
		float z = direction.getZ();
		float angle=-1;
		
		/*              x=1
		 *              |
		 *       270-360|0-90
		 *z=-1 <----------------> z=1
		 *       180-270|90-180
		 *              |
		 *              x=-1
		 */
		
		if(x>0){
			// 270 - 90
			if(z>0){
				// 0-90
				angle = 90*z;
			}
			else if(z<0){
				// 270-360
				angle = 270+(90*x);
			}
			else angle = 0;
		}
		else if(x<0){
			// 90 - 270
			if(z>0){
				// 90-180
				angle = 90+(90*z);
			}
			else if(z<0){
				// 180-270
				angle = 180+(90*x);
			}
			else angle = 180;
		}
		else{
			if(z==1) angle=90;
			else if(z==-1) angle=180;
		}
		
		return angle;
	}
}
