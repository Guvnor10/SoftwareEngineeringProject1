package edu.westga.cs3211.pirate_ship_inventory_manager.model;

// TODO: Auto-generated Javadoc
/**
 * Defines a person.
 *
 * @author CS 3211
 * @version Fall 2025
 */
public class User {

	/** The role. */
	private Roles role;

	/** The name. */
	private String username;

	private String password;

	/**
	 * Instantiates a new users.
	 * 
	 * @param username the name
	 * @param role     the role
	 * @param password the password
	 */
	public User(String username, String password, Roles role) {
		if (username == null || username.isEmpty()) {
			throw new IllegalArgumentException("Username cannot be null or empty");
		}
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Password cannot be null or empty");
		}
		if (role == null) {
			throw new IllegalArgumentException("Role cannot be null");
		}

		this.username = username;
		this.password = password;
		this.role = role;
	}

	/**
	 * Gets the role.
	 *
	 * @return the role
	 */
	public Roles getRole() {
		return this.role;
	}

	/**
	 * Sets the role.
	 *
	 * @param role the new role
	 */
	public void setRole(Roles role) {
		this.role = role;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getUserName() {
		return this.username;
	}

	/**
	 * Sets the name.
	 *
	 * @param username the new name
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	/**
	 * Gets the password
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the password
	 *
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

}
