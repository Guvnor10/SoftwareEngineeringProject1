package testLoginViewModel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;

class TestLoginViewModel {

    @AfterEach
    void reset() {
        LoginViewModel.logout();
    }
    
    @Test
    @DisplayName("Class can be instantiated for coverage")
    void testPrivateConstructor() throws Exception {
        var constructor = LoginViewModel.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        constructor.newInstance();  // forces coverage
    }


    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "", "   ", "JohnDoe", "Quartermaster01" })
    @DisplayName("setLoggedInUser + getLoggedInUser store and retrieve username")
    void testSetAndGetLoggedInUser(String username) {
        LoginViewModel.setLoggedInUser(username);
        assertEquals(username, LoginViewModel.getLoggedInUser());
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("setLoggedInRole + getLoggedInRole store and retrieve role")
    void testSetAndGetLoggedInRole(Roles role) {
        LoginViewModel.setLoggedInRole(role);
        assertEquals(role, LoginViewModel.getLoggedInRole());
    }

    @ParameterizedTest
    @EnumSource(Roles.class)
    @DisplayName("logout clears both username and role")
    void testLogout(Roles role) {
        LoginViewModel.setLoggedInUser("TestUser");
        LoginViewModel.setLoggedInRole(role);

        LoginViewModel.logout();

        assertNull(LoginViewModel.getLoggedInUser());
        assertNull(LoginViewModel.getLoggedInRole());
    }
}
