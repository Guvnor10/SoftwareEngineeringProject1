package edu.westga.cs3211.pirate_ship_inventory_manager.model.authenticator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Authenticator;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;

class TestAuthenticator {

    @Test
    void verifyCredentialsReturnsTrueForValidCrewmate() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("crewmate1", "pass1");

        assertTrue(result, "Valid crewmate credentials should return true");
    }

    @Test
    void verifyCredentialsReturnsTrueForValidQuartermaster() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("quarter1", "pass2");

        assertTrue(result, "Valid quartermaster credentials should return true");
    }

    @Test
    void verifyCredentialsReturnsFalseForWrongPassword() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("crewmate1", "wrongPass");

        assertFalse(result, "Wrong password should return false");
    }

    @Test
    void verifyCredentialsReturnsFalseForUnknownUser() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("unknownUser", "pass1");

        assertFalse(result, "Unknown username should return false");
    }

    @Test
    void verifyCredentialsReturnsFalseForNullUsername() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials(null, "pass1");

        assertFalse(result, "Null username should behave like unknown user and return false");
    }

    @Test
    void verifyCredentialsReturnsFalseForNullPassword() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("crewmate1", null);

        assertFalse(result, "Null password should return false");
    }

    @Test
    void getUserReturnsCorrectUserForExistingUsername() {
        Authenticator auth = new Authenticator();

        User result = auth.getUser("quarter1");

        assertNotNull(result, "Existing username should return a User");
        assertEquals("quarter1", result.getUserName(), "Username should match");
        assertEquals("pass2", result.getPassword(), "Password should match");
        assertEquals(Roles.QUARTERMASTER, result.getRole(), "Role should be QUARTERMASTER");
    }

    @Test
    void getUserReturnsNullForUnknownUsername() {
        Authenticator auth = new Authenticator();

        User result = auth.getUser("doesNotExist");

        assertNull(result, "Unknown username should return null");
    }
}