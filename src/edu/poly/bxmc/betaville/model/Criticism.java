package edu.poly.bxmc.betaville.model;

import java.io.Serializable;

/**
 * @author Skye Book
 *
 */
public class Criticism implements Serializable{
	private static final long serialVersionUID = 1L;
	protected String user;
	protected int designID;
	
	public Criticism(int designID, String user){
		this.designID=designID;
		this.user=user;
	}
	
	public String getUser(){
		return user;
	}
	public int getDesignID(){
		return designID;
	}
}
