package net.betaville.ng.xml;

import java.io.File;
import java.io.IOException;

import net.betaville.ng.util.DriveFinder;

import org.apache.log4j.Logger;


/**
 * Lists all of Betaville's default preferences and sets them
 * up if they are not currently available.
 * @author Skye Book
 *
 */
public class UpdatedPreferenceWriter {
	private static Logger logger = Logger.getLogger(UpdatedPreferenceWriter.class);
	

	/**
	 * @throws IOException 
	 * 
	 */
	public static void writeDefaultPreferences() throws IOException {
		logger.info("Writing preferences");
		if(System.getProperty("betaville.display.fullscreen")==null) System.setProperty("betaville.display.fullscreen", "false");
		if(System.getProperty("betaville.display.resolution")==null) System.setProperty("betaville.display.resolution", "EMPTY");
		if(System.getProperty("betaville.display.textured")==null) System.setProperty("betaville.display.textured", "true");
		if(System.getProperty("betaville.display.model.loadonstart")==null) System.setProperty("betaville.display.model.loadonstart", "true");
		if(System.getProperty("betaville.display.fog.enabled")==null) System.setProperty("betaville.display.fog.enabled", "true");
		if(System.getProperty("betaville.display.terrain.usegenerated")==null) System.setProperty("betaville.display.terrain.usegenerated", "false");
		if(System.getProperty("betaville.sound.volume.master")==null) System.setProperty("betaville.sound.volume.master", "1.0");
		if(System.getProperty("betaville.cache.location")==null) System.setProperty("betaville.cache.location", new File(DriveFinder.getHomeDir().toString()+"/.betaville/cache/").toURI().toURL().toString());
		if(System.getProperty("betaville.cache3.location")==null) System.setProperty("betaville.cache3.location", new File(DriveFinder.getHomeDir().toString()+"/.betaville/cache3/").toURI().toURL().toString());
		if(System.getProperty("betaville.cache.size")==null) System.setProperty("betaville.cache.size", "300");
		if(System.getProperty("betaville.server")==null)System.setProperty("betaville.server", "master.betaville.net");
		if(System.getProperty("betaville.startup.showsettings")==null) System.setProperty("betaville.startup.showsettings", "true");
		if(System.getProperty("betaville.startup.city")==null) System.setProperty("betaville.startup.city", "2");
		if(System.getProperty("betaville.updater.enabled")==null) System.setProperty("betaville.updater.enabled", "true");
		if(System.getProperty("betaville.updater.base")==null) System.setProperty("betaville.updater.base", "true");
		if(System.getProperty("betaville.updater.comments")==null) System.setProperty("betaville.updater.comments", "true");
		if(System.getProperty("betaville.ui.pyramids.offcolor")==null) System.setProperty("betaville.ui.pyramids.offcolor", "RGB 0 0 255");
		if(System.getProperty("betaville.ui.pyramids.oncolor")==null) System.setProperty("betaville.ui.pyramids.oncolor", "RGB 0 255 0");
		if(System.getProperty("betaville.net.usessl")==null) System.setProperty("betaville.net.usessl", "false");
		if(System.getProperty("betaville.kiosk.enabled")==null) System.setProperty("betaville.kiosk.enabled", "false");
		if(System.getProperty("betaville.kiosk.password")==null) System.setProperty("betaville.kiosk.password", ""); // Uses an SHA1 hash
		if(System.getProperty("betaville.kiosk.requirepass")==null) System.setProperty("betaville.kiosk.requirepass", "false");
		if(System.getProperty("betaville.kiosk.refresh")==null) System.setProperty("betaville.kiosk.refresh", "0");
		if(System.getProperty("betaville.guestmode.enabled")==null) System.setProperty("betaville.guestmode.enabled", "false");
		if(System.getProperty("betaville.license.content.agree")==null) System.setProperty("betaville.license.content.agree", "false");
		
		PreferenceWriter pr = new PreferenceWriter();
		pr.writeData();
	}

}