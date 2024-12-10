package org.example;

import org.example.Model.CalculationHistory;
import org.example.Service.*;

public class ServiceLocator {
    private final EasyCalculatorService easyCalculatorService;
    private final HardCalculatorService hardCalculatorService;
    private final CalculationHistory calculationHistory;
    private final FunctionService functionService;

    public ServiceLocator() {
/*        this.easyCalculatorService = new EasyCalculatorServiceImpl();
        this.hardCalculatorService = new HardCalculatorServiceImpl();*/
        this.calculationHistory = new CalculationHistory();
        //this.functionService = new FunctionServiceImpl(hardCalculatorService, calculationHistory);
        this.functionService = new FunctionServiceImpl(calculationHistory);
        this.easyCalculatorService = new EasyCalculatorServiceImpl(calculationHistory);
        this.hardCalculatorService = new HardCalculatorServiceImpl(calculationHistory);
    }

    public HardCalculatorService getHardCalculatorService() {
        return hardCalculatorService;
    }
    public EasyCalculatorService getEasyCalculatorService() {
        return easyCalculatorService;
    }
    public CalculationHistory getCalculationHistory() {
        return calculationHistory;
    }
    public FunctionService getFunctionService() {
        return functionService;
    }
}
