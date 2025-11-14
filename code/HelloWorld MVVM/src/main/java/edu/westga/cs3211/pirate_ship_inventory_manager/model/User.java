package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.ArrayList;
import java.util.List;

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
	 * @param role the role
	 * @param name the name
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
		return role;
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
		return username;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setUserName(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
