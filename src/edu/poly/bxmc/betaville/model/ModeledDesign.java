package edu.poly.bxmc.betaville.model;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;

/**
 * A ModeledDesign represents a fully three-dimensional proposal
 * or otherwise existing component of the city.
 * @author Skye Book
 *
 */
public class ModeledDesign extends Design{
	private static final long serialVersionUID = 1L;
	private float dimensionX;
	private float dimensionY;
	private float dimensionZ;
	private float rotX=0;
	private float rotY=0;
	private float rotZ=0;
	private boolean textured;
	
	public ModeledDesign(){}
	
	public ModeledDesign(String name, UTMCoordinate coordinate, String address, 
			int cityID, String user, String description, String filepath, String url, boolean isVisible, float rotX, float rotY, float rotZ, boolean textured){
		super(name, coordinate, address, cityID, user, description, filepath, url, isVisible);
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
		this.textured=textured;
	}
	
	public ModeledDesign(String name, UTMCoordinate coordinate, String address, 
			String user, String description, String filepath, String url, boolean isVisible, float rotX, float rotY, float rotZ, boolean textured){
		super(name, coordinate, address, user, description, filepath, url, isVisible);
		this.rotX=rotX;
		this.rotY=rotY;
		this.rotZ=rotZ;
		this.textured=textured;
	}
	
	/**
	 * @return Length in meters
	 */
	public float getLength(){
		return dimensionX;
	}
	
	/**
	 * @return Width in meters
	 */
	public float getWidth(){
		return dimensionZ;
	}
	
	/**
	 * @return Height in meters
	 */
	public float getHeight(){
		return dimensionY;
	}
	
	public float getRotationX() {
		return rotX;
	}

	public void setRotationX(float rotation) {
		this.rotX = rotation;
	}
	
	public float getRotationY() {
		return rotY;
	}
	
	public void setRotationY(float rotation) {
		this.rotY = rotation;
	}
	
	public float getRotationZ() {
		return rotZ;
	}
	
	public void setRotationZ(float rotation) {
		this.rotZ = rotation;
	}

	public boolean isTextured() {
		return textured;
	}

	public void setTextured(boolean textured) {
		this.textured = textured;
	}
	
	/**
	 * Performs a shallow copy of {@link ModeledDesign} data
	 * into an object
	 * @param design
	 * @see Design#load(Design)
	 */
	public void load(ModeledDesign design){
		super.load(design);
		dimensionX = design.dimensionX;
		dimensionY = design.dimensionY;
		dimensionZ = design.dimensionZ;
		rotX = design.rotX;
		rotY = design.rotY;
		rotZ = design.rotZ;
		textured = design.textured;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Float.floatToIntBits(dimensionX);
		result = prime * result + Float.floatToIntBits(dimensionY);
		result = prime * result + Float.floatToIntBits(dimensionZ);
		result = prime * result + Float.floatToIntBits(rotX);
		result = prime * result + Float.floatToIntBits(rotY);
		result = prime * result + Float.floatToIntBits(rotZ);
		result = prime * result + (textured ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ModeledDesign other = (ModeledDesign) obj;
		if (Float.floatToIntBits(dimensionX) != Float
				.floatToIntBits(other.dimensionX))
			return false;
		if (Float.floatToIntBits(dimensionY) != Float
				.floatToIntBits(other.dimensionY))
			return false;
		if (Float.floatToIntBits(dimensionZ) != Float
				.floatToIntBits(other.dimensionZ))
			return false;
		if (Float.floatToIntBits(rotX) != Float.floatToIntBits(other.rotX))
			return false;
		if (Float.floatToIntBits(rotY) != Float.floatToIntBits(other.rotY))
			return false;
		if (Float.floatToIntBits(rotZ) != Float.floatToIntBits(other.rotZ))
			return false;
		if (textured != other.textured)
			return false;
		return true;
	}
	
}