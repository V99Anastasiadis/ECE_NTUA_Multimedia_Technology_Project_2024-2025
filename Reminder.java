public class Reminder {
    private String type; // "1 day before", "1 week before", "1 month before", "Custom date"
    private String reminderDate;

    public Reminder(String type, String reminderDate) {
        this.type = type;
        this.reminderDate = reminderDate;
    }

    // Getters and setters
}