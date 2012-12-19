package net.betaville.ng.util;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Math functions to make life [a bit] easier.
 * @author Skye Book
 *
 */
public class Math {
	
	private static final MathContext half = new MathContext(2);

	/**
	 * Splits a floating point number into two integers, its whole
	 * and its fraction
	 * @param number
	 * @return An array with a size of two.  The whole value is located
	 * at position 0 and the fractional value is located at position 1
	 */
	public static int[] splitFraction(float number){
		int whole = (int)number;
		float part = (number-whole);
		if(part>.5f){
			part = (int)(part*100)+1;
		}
		else{
			part = (int)(part*100);
		}

		return new int[]{whole, (int)part};
	}
	
	/**
	 * Splits a double-precision point number into two integers, its whole
	 * and its fraction
	 * @param number
	 * @return An array with a size of two.  The whole value is located
	 * at position 0 and the fractional value is located at position 1
	 */
	public static int[] splitFraction(double number){
		int whole = (int)number;
		BigDecimal part = new BigDecimal(100*(number-whole));
		return new int[]{whole, part.round(half).intValue()};
	}
}
