package net.betaville.ng.taskqueue;

/**
 * Can be provided to BetavilleUpdater with actions to be performed at intervals
 * @author Skye Book
 *
 */
public interface Updater {
	
	public boolean isUpdateRequired();
	public void doUpdate();
}
