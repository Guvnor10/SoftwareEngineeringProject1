package edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;

/**
 * Class
 * 
 * @author gn00021 View-model for the Quartermaster landing page.
 *
 *         Provides greeting text and role-based permissions for the UI.
 * @version Fall2025
 */
public class QuarterMasterLandingPageViewModel {

	/**
	 * Returns the greeting text to show on the Quartermaster landing page.
	 *
	 * If the role is missing (which shouldn't normally happen), it falls back to a
	 * generic greeting.
	 *
	 * @return greeting string.
	 */
	public String getGreetingText() {
		Roles role = LoginViewModel.getLoggedInRole();

		if (role == null) {
			return "Greetings, Quartermaster!";
		}

		switch (role) {
		case QUARTERMASTER:
			return "Greetings, Quartermaster!";
		case CREWMATE:
			return "Greetings, CrewMate!";
		case COOK:
			return "Greetings, Cook!";
		default:
			return "Greetings, Quartermaster!";
		}
	}

	/**
	 * Quartermasters have full authority — this returns whether they can add stock.
	 *
	 * @return true if add-stock functionality should be enabled.
	 */
	public boolean canAddStock() {
		return LoginViewModel.getLoggedInRole() == Roles.QUARTERMASTER;
	}

	/**
	 * Quartermasters are the ONLY ones allowed to view stock change logs.
	 *
	 * @return true if View Stock Changes button should be enabled.
	 */
	public boolean canViewStockChanges() {
		return LoginViewModel.getLoggedInRole() == Roles.QUARTERMASTER;
	}

	/**
	 * Convenience helper: returns true if the logged-in user is Quartermaster.
	 *
	 * @return true if Quartermaster.
	 */
	public boolean isQuartermaster() {
		return LoginViewModel.getLoggedInRole() == Roles.QUARTERMASTER;
	}
}
