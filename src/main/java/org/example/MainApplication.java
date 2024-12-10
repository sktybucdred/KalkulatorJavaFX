package org.example;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    private ServiceLocator serviceLocator;
    private ControllerFactory controllerFactory;
    private NavigationManager navigationManager;

    @Override
    public void start(Stage primaryStage) {
        // Krok 1: Inicjalizacja ServiceLocator
        serviceLocator = new ServiceLocator();

        // Krok 2: Inicjalizacja ControllerFactory
        controllerFactory = new ControllerFactory(serviceLocator, null);

        // Krok 3: Inicjalizacja NavigationManager
        navigationManager = new NavigationManager(primaryStage, controllerFactory, serviceLocator);

        // Krok 4: Aktualizacja ControllerFactory z NavigationManager
        controllerFactory.setNavigationManager(navigationManager);

        // Krok 5: Załadowanie początkowego widoku
        navigationManager.navigateTo("/View/MainView.fxml");

        primaryStage.setTitle("Kalkulator i Funkcje - JavaFX");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
