package net.betaville.ng.model.flags;

import edu.poly.bxmc.betaville.model.Design;

/**
 * @author Skye Book
 *
 */
public class FixedFlagPositionStrategy extends AbstractFlagPositionStrategy {

	/*
	 * @see edu.poly.bxmc.betaville.flags.AbstractFlagPositionStrategy#findHeight(edu.poly.bxmc.betaville.model.Design)
	 */
	@Override
	public int findHeight(Design base) {
		// place each flag at 50 meters
		return 50;
	}

}
