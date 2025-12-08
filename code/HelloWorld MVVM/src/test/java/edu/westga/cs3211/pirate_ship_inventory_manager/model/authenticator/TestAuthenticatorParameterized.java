package edu.westga.cs3211.pirate_ship_inventory_manager.model.authenticator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Authenticator;

class TestAuthenticatorParameterized {

    @ParameterizedTest
    @CsvSource({
        "crewmate1, wrongpass, false",
        "quarter1, nope, false",
        "cook1, 123, false",

        "unknownUser, pass1, false",
        "ghost, pass2, false",

        "crewmate1, pass1, true",
        "quarter1, pass2, true",
        "cook1, pass3, true"
    })
    @DisplayName("verifyCredentials — all valid/invalid combinations")
    void testVerifyCredentials(String username, String password, boolean expected) {
        Authenticator auth = new Authenticator();
        assertEquals(expected, auth.verifyCredentials(username, password));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   " })
    @DisplayName("verifyCredentials — null or blank username returns false")
    void testVerifyCredentialsNullOrBlankUsername(String username) {
        Authenticator auth = new Authenticator();
        assertFalse(auth.verifyCredentials(username, "doesntmatter"));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("verifyCredentials — null password returns false")
    void testVerifyCredentialsNullPassword(String password) {
        Authenticator auth = new Authenticator();
        assertFalse(auth.verifyCredentials("crewmate1", password));
    }

 
    @ParameterizedTest
    @CsvSource({
        "crewmate1",
        "quarter1",
        "cook1"
    })
    @DisplayName("getUser — returns valid User objects for known usernames")
    void testGetUserValid(String username) {
        Authenticator auth = new Authenticator();
        assertNotNull(auth.getUser(username));
        assertEquals(username, auth.getUser(username).getUserName());
    }

    @ParameterizedTest
    @ValueSource(strings = { "ghost", "random", "nope" })
    @DisplayName("getUser — returns null for unknown users")
    void testGetUserInvalid(String username) {
        Authenticator auth = new Authenticator();
        assertNull(auth.getUser(username));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("getUser — null username returns null")
    void testGetUserNull(String username) {
        Authenticator auth = new Authenticator();
        assertNull(auth.getUser(username));
    }
}
