package net.betaville.opendata;

import java.util.Vector;

import net.betaville.opendata.domain.ElectricityConsumption;
import net.betaville.opendata.domain.OilBoilerFacility;
import net.betaville.opendata.exceptions.OpenDataApiException;
import net.betaville.opendata.feeds.ElectricityConsumptionFeed;
import net.betaville.opendata.feeds.OilBoilerFeed;

/**
 * DOCME
 * 
 * @author Andre Koenig (andre.koenig@gmail.com)
 *
 */
public class App {

	public static void main(String args[]) {
		try {
			System.out.println("=> Grabing the 'Oil Boiler Facility' data");

			Vector<OilBoilerFacility> facilities = OilBoilerFeed.getInstance().findAll();
			
			int i = 0;
			for (OilBoilerFacility facility : facilities) {
				i++;
				System.out.println(facility.toString());
			}
			System.out.println("COUNT " + i);
			
			System.out.println("=> Grabing the 'Electricity Consumption' data");

			Vector<ElectricityConsumption> consumptions = ElectricityConsumptionFeed.getInstance().findAll();
			
			i = 0;
			
			for (ElectricityConsumption consumption : consumptions) {
				i++;
				System.out.println(consumption.toString());
			}
			
			System.out.println("COUNT " + i);
			
		} catch (OpenDataApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
