package org.example.Model;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;

public class FunctionModel {
    private String name;
    private String expression;
    private List<XYChart.Series<Number, Number>> subSeriesList = new ArrayList<>();

    public FunctionModel(String name, String expression) {
        this.name = name;
        this.expression = expression;
        this.subSeriesList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public List<XYChart.Series<Number, Number>> getSubSeriesList() {
        return subSeriesList;
    }

    public void addSubSeries(XYChart.Series<Number, Number> series) {
        subSeriesList.add(series);
    }
}

