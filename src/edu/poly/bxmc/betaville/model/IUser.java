package edu.poly.bxmc.betaville.model;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Interface <IUser> -
 * 
 * @author <a href="mailto:skye.book@gmail.com">Skye Book
 */
public interface IUser{
	public static enum UserType implements Serializable{
		/**
		 * Can view and do nothing else
		 */
		GUEST,
		/**
		 * Can make proposals, versions, comments
		 */
		MEMBER,
		
		/**
		 * Member who can also edit the base model
		 */
		BASE_COMMITTER,
		
		/**
		 * Member who can retrieve statistical data from the server
		 */
		DATA_SEARCHER,
		
		/**
		 * Base Committer who can also confirm (or dispel) spam claims
		 */
		MODERATOR,
		
		/**
		 * Moderator who can do most other things :)
		 */
		ADMIN};
	
	/**
	 * Method <getUserName> - Returns the current user's name
	 * 
	 * @return
	 */
	public String getUserName();

	/**
	 * Method <setUserName> - Sets the user name.
	 * 
	 * @param newUserName
	 *            New user name
	 */
	public void setUserName(String newUserName);
	
	public String getUserPass();
	
	public static Comparator<UserType> HIGHER_OR_EQUAL = new Comparator<UserType>(){
		public int compare(UserType arg0, UserType arg1) {
			return getLevel(arg0).compareTo(getLevel(arg1));
		}
		
		private Integer getLevel(UserType type){
			switch (type) {
			case GUEST:
				return 0;
			case MEMBER:
				return 1;
			case BASE_COMMITTER:
				return 2;
			case DATA_SEARCHER:
				return 3;
			case MODERATOR:
				return 4;
			case ADMIN:
				return 5;
			
			/* If a mysterious user type finds its
			 * way here, then force it to a regular
			 * member role (this could otherwise
			 * be a potential security risk for
			 * impersonating a moderator or admin
			 */
			default:
				return 1;
			}
		}
	};
}
