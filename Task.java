import java.io.ObjectInputFilter;
import java.rmi.server.RemoteServer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Task {
    public enum TaskStatus {
        OPEN,
        IN_PROGRESS,
        POSTPONED,
        COMPLETED,
        DELAYED;
    }
    private static final String FILE_PATH = "medialab/tasks.json";
    private String title;
    private String description;
    private Category category;
    private List<Priority> priorities;
    private LocalDate dueDate;
    private TaskStatus status; //default status is OPEN
    private List<Reminder> reminders;


    public Task(String title, String description, Category category, LocalDate dueDate, TaskStatus status) {
        this.title = title;
        this.description = description;
        //problem on creation what will happen if the category is not in the list
        this.category = category;
        this.dueDate = dueDate;
        this.status = TaskStatus.OPEN;
        this.reminders = new ArrayList<>();        
    }   

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public List<Priority> getPriority() { return priorities; } //not sure if this is correct
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; } //not sure if this is correct

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { 
        this.category.deleteTask(this);
        this.category = category; 
        category.addTask(this);
    }
    public void setPriority(Priority priority) { //not sure how to handle priorities i have to revisit this
        this.priorities.add(priority);
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
            for (int i = 0; i < reminders.size(); i++) {
                Reminder reminder = reminders.get(i);
                reminder.deleteReminder();
            }
        }
    } 
    public void setReminder(LocalDate reminderDate, String message) {
        Reminder reminder = new Reminder(reminderDate, message, this);
        reminders.add(reminder);    
    }

    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
        //i don't know how to call the function to delete the specific reminder
    }

    public void deleteTask() {
        List<Reminder> reminders = getReminders();
        //this deletes every reminder that is associated with the task
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            deleteReminder(reminder);
            reminder.getTask().getReminders().remove(reminder);
        }
        Category.renoveTask(this);
        Priority.removeTask(this);
    }
    
}
