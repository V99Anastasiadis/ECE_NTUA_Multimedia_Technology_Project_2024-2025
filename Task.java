public class Task {
    private String title;
    private String description;
    private Category category;
    private Priority priority;
    private String dueDate;
    private String status; // "Open", "In Progress", "Postponed", "Completed", "Delayed"
    private List<Reminder> reminders;

    // Getters and setters
    
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