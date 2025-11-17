package edu.westga.cs3211.pirate_ship_inventory_manager.model.authenticator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Authenticator;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;

class TestAuthenticator {

	@Test
    void verifyCredentialsShouldReturnTrueForValidUser() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("crew", "pass");

        assertTrue(result);
    }

    @Test
    void verifyCredentialsShouldReturnFalseForWrongPassword() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("crew", "wrong");

        assertFalse(result);
    }

    @Test
    void verifyCredentialsShouldReturnFalseForUnknownUser() {
        Authenticator auth = new Authenticator();

        boolean result = auth.verifyCredentials("nobody", "anything");

        assertFalse(result);
    }

    @Test
    void getUserShouldReturnUserForKnownUsername() {
        Authenticator auth = new Authenticator();

        User user = auth.getUser("crew");

        assertNotNull(user);
        assertEquals("crew", user.getUserName());
        assertEquals(Roles.CREWMATE, user.getRole());
    }

    @Test
    void getUserShouldReturnNullForUnknownUsername() {
        Authenticator auth = new Authenticator();

        User user = auth.getUser("nobody");

        assertNull(user);
    }
    
    @Test
    void verifyCredentialsShouldReturnFalseWhenUsernameIsNull() {
        Authenticator auth = new Authenticator();
        boolean result = auth.verifyCredentials(null, "pass");
        assertFalse(result);
    }

    @Test
    void verifyCredentialsShouldReturnFalseWhenPasswordIsNull() {
        Authenticator auth = new Authenticator();
        boolean result = auth.verifyCredentials("crew", null);
        assertFalse(result);
    }
}