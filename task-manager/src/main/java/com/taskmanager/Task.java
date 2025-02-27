package com.taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
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

    private Category findCategoryByName(String name) {
        if (appData == null || appData.getCategories() == null) {
            return null;
        }
        return appData.getCategories().stream()
                .filter(category -> category.getName() != null && category.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    

    private Priority findPriorityByName(String name) {
        if (appData == null || appData.getPriorities() == null) {
            return null;
        }
        return appData.getPriorities().stream()
                .filter(priority -> priority.getName() != null && priority.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    
    

    private boolean isDuplicateReminder(LocalDate date, String message) {
        return this.reminders.stream()  
            .anyMatch(reminder -> reminder.getReminderDate().equals(date) && 
                                  reminder.getMessage().equalsIgnoreCase(message));
    }

    public Task(String title, String description, String category, String priority, LocalDate dueDate, TaskStatus status) {
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

    public static Task fromJSON(JSONObject jsonObject) {
        String title = (String) jsonObject.get("title");
        String description = (String) jsonObject.get("description");
        String categoryName = (String) jsonObject.get("category");
        String priorityName = (String) jsonObject.get("priority");
        LocalDate dueDate = LocalDate.parse((String) jsonObject.get("dueDate"), DateTimeFormatter.ISO_DATE);
        TaskStatus status = TaskStatus.valueOf((String) jsonObject.get("status"));

        Category category = new Category(categoryName);
        Priority priority = new Priority(priorityName);

        Task task = new Task(title, description, categoryName, priorityName, dueDate, status);
        JSONArray remindersArray = (JSONArray) jsonObject.get("reminders");
        if (remindersArray != null) {
            for (Object obj : remindersArray) {
                JSONObject reminderJSON = (JSONObject) obj;
                Reminder reminder = Reminder.fromJSON(reminderJSON, task);
                task.addReminder(reminder);
            }
        }
        return task;
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", title);
        jsonObject.put("description", description);
        jsonObject.put("category", category.getName());
        jsonObject.put("priority", priority.getName());
        jsonObject.put("dueDate", dueDate.toString());
        jsonObject.put("status", status.toString());
        JSONArray remindersArray = new JSONArray();
        for (Reminder reminder : reminders) {
            remindersArray.add(reminder.toJSON());
        }
        jsonObject.put("reminders", remindersArray);
        return jsonObject;
    }

    //  **Getters**
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; }

    //  **Setters**
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }

    public void setCategory(String category) { 
        if (this.category != null) {
            this.category.deleteTask(this);
        }
        if (findCategoryByName(category) != null) {
            this.category = findCategoryByName(category);
        } else {
            this.category = new Category(category);
        }
        this.category.addTask(this);
    }

    public void setPriority(String priorityNew) {
        if (this.priority != null) {
            this.priority.deleteTask(this);
        }
        if (findPriorityByName(priorityNew) != null) {
            this.priority = findPriorityByName(priorityNew);
        } else {
            this.priority = new Priority(priorityNew);
        }
        this.priority.addTask(this);
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

    //  **Δημιουργία υπενθύμισης**
    public void setReminder(LocalDate reminderDate, String message) {
        LocalDate today = LocalDate.now();
        if (getStatus() != Task.TaskStatus.COMPLETED && !reminderDate.isBefore(today) && !isDuplicateReminder(reminderDate, message)) {
            Reminder reminder = new Reminder(reminderDate, message, this);
            reminders.add(reminder);  
        }else if(getStatus() == Task.TaskStatus.COMPLETED) {
            System.out.println("You can't set a reminder for a completed task");
        } else if(reminderDate.isBefore(today)) {
            System.out.println("You can't set a reminder for a past date");            
        } else if(isDuplicateReminder(reminderDate, message)){
            System.out.println("You can't set a duplicate reminder");
        }
    }

    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    //  **Διαγραφή υπενθύμισης**
    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
        reminder.deleteReminder();
    }

    public void clearReminderAlerts() {
        for (Reminder reminder : reminders) {
            deleteReminder(reminder);
        }
    }

    //  **Διαγραφή εργασίας**
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
