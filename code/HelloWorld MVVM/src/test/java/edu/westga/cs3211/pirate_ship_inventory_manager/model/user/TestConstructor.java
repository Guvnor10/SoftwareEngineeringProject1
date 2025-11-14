package edu.westga.cs3211.pirate_ship_inventory_manager.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;

class TestConstructor {

	@Test
    void testNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "pass123", Roles.CREWMATE);
        });
    }

    @Test
    void testNullPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Bob", null, Roles.CREWMATE);
        });
    }

    @Test
    void testNullRole() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Bob", "pass123", null);
        });
    }

    @Test
    void testValidUserCreation() {
        User result = new User("Bob", "pass123", Roles.CREWMATE);

        assertEquals("Bob", result.getUserName(), "checking username");
        assertEquals("pass123", result.getPassword(), "checking password");
        assertEquals(Roles.CREWMATE, result.getRole(), "checking role");
    }

    @Test
    void testEmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "pass123", Roles.CREWMATE);
        });
    }

    @Test
    void testEmptyPassword() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Bob", "", Roles.CREWMATE);
        });
    }

}
