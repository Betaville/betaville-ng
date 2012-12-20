package net.betaville.opendata.feeds;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.Vector;

import net.betaville.opendata.domain.OilBoilerFacility;
import net.betaville.opendata.exceptions.OpenDataApiException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * DOCME
 * 
 * @author Andre Koenig (andre.koenig@gmail.com)
 *
 */
public class OilBoilerFeed extends AbstractFeed {

	// DOCME
	private static final String OIL_BOILER_API_ENDPOINT = "http://nycopendata.jit.su/feed/51?betaville=true";
	
	// DOCME
	private static volatile OilBoilerFeed instance = null;
	
	// DOCME
	private OilBoilerFeed() {}
	
	/**
	 * DOCME
	 * 
	 * @return
	 */
	public static OilBoilerFeed getInstance() {
		if (instance == null) {
			synchronized (OilBoilerFeed .class) {
				if (instance == null) {
					instance = new OilBoilerFeed();
				}
			}
		}
		
		return instance;
	}
	
	/**
	 * DOCME
	 *
	 * @return
	 * @throws OpenDataApiException 
	 */
	public Vector<OilBoilerFacility> findAll() throws OpenDataApiException {
		BufferedReader response = this.doGet(OIL_BOILER_API_ENDPOINT);

		Gson gson = new Gson();

		Type collectionType = new TypeToken<Vector<OilBoilerFacility>>(){}.getType();
		Vector<OilBoilerFacility> facilities = gson.fromJson(response, collectionType);

		return facilities;
	}
}
