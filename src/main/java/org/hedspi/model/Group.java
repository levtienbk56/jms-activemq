package org.hedspi.model;

import java.util.ArrayList;
import java.util.List;

/**
 * this class define list friends
 * 
 * @author trungtran.vn
 *
 */
public class Group {
	private List<User> users = new ArrayList<User>();

	public Group() {
	}

	/**
	 * create list User from list of username
	 * 
	 * @param listName
	 *            list of user name, need to split
	 */
	public Group(String listName) {
		String[] arr = listName.trim().split(",");
		for (int i = 0; i < arr.length; i++) {
			User u = new User(arr[i].trim(), "");
			users.add(u);
		}
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	// sum of hashcode of all usernames
	public int getHashCode() {
		int hash = 0;
		for (User u : users) {
			hash += u.getHashCode();
		}
		return hash;
	}

}
