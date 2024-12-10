package org.example.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
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
        removeButton.setDisable(true); // Początkowo przycisk usuwania jest wyłączony
    }

    private void setupChart() {
        functionChart.setCreateSymbols(false); // Wyłączenie punktów na wykresie mozna zmienic pozniej na true
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
            functionField.clear(); // Opcjonalnie: wyczyść pole funkcji po dodaniu
        } catch (IllegalArgumentException e) {
            errorLabel.setText(e.getMessage());
            functionChart.getData().clear();
        } catch (Exception e) {
            errorLabel.setText("Wystąpił nieoczekiwany błąd.");
            functionChart.getData().clear();
        }
    }

    private void plotFunction(FunctionModel functionModel, List<Double> points, double minX, double step) {
        // Lista sub-serii, które będziemy dodawać do wykresu
        List<XYChart.Series<Number, Number>> subSeriesList = new ArrayList<>();

        // Tworzymy pierwszą serię
        XYChart.Series<Number, Number> currentSeries = new XYChart.Series<>();
        currentSeries.setName(functionModel.getName() + " Part 1");
        subSeriesList.add(currentSeries);

        double x = minX;
        int subSeriesCount = 1;

        for (Double y : points) {
            // Sprawdzenie czy wartość y jest poprawna
            if (Double.isNaN(y) || Double.isInfinite(y)) {
                // Jeśli y jest nieokreślony, kończymy aktualną serię (tylko jeśli zawiera dane)
                // i rozpoczynamy nową
                if (!currentSeries.getData().isEmpty()) {
                    currentSeries = new XYChart.Series<>();
                    subSeriesCount++;
                    currentSeries.setName(functionModel.getName() + " Part " + subSeriesCount);
                    subSeriesList.add(currentSeries);
                }
            } else {
                // Dodajemy poprawny punkt do aktualnej serii
                XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(x, y);

                // Dodawanie tooltipa
                dataPoint.nodeProperty().addListener((observable, oldNode, newNode) -> {
                    if (newNode != null) {
                        Tooltip tooltip = new Tooltip("x: " + dataPoint.getXValue() + ", y: " + dataPoint.getYValue());
                        Tooltip.install(newNode, tooltip);
                    }
                });

                currentSeries.getData().add(dataPoint);
            }

            x += step;
        }

        // Dodajemy sub-serie do wykresu, pomijając puste
        for (XYChart.Series<Number, Number> series : subSeriesList) {
            if (!series.getData().isEmpty()) {
                functionChart.getData().add(series);
            }
        }
    }

    @FXML
    private void handleRemoveFunction() {
        int selectedIndex = functionListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            FunctionModel functionToRemove = functions.get(selectedIndex);
            functionChart.getData().remove(functionToRemove.getSeries());
            functions.remove(selectedIndex);
            functionListView.getItems().remove(selectedIndex);
            errorLabel.setText("");
        }
    }
}
