package net.betaville.ng.model.flags;

import java.util.List;

import edu.poly.bxmc.betaville.model.Design;


/**
 * @author Skye Book
 *
 */
public interface IFlagSelectionListener {
	/**
	 * Fired when a flag is selected from the scene
	 * @param rootDesigns - The designs that the proposals here are mounted to
	 */
	public void flagSelected(List<Design> rootDesigns);
	
	/**
	 * Called when a flag has not been selected
	 */
	public void flagDeselected();
}
