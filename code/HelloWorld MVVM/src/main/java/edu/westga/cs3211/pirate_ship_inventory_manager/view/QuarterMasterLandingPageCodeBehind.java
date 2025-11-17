package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.QuarterMasterLandingPageViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Class
 * 
 * @author gn00021 The Class QuarterMasterLandingPageCodeBehind.
 * @version Fall2025
 */
public class QuarterMasterLandingPageCodeBehind {

	/** The resources. */
	@FXML
	private ResourceBundle resources;

	/** The location. */
	@FXML
	private URL location;

	/** The add stock changes button. */
	@FXML
	private Button addStockChangesButton;

	/** The logout button. */
	@FXML
	private Button logoutButton;

	/** The greeting label. */
	@FXML
	private Label greetingLabel;

	/** The view stock changes button. */
	@FXML
	private Button viewStockChangesButton;

	/** The view model. */
	private final QuarterMasterLandingPageViewModel viewModel = new QuarterMasterLandingPageViewModel();

	/**
	 * Go to add stock landing.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	void goToAddStockLanding(ActionEvent event) throws IOException {
		LoginViewModel.setLoggedInRole(Roles.QUARTERMASTER);
		Parent root = FXMLLoader.load(getClass()
				.getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/AddStockChangesPage.fxml"));

		Stage stage = (Stage) this.addStockChangesButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Go to view stock landing.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	void goToViewStockLanding(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass()
				.getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/ViewStockChangesPage.fxml"));

		Stage stage = (Stage) this.addStockChangesButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Back to login page.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	void backToLoginPage(ActionEvent event) throws IOException {
		Stage stage = (Stage) this.logoutButton.getScene().getWindow();

		Parent root = FXMLLoader
				.load(getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

		stage.setScene(new Scene(root));
		stage.show();
	}

	/**
	 * Initialize.
	 */
	@FXML
	void initialize() {
		assert this.addStockChangesButton != null
				: "fx:id=\"addStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
		assert this.greetingLabel != null
				: "fx:id=\"greetingLabel\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
		assert this.viewStockChangesButton != null
				: "fx:id=\"viewStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";

		this.greetingLabel.setText(this.viewModel.getGreetingText());

		this.addStockChangesButton.setDisable(!this.viewModel.canAddStock());
		this.viewStockChangesButton.setDisable(!this.viewModel.canViewStockChanges());
	}

}