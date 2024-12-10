package org.example.Service;

public interface EasyCalculatorService {
    double add(double a, double b);
    double subtract(double a, double b);
    double multiply(double a, double b);
    double divide(double a, double b);
    double power(double base, double exponent);
    double sqrt(double value);

    double calculate(double operand1, double operand2, String operation);
}
