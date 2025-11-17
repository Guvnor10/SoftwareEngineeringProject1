package edu.westga.cs3211.pirate_ship_inventory_manager.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;

class TestConstructor {

	@Test
    void constructorShouldCreateUser() {
        User u = new User("jack", "pwd", Roles.CREWMATE);

        assertEquals("jack", u.getUserName());
        assertEquals("pwd", u.getPassword());
        assertEquals(Roles.CREWMATE, u.getRole());
    }

    @Test
    void equalsAndHashCodeShouldWork() {
        User u1 = new User("jack", "pwd", Roles.CREWMATE);
        User u2 = new User("jack", "pwd", Roles.CREWMATE);
        User u3 = new User("rose", "pwd2", Roles.QUARTERMASTER);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());

        assertNotEquals(u1, u3);
        assertNotEquals(u1, null);
        assertNotEquals(u1, "not a user");
    }
}
