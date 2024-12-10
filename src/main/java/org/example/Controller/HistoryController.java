package org.example.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.Model.CalculationHistory;
import org.example.Model.CalculationResult;

public class HistoryController extends BaseController {
    @FXML
    private TableView<CalculationResult> historyTableView;
    @FXML
    private TableColumn<CalculationResult, String> expressionColumn;
    @FXML
    private TableColumn<CalculationResult, Double> resultColumn;

    @FXML
    private void initialize() {
        setupTableColumns();
        loadHistory();
    }

    private void setupTableColumns() {
        expressionColumn.setCellValueFactory(new PropertyValueFactory<>("expression"));
        resultColumn.setCellValueFactory(new PropertyValueFactory<>("result"));
    }

    private void loadHistory() {
        if(serviceLocator != null && serviceLocator.getCalculationHistory() != null) {
            CalculationHistory history = serviceLocator.getCalculationHistory();
            historyTableView.getItems().clear();
            historyTableView.getItems().addAll(history.getHistory());
        }
    }

    @FXML
    private void clearHistory() {
        if(serviceLocator != null && serviceLocator.getCalculationHistory() != null) {
            serviceLocator.getCalculationHistory().getHistory().clear();
            historyTableView.getItems().clear();
        }
    }
}
