
/*//import taskmanagement.Task;
import taskmanagement.Category;
import taskmanagement.Reminder;
import taskmanagement.Priority;
import taskmanagement.TaskManager;*/
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//import Priority;
public class Task {

    public enum TaskStatus {
        OPEN,
        IN_PROGRESS,
        POSTPONED,
        COMPLETED,
        DELAYED;
    }
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private LocalDate dueDate;
    private TaskStatus status; //default status is OPEN
    private List<Reminder> reminders;

    //constructor
    public Task(String title, String description, Category category, Priority priority, LocalDate dueDate, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.OPEN;
        this.reminders = new ArrayList<>();
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setCategory(Category category) { this.category = category; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setReminders(List<Reminder> reminders) { this.reminders = reminders; }

    //Ο χρήστης θα μπορεί να τροποποιήσει όλα τα στοιχεία μιας εργασίας 
    public void updateTitle(String newTitle) { this.title = newTitle; }
    public void updateDescription(String newDescription) { this.description = newDescription; }
    public void updateCategory(Category newCategory) { this.category = newCategory; }
    public void updatePriority(Priority newPriority) { this.priority = newPriority; }
    public void updateDueDate(LocalDate newDueDate) { this.dueDate = newDueDate; }
    public void updateStatus(TaskStatus newStatus) { this.status = newStatus; }
    public void addReminder(Reminder reminder) { reminders.add(reminder); }
    public void removeReminder(Reminder reminder) { reminders.remove(reminder); }

    //Ο χρήστης θα μπορεί να προχωρήσει στην διαγραφή εργασιών.
    public void deleteTask() { category.removeTask(this);}
}