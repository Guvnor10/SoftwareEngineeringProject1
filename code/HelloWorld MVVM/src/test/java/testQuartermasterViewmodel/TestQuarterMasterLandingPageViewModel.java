package testQuartermasterViewmodel;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.QuarterMasterLandingPageViewModel;

class TestQuarterMasterLandingPageViewModel {

	private final QuarterMasterLandingPageViewModel vm = new QuarterMasterLandingPageViewModel();

    @AfterEach
    void resetRole() {
        LoginViewModel.setLoggedInRole(null);
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("getGreetingText returns correct string for each role")
    void testGetGreetingText(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        String result = vm.getGreetingText();

        if (role == null) {
            assertEquals("Greetings, Quartermaster!", result);
        } else {
            switch (role) {
                case QUARTERMASTER:
                    assertEquals("Greetings, Quartermaster!", result);
                    break;
                case CREWMATE:
                    assertEquals("Greetings, CrewMate!", result);
                    break;
                case COOK:
                    assertEquals("Greetings, Cook!", result);
                    break;
                default:
                    assertEquals("Greetings, Quartermaster!", result);
                    break;
            }
        }
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("canAddStock is true ONLY for Quartermaster")
    void testCanAddStock(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        boolean expected = (role == Roles.QUARTERMASTER);

        assertEquals(expected, vm.canAddStock());
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("canViewStockChanges is true ONLY for Quartermaster")
    void testCanViewStockChanges(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        boolean expected = (role == Roles.QUARTERMASTER);

        assertEquals(expected, vm.canViewStockChanges());
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("isQuartermaster returns true ONLY for Quartermaster")
    void testIsQuartermaster(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        boolean expected = (role == Roles.QUARTERMASTER);

        assertEquals(expected, vm.isQuartermaster());
    }

}
