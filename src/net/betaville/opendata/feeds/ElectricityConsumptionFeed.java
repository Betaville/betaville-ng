package net.betaville.opendata.feeds;

import java.io.BufferedReader;
import java.lang.reflect.Type;
import java.util.Vector;

import net.betaville.opendata.domain.ElectricityConsumption;
import net.betaville.opendata.exceptions.OpenDataApiException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * DOCME
 * 
 * @author Andre Koenig (andre.koenig@gmail.com)
 *
 */
public class ElectricityConsumptionFeed extends AbstractFeed {

	// DOCME
	private static final String ELECTRICITY_CONSUMPTION_API_ENDPOINT = "http://nycopendata.jit.su/feed/53?betaville=true";
	
	// DOCME
	private static volatile ElectricityConsumptionFeed instance = null;
	
	// DOCME
	private ElectricityConsumptionFeed() {}
	
	/**
	 * DOCME
	 * 
	 * @return
	 */
	public static ElectricityConsumptionFeed getInstance() {
		if (instance == null) {
			synchronized (ElectricityConsumptionFeed .class) {
				if (instance == null) {
					instance = new ElectricityConsumptionFeed();
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
	public Vector<ElectricityConsumption> findAll() throws OpenDataApiException {
		BufferedReader response = this.doGet(ELECTRICITY_CONSUMPTION_API_ENDPOINT);

		Gson gson = new Gson();

		Type collectionType = new TypeToken<Vector<ElectricityConsumption>>(){}.getType();
		Vector<ElectricityConsumption> eConsumptions = gson.fromJson(response, collectionType);

		return eConsumptions;
	}
}
