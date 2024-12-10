package org.example;

import javafx.util.Callback;
import org.example.Controller.*;

public class ControllerFactory implements Callback<Class<?>, Object> {
    private final ServiceLocator serviceLocator;
    private NavigationManager navigationManager;

    public ControllerFactory(ServiceLocator serviceLocator, NavigationManager navigationManager) {
        this.serviceLocator = serviceLocator;
        this.navigationManager = navigationManager;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        try {
            Object controller = controllerClass.getDeclaredConstructor().newInstance();

            if (controller instanceof BaseController) {
                ((BaseController) controller).initializeDependencies(
                        navigationManager.getPrimaryStage(),
                        serviceLocator,
                        navigationManager
                );

                // Specyficzne zależności dla niektórych kontrolerów
                if (controller instanceof EasyCalculatorController) {
                    ((EasyCalculatorController) controller)
                            .setCalculatorService(serviceLocator.getEasyCalculatorService());
                } else if (controller instanceof HardCalculatorController) {
                    ((HardCalculatorController) controller)
                            .setCalculatorService(serviceLocator.getHardCalculatorService());
                } else if (controller instanceof FunctionController) {
                    ((FunctionController) controller)
                            .setFunctionService(serviceLocator.getFunctionService());
                }
                // Dodaj inne specyficzne zależności tutaj
            }

            return controller;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setNavigationManager(NavigationManager navigationManager) {
        this.navigationManager = navigationManager;
    }
}
