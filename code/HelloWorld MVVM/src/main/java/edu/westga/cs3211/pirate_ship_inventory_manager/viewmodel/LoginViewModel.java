package edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;

// TODO: Auto-generated Javadoc
/**
 * The Class LoginViewModel.
 */
public class LoginViewModel {

	/** The logged in user name. */
	private static String loggedInUserName = null;
	
	/** The logged in role. */
	private static Roles loggedInRole = null;

	/**
	 * Sets the logged in user.
	 *
	 * @param username the new logged in user
	 */
	public static void setLoggedInUser(String username) {
		loggedInUserName = username;
	}

	/**
	 * Sets the logged in role.
	 *
	 * @param role the new logged in role
	 */
	public static void setLoggedInRole(Roles role) {
		loggedInRole = role;
	}

	/**
	 * Gets the logged in user.
	 *
	 * @return the logged in user
	 */
	public static String getLoggedInUser() {
		return loggedInUserName;
	}

	/**
	 * Gets the logged in role.
	 *
	 * @return the logged in role
	 */
	public static Roles getLoggedInRole() {
		return loggedInRole;
	}

	/**
	 * Logout.
	 */
	public static void logout() {
		loggedInUserName = null;
		loggedInRole = null;
	}
}