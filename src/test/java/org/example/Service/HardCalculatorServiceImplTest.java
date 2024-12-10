package org.example.Service;

import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.HardCalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HardCalculatorServiceImplTest {

    private CalculationHistory calculationHistory;
    private HardCalculatorServiceImpl calculatorService;

    @BeforeEach
    void setUp() {
        calculationHistory = new CalculationHistory();
        calculatorService = new HardCalculatorServiceImpl(calculationHistory);
    }

    @Test
    void testSimpleAddition() {
        String expression = "2 + 3";
        double expected = 5.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Simple addition should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testMixedOperations() {
        String expression = "2 + 3 * 4";
        double expected = 14.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Mixed operations should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testParentheses() {
        String expression = "(2 + 3) * 4";
        double expected = 20.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Operations with parentheses should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testUnaryMinus() {
        String expression = "-3 + 2";
        double expected = -1.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Expression with unary minus should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testExponentiation() {
        String expression = "2 ^ 3";
        double expected = 8.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Exponentiation should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testComplexExpression() {
        String expression = "-3 + (2 * 4) - 5 / (1 + 1)";
        double expected = -3 + (2 * 4) - (double) 5 / (1 + 1); // = -3 + 8 - 2.5 = 2.5

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Complex expression should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testEmptyExpressionThrowsException() {
        String expression = "";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(expression);
        }, "Empty expression should throw IllegalArgumentException");

        assertEquals("Puste wyrażenie", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testInvalidCharactersThrowsException() {
        String expression = "2 + a";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(expression);
        }, "Expression with invalid characters should throw IllegalArgumentException");

        assertEquals("Wyrażenie zawiera niepoprawny znak: a", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testMismatchedParenthesesThrowsException() {
        String expression = "(2 + 3";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(expression);
        }, "Expression with mismatched parentheses should throw IllegalArgumentException");

        assertEquals("Brakujący nawias zamykający", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }
    @Test
    void testExtraClosingParenthesisThrowsException() {
        String expression = "2 + 3)";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(expression);
        }, "Expression with extra closing parenthesis should throw IllegalArgumentException");

        assertEquals("Brakujący nawias otwierający", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }


    @Test
    void testDivisionByZeroThrowsException() {
        String expression = "4 / 0";
        double result = calculatorService.calculate(expression);
        assertTrue(Double.isInfinite(result), "Division by zero should return Infinity");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");
    }

    @Test
    void testUnknownOperatorThrowsException() {
        String expression = "2 % 3";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(expression);
        }, "Expression with unknown operator should throw IllegalArgumentException");

        assertEquals("Wyrażenie zawiera niepoprawny znak: %", exception.getMessage(), "Exception message should match");
        assertEquals(0, calculationHistory.getHistory().size(), "No calculation should be recorded");
    }

    @Test
    void testExpressionWithMultipleUnaryMinus() {
        String expression = "-3 + (-2) * 4";
        double expected = -3 + (-2) * 4; // = -3 - 8 = -11

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Expression with multiple unary minuses should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testExpressionWithDecimalNumbers() {
        String expression = "3.5 * 2.2";
        double expected = 3.5 * 2.2; // = 7.7

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Expression with decimal numbers should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testComplexNestedParentheses() {
        String expression = "((2 + 3) * (4 - 2)) / (1 + 1)";
        double expected = ((2 + 3) * (4 - 2)) / (1 + 1); // = (5 * 2) / 2 = 5.0

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Expression with nested parentheses should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression, recorded.getExpression(), "Recorded expression should match");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }

    @Test
    void testMultipleCalculations() {
        String expression1 = "1 + 1";
        double expected1 = 2.0;

        String expression2 = "5 * 3";
        double expected2 = 15.0;

        String expression3 = "10 / 2";
        double expected3 = 5.0;

        double result1 = calculatorService.calculate(expression1);
        double result2 = calculatorService.calculate(expression2);
        double result3 = calculatorService.calculate(expression3);

        assertEquals(expected1, result1, 1e-9, "First calculation should return correct result");
        assertEquals(expected2, result2, 1e-9, "Second calculation should return correct result");
        assertEquals(expected3, result3, 1e-9, "Third calculation should return correct result");
        assertEquals(3, calculationHistory.getHistory().size(), "Three calculations should be recorded");

        CalculationResult recorded1 = calculationHistory.getHistory().get(0);
        assertEquals(expression1, recorded1.getExpression(), "First recorded expression should match");
        assertEquals(expected1, recorded1.getResult(), 1e-9, "First recorded result should match");

        CalculationResult recorded2 = calculationHistory.getHistory().get(1);
        assertEquals(expression2, recorded2.getExpression(), "Second recorded expression should match");
        assertEquals(expected2, recorded2.getResult(), 1e-9, "Second recorded result should match");

        CalculationResult recorded3 = calculationHistory.getHistory().get(2);
        assertEquals(expression3, recorded3.getExpression(), "Third recorded expression should match");
        assertEquals(expected3, recorded3.getResult(), 1e-9, "Third recorded result should match");
    }

    @Test
    void testExpressionWithSpaces() {
        String expression = "  2    +    3   *   4  ";
        double expected = 14.0;

        double result = calculatorService.calculate(expression);

        assertEquals(expected, result, 1e-9, "Expression with irregular spaces should return correct result");
        assertEquals(1, calculationHistory.getHistory().size(), "One calculation should be recorded");

        CalculationResult recorded = calculationHistory.getHistory().get(0);
        assertEquals(expression.trim().replaceAll(" +", " "), recorded.getExpression(), "Recorded expression should match normalized input");
        assertEquals(expected, recorded.getResult(), 1e-9, "Recorded result should match");
    }
}
