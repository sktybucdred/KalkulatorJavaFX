package org.example.Service;

import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.EasyCalculationResult;

public class EasyCalculatorServiceImpl implements EasyCalculatorService {
    private final CalculationHistory calculationHistory;

    public EasyCalculatorServiceImpl(CalculationHistory calculationHistory) {
        this.calculationHistory = calculationHistory;
    }
    @Override
    public double add(double a, double b) {
        double result = a + b;
        recordCalculation(a, b, "+", result);
        return result;
    }

    @Override
    public double subtract(double a, double b) {
        double result = a - b;
        recordCalculation(a, b, "-", result);
        return result;
    }

    @Override
    public double multiply(double a, double b) {
        double result = a * b;
        recordCalculation(a, b, "*", result);
        return result;
    }

    @Override
    public double divide(double a, double b) {
        if(b == 0) {
            throw new IllegalArgumentException("Nie można dzielić przez zero.");
        }
        double result = a / b;
        recordCalculation(a, b, "/", result);
        return result;
    }

    @Override
    public double power(double base, double exponent) {
        double result = Math.pow(base, exponent);
        recordCalculation(base, exponent, "^", result);
        return result;
    }

    @Override
    public double sqrt(double value) {
        if(value < 0) {
            throw new IllegalArgumentException("Nie można obliczyć pierwiastka kwadratowego z liczby ujemnej.");
        }
        double result = Math.sqrt(value);
        recordCalculation(value, 0, "sqrt", result); // drugi operand jest ignorowany
        return result;
    }

    @Override
    public double calculate(double a, double b, String operation) {
        double result;
        switch (operation) {
            case "+":
                result = add(a, b);
                break;
            case "-":
                result = subtract(a, b);
                break;
            case "*":
                result = multiply(a, b);
                break;
            case "/":
                result = divide(a, b);
                break;
            case "^":
                result = power(a, b);
                break;
            case "sqrt":
                result = sqrt(a);
                break;
            default:
                throw new IllegalArgumentException("Nieznany operator: " + operation);
        }
        return result;
    }

    private void recordCalculation(double a, double b, String operation, double result) {
        if(calculationHistory != null) {
            String expression = (operation.equals("sqrt")) ? "sqrt(" + a + ")" : a + " " + operation + " " + b;
            CalculationResult calculationResult = new EasyCalculationResult(expression, result);
            calculationHistory.addCalculationResult(calculationResult);
        }
    }
}
