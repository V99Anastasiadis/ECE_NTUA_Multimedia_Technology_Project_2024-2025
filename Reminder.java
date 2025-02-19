import java.time.LocalDate;
public class Reminder {
    private LocalDate reminderDate;
    private String message;
    private Task task;
    
    public Reminder(LocalDate reminderDate, String message, Task task) {
        if(task.getStatus() == Task.TaskStatus.COMPLETED) {
            System.out.println("You can't set a reminder for a completed task");
            return;
        } else {
            this.reminderDate = reminderDate;
            this.message = message;
            this.task = task;
        }
    }

    public LocalDate getReminderDate() { return reminderDate; }
    public String getMessage() { return message; }
    public Task getTask() { return task; }

    public void setReminderDate(LocalDate reminderDate) { this.reminderDate = reminderDate; }
    public void setMessage(String message) { this.message = message; }

    public void deleteReminder() {
        //delete the reminder
    }

    //i have to create a function to show the pop reminders
}
