public class Category {
    private String name;
    private List<Task> tasks;

    public Category(String name) {
        this.name = name;
        tasks = new ArrayList<>();
    }

    // Getters and setters
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
    }
}