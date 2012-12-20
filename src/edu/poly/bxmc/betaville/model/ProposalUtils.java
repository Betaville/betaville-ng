package edu.poly.bxmc.betaville.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Skye Book
 *
 */
public class ProposalUtils {
	
	/**
	 * Creates a List from a string array of designID's.
	 * @param removables
	 * @return A list containing the values in the array or an empty list of the array format was invalid
	 * or empty
	 */
	public static List<Integer> interpretRemovablesString(String removables){
		List<Integer> list = new ArrayList<Integer>();
		
		// return an empty list if we've received a null value
		if(removables==null) return list;
		
		// For as long as the list is valid, continue reading the values
		while(validateRemovableList(removables)){
			list.add(Integer.parseInt(new String(removables.substring(0, removables.indexOf(";")))));
			removables = new String(removables.substring(removables.indexOf(";")+1, removables.length()));
		}
		return list;
	}
	
	public static boolean validateRemovableList(String removables){
		return removables.matches("([0-9]+;)+");
	}
}
