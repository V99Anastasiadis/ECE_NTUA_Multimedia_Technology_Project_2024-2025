package com.taskmanager;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
        Priority defaultPriority = new Priority("default");
        appData.getPriorities().add(defaultPriority);
        // Ορισμός του cell value factory για κάθε στήλη
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        appData = DataManager.loadData();
        taskTable.getItems().setAll(appData.getTasks());

        // Φόρτωση των δεδομένων από το JSON αρχείο μέσω του DataManager
        appData = DataManager.loadData();
        if (appData == null) {
            appData = new AppData();
        }
        taskTable.getItems().setAll(appData.getTasks());
        checkReminders();
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

    public void checkReminders() {
        for (Task task : appData.getTasks()) {
            for (Reminder reminder : task.getReminders()) {
                reminder.showReminder();
            }
        }
    }

    /**
     * Καλείται όταν ο χρήστης πατάει το κουμπί "Save Tasks".
     */
    @FXML
    public void saveData(ActionEvent event) {
        DataManager.saveData(appData);
    }

    @FXML
    public void addTask(ActionEvent event) {
        Task newTask = new Task("New Task", "Description", new Category("Work"), new Priority("High"), LocalDate.now(), Task.TaskStatus.OPEN);
        appData.getTasks().add(newTask);
        DataManager.saveData(appData);
        taskTable.getItems().setAll(appData.getTasks()); // Ενημέρωση UI
    }

    @FXML
    public void searchTasks(ActionEvent event) {
        String query = searchField.getText().toLowerCase();
        List<Task> results = appData.getTasks().stream()
            .filter(task -> task.getTitle().toLowerCase().contains(query) || 
                            task.getCategory().getName().toLowerCase().contains(query) ||
                            task.getPriority().getName().toLowerCase().contains(query))
            .collect(Collectors.toList());
        // Ενημέρωση του TableView με τα αποτελέσματα
        taskTable.getItems().setAll(results);
    }

    @FXML
    private Label totalTasksLabel, completedTasksLabel, delayedTasksLabel, upcomingTasksLabel;
    @FXML 
    private javafx.scene.control.TextField searchField;

    public void updateStatistics() {
        long total = appData.getTasks().size();
        long completed = appData.getTasks().stream().filter(t -> t.getStatus() == Task.TaskStatus.COMPLETED).count();
        long delayed = appData.getTasks().stream().filter(t -> t.getStatus() == Task.TaskStatus.DELAYED).count();
        long upcoming = appData.getTasks().stream().filter(t -> t.getDueDate().isBefore(LocalDate.now().plusDays(7))).count();

        totalTasksLabel.setText("Σύνολο: " + total);
        completedTasksLabel.setText("Ολοκληρωμένα: " + completed);
        delayedTasksLabel.setText("Καθυστερημένα: " + delayed);
        upcomingTasksLabel.setText("Προσεχή: " + upcoming);
    }

}
