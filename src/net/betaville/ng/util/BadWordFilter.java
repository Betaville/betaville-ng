package net.betaville.ng.util;

import java.util.ArrayList;

/**
 * @author Skye Book
 *
 */
public class BadWordFilter implements ITextFilter {
	ArrayList<String> regexFilters;

	/**
	 * 
	 */
	public BadWordFilter(){
		regexFilters = new ArrayList<String>();
		regexFilters.add("sh[Ii1!]t");
		regexFilters.add("assh[o0]le[Ssz]?");
		regexFilters.add("");
		regexFilters.add("");
		regexFilters.add("");
		regexFilters.add("");
		regexFilters.add("");
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.util.ITextFilter#isClean(java.lang.String)
	 */
	public boolean isClean(String text) {
		// TODO Auto-generated method stub
		return false;
	}

}
