package net.betaville.ng;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Enables the loading of resources from a Jar file
 * when using Web Start or from a folder in the
 * Betaville project when being run from an IDE
 * @author Skye Book
 */
public class ResourceLoader {
	private static Logger logger = Logger.getLogger(ResourceLoader.class);
	
	public static URL loadResource(String resource){
		URL res = ResourceLoader.class.getResource(resource);
		if(res==null){
			try {
				res = new File(resource.substring(resource.indexOf("/")+1)).toURI().toURL();
			} catch (MalformedURLException e) {
				// This shouldn't be happening since its coming straight from Java
				logger.error("MalformedURLException When Loading Resource", e);
			}
		}
		return res;
	}
}
