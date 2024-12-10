package org.example.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.NavigationManager;
import org.example.ServiceLocator;

import java.io.IOException;

public abstract class BaseController {
    protected Stage primaryStage;
    protected ServiceLocator serviceLocator;
    protected NavigationManager navigationManager;

    public void initializeDependencies(Stage primaryStage, ServiceLocator serviceLocator, NavigationManager navigationManager) {
        this.primaryStage = primaryStage;
        this.serviceLocator = serviceLocator;
        this.navigationManager = navigationManager;
    }
    @FXML
    public void goBackToMainMenu() {
        navigationManager.navigateTo("/View/MainView.fxml");
    }

    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
