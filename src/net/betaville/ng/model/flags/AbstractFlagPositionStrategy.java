package net.betaville.ng.model.flags;

import edu.poly.bxmc.betaville.model.Design;

/**
 * @author Skye Book
 *
 */
public abstract class AbstractFlagPositionStrategy{
	
	/**
	 * Finds the appropriate height where a flag should be placed
	 * @param base The object that this flag should be placed over
	 * @return The height at which to place a flag
	 */
	public abstract int findHeight(Design base);
}