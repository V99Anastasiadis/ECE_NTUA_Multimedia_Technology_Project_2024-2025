package com.taskmanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller {

    // Αναφορές στα στοιχεία του UI από το FXML
    @FXML private TableView<Task> taskTable;
    @FXML private TableColumn<Task, String> titleColumn;
    @FXML private TableColumn<Task, String> categoryColumn;
    @FXML private TableColumn<Task, String> priorityColumn;
    @FXML private TableColumn<Task, String> dueDateColumn;
    @FXML private TableColumn<Task, String> statusColumn;

    // Αντικείμενο για τα δεδομένα της εφαρμογής
    private AppData appData;

    /**
     * Η initialize() μέθοδος εκτελείται αυτόματα μετά τη φόρτωση του FXML.
     * Εδώ ορίζουμε τις στήλες του TableView και φορτώνουμε τα δεδομένα.
     */
    @FXML
    public void initialize() {
        // Ορισμός του cell value factory για κάθε στήλη
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Φόρτωση των δεδομένων από το JSON αρχείο μέσω του DataManager
        appData = DataManager.loadData();
        if (appData == null) {
            appData = new AppData();
        }
        taskTable.getItems().setAll(appData.getTasks());
    }

    /**
     * Καλείται όταν ο χρήστης πατάει το κουμπί "Load Tasks".
     */
    @FXML
    public void loadData(ActionEvent event) {
        appData = DataManager.loadData();
        if (appData != null) {
            taskTable.getItems().setAll(appData.getTasks());
        }
    }

    /**
     * Καλείται όταν ο χρήστης πατάει το κουμπί "Save Tasks".
     */
    @FXML
    public void saveData(ActionEvent event) {
        DataManager.saveData(appData);
    }
}
