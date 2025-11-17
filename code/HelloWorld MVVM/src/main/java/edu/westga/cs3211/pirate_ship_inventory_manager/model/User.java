package edu.westga.cs3211.pirate_ship_inventory_manager.model;

import java.util.Objects;

// TODO: Auto-generated Javadoc
/**
 * Defines a person.
 *
 * @author CS 3211
 * @version Fall 2025
 */
public class User {

    /** The user name. */
    private final String userName;
    
    /** The password. */
    private final String password;
    
    /** The role. */
    private final Roles role;

    /**
     * Instantiates a new user.
     *
     * @param userName the user name
     * @param password the password
     * @param role the role
     */
    public User(String userName, String password, Roles role) {
        if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }

        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the user name.
     *
     * @return the user name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return this.password;
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
     * Equals.
     *
     * @param other the other
     * @return true, if successful
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;                  
        }
        if (!(other instanceof User)) {
            return false;                   
        }
        User that = (User) other;
        return Objects.equals(this.userName, that.userName)
            && Objects.equals(this.password, that.password)
            && this.role == that.role;
    }

    /**
     * Hash code.
     *
     * @return the int
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.userName, this.password, this.role);
    }

}
