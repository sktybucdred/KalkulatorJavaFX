package org.example.Controller;


import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.Model.HardCalculationResult;
import org.example.Service.HardCalculatorService;
import org.example.Service.HardCalculatorServiceImpl;


public class HardCalculatorController extends BaseController {
    @FXML
    private TextField expressionField;
    @FXML
    private TextField resultField;

    private HardCalculatorService calculatorService;

    public void setCalculatorService(HardCalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @FXML
    private void calculate() {
        try {
            String expression = expressionField.getText();
            double result = calculatorService.calculate(expression);
            resultField.setText(String.valueOf(result));
        } catch (Exception e) {
            resultField.setText("Błąd: " + e.getMessage());
        }
    }
}
