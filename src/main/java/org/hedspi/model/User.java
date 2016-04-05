package org.hedspi.model;

/**
 * this class define Me!
 * @author trungtran.vn
 *
 */
public class User {
	private String username;
	private String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getHashCode() {
		return this.username.hashCode();
	}

}
