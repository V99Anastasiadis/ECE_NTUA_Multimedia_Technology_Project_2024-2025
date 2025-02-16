

/*import taskmanagement.Task;
import taskmanagement.Category;
//import taskmanagement.Reminder;
import taskmanagement.Priority;
import taskmanagement.TaskManager;*/

public class Reminder {
    private String type; // "1 day before", "1 week before", "1 month before", "Custom date"
    private String reminderDate;

    public Reminder(String type, String reminderDate) {
        this.type = type;
        this.reminderDate = reminderDate;
    }

    public String getType() {
        return type;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    // Setters defined within the class
    public void setType(String type) {
        this.type = type;
    }

}