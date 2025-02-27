package com.taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

public class Task {
    public enum TaskStatus {
        OPEN, IN_PROGRESS, POSTPONED, COMPLETED, DELAYED;
    }

    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private LocalDate dueDate;
    private TaskStatus status;
    private List<Reminder> reminders;
    private AppData appData;

    private Category findCategoryByName(Category name) {
        return appData.getCategories().stream()
            .filter(category -> category.getName().equals(name.getName()))
            .findFirst()
            .orElse(null);
    }

    private Priority findPriorityByName(Priority name) {
        return appData.getPriorities().stream()
            .filter(priority -> priority.getName().equalsIgnoreCase(name.getName()))
            .findFirst()
            .orElse(null);
    }

    public Task(String title, String description, Category category, Priority priority, LocalDate dueDate, TaskStatus status) {
        appData.getTasks().add(this);
        setTitle(title);
        setDescription(description);
        setCategory(category);
        setPriority(priority);
        setDueDate(dueDate);
        setStatus(status);
        this.reminders = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String message = "default";
        Reminder reminder = new Reminder(today.minusDays(7), message, this);
        addReminder(reminder);    
        reminder = new Reminder(today.minusDays(1), message, this);
        addReminder(reminder);
        reminder = new Reminder(today.minusDays(30), message, this);
        addReminder(reminder);        
    }

    // 📌 **Διορθωμένη fromJSON()**
    public static Task fromJSON(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        String description = (String) jsonObject.get("description");
        String categoryName = (String) jsonObject.get("category");
        String priorityName = (String) jsonObject.get("priority");
        LocalDate dueDate = LocalDate.parse((String) jsonObject.get("dueDate"), DateTimeFormatter.ISO_DATE);
        TaskStatus status = TaskStatus.valueOf((String) jsonObject.get("status"));

        Category category = new Category(categoryName);
        Priority priority = new Priority(priorityName);

        return new Task(title, description, category, priority, dueDate, status);
    }

    // 📌 **Διορθωμένη toJSON()**
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("description", description);
        jsonObject.put("category", category.getName());
        jsonObject.put("priority", priority.getName());
        jsonObject.put("dueDate", dueDate.toString());
        jsonObject.put("status", status.toString());

        return jsonObject;
    }

    // 📌 **Getters**
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; }

    // 📌 **Setters**
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    public void setCategory(Category category) { 
        if (this.category != null) {
            this.category.deleteTask(this);
        }
        if (findCategoryByName(category) != null) {
            this.category = category;
        } else {
            this.category = new Category(category.getName());
        }
        category.addTask(this);
    }

    public void setPriority(Priority priorityNew) {
        if (this.priority != null) {
            this.priority.deleteTask(this);
        }
        if (findPriorityByName(priorityNew) != null) {
            this.priority = priorityNew;
        } else {
            this.priority = new Priority(priorityNew.getName());
        }
        priority.addTask(this);
    }

    public void setDueDate(LocalDate dueDate) { 
        this.dueDate = dueDate; 
        if (dueDate.isBefore(LocalDate.now()) && status != TaskStatus.COMPLETED) {
            status = TaskStatus.DELAYED;
        }
    }

    public void setStatus(TaskStatus status) { 
        this.status = status;
        if(status == TaskStatus.COMPLETED) {
            for (Reminder reminder : new ArrayList<>(reminders)) {
                reminder.deleteReminder();
            }
        }
    }

    // 📌 **Δημιουργία υπενθύμισης**
    public void setReminder(LocalDate reminderDate, String message) {
        LocalDate today = LocalDate.now();
        if (getStatus() != Task.TaskStatus.COMPLETED && !reminderDate.isBefore(today)){
            Reminder reminder = new Reminder(reminderDate, message, this);
            reminders.add(reminder);  
        }else if(getStatus() == Task.TaskStatus.COMPLETED) {
            System.out.println("You can't set a reminder for a completed task");
        } else if(reminderDate.isBefore(today)) {
            System.out.println("You can't set a reminder for a past date");            
        }
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    // 📌 **Διαγραφή υπενθύμισης**
    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
        reminder.deleteReminder();
    }

    public void clearReminderAlerts() {
        for (Reminder reminder : reminders) {
            deleteReminder(reminder);
        }
    }

    // 📌 **Διαγραφή εργασίας**
    public void deleteTask() {
        clearReminderAlerts();

        if (category != null) {
            category.deleteTask(this);
        }
        if (priority != null) {
            priority.deleteTask(this);
        }
    }
}
