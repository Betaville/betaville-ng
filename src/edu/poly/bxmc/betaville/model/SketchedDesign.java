package edu.poly.bxmc.betaville.model;


import com.jme3.math.Vector2f;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;


/**
 * A SketchedDesign represents a 2D design meant to be seen as
 * an overlay on the ground, either as a precursor to a later
 * proposal or as a final product
 * @author Skye Book
 *
 */
public class SketchedDesign extends Design{
	private static final long serialVersionUID = 1L;
	private Vector2f dimensions;
	private char upPlane;
	private int rotation;
	
	public SketchedDesign(){}
	
	public SketchedDesign(String name, UTMCoordinate coordinate, String address,
			int cityID, String user, String description, String filepath, String url, boolean isVisible, int rotation, char upPlane){
		super(name, coordinate, address, cityID, user, description, filepath, url, isVisible);
	}

	public char getUpPlane() {
		return upPlane;
	}

	public void setUpPlane(char upPlane) {
		this.upPlane = upPlane;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getLength() {
		// TODO: What is the scale?
		float sceneScale = 200f;
		return (int)(dimensions.getX() * sceneScale);
	}
	
	public int getWidth() {
		// TODO: What is the scale?
		float sceneScale = 1;
		return (int)(dimensions.getY() * sceneScale);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((dimensions == null) ? 0 : dimensions.hashCode());
		result = prime * result + rotation;
		result = prime * result + upPlane;
		return result;
	}
	
	/**
	 * Performs a shallow copy of {@link SketchedDesign} data
	 * into an object
	 * @param design
	 * @see Design#load(Design)
	 */
	public void load(SketchedDesign design){
		super.load(design);
		dimensions = design.dimensions;
		upPlane = design.upPlane;
		rotation = design.rotation;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SketchedDesign other = (SketchedDesign) obj;
		if (dimensions == null) {
			if (other.dimensions != null)
				return false;
		} else if (!dimensions.equals(other.dimensions))
			return false;
		if (rotation != other.rotation)
			return false;
		if (upPlane != other.upPlane)
			return false;
		return true;
	}
}
