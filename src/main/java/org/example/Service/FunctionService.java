package org.example.Service;

import java.util.List;

public interface FunctionService {
    //void validateFunction(String function);

    List<Double> calculateFunction(String function, double minX, double maxX, double step);
}
