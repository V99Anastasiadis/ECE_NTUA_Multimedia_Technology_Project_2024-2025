package taskmanagement;
/*import taskmanagement.Task;
//import taskmanagement.Category;
import taskmanagement.Reminder;
import taskmanagement.Priority;
import taskmanagement.TaskManager;*/
import java.util.List;
import java.util.ArrayList;

public class Category {
    private String name;
    private List<Task> tasks;

    public Category(String name) {
        this.name = name;
        tasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    public void removeTask(Task task) {
        tasks.remove(task);
    }
}