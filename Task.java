import Category.Category;
import Reminder.Reminder;
import Priority.Priority;
import java.time.LocalDate;
public class Task {
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private String dueDate;
    private String status; // "Open", "In Progress", "Postponed", "Completed", "Delayed"
    private List<Reminder> reminders;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Reminder> getReminders() {
        return reminders;
    }

    public void setReminders(List<Reminder> reminders) {
        this.reminders = reminders;
    }

    public void changeStatus(String newStatus) {
        // Validate new status and update accordingly
        // If setting to "Completed", remove all associated reminders
    }
    
    public void addReminder(Reminder reminder) {
        // Validate reminder based on task due date
        reminders.add(reminder);
    }
    
    public void removeReminder(Reminder reminder) {
        reminders.remove(reminder);
    }
}