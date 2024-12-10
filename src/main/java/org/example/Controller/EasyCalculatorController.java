package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.Service.EasyCalculatorService;

public class EasyCalculatorController extends BaseController {
    @FXML
    private TextField operand1Field;
    @FXML
    private TextField operand2Field;
    @FXML
    private TextField operationField;
    @FXML
    private TextField resultField;

    private EasyCalculatorService calculatorService;

    public void setCalculatorService(EasyCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @FXML
    private void calculate() {
        try {
            double operand1 = Double.parseDouble(operand1Field.getText());
            double operand2 = Double.parseDouble(operand2Field.getText());
            String operation = operationField.getText();

            double result = calculatorService.calculate(operand1, operand2, operation);
            resultField.setText(String.valueOf(result));
        } catch (NumberFormatException e) {
            showError("Wprowadzono nieprawidłową liczbę.");
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Wystąpił nieoczekiwany błąd.");
        }
    }

    @FXML
    private void goBack() {
        goBackToMainMenu();
    }
}
