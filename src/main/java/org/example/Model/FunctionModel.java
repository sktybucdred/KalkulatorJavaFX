package org.example.Model;

import javafx.scene.chart.XYChart;

public class FunctionModel {
    private String name;
    private String expression;
    private XYChart.Series<Number, Number> series;

    public FunctionModel(String name, String expression) {
        this.name = name;
        this.expression = expression;
        this.series = new XYChart.Series<>();
        this.series.setName(name);
    }

    public String getName() {
        return name;
    }

    public String getExpression() {
        return expression;
    }

    public XYChart.Series<Number, Number> getSeries() {
        return series;
    }
}

