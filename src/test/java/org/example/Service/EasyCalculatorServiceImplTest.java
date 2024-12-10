// EasyCalculatorServiceImplTest.java
package org.example.Service;

import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.EasyCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EasyCalculatorServiceImplTest {

    private CalculationHistory calculationHistory;
    private EasyCalculatorServiceImpl calculatorService;

    @BeforeEach
    void setUp() {
        calculationHistory = new CalculationHistory();
        calculatorService = new EasyCalculatorServiceImpl(calculationHistory);
    }

    @Test
    void testAdd() {
        double a = 5.0;
        double b = 3.0;
        double expected = 8.0;

        double result = calculatorService.add(a, b);

        assertEquals(expected, result, 1e-9, "Addition result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("5.0 + 3.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testSubtract() {
        double a = 10.0;
        double b = 4.0;
        double expected = 6.0;

        double result = calculatorService.subtract(a, b);

        assertEquals(expected, result, 1e-9, "Subtraction result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("10.0 - 4.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testMultiply() {
        double a = 4.0;
        double b = 5.0;
        double expected = 20.0;

        double result = calculatorService.multiply(a, b);

        assertEquals(expected, result, 1e-9, "Multiplication result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("4.0 * 5.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testDivide() {
        double a = 10.0;
        double b = 2.0;
        double expected = 5.0;

        double result = calculatorService.divide(a, b);

        assertEquals(expected, result, 1e-9, "Division result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("10.0 / 2.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testDivideByZeroThrowsException() {
        double a = 10.0;
        double b = 0.0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.divide(a, b);
        });

        assertEquals("Nie można dzielić przez zero.", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testPower() {
        double base = 2.0;
        double exponent = 3.0;
        double expected = 8.0;

        double result = calculatorService.power(base, exponent);

        assertEquals(expected, result, 1e-9, "Power result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("2.0 ^ 3.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testSqrt() {
        double a = 16.0;
        double expected = 4.0;

        double result = calculatorService.sqrt(a);

        assertEquals(expected, result, 1e-9, "Square root result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("sqrt(16.0)", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testSqrtOfNegativeNumberThrowsException() {
        double a = -4.0;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.sqrt(a);
        });

        assertEquals("Nie można obliczyć pierwiastka kwadratowego z liczby ujemnej.", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testCalculateWithAddition() {
        double a = 7.0;
        double b = 3.0;
        String operation = "+";
        double expected = 10.0;

        double result = calculatorService.calculate(a, b, operation);

        assertEquals(expected, result, 1e-9, "Calculate addition result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("7.0 + 3.0", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }

    @Test
    void testCalculateWithUnknownOperatorThrowsException() {
        double a = 2.0;
        double b = 3.0;
        String operation = "%";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(a, b, operation);
        });

        assertEquals("Nieznany operator: %", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testCalculateWithSqrt() {
        double a = 25.0;
        double b = 0.0; // Ignorowany w przypadku sqrt
        String operation = "sqrt";
        double expected = 5.0;

        double result = calculatorService.calculate(a, b, operation);

        assertEquals(expected, result, 1e-9, "Calculate sqrt result should be correct");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals("sqrt(25.0)", recorded.getExpression(), "Expression should be recorded correctly");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match the calculation");
    }
}
