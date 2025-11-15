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
    	LoginViewModel.setLoggedInRole(Roles.QUARTERMASTER);
    	Parent root = FXMLLoader.load(
    	        getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/AddStockChangesPage.fxml")
    	    );

    	    Stage stage = (Stage) this.addStockChangesButton.getScene().getWindow();
    	    Scene scene = new Scene(root);
    	    stage.setScene(scene);
    	    stage.show();
    }

    @FXML
    void goToViewStockLanding(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(
    	        getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/ViewStockChangesPage.fxml")
    	    );

    	    Stage stage = (Stage) this.addStockChangesButton.getScene().getWindow();
    	    Scene scene = new Scene(root);
    	    stage.setScene(scene);
    	    stage.show();
    }
    
    @FXML
    void backToLoginPage(ActionEvent event) throws IOException {
    	Stage stage = (Stage) this.logoutButton.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource(
            "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"
        ));

        stage.setScene(new Scene(root));
        stage.show(); 
    }

    @FXML
    void initialize() {
        assert this.addStockChangesButton != null : "fx:id=\"addStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
        assert this.greetingLabel != null : "fx:id=\"greetingLabel\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";
        assert this.viewStockChangesButton != null : "fx:id=\"viewStockChangesButton\" was not injected: check your FXML file 'QuarterMasterLandingPage.fxml'.";

    }

}

