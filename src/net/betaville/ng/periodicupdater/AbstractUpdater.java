package net.betaville.ng.periodicupdater;


/**
 * @author Skye Book
 *
 */
public abstract class AbstractUpdater implements Updater{
	private long updateInterval;
	
	private long lastUpdate;
	
	
	public AbstractUpdater(long updateInterval) {
		this.updateInterval=updateInterval;
	}

	public long getUpdateInterval() {
		return updateInterval;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * Implementable method to give other objects the
	 * opportunity to see if any systems are in the
	 * middle of an update
	 * @return true if there is an update in progress,
	 * false if there is no update currently occuring
	 */
	public abstract boolean isInUpdate();
}
