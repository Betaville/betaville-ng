package edu.poly.bxmc.betaville.model;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;

/**
 * Signifies an empty place holder used when proposals
 * are to be created on empty space (or otherwise without
 * any connection/link to the other designs in the area).
 * @author Skye Book
 *
 */
public class EmptyDesign extends Design {
	private static final long serialVersionUID = 1L;
	private int length;
	private int width;
	
	public EmptyDesign(){}

	/**
	 * @param name
	 * @param coordinate
	 * @param address
	 * @param user
	 * @param description
	 * @param filepath
	 * @param url
	 * @param isVisible
	 */
	public EmptyDesign(UTMCoordinate coordinate, String address,
			String user, String description, String url,
			boolean isVisible, int length, int width) {
		super("EMPTY_DESIGN", coordinate, address, user, description, "EMPTY", url,
				isVisible);
		this.length=length;
		this.width=width;
	}

	/**
	 * @param name
	 * @param coordinate
	 * @param address
	 * @param cityID
	 * @param user
	 * @param description
	 * @param filepath
	 * @param url
	 * @param isVisible
	 */
	public EmptyDesign(UTMCoordinate coordinate, String address,
			int cityID, String user, String description,
			String url, boolean isVisible, int length, int width) {
		super("EMPTY_DESIGN", coordinate, address, cityID, user, description, "EMPTY",
				url, isVisible);
		this.length=length;
		this.width=width;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Performs a shallow copy of {@link EmptyDesign} data
	 * into an object
	 * @param design
	 * @see Design#load(Design)
	 */
	public void load(EmptyDesign design){
		super.load(design);
		length = design.length;
		width = design.width;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + length;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmptyDesign other = (EmptyDesign) obj;
		if (length != other.length)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
