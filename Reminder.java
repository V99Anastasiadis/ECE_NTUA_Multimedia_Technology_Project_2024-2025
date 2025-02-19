import java.time.LocalDate;
public class Reminder {
    private LocalDate reminderDate;
    private String message;
    private Task task;
    
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

    //i have to call a function to destroy the pop reminders when it is called in task
    //i have to create a function to show the pop reminders
    //i have to create a function to delete the pop reminders of alla tasks from a deleted category
}
