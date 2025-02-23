import java.time.LocalDate;
public class Reminder {
    private LocalDate reminderDate;
    private String message;
    private Task task;
    
    //flag exists in order to prevent the recurcive call of the constructor
    public Reminder(LocalDate reminderDate, String message, Task task , boolean flag) {
        LocalDate today = LocalDate.now();
        if(task.getStatus() == Task.TaskStatus.COMPLETED) {
            System.out.println("You can't set a reminder for a completed task");
            return;
        } else if(reminderDate.isBefore(today)) {
            System.out.println("You can't set a reminder for a past date");
            return;
        } else {
            this.reminderDate = reminderDate;
            this.message = message;
            this.task = task;
            if(flag) {
                LocalDate lastWeek = today.minusDays(7);
                LocalDate lastDay = today.minusDays(1);
                LocalDate lastMonth = today.minusDays(30);
                if(!lastDay.isBefore(today)){
                    Reminder reminder = new Reminder(reminderDate, message, task,false);
                }
                if(!lastWeek.isBefore(today)){
                    Reminder reminder = new Reminder(reminderDate, message, task,false);
                }
                if(!lastMonth.isBefore(today)){
                    Reminder reminder = new Reminder(reminderDate, message, task,false);
                }                
            }
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
