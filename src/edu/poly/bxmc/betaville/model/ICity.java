package edu.poly.bxmc.betaville.model;

import java.io.Serializable;

/**
 * Interface <ICity> - Interface which allows to access to the data of a city
 * 
 * @author Caroline Bouchat
 * @author Skye Book (Minor Additions)
 * @version 0.1 - Spring 2009
 */
public interface ICity extends Serializable{

	/**
	 * Method <getCity> - Gets the city name
	 * 
	 * @return City name
	 */
	public String getCity();

	/**
	 * Method <getCountry> - Gets the name of the country in which the city is located.
	 * 
	 * @return Country name
	 */
	public String getCountry();
	
	/**
	 * Method <getNumberDesigns> - Gets the number of designs located in the city
	 * 
	 * @return Number of designs
	 */
	public int getNumberDesigns();

	/**
	 * Method <getState> - Gets the state in which the city is located
	 * 
	 * @return State name
	 */
	public String getState();
	
	public int getCityID();
}
