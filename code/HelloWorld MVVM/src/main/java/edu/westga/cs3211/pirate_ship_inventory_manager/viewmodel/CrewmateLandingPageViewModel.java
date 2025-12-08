package edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;

/**
 * View-model for the Crewmate landing page.
 * 
 * @author gn00021 It uses LoginViewModel's static state to know who is logged
 *         in and what role they have, and exposes simple strings/flags for the
 *         UI.
 * @version Fall2025
 */
public class CrewmateLandingPageViewModel {
	public CrewmateLandingPageViewModel() {
		
	}

	/**
	 * Returns the greeting text to show at the top of the Crewmate landing page.
	 * 
	 * If the role is missing (shouldn't normally happen), it falls back to a
	 * generic greeting.
	 * 
	 * @return greeting string.
	 */
	public String getGreetingText() {
		Roles role = LoginViewModel.getLoggedInRole();

		if (role == null) {
			return "Greetings, CrewMate!";
		}

		switch (role) {
		case CREWMATE:
			return "Greetings, CrewMate!";
		case QUARTERMASTER:
			return "Greetings, Quartermaster!";
		case COOK:
			return "Greetings, Cook!";
		default:
			return "Greetings, CrewMate!";
		}
	}

	/**
	 * Returns true if the currently logged-in role is allowed to add stock.
	 * 
	 * According to your use cases, Crewmates and Quartermasters can add stock.
	 * 
	 * @return true if add-stock should be enabled.
	 */
	public boolean canAddStock() {
		Roles role = LoginViewModel.getLoggedInRole();
		return role == Roles.CREWMATE || role == Roles.QUARTERMASTER;
	}

	/**
	 * Convenience helper: check that the logged-in user really is a Crewmate.
	 *
	 * @return true if role is CREWMATE.
	 */
	public boolean isCrewmate() {
		return LoginViewModel.getLoggedInRole() == Roles.CREWMATE;
	}
}
