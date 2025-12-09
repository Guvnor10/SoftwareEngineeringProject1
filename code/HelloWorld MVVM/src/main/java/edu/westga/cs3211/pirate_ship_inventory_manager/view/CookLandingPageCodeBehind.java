package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;

/**
 * code behind for the cooklanding page 
 * @author jr00381
 * @version Fall 2025
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The Class CookLandingPageCodeBehind.
 * @version Fall 25
 * @author gn00021
 */
public class CookLandingPageCodeBehind {

	/** The add stock button. */
	@FXML
	private Button addStockButton;

	/** The greeting label. */
	@FXML
	private Label greetingLabel;

	/** The logout button. */
	@FXML
	private Button logoutButton;

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
	 * View food.
	 *
	 * @param event the event
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@FXML
	void viewFood(ActionEvent event) throws IOException {
		Stage stage = (Stage) this.logoutButton.getScene().getWindow();

		Parent root = FXMLLoader.load(
				getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/ViewFoodPage.fxml"));

		stage.setScene(new Scene(root));
		stage.show();
	}

}
