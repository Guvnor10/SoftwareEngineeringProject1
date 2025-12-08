package testCrewmateViewModel;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.CrewmateLandingPageViewModel;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;

class TestCrewmateLandingPageViewModel {

    @AfterEach
    void reset() {
        LoginViewModel.logout();
    }

 
    @DisplayName("Private constructor can be accessed for coverage")
    @org.junit.jupiter.api.Test
    void testPrivateConstructor() throws Exception {
        Constructor<CrewmateLandingPageViewModel> constructor =
                CrewmateLandingPageViewModel.class.getDeclaredConstructor();

        constructor.setAccessible(true);
        constructor.newInstance(); // executes private constructor
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("getGreetingText returns correct greeting per role")
    void testGetGreetingText(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        CrewmateLandingPageViewModel vm = new CrewmateLandingPageViewModel();
        String result = vm.getGreetingText();

        if (role == null) {
            assertEquals("Greetings, CrewMate!", result);
            return;
        }

        switch (role) {
            case CREWMATE:
                assertEquals("Greetings, CrewMate!", result);
                break;
            case QUARTERMASTER:
                assertEquals("Greetings, Quartermaster!", result);
                break;
            case COOK:
                assertEquals("Greetings, Cook!", result);
                break;
            default:
                assertEquals("Greetings, CrewMate!", result);
                break;
        }
    }


    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("canAddStock returns correct boolean per role")
    void testCanAddStock(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        CrewmateLandingPageViewModel vm = new CrewmateLandingPageViewModel();

        boolean expected = (role == Roles.CREWMATE || role == Roles.QUARTERMASTER);

        assertEquals(expected, vm.canAddStock());
    }

  
    @ParameterizedTest
    @NullSource
    @EnumSource(Roles.class)
    @DisplayName("isCrewmate returns true only for CREWMATE")
    void testIsCrewmate(Roles role) {
        LoginViewModel.setLoggedInRole(role);

        CrewmateLandingPageViewModel vm = new CrewmateLandingPageViewModel();

        boolean expected = (role == Roles.CREWMATE);

        assertEquals(expected, vm.isCrewmate());
    }
}
