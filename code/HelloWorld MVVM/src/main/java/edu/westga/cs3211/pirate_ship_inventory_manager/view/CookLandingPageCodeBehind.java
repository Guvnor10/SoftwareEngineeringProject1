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

public class CookLandingPageCodeBehind {

    @FXML
    private Button addStockButton;

    @FXML
    private Label greetingLabel;

    @FXML
    private Button logoutButton;

    @FXML
    void backToLoginPage(ActionEvent event) throws IOException {
    	Stage stage = (Stage) this.logoutButton.getScene().getWindow();

		Parent root = FXMLLoader
				.load(getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/LoginPage.fxml"));

		stage.setScene(new Scene(root));
		stage.show();
    }

    @FXML
    void viewFood(ActionEvent event) throws IOException {
    	Stage stage = (Stage) this.logoutButton.getScene().getWindow();

		Parent root = FXMLLoader
				.load(getClass().getResource("/edu/westga/cs3211/pirate_ship_inventory_manager/view/ViewFoodPage.fxml"));

		stage.setScene(new Scene(root));
		stage.show();
    }

}
