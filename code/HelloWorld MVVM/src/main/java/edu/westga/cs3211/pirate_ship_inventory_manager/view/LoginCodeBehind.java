package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Authenticator;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label greetingLabel;
    
    @FXML
    private Label errorLabel;

    @FXML
    private Button loginButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    void handleSubmit(ActionEvent event) {
    	String enteredUsername = usernameTextField.getText();
        String enteredPassword = passwordTextField.getText();

        Authenticator auth = new Authenticator();

        boolean valid = auth.verifyCredentials(enteredUsername, enteredPassword);

        if (!valid) {
            errorLabel.setText("Invalid username or password.");
            errorLabel.setVisible(true);
            return;
        }
        
        errorLabel.setVisible(false);

        // Login was successful
        User loggedIn = auth.getUser(enteredUsername);

        try {
            if (loggedIn.getRole() == Roles.CREWMATE) {
                loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml");
            } else if (loggedIn.getRole() == Roles.QUARTERMASTER) {
                loadPage("/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml");
            } else if (loggedIn.getRole() == Roles.COOK) {
                // Optional: add cook page later
                greetingLabel.setText("Cook login successful (page not implemented).");
            }
        } catch (IOException e) {
            greetingLabel.setText("Error loading page.");
            e.printStackTrace();
        }
    }

    private void loadPage(String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) loginButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
    	assert greetingLabel != null : "fx:id=\"greetingLabel\" not injected.";
        assert loginButton != null : "fx:id=\"loginButton\" not injected.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" not injected.";
        assert usernameTextField != null : "fx:id=\"usernameTextField\" not injected.";
        
        errorLabel.setText("");   // hide by clearing text
        errorLabel.setVisible(false);

        // Disable login button until both fields have text
        loginButton.disableProperty().bind(
            usernameTextField.textProperty().isEmpty()
                .or(passwordTextField.textProperty().isEmpty())
        );

    }

}
 