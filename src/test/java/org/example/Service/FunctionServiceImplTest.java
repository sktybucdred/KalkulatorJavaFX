package org.example.Service;

import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.HardCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FunctionServiceImplTest {

    private CalculationHistory calculationHistory;
    private FunctionServiceImpl functionService;

    @BeforeEach
    void setUp() {
        calculationHistory = new CalculationHistory();
        functionService = new FunctionServiceImpl(calculationHistory);
    }

    @Test
    void testSimpleLinearFunction() {
        String function = "x + 1";
        double minX = 0;
        double maxX = 5;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(1.0, 2.0, 3.0, 4.0, 5.0, 6.0);

        assertEquals(expected, results, "Simple linear function should return correct results");
        assertEquals(6, calculationHistory.getHistory().size(), "Six calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = expected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match");
        }
    }

    @Test
    void testQuadraticFunction() {
        String function = "x^2";
        double minX = -2;
        double maxX = 2;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(4.0, 1.0, 0.0, 1.0, 4.0);

        assertEquals(expected, results, "Quadratic function should return correct results");
        assertEquals(5, calculationHistory.getHistory().size(), "Five calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = expected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match");
        }
    }

    @Test
    void testFunctionWithUnaryMinus() {
        String function = "-x + 2";
        double minX = -2;
        double maxX = 2;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(4.0, 3.0, 2.0, 1.0, 0.0);

        assertEquals(expected, results, "Function with unary minus should return correct results");
        assertEquals(5, calculationHistory.getHistory().size(), "Five calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = expected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match");
        }
    }

    @Test
    void testFunctionWithMultiplicationAndDivision() {
        String function = "2*x / (x + 1)";
        double minX = 1;
        double maxX = 3;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(1.0, 1.33333333, 1.5); // Rounded to 2 decimal places: 1.0, 1.33, 1.5

        List<Double> roundedExpected = List.of(1.0, 1.33, 1.5);

        assertEquals(roundedExpected, results, "Function with multiplication and division should return correct results");
        assertEquals(3, calculationHistory.getHistory().size(), "Three calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = roundedExpected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-2, "Recorded result should match");
        }
    }

    @Test
    void testFunctionWithExponentiation() {
        String function = "x^3 - 2*x";
        double minX = -2;
        double maxX = 2;
        double step = 2;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(-4.0, 0.0, 4.0);

        assertEquals(expected, results, "Function with exponentiation should return correct results");
        assertEquals(3, calculationHistory.getHistory().size(), "Three calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = expected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match");
        }
    }

    @Test
    void testFunctionLeadingToInfinity() {
        String function = "1 / (x - 1)";
        double minX = 0;
        double maxX = 2;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(-1.0, Double.NaN, 1.0);

        assertEquals(expected, results, "Function leading to Infinity should return NaN for undefined points");
        assertEquals(2, calculationHistory.getHistory().size(), "Only valid calculations should be recorded");

        // x=0: 1/(0-1)= -1.0
        CalculationResult recorded0 = calculationHistory.getHistory().get(0);
        assertEquals(function + " with x=0.0", recorded0.getExpression(), "Recorded expression should match");
        assertEquals(-1.0, recorded0.getResult(), 1e-9, "Recorded result should match");

        // x=1: 1/(1-1)= Infinity -> NaN in results, not recorded
        // x=2: 1/(2-1)= 1.0
        CalculationResult recorded1 = calculationHistory.getHistory().get(1);
        assertEquals(function + " with x=2.0", recorded1.getExpression(), "Recorded expression should match");
        assertEquals(1.0, recorded1.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testFunctionWithNaNResult() {
        String function = "sqrt(x)";
        double minX = -1;
        double maxX = 1;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(Double.NaN, 0.0, 1.0);

        assertEquals(expected, results, "Function leading to NaN should return NaN for invalid points");
        assertEquals(2, calculationHistory.getHistory().size(), "Only valid calculations should be recorded");

        // x=-1: sqrt(-1)= NaN -> not recorded
        // x=0: sqrt(0)=0.0
        CalculationResult recorded0 = calculationHistory.getHistory().get(0);
        assertEquals(function + " with x=0.0", recorded0.getExpression(), "Recorded expression should match");
        assertEquals(0.0, recorded0.getResult(), 1e-9, "Recorded result should match");

        // x=1: sqrt(1)=1.0
        CalculationResult recorded1 = calculationHistory.getHistory().get(1);
        assertEquals(function + " with x=1.0", recorded1.getExpression(), "Recorded expression should match");
        assertEquals(1.0, recorded1.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testEmptyFunctionThrowsException() {
        String function = "";
        double minX = 0;
        double maxX = 1;
        double step = 0.1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            functionService.calculateFunction(function, minX, maxX, step);
        }, "Empty function should throw IllegalArgumentException");

        assertEquals("Funkcja nie może być pusta", exception.getMessage(), "Exception message should match");
        assertTrue(calculationHistory.getHistory().isEmpty(), "No calculations should be recorded");
    }

    @Test
    void testInvalidFunctionThrowsException() {
        String function = "2 + a*x";
        double minX = 0;
        double maxX = 1;
        double step = 0.1;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            functionService.calculateFunction(function, minX, maxX, step);
        }, "Invalid function should throw IllegalArgumentException");

        assertTrue(exception.getMessage().startsWith("Niepoprawne wyrażenie funkcji:"), "Exception message should indicate invalid expression");
        assertTrue(calculationHistory.getHistory().isEmpty(), "No calculations should be recorded");
    }

    @Test
    void testFunctionWithLargeYValues() {
        String function = "x^3";
        double minX = 1000;
        double maxX = 1002;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(Double.NaN, Double.NaN, Double.NaN); // 1000^3=1e9, 1001^3=~1.003e9, 1002^3=~1.006e9 >1e6

        assertEquals(expected, results, "Function with large y-values should return NaN due to limit");
        assertTrue(calculationHistory.getHistory().isEmpty(), "No calculations should be recorded as all y-values exceed the limit");
    }


    @Test
    void testMultipleCalculations() {
        String function1 = "x + 1";
        String function2 = "x^2";
        double minX1 = 0;
        double maxX1 = 2;
        double step1 = 1;

        double minX2 = -1;
        double maxX2 = 1;
        double step2 = 1;

        // First calculation
        List<Double> results1 = functionService.calculateFunction(function1, minX1, maxX1, step1);
        List<Double> expected1 = List.of(1.0, 2.0, 3.0);

        assertEquals(expected1, results1, "First function calculations should be correct");
        assertEquals(3, calculationHistory.getHistory().size(), "Three calculations should be recorded for first function");

        // Second calculation
        List<Double> results2 = functionService.calculateFunction(function2, minX2, maxX2, step2);
        List<Double> expected2 = List.of(1.0, 0.0, 1.0);

        assertEquals(expected2, results2, "Second function calculations should be correct");
        assertEquals(6, calculationHistory.getHistory().size(), "Six calculations should be recorded in total");

        // Verify first function's records
        for (int i = 0; i < results1.size(); i++) {
            double x = minX1 + i * step1;
            double y = expected1.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function1 + " with x=" + x, recorded.getExpression(), "Recorded expression should match for first function");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match for first function");
        }

        // Verify second function's records
        for (int i = 0; i < results2.size(); i++) {
            double x = minX2 + i * step2;
            double y = expected2.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(3 + i);
            assertEquals(function2 + " with x=" + x, recorded.getExpression(), "Recorded expression should match for second function");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match for second function");
        }
    }

    @Test
    void testFunctionWithIrregularSpaces() {
        String function = "  2*x    +   3  ";
        double minX = 1;
        double maxX = 3;
        double step = 1;

        List<Double> results = functionService.calculateFunction(function, minX, maxX, step);
        List<Double> expected = List.of(5.0, 7.0, 9.0);

        assertEquals(expected, results, "Function with irregular spaces should return correct results");
        assertEquals(3, calculationHistory.getHistory().size(), "Three calculations should be recorded");

        for (int i = 0; i < results.size(); i++) {
            double x = minX + i * step;
            double y = expected.get(i);
            CalculationResult recorded = calculationHistory.getHistory().get(i);
            assertEquals(function + " with x=" + x, recorded.getExpression(), "Recorded expression should match");
            assertEquals(y, recorded.getResult(), 1e-9, "Recorded result should match");
        }
    }
}
