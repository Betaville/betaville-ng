package net.betaville.ng;

import java.io.File;

/**
 * A listener for being notified of changes to the cache
 * @author Skye Book
 *
 */
public interface ICacheModifiedListener {
	
	/**
	 * A file has just been added to the cache
	 * @param file The {@link File} that has been added.
	 * @param newCacheSize
	 */
	public void fileAdded(File file, long newCacheSize);
	
	/**
	 * A file has just been removed from the cache
	 * @param file The {@link File} that has been removed.  (File
	 * gets deleted before this method is called)
	 * @param newCacheSize
	 */
	public void fileRemoved(File file, long newCacheSize);

}
