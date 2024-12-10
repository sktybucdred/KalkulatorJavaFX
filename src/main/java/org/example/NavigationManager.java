package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.Controller.BaseController;

import java.io.IOException;

public class NavigationManager {
    private final Stage primaryStage;
    private final ControllerFactory controllerFactory;
    private final ServiceLocator serviceLocator;

    public NavigationManager(Stage primaryStage, ControllerFactory controllerFactory, ServiceLocator serviceLocator) {
        this.primaryStage = primaryStage;
        this.controllerFactory = controllerFactory;
        this.serviceLocator = serviceLocator;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            loader.setControllerFactory(controllerFactory);
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/View/styles.css").toExternalForm());
            BaseController controller = loader.getController();
            primaryStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            showError("Nie udało się przełączyć sceny: " + e.getMessage());
        }
    }

    public void closeApplication() {
        primaryStage.close();
    }

    private void showError(String message) {
        // Implementacja wyświetlania błędu użytkownikowi, np. Alert
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
