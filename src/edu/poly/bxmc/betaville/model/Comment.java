package edu.poly.bxmc.betaville.model;

import java.io.Serializable;

/**
 * A comment about a {@link Design}
 * @author Skye Book
 *
 */
public class Comment extends Criticism implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String comment;
	private int repliesToCommentID;
	private String date;
	private int indentLevel;
	private int commentDesignID;
	private String commentUser;

	/**
	 * 
	 */
	public Comment(int id, int designID, String user, String comment){
		super(designID, user);
		this.commentDesignID=designID;
		this.id=id;
		this.comment=comment;
	}
	
	/**
	 * 
	 */
	public Comment(int id, int designID, String user, String comment, int repliesTo){
		super(designID, user);
		this.id=id;
		this.comment=comment;
		this.repliesToCommentID=repliesTo;
	}
	
	/**
	 * 
	 */
	public Comment(int id, int designID, String user, String comment, int repliesTo, String date){
		super(designID, user);
		this.id=id;
		this.comment=comment;
		this.repliesToCommentID=repliesTo;
		this.date=date;
	}
	
	/**
	 * Gets a user's comments about a proposal
	 * @return User's comments
	 */
	public String getComment(){
		return comment;
	}
	
	/**
	 * 
	 * @return
	 */
	public int repliesTo(){
		return repliesToCommentID;
	}
	
	public void setID(int id){
		this.id=id;
	}
	
	/**
	 * Retrieves the ID of this <code>Comment</code>
	 * @return Unique identification number
	 */
	public int getID(){
		return id;
	}
	
	public int getCommentDesignID() {
		return commentDesignID;
	}
	
	public void setComment(String comment) {
		this.comment=comment;
	}
	
	public String getDate(){
		return date;
	}

	public String getUser() {
		return commentUser;
	}
	
	public void setIndentLevel(int level){
		indentLevel=level;
	}
	
	public void setDate(String date) {
		this.date=date;
	}
	
	public void setDesignID(String designs) {
		this.commentDesignID=Integer.parseInt(designs);
	}
	
	public void setUser(String user) {
		this.commentUser = user;
	}
	
	public void setRepliesTo(int replies) {
		this.repliesToCommentID = replies;
	}
	/**
	 * Retrieves the level of indentation based on
	 * which comment this is replying to.
	 * @return Level of indentation from 0 (root) to theoretical infinity
	 */
	public int getIndentLevel(){
		return indentLevel;
	}
}