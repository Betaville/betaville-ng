package edu.poly.bxmc.betaville.jme.map;

import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * <code>Rotator</code> provides common Quaternions to perform
 * rotations as well as methods to find less popular Quaternion
 * calculations.
 * 
 * NOTES:<br/>
 * Yaw = Rotations about Y
 * Pitch = Rotations about Z
 * Roll = Rotations about X
 * @author Skye Book
 *
 */
public class Rotator {
	public static final Quaternion ROLL045  = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,0,1));
	public static final Quaternion ROLL090  = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,0,1));
	public static final Quaternion ROLL180  = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(0,0,1));
	public static final Quaternion ROLL270  = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(0,0,1));
	public static final Quaternion YAW045n  = new Quaternion().fromAngleAxis(- FastMath.PI/4, new Vector3f(0,1,0));
	public static final Quaternion YAW045   = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(0,1,0));
	public static final Quaternion YAW090   = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(0,1,0));
	public static final Quaternion YAW180   = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(0,1,0));
	public static final Quaternion YAW270   = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(0,1,0));
	public static final Quaternion PITCH045 = new Quaternion().fromAngleAxis(FastMath.PI/4,   new Vector3f(1,0,0));
	public static final Quaternion PITCH090 = new Quaternion().fromAngleAxis(FastMath.PI/2,   new Vector3f(1,0,0));
	public static final Quaternion PITCH180 = new Quaternion().fromAngleAxis(FastMath.PI  ,   new Vector3f(1,0,0));
	public static final Quaternion PITCH270 = new Quaternion().fromAngleAxis(FastMath.PI*3/2, new Vector3f(1,0,0));
	public static final Quaternion UPRIGHT  = new Quaternion().fromAngleAxis(- FastMath.PI/2, new Vector3f(1,0,0));

	/**
	 * Creates a <code>Quaternion</code> rotation from a supplied
	 * number of degrees around the X axis
	 * @param degrees to be converted to a Quaternion rotation.
	 * @return Quaternion calculation of the requested angle.
	 */
	public static Quaternion angleX(float degrees){
		return new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * degrees,  Vector3f.UNIT_X);
	}
	
	/**
	 * Creates a <code>Quaternion</code> rotation from a supplied
	 * number of degrees around the Y axis
	 * @param degrees to be converted to a Quaternion rotation.
	 * @return Quaternion calculation of the requested angle.
	 */
	public static Quaternion angleY(float degrees){
		return new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * degrees,  Vector3f.UNIT_Y);
	}
	
	/**
	 * Creates a <code>Quaternion</code> rotation from a supplied
	 * number of degrees around the Z axis
	 * @param degrees to be converted to a Quaternion rotation.
	 * @return Quaternion calculation of the requested angle.
	 */
	public static Quaternion angleZ(float degrees){
		return new Quaternion().fromAngleAxis(FastMath.DEG_TO_RAD * degrees,  Vector3f.UNIT_Z);
	}
	
	public static Quaternion fromThreeAngles(float x, float y, float z){
		return new Quaternion().fromAngles(FastMath.DEG_TO_RAD * x, FastMath.DEG_TO_RAD * y, FastMath.DEG_TO_RAD * z);
	}
}
