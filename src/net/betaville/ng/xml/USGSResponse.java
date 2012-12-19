package net.betaville.ng.xml;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import net.betaville.ng.net.UnexpectedServerResponse;

import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * 
 * @author Skye Book
 *
 */
public class USGSResponse extends XMLReader {
	private double elevation;
	private String unit=null;
	
	private int attempts=0;
	private int maxAttempts=3;
	
	public USGSResponse(){
	}
	
	public USGSResponse(double lat, double lon){
		doRequest(lat, lon);
	}
	
	public void doRequest(double lat, double lon){
		try {
			attempts++;
			URL url = new URL("http://gisdata.usgs.gov/xmlwebservices2/elevation_service.asmx/getElevation?X_Value="+lon+"&Y_Value="+lat+"&Elevation_Units=METERS&Source_Layer=-1&Elevation_Only=false");
			//URL url = new URL("http://gisdata.usgs.gov/xmlwebservices2/elevation_service.asmx/getElevation?X_Value=-113.17599390815296&Y_Value=36.13611366563154&Elevation_Units=METERS&Source_Layer=-1&Elevation_Only=false");
			loadStream(url.openStream());
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			if(e.getMessage().contains("503")){
				System.out.println(e.getClass().getName()+": 503 Error");
				
				if(attempts<maxAttempts+1){
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					attempts++;
					System.out.println("Trying attempt " + attempts + " of " + maxAttempts);
					doRequest(lat, lon);
				}
				else{
					System.out.println("Failed request for lat/lon: " + lat +", " + lon);
					System.out.println("Setting elevation to 0");
					elevation=0;
				}
			}
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.poly.bxmc.betaville.xml.XMLReader#parse()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void parse() throws UnexpectedServerResponse{
		Iterator it = rootElement.getChild("Elevation_Query").getChildren().iterator();
		while(it.hasNext()){
			Element current = (Element)it.next();
			String name = current.getName();
			String value = current.getValue();
			
			if(name.toLowerCase().contains("elevation")){
				elevation = Double.parseDouble(value);
			}
			else if(name.toLowerCase().contains("units")){
				unit = value;
			}
		}
		
		// The xml is always formatted the same and will always have a value for the type of unit,
		// if this is still null then there was an error
		if(unit==null){
			String tree = "";
			it = rootElement.getChild("Elevation_Query").getChildren().iterator();
			while(it.hasNext()){
				Element current = (Element)it.next();
				tree+=current.getName()+": "+current.getValue()+"\n";
			}
			throw new UnexpectedServerResponse("The USGS returned an unexpected value:\n"+tree);
		}
		
	}

	/**
	 * @return the elevation
	 */
	public double getElevation() {
		return elevation;
	}

	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}
	
	/**
	 * Get the full structure of the USGS response
	 * @return Returns a JDOM tree starting with the root element
	 */
	public Element getFullResponseStructure(){
		return rootElement;
	}
}
