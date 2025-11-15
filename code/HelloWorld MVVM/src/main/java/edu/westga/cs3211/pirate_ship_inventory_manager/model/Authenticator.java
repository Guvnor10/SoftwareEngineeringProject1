package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.HashMap;
import java.util.Map;

/** Class
 * @author gn00021
 * The Class Authenticator.
 * @version Fall 2025
 */
public class Authenticator {
	
	/** The user. */
	private final Map<String, User> user = new HashMap<>();

	/**
	 * Instantiates a new authenticator.
	 */
	public Authenticator() {
		this.user.put("crewmate1", new User("crewmate1", "pass1", Roles.CREWMATE));
		this.user.put("quarter1", new User("quarter1", "pass2", Roles.QUARTERMASTER));
		this.user.put("cook1", new User("cook1", "pass3", Roles.COOK));
	}

	/**
	 * Verify credentials.
	 *
	 * @param username the username
	 * @param password the password
	 * @return true, if successful
	 */
	public boolean verifyCredentials(String username, String password) {
		if (!this.user.containsKey(username)) {
			return false;
		}
		User foundUser = this.user.get(username);
		return foundUser.getPassword().equals(password);
	}

	/**
	 * Gets the user.
	 *
	 * @param username the username
	 * @return the user
	 */
	public User getUser(String username) {
		return this.user.get(username);
	}

}
