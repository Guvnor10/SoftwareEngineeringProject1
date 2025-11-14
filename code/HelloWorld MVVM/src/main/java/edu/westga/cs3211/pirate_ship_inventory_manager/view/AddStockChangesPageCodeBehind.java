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
import javafx.stage.Stage;

public class AddStockChangesPageCodeBehind {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addStockButton;

    @FXML
    private Button homeButton;

    @FXML
    private Button logoutButton;
    
    private static String role;

    /**
     * Call this method before loading this page so we know the user's role.
     */
    public static void setUserRole(String userRole) {
        role = userRole;
    }

    @FXML
    void handleSubmit(ActionEvent event) {

    }
    
    @FXML
    void backToLandingPage() throws IOException {
    	 Stage stage = (Stage) homeButton.getScene().getWindow();

         Parent root;

         if ("QUARTERMASTER".equals(role)) {
             root = FXMLLoader.load(getClass().getResource(
                 "/edu/westga/cs3211/pirate_ship_inventory_manager/view/QuarterMasterLandingPage.fxml"
             ));
         } else {
             root = FXMLLoader.load(getClass().getResource(
                 "/edu/westga/cs3211/pirate_ship_inventory_manager/view/CrewmateLandingPage.fxml"
             ));
         }

         stage.setScene(new Scene(root));
         stage.show();
     
    }

    @FXML
    void backToLoginPage() throws IOException {
    	 Stage stage = (Stage) logoutButton.getScene().getWindow();

         Parent root = FXMLLoader.load(getClass().getResource(
             "/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"
         ));

         stage.setScene(new Scene(root));
         stage.show();   	
    }
    
    @FXML
    void initialize() {
        assert addStockButton != null : "fx:id=\"addStockButton\" was not injected: check your FXML file 'AddStockChangesPage.fxml'.";
        assert homeButton != null : "fx:id=\"homeButton\" was not injected: check your FXML file 'AddStockChangesPage.fxml'.";
        assert logoutButton != null : "fx:id=\"logoutButton\" was not injected: check your FXML file 'AddStockChangesPage.fxml'.";

    }

}
