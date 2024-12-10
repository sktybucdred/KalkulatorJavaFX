package org.example.Controller;

import javafx.fxml.FXML;

public class MainController extends BaseController {

    @FXML
    private void handleEasyCalculator() {
        navigationManager.navigateTo("/View/EasyCalculatorView.fxml");
    }

    @FXML
    private void handleHardCalculator() {
        navigationManager.navigateTo("/View/HardCalculatorView.fxml");
    }

    @FXML
    private void handleHistory() {
        navigationManager.navigateTo("/View/HistoryView.fxml");
    }

    @FXML
    private void handleFunctions() {
        navigationManager.navigateTo("/View/FunctionView.fxml");
    }

    @FXML
    private void handleExit() {
        navigationManager.closeApplication();
    }
}
