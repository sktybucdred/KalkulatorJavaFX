package org.example.Model;

public class EasyCalculationResult implements CalculationResult{
    private String expression;

    private double result;

    public EasyCalculationResult(String expression, double result) {
        this.expression = expression;
        this.result = result;
    }

    public String getExpression(){
        return expression;
    }

    public double getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Wyrażenie: " + expression + ", wynik: " + result;
    }
}
