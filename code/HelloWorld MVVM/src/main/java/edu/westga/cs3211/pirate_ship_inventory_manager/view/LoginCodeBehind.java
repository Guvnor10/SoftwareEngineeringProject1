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

/** Class
 * @author gn00021
 * The Class LoginCodeBehind.
 * @version Fall2025
 */
public class LoginCodeBehind {

	/** The resources. */
	@FXML
	private ResourceBundle resources;

	/** The location. */
	@FXML
	private URL location;

	/** The greeting label. */
	@FXML
	private Label greetingLabel;

	/** The error label. */
	@FXML
	private Label errorLabel;

	/** The login button. */
	@FXML
	private Button loginButton;

	/** The password text field. */
	@FXML
	private TextField passwordTextField;

	/** The username text field. */
	@FXML
	private TextField usernameTextField;

	/**
	 * Handle submit.
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
			this.errorLabel.setText("Invalid username or password.");
			this.errorLabel.setVisible(true);
			return;
		}

		this.errorLabel.setVisible(false);

		// Login was successful
		User loggedIn = auth.getUser(enteredUsername);
		
		LoginViewModel.setLoggedInUser(loggedIn.getUserName());
		LoginViewModel.setLoggedInRole(loggedIn.getRole());
		try {
			if (loggedIn.getRole() == Roles.CREWMATE) {
				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml");
			} else if (loggedIn.getRole() == Roles.QUARTERMASTER) {
				this.loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
			} else if (loggedIn.getRole() == Roles.COOK) {
				// Optional: add cook page later
				this.greetingLabel.setText("Cook login successful (page not implemented).");
			}
		} catch (IOException e0) {
			this.greetingLabel.setText("Error loading page.");
			e0.printStackTrace();
		}
	}

	/**
	 * Load page.
	 *
	 * @param fxmlPath the fxml path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadPage(String fxmlPath) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
		Stage stage = (Stage) this.loginButton.getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * Initialize.
	 */
	@FXML
	void initialize() {
		assert this.greetingLabel != null : "fx:id=\"greetingLabel\" not injected.";
		assert this.loginButton != null : "fx:id=\"loginButton\" not injected.";
		assert this.passwordTextField != null : "fx:id=\"passwordTextField\" not injected.";
		assert this.usernameTextField != null : "fx:id=\"usernameTextField\" not injected.";

		this.errorLabel.setText(""); 
		this.errorLabel.setVisible(false);

		// Disable login button until both fields have text
		this.loginButton.disableProperty()
				.bind(this.usernameTextField.textProperty().isEmpty().or(this.passwordTextField.textProperty().isEmpty()));

	}

}
