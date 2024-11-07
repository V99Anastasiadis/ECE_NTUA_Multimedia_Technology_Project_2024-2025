public class TaskManager {
    private List<Task> tasks;
    private List<Category> categories;
    private List<Priority> priorities;

    public TaskManager() {
        tasks = new ArrayList<>();
        categories = new ArrayList<>();
        priorities = new ArrayList<>();
        
        // Add default priority "Default"
        priorities.add(new Priority("Default"));
    }

    // CRUD methods for tasks, categories, and priorities
    
    public void addTask(Task task) {
        tasks.add(task);
        task.getCategory().addTask(task);
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        task.getCategory().removeTask(task);
        
        // Remove any associated reminders
        for (Reminder reminder : task.getReminders()) {
            task.removeReminder(reminder);
        }
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        
        // Remove all tasks in the category
        for (Task task : category.getTasks()) {
            removeTask(task);
        }
    }

    public void addPriority(Priority priority) {
        priorities.add(priority);
    }

    public void removePriority(Priority priority) {
        // Don't allow removing the "Default" priority
        if (!priority.getName().equals("Default")) {
            priorities.remove(priority);
            
            // Set all tasks with this priority to "Default"
            for (Task task : tasks) {
                if (task.getPriority().equals(priority)) {
                    task.setPriority(getPriority("Default"));
                }
            }
        }
    }

    public Priority getPriority(String name) {
        for (Priority priority : priorities) {
            if (priority.getName().equals(name)) {
                return priority;
            }
        }
        return null;
    }

    public void updateTaskStatus() {
        // Check for tasks with "Delayed" status and update them
        for (Task task : tasks) {
            if (!task.getStatus().equals("Completed") && task.getDueDate().isBefore(LocalDate.now())) {
                task.changeStatus("Delayed");
            }
        }
    }
    
}