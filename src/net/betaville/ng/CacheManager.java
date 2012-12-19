package net.betaville.ng;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import net.betaville.ng.net.NetPool;
import net.betaville.ng.net.UnprotectedManager;
import net.betaville.ng.util.OS;

import org.apache.log4j.Logger;

import edu.poly.bxmc.betaville.jme.map.UTMCoordinate;
import edu.poly.bxmc.betaville.model.Design;
import edu.poly.bxmc.betaville.net.PhysicalFileTransporter;


/**
 * Manages Betaville's cache directory.
 * @author Skye Book
 *
 */
public class CacheManager {
	private static final Logger logger = Logger.getLogger(CacheManager.class);

	private static CacheManager cm = null;

	// Holds the maximum size of the cache in MB
	private int maxSize;

	// Stores the current size of the cache in bytes
	private long currentSize;

	private HashMap<String,Long> files;

	private ArrayList<ICacheModifiedListener> cacheModifiedListeners;

	/**
	 * Private constructor to enforce singleton class.
	 */
	private CacheManager() {
		files = new HashMap<String,Long>();
		cacheModifiedListeners = new ArrayList<ICacheModifiedListener>();
		countFiles();
	}

	private void countFiles(){
		try {
			File cacheDir = new File(SettingsPreferences.getDataFolder().toURI());
			if(cacheDir.isDirectory()){
				File[] dirFiles = cacheDir.listFiles();
				for(int i=0; i<dirFiles.length; i++){
					doFileRegistration(dirFiles[i]);
				}
				logger.info("Cache has " + dirFiles.length + " files at a size of " + (currentSize/1000 )+ "MB");
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	private boolean findFile(String file){
		File target;
		try {
			target = new File(new URL(SettingsPreferences.getDataFolder()+file).toURI());
			return target.exists();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean findThumb(int designID){
		File target;
		try {
			target = new File(new URL(SettingsPreferences.getDataFolder()+"thumbnail/"+designID+".png").toURI());
			logger.debug("Looking for thumbnail in " + target.toString());
			return target.exists();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int getSizeOfFile(String fileName){
		Long value = files.get(removeExtension(fileName));
		if(value!=null){
			return (int)(value/1000);
		}
		else return 0;
	}
	
	/**
	 * Deletes all files from the cache
	 */
	public void deleteAllFiles(){
		ArrayList<String> toDelete = new ArrayList<String>();
		
		// Accumulate a list of files to delete
		for(Entry<String, Long> file : files.entrySet()){
			toDelete.add(file.getKey());
		}
		
		// delete the files
		for(String fileToDelete : toDelete){
			deleteFile(fileToDelete);
		}
	}

	/**
	 * Adds a file to those currently tracked by the cache
	 * @param fileName
	 */
	private void registerFileInCache(String fileName){
		File file = new File(SettingsPreferences.getDataFolder()+fileName);
		if(file.exists()){
			doFileRegistration(file);
			if(maxSize<currentSize){
				// Do something drastic
			}
		}
	}

	private void doFileRegistration(File file){
		if(file.isFile()){
			files.put(removeExtension(file.getName()), file.length());
			currentSize+=file.length();
			
			// notify the listeners
			for(ICacheModifiedListener listener : cacheModifiedListeners){
				listener.fileAdded(file, currentSize);
			}
		}
	}

	public void deleteFile(String fileName){
		File file = new File(SettingsPreferences.getDataFolder()+fileName);
		if(file.exists()){
			long length = file.length();
			if(file.delete()){
				files.remove(removeExtension(fileName));
				currentSize-=length;
				
				// notify the listeners
				for(ICacheModifiedListener listener : cacheModifiedListeners){
					listener.fileRemoved(file, currentSize);
				}
			}
		}
		else{
			logger.warn("No File to delete");
		}
	}

	/**
	 * Removes file from the cache that are not in the same
	 * UTM zone as the given coordinate.
	 * @param currentLocation The coordinate from which to
	 * determine which UTM zone's files <em>not</em> to delete.
	 */
	public void maintain(UTMCoordinate currentLocation){
		Iterator<Design> it = SettingsPreferences.getCity().getDesigns().iterator();
		while(it.hasNext()){
			Design design = it.next();
			if(design.getCoordinate().getLatZone()!=currentLocation.getLatZone()
					|| design.getCoordinate().getLonZone()!=currentLocation.getLonZone()){
				deleteFile(design.getFilepath());
			}
		}
	}

	/**
	 * Strips a filename, and the final dot, from a String
	 * @param name The filename from which to remove the extension
	 * @return The shortened filename
	 */
	private String removeExtension(String name){
		return name.substring(0, name.lastIndexOf("."));
	}

	private boolean doRequestFile(int designID, String filename, UnprotectedManager manager, boolean keepAlive){
		if(!findFile(filename)){
			PhysicalFileTransporter pft = manager.requestFile(designID);
			if(!keepAlive){
				manager.close();
			}
			if(pft!=null){
				try {
					pft.writeToFileSystem(new File(new URL(SettingsPreferences.getDataFolder()+filename).toURI()));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				registerFileInCache(filename);
				return true;
			}
			else return false;
		}
		else{
			return true;
		}
	}

	/**
	 * Request a file from the server using an externally
	 * maintained client manager.
	 * @param designID
	 * @param filename
	 * @param manager
	 * @return
	 */
	public boolean requestFile(int designID, String filename, UnprotectedManager manager){
		if(findFile(filename)) return true;
		else return doRequestFile(designID, filename, manager, true);
	}

	/**
	 * Request a file using a ClientManager created from within this method.
	 * @param designID
	 * @param filename
	 * @return
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public boolean requestFile(int designID, String filename) throws UnknownHostException, IOException{
		if(findFile(filename)) return true;
		else return doRequestFile(designID, filename, NetPool.getPool().getConnection(), true);
	}

	/**
	 * Requests the thumbnail for the specified design id
	 * @param designID
	 * @return
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public boolean requestThumbnail(int designID) throws UnknownHostException, IOException{
		if(!findThumb(designID)){
			logger.debug("Requesting a thumbnail for design "+designID);
			PhysicalFileTransporter pft = NetPool.getPool().getConnection().requestThumbnail(designID);
			if(pft!=null){
				try {
					File thumbFolder = new File(new URL(SettingsPreferences.getDataFolder()+"thumbnail/").toURI());
					File thumbFile = new File(new URL(SettingsPreferences.getDataFolder()+"thumbnail/"+designID+".png").toURI());
					if(thumbFolder.mkdirs()) logger.info("Thumbnail folder created");
					pft.writeToFileSystem(thumbFile);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				return true;
			}
			else{
				logger.debug("thumbnail PFT is null");
				return false;
			}
		}
		else{
			return true;
		}
	}

	public static URL getCachedThumbnailURL(int designID){
		try {
			URL url = new URL(SettingsPreferences.getDataFolder().toString().substring(0, SettingsPreferences.getDataFolder().toString().length()-1)+"thumbnail/"+designID+".png");
			if(OS.isWindows()){
				url = new URL(SettingsPreferences.getDataFolder()+"thumbnail/"+designID+".png");
			}
			logger.info("Thumbnail URL: " + url.toString());
			return url;
		} catch (MalformedURLException e) {
			logger.error("A bad URL was created when generating the cached thumbnail URL", e);
			return null;
		}
	}

	/**
	 * Adds a modification listener to this cache
	 * @param listener The {@link ICacheModifiedListener} to add
	 */
	public void addCacheModifiedListener(ICacheModifiedListener listener){
		cacheModifiedListeners.add(listener);
	}

	/**
	 * Removes a modification listener from this cache
	 * @param listener The {@link ICacheModifiedListener} to remove
	 */
	public void removeCacheModifiedListener(ICacheModifiedListener listener){
		cacheModifiedListeners.remove(listener);
	}
	
	/**
	 * Gets the number of files saved in the cache
	 * @return The number of files saved in the cache
	 */
	public int getNumberOfFiles(){
		return files.size();
	}
	
	/**
	 * Gets the total file size of the cache
	 * @return The size of all of the cached files in bytes
	 */
	public long getSizeOfCache(){
		return currentSize;
	}

	/**
	 * @return The static instance of the {@link CacheManager}
	 */
	public synchronized static CacheManager getCacheManager(){
		if(cm!=null){
			return cm;
		}
		else{
			cm = new CacheManager();
			return cm;
		}
	}
}