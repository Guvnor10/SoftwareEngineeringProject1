package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Authenticator;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Code-behind for LoginPage.fxml.
 *
 * @author gn00021
 * @version Fall2025
 */
public class LoginCodeBehind {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label greetingLabel;

	@FXML
	private Button loginButton;

	@FXML
	private TextField passwordTextField;

	@FXML
	private TextField usernameTextField;

	/**
	 * Handles the Log In button click.
	 *
	 * @param event the event
	 */
	@FXML
	void handleSubmit(ActionEvent event) {
		String enteredUsername = this.usernameTextField.getText();
		String enteredPassword = this.passwordTextField.getText();

		Authenticator auth = new Authenticator();
		boolean valid = auth.verifyCredentials(enteredUsername, enteredPassword);

		if (!valid) {

			try {
				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/InvalidCredentialsPage.fxml");
			} catch (IOException e0) {

				this.greetingLabel.setText("Error loading Invalid Credentials page.");
				e0.printStackTrace();
			}
			return;
		}

		User loggedIn = auth.getUser(enteredUsername);

		LoginViewModel.setLoggedInUser(loggedIn.getUserName());
		LoginViewModel.setLoggedInRole(loggedIn.getRole());

		try {
			if (loggedIn.getRole() == Roles.CREWMATE) {
				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml");
			} else if (loggedIn.getRole() == Roles.QUARTERMASTER) {
				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
			} else if (loggedIn.getRole() == Roles.COOK) {

				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/CookLandingPage.fxml");
			}
		} catch (IOException e0) {
			this.greetingLabel.setText("Error loading page.");
			e0.printStackTrace();
		}
	}

	/**
	 * Loads a new FXML page into the current stage.
	 *
	 * @param fxmlPath the FXML resource path
	 * @throws IOException if the FXML cannot be loaded
	 */
	private void loadPage(String fxmlPath) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
		Stage stage = (Stage) this.loginButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Initialize the login page.
	 */
	@FXML
	void initialize() {
		assert this.greetingLabel != null : "fx:id=\"greetingLabel\" was not injected.";
		assert this.loginButton != null : "fx:id=\"loginButton\" was not injected.";
		assert this.passwordTextField != null : "fx:id=\"passwordTextField\" was not injected.";
		assert this.usernameTextField != null : "fx:id=\"usernameTextField\" was not injected.";

		this.loginButton.disableProperty().bind(
				this.usernameTextField.textProperty().isEmpty().or(this.passwordTextField.textProperty().isEmpty()));
	}
}