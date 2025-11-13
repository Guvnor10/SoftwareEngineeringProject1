package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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

    @FXML
    void handleSubmit(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert greetingLabel != null : "fx:id=\"greetingLabel\" was not injected: check your FXML file 'hello.fxml'.";
        assert loginButton != null : "fx:id=\"loginButton\" was not injected: check your FXML file 'hello.fxml'.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'hello.fxml'.";
        assert usernameTextField != null : "fx:id=\"usernameTextField\" was not injected: check your FXML file 'hello.fxml'.";

    }

}

