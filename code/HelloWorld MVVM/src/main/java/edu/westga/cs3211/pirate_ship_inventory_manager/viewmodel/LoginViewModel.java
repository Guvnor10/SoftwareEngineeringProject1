
package edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;

/**
 * <p><b>Login View-Model</b></p>
 *
 * <p>Maintains static state for the currently logged-in user and role.</p>
 *
 * <p>All members are static; the class is non-instantiable.</p>
 *
 * @author gn00021
 * @version Fall2025
 */
public final class LoginViewModel {

    /** The logged-in user name (null when no session). */
    private static String loggedInUserName = null;

    /** The logged-in role (null when no session). */
    private static Roles loggedInRole = null;

    /**
     * Non-instantiable utility class.
     */
    private LoginViewModel() {
        // Prevent instantiation
    }

    /**
     * Sets the logged-in user name.
     *
     * @param username the user name to set (may be null to clear)
     */
    public static void setLoggedInUser(String username) {
        loggedInUserName = username;
    }

    /**
     * Sets the logged-in role.
     *
     * @param role the role to set (may be null to clear)
     */
    public static void setLoggedInRole(Roles role) {
        loggedInRole = role;
    }

    /**
     * Gets the logged-in user name.
     *
     * @return the current user name, or {@code null} if none
     */
    public static String getLoggedInUser() {
        return loggedInUserName;
    }

    /**
     * Gets the logged-in role.
     *
     * @return the current role, or {@code null} if none
     */
    public static Roles getLoggedInRole() {
        return loggedInRole;
    }

    /**
     * Clears the logged-in user and role.
     */
    public static void logout() {
        loggedInUserName = null;
        loggedInRole = null;
    }
}