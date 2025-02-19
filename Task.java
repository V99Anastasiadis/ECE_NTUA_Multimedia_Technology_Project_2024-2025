import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private Priority priority;
    private LocalDate dueDate;
    private TaskStatus status; //default status is OPEN
    private List<Reminder> reminders;

    public Task(String title, String description, Category category, Priority priority, LocalDate dueDate, TaskStatus status) {
        this.title = title;
        this.description = description;
        //problem on creation what will happen if the category is not in the list
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.OPEN;
        this.reminders = new ArrayList<>();        
    }   

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { 
        this.category.deleteTask(this);
        this.category = category; 
        category.addTask(this);
    }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueDate(LocalDate dueDate) { 
        this.dueDate = dueDate; 
        if (dueDate.isBefore(LocalDate.now()) && status != TaskStatus.COMPLETED) {
            status = TaskStatus.DELAYED;
        }
    }
    public void setStatus(TaskStatus status) { this.status = status; } 
    public void setReminders(LocalDate reminderDate, String message) {
        Reminder reminder = new Reminder(reminderDate, message, this);
        reminders.add(reminder);    
    }

    public void deleteReminder(Reminder reminder) {
        reminders.remove(reminder);
        //i have to call a function to destroy the pop reminders when i do this
    }

    public void deleteTask() {
        List<Reminder> reminders = getReminders();
        //this deletes every reminder that is associated with the task
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            deleteReminder(reminder);
        }
        //i have to call a function to remove the task from the list of tasks of category
        //i have to call a function to remove the task from the list of tasks of priority
    }
    
}
