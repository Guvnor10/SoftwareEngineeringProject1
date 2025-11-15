package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.westga.cs3211.pirate_ship_inventory_manager.model.Roles;
import edu.westga.cs3211.pirate_ship_inventory_manager.viewmodel.LoginViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CrewmateLandingPageCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addStockButton;
    
    @FXML
    private Button logoutButton;
    
    @FXML
    private Label greetingLabel;

    @FXML
    void addStockChanges(ActionEvent event) throws IOException {
    	LoginViewModel.setLoggedInRole(Roles.CREWMATE);
    	
        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/AddStockChangesPage.fxml"
        ));

        Stage stage = (Stage) addStockButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    void backToLoginPage(ActionEvent event) throws IOException {
    	Stage stage = (Stage) logoutButton.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"
        ));

        stage.setScene(new Scene(root));
        stage.show();   	
    }
    
    @FXML
    void initialize() {
        assert addStockButton != null : "fx:id=\"addStockButton\" was not injected: check your FXML file 'CrewmateLandingPage.fxml'.";
        assert greetingLabel != null : "fx:id=\"greetingLabel\" was not injected: check your FXML file 'CrewmateLandingPage.fxml'.";

    }

}

