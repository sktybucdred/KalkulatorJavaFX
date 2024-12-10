package org.example.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import org.example.Model.FunctionModel;
import org.example.Service.FunctionService;

import java.util.ArrayList;
import java.util.List;

public class FunctionController extends BaseController {
    @FXML
    private TextField functionField;
    @FXML
    private TextField minXField;
    @FXML
    private TextField maxXField;
    @FXML
    private TextField stepField;
    @FXML
    private Button drawButton;
    @FXML
    private Button removeButton;
    @FXML
    private ListView<String> functionListView;
    @FXML
    private LineChart<Number, Number> functionChart;
    @FXML
    private Label errorLabel;

    private FunctionService functionService;
    private ObservableList<FunctionModel> functions;


    // Setter do wstrzyknięcia FunctionService
    public void setFunctionService(FunctionService functionService) {
        this.functionService = functionService;
    }


    @FXML
    private void initialize() {
        setupChart();
        setupFunctionListView();
        removeButton.setDisable(true);
    }

    private void setupChart() {
        functionChart.setCreateSymbols(true);
    }

    private void setupFunctionListView() {
        functions = FXCollections.observableArrayList();
        functionListView.setItems(FXCollections.observableArrayList());
        functionListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeButton.setDisable(newValue == null);
        });
    }

    @FXML
    private void handleDrawFunction() {
        String function = functionField.getText();
        String minXText = minXField.getText();
        String maxXText = maxXField.getText();
        String stepText = stepField.getText();

        if (function == null || function.trim().isEmpty()) {
            errorLabel.setText("Funkcja nie może być pusta.");
            return;
        }

        double minX = -10;
        double maxX = 10;
        double step = 0.5;

        try {
            if (!minXText.isEmpty()) {
                minX = Double.parseDouble(minXText);
            }
            if (!maxXText.isEmpty()) {
                maxX = Double.parseDouble(maxXText);
            }
            if (!stepText.isEmpty()) {
                step = Double.parseDouble(stepText);
                if (step <= 0) {
                    throw new IllegalArgumentException("Krok musi być dodatni.");
                }
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Zakres X i krok muszą być liczbami.");
            return;
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            return;
        }

        try {
            List<Double> points = functionService.calculateFunction(function, minX, maxX, step);
            FunctionModel functionModel = new FunctionModel(function, function);
            plotFunction(functionModel, points, minX, step);
            functions.add(functionModel);
            functionListView.getItems().add(functionModel.getName());
            errorLabel.setText("");
            functionField.clear();
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            functionChart.getData().clear();
        } catch (Exception e) {
            errorLabel.setText("Wystąpił nieoczekiwany błąd.");
            functionChart.getData().clear();
        }
    }

    private void plotFunction(FunctionModel functionModel, List<Double> points, double minX, double step) {
        List<XYChart.Series<Number, Number>> subSeriesList = new ArrayList<>();
        XYChart.Series<Number, Number> currentSeries = new XYChart.Series<>();
        currentSeries.setName(functionModel.getName() + " Part 1");
        subSeriesList.add(currentSeries);

        double x = minX;
        int subSeriesCount = 1;

        for (Double y : points) {
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                if (!currentSeries.getData().isEmpty()) {
                    currentSeries = new XYChart.Series<>();
                    subSeriesCount++;
                    currentSeries.setName(functionModel.getName() + " Part " + subSeriesCount);
                    subSeriesList.add(currentSeries);
                }
            } else {
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(x, y);
                // Tworzymy węzeł symbolu
                Circle circle = new Circle(5);
                circle.setStyle("-fx-fill: red;");
                // Upewniamy się, że węzeł reaguje na mysz
                circle.setMouseTransparent(false);
                circle.setPickOnBounds(true);
                dataPoint.setNode(circle);
                currentSeries.getData().add(dataPoint);
            }
            x += step;
        }

        for (XYChart.Series<Number, Number> series : subSeriesList) {
            if (!series.getData().isEmpty()) {
                functionChart.getData().add(series);
                functionModel.addSubSeries(series);
            }
        }

        // Wymuszamy przeliczenie stylów i układu po dodaniu wszystkich danych
        Platform.runLater(() -> {
            functionChart.applyCss();
            functionChart.layout();

            for (XYChart.Series<Number, Number> series : functionModel.getSubSeriesList()) {
                for (XYChart.Data<Number, Number> data : series.getData()) {
                    Node node = data.getNode();
                    if (node != null) {
                        Tooltip tooltip = new Tooltip("x: " + data.getXValue() + "\ny: " + data.getYValue());
                        // Zamiast Tooltip.install, sterujemy ręcznie wyświetlaniem, aby mieć pewność
                        node.setOnMouseEntered(e -> {
                            // Wyświetlamy tooltip ręcznie w miejscu kursora
                            tooltip.show(node, e.getScreenX() + 10, e.getScreenY() + 10);
                        });
                        node.setOnMouseExited(e -> tooltip.hide());
                    }
                }
            }
        });
    }



    @FXML
    private void handleRemoveFunction() {
        int selectedIndex = functionListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            FunctionModel functionToRemove = functions.get(selectedIndex);
            // Usuwamy wszystkie subserii należące do tej funkcji
            for (XYChart.Series<Number, Number> s : functionToRemove.getSubSeriesList()) {
                functionChart.getData().remove(s);
            }
            functions.remove(selectedIndex);
            functionListView.getItems().remove(selectedIndex);
            errorLabel.setText("");
        }
    }
}
