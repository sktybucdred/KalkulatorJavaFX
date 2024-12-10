package org.example.Service;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.HardCalculationResult;

import java.util.ArrayList;
import java.util.List;

public class FunctionServiceImpl implements FunctionService {
    private final CalculationHistory calculationHistory;
    public FunctionServiceImpl(CalculationHistory calculationHistory) {
        this.calculationHistory = calculationHistory;
    }
    @Override
    public List<Double> calculateFunction(String function, double minX, double maxX, double step) {
        if (function == null || function.isEmpty()) {
            throw new IllegalArgumentException("Funkcja nie może być pusta");
        }
        if (step == 0) {
            throw new IllegalArgumentException("Krok musi być różny od zera.");
        }
        List<Double> results = new ArrayList<>();
        Expression expression;
        if (step < 0) {
            step = Math.abs(step);
        }
        if (maxX < minX) {
            double temp = maxX;
            maxX = minX;
            minX = temp;
        }
        try {
            expression = new ExpressionBuilder(function)
                    .variable("x")
                    .build();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Niepoprawne wyrażenie funkcji: " + e.getMessage());
        }

        for (double x = minX; x <= maxX; x += step) {
            try {
                double y = expression.setVariable("x", x).evaluate();
                if (Double.isInfinite(y) || Double.isNaN(y) || Math.abs(y) > 1e6) { // Dodanie limitu
                    results.add(Double.NaN);
                    continue;
                }
                double roundedValue = Math.round(y * 100.0) / 100.0;
                results.add(roundedValue);

                // Dodanie obliczenia do historii
                CalculationResult calculationResult = new HardCalculationResult(function + " with x=" + x, roundedValue);
                calculationHistory.addCalculationResult(calculationResult);
            } catch (ArithmeticException | IllegalArgumentException e) {
                results.add(Double.NaN);
            }
        }
        return results;
    }

/*    public List<Double> calculateFunction(String function) {
        if(function == null || function.isEmpty()) {
            throw new IllegalArgumentException("Funkcja nie może być pusta");
        }
        validateFunction(function);
        List<Double> results = new ArrayList<>();
        for(double x = -10; x <= 10; x+=0.5) {
            String currentExpression = preprocessFunction(function);
            currentExpression = currentExpression.replace("x", Double.toString(x));
            try{
                double y = calculatorService.calculate(currentExpression);
                if(Double.isInfinite(y) || Double.isNaN(y)) {
                    results.add(Double.NaN);
                    continue;
                }
                double roundedValue = Math.round(y * 100.0) / 100.0;
                results.add(roundedValue);
            }catch(Exception e){
                results.add(Double.NaN);
            }
        }
        return results;
    }
    private String preprocessFunction(String function) {
        // Dodaj '*' między liczbą a 'x' (np. 4x -> 4*x)
        function = function.replaceAll("(\\d)(x)", "$1*$2");
        // Dodaj '*' między 'x' a liczbą (np. x2 -> x*2)
        function = function.replaceAll("(x)(\\d)", "$1*$2");
        // Dodaj '*' między ')' a liczbą lub 'x' (np. )(x -> )*x)
        function = function.replaceAll("\\)(\\d|x)", ")*$1");
        // Dodaj '*' między liczbą lub 'x' a '(' (np. x( -> x*( )
        function = function.replaceAll("(\\d|x)\\(", "$1*(");
        // Dodaj '*' między ')' a '(' (np. )( -> )*()
        function = function.replaceAll("\\)\\(", ")*(");
        return function;
    }

    public void validateFunction(String function) {
        for (char token : function.toCharArray()) {
            if (!Character.isDigit(token) && token != 'x' && token != '+' && token != '-' && token != '*' &&
                    token != '/' && token != '(' && token != ')' && token != '.' && token != '^' && !Character.isWhitespace(token)) {
                throw new IllegalArgumentException("Funkcja zawiera niepoprawny znak: " + token);
            }
        }
    }*/

}
