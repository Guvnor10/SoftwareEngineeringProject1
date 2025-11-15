package edu.westga.cs3211.pirate_ship_inventory_manager.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class QuarterMasterLandingPageCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addStockChangesButton;
    
    @FXML
    private Button logoutButton;

    @FXML
    private Label greetingLabel;

    @FXML
    private Button viewStockChangesButton;

    @FXML
    void goToAddStockLanding(ActionEvent event) throws IOException {
    	AddStockChangesPageCodeBehind.setLastRole("QUARTERMASTER");
    	Parent root = FXMLLoader.load(
    	        getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/AddStockChangesPage.fxml")
    	    );

    	    Stage stage = (Stage) addStockChangesButton.getScene().getWindow();
    	    Scene scene = new Scene(root);
    	    stage.setScene(scene);
    	    stage.show();
    }

    @FXML
    void goToViewStockLanding(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(
    	        getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/ViewStockChangesPage.fxml")
    	    );

    	    Stage stage = (Stage) addStockChangesButton.getScene().getWindow();
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
        assert addStockChangesButton != null : "fx:id=\"addStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
        assert greetingLabel != null : "fx:id=\"greetingLabel\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
        assert viewStockChangesButton != null : "fx:id=\"viewStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";

    }

}

