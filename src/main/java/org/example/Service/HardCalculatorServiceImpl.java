package org.example.Service;

import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;
import org.example.Model.HardCalculationResult;

import java.util.Stack;

public class HardCalculatorServiceImpl implements HardCalculatorService {
    private CalculationHistory calculationHistory;

    public HardCalculatorServiceImpl(CalculationHistory calculationHistory) {
        this.calculationHistory = calculationHistory;
    }
    public double calculate(String expression) {
        validateExpression(expression);
        String rpn = convertToReversePolishNotation(expression);
        double result = evaluateReversePolishNotation(rpn);
        recordCalculation(expression, result);
        return result;
    }

    private double evaluateReversePolishNotation(String expression) {
        Stack<Double> operands = new Stack<>();
        for (String token : expression.split(" ")) {
            if (token.isEmpty()) {
                continue;
            }
            if (isNumeric(token)) {
                operands.push(Double.parseDouble(token));
            } else if (token.equals("~")) {
                double operand = operands.pop();
                operands.push(-operand);
            } else {
                double operand2 = operands.pop();
                double operand1 = operands.pop();
                switch (token) {
                    case "+":
                        operands.push(operand1 + operand2);
                        break;
                    case "-":
                        operands.push(operand1 - operand2);
                        break;
                    case "*":
                        operands.push(operand1 * operand2);
                        break;
                    case "/":
                        operands.push(operand1 / operand2);
                        break;
                    case "^":
                        operands.push(Math.pow(operand1, operand2));
                        break;
                    default:
                        throw new IllegalArgumentException("Nieznany operator: " + token);
                }
            }
        }
        return operands.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isUnaryMinus(char token, int i, String expression) {
        if (token != '-') {
            return false;
        }
        if (i == 0) {
            return true;
        }
        char prevToken = expression.charAt(i - 1);
        return prevToken == '(' || isOperator(prevToken);
    }

    private String convertToReversePolishNotation(String expression) {
        if(expression.isEmpty()){
            throw new IllegalArgumentException("Puste wyrażenie");
        }
        StringBuilder output = new StringBuilder();
        Stack<Character> operators = new Stack<>();
        int i = 0;

        while (i < expression.length()) {
            char token = expression.charAt(i);

            // Parsowanie liczby
            if (Character.isDigit(token) || (token == '.' && i > 0 && Character.isDigit(expression.charAt(i - 1)))) {
                StringBuilder number = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                output.append(number).append(' ');
                continue;
            }

            // Wykrywanie unarnego minusa
            else if (isUnaryMinus(token, i, expression)) {
                operators.push('~'); // Używamy '~' jako unarnego minusa
                i++;
            }

            // Obsługa nawiasów i operatorów
            else if (token == '(') {
                operators.push(token);
                i++;
            } else if (token == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    output.append(operators.pop()).append(' ');
                }
                if (!operators.isEmpty() && operators.peek() == '(') {
                    operators.pop(); // Usuń '(' ze stosu
                } else {
                    throw new IllegalArgumentException("Brakujący nawias otwierający");
                }
                // Obsługa unarnego minusa po zamknięciu nawiasu
                if (!operators.isEmpty() && operators.peek() == '~') {
                    output.append(operators.pop()).append(' ');
                }
                i++;
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && precedence(operators.peek()) >= precedence(token)) {
                    output.append(operators.pop()).append(' ');
                }
                operators.push(token);
                i++;
            } else if (Character.isWhitespace(token)) {
                i++;
            } else {
                throw new IllegalArgumentException("Nieznany znak: " + token);
            }
        }

        // Dodaj pozostałe operatory do wyjścia
        while (!operators.isEmpty()) {
            char op = operators.pop();
            if (op == '(') {
                throw new IllegalArgumentException("Brakujący nawias zamykający");
            }
            output.append(op).append(' ');
        }

        return output.toString().trim();
    }

    private boolean isOperator(char token) {
        return token == '+' || token == '-' || token == '*' || token == '/' || token == '^' || token == '~';
    }

    private int precedence(char operator) {
        switch (operator) {
            case '~':
                return 4; // Unarny minus ma najwyższy priorytet
            case '^':
                return 3;
            case '*':
            case '/':
                return 2;
            case '+':
            case '-':
                return 1;
            default:
                return -1;
        }
    }

    private void validateExpression(String expression) {
        for (int i = 0; i < expression.length(); i++) {
            char token = expression.charAt(i);
            if (!Character.isDigit(token) && token != '+' && token != '-' && token != '*' && token != '/' &&
                    token != '(' && token != ')' && token != '.' && token != '^' && !Character.isWhitespace(token)) {
                throw new IllegalArgumentException("Wyrażenie zawiera niepoprawny znak: " + token);
            }
        }
    }
    private void recordCalculation(String expression, double result) {
        if(calculationHistory != null) {
            String normalizedExpression = expression.trim().replaceAll("\\s+", " ");
            CalculationResult calculationResult = new HardCalculationResult(normalizedExpression, result);
            calculationHistory.addCalculationResult(calculationResult);
        }
    }

}
