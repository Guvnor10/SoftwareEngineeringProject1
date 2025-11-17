package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/** Class
 * @author gn00021
 * The Class InvalidCredentialsPageCodeBehind.
 * @version Fall2025
 */
public class InvalidCredentialsPageCodeBehind {

    /** The error label. */
    @FXML
    private Label errorLabel;    

    /** The login button. */
    @FXML
    private Button loginButton;    

    /**
     * Initialize.
     */
    @FXML
    private void initialize() {

        this.errorLabel.setText("Invalid username or password.");
    }

    /**
     * Back to login.
     *
     * @param event the event
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @FXML
    private void backToLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(
            getClass().getResource(
                "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

        Stage stage = (Stage) this.loginButton.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}