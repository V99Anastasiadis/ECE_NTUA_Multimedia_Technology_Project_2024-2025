package com.taskmanager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.simple.JSONObject;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
public class Reminder {
    private LocalDate reminderDate;
    private String message;
    private Task task;
    private Alert alert;
    
    public Reminder(LocalDate reminderDate, String message, Task task) {
        setReminderDate(reminderDate);
        setMessage(message);
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

    public static Reminder fromJSON(JSONObject jsonObject, Task task) {
        LocalDate reminderDate = LocalDate.parse((String) jsonObject.get("reminderDate"), DateTimeFormatter.ISO_DATE);
        String message = (String) jsonObject.get("message");

        return new Reminder(reminderDate, message, task);
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reminderDate", reminderDate.toString());
        jsonObject.put("message", message);
        return jsonObject;
    }

    public void deleteReminder() {
        if (alert != null) {
            alert.close(); // Κλείσιμο της ειδοποίησης
        }
    }
}
