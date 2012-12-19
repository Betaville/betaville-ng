package edu.poly.bxmc.betaville.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains information about a proposal type
 * @author Skye Book
 *
 */
public class ProposalPermission implements Serializable{
	private static final long serialVersionUID = 1L;

	public enum Type {CLOSED, GROUP, ALL};
	
	private Type type;
	private List<String> users;

	public ProposalPermission(Type type, List<String> users) {
		this.type=type;
		this.users=users;
	}
	
	public ProposalPermission(Type type) {
		this(type, new ArrayList<String>());
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return the users
	 */
	public List<String> getUsers() {
		return users;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<String> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((users == null) ? 0 : users.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProposalPermission other = (ProposalPermission) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (users == null) {
			if (other.users != null)
				return false;
		} else if (!users.equals(other.users))
			return false;
		return true;
	}
}
