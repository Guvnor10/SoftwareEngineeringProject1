package edu.westga.cs3211.pirate_ship_inventory_manager.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;

class TestUserParameterized {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   " })
    @DisplayName("Constructor rejects null or blank username")
    void testConstructorRejectsBadUsername(String badName) {
        assertThrows(IllegalArgumentException.class, () ->
                new User(badName, "pass", Roles.CREWMATE));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Constructor rejects null password")
    void testConstructorRejectsNullPassword(String password) {
        assertThrows(IllegalArgumentException.class, () ->
                new User("user", password, Roles.CREWMATE));
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("Constructor rejects null role")
    void testConstructorRejectsNullRole(Roles role) {
        assertThrows(IllegalArgumentException.class, () ->
                new User("user", "pass", role));
    }



    @ParameterizedTest
    @EnumSource(Roles.class)
    @DisplayName("Constructor creates valid user with all roles")
    void testConstructorValid(Roles role) {
        User user = new User("john", "secret", role);

        assertEquals("john", user.getUserName());
        assertEquals("secret", user.getPassword());
        assertEquals(role, user.getRole());
    }



    @ParameterizedTest
    @EnumSource(Roles.class)
    @DisplayName("equals: users with same values are equal")
    void testEqualsSameValues(Roles role) {
        User u1 = new User("john", "pass", role);
        User u2 = new User("john", "pass", role);

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @ParameterizedTest
    @ValueSource(strings = { "john", "mary", "alex" })
    @DisplayName("equals: different usernames not equal")
    void testEqualsDifferentUsernames(String username) {
        User base = new User("fixed", "pass", Roles.CREWMATE);
        User other = new User(username, "pass", Roles.CREWMATE);

        boolean expected = username.equals("fixed");
        assertEquals(expected, base.equals(other));
    }

    @ParameterizedTest
    @ValueSource(strings = { "pass1", "pass2", "pass3" })
    @DisplayName("equals: different passwords not equal")
    void testEqualsDifferentPasswords(String password) {
        User base = new User("user", "fixed", Roles.CREWMATE);
        User other = new User("user", password, Roles.CREWMATE);

        boolean expected = password.equals("fixed");
        assertEquals(expected, base.equals(other));
    }

    @ParameterizedTest
    @EnumSource(Roles.class)
    @DisplayName("equals: different roles not equal")
    void testEqualsDifferentRoles(Roles role) {
        User base = new User("user", "pass", Roles.CREWMATE);
        User other = new User("user", "pass", role);

        boolean expected = role == Roles.CREWMATE;
        assertEquals(expected, base.equals(other));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "not a user" })
    @DisplayName("equals: not equal to null or non-User objects")
    void testEqualsInvalidOther(Object other) {
        User user = new User("john", "pass", Roles.CREWMATE);
        assertNotEquals(user, other);
    }

    @ParameterizedTest
    @EnumSource(Roles.class)
    @DisplayName("equals: object equals itself")
    void testEqualsSelf(Roles role) {
        User user = new User("john", "pass", role);
        assertEquals(user, user);
    }
}
