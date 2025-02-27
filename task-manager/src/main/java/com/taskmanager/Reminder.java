package com.taskmanager;
import java.time.LocalDate;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class Reminder {
    private LocalDate reminderDate;
    private String message;
    private Task task;
    private Alert alert;
    
    public Reminder(LocalDate reminderDate, String message, Task task) {
        this.reminderDate = reminderDate;
        this.message = message;
        this.task = task;
    }

    public LocalDate getReminderDate() { return reminderDate; }
    public String getMessage() { return message; }
    public Task getTask() { return task; }

    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }
    public void setMessage(String message) { this.message = message; }

    public void showReminder() {
        if (reminderDate.isEqual(LocalDate.now())) {
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Reminder");
            alert.setHeaderText("Task Reminder");
            alert.setContentText("Task: " + task.getTitle() + "\nMessage: " + message);
            alert.showAndWait();
        }
    }

    public void deleteReminder() {
        if (alert != null) {
            alert.close(); // Κλείσιμο της ειδοποίησης
        }
    }
}
