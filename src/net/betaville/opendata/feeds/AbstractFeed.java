package net.betaville.opendata.feeds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * DOCME
 * 
 * @author Andre Koenig (andre.koenig@gmail.com)
 *
 */
public abstract class AbstractFeed {

	protected enum PARSE_TYPES {
		CSV
	};
	
	/**
	 * DOCME
	 *
	 * @param url
	 * @return
	 */
	protected BufferedReader doGet(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(url);

		try {
			HttpResponse getResponse = client.execute(getRequest);

			// final int statusCode = getResponse.getStatusLine().getStatusCode();
			// TODO: Implement some exception handling.
			
			HttpEntity getResponseEntity = getResponse.getEntity();
			
			return new BufferedReader(new InputStreamReader(getResponseEntity.getContent()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
