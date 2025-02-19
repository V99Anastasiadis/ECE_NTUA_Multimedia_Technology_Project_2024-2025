import java.util.List;
import java.util.Locale;
public class Category {
    private List<Task> tasks;
    private String name;

    public Category(String name){
        this.name = name;
    }

    public String getName() { return name; }
    public List<Task> getTasks() { return tasks; }

    public void setName(String name) { this.name = name; }
    
    //each time a task is added to this category, it will be added to the list of tasks
    public void addTask(Task task) {
        tasks.add(task);
    }

    //when you delete a category it deletes all the assowciated tasks
    public void deleteCategory(Category category) {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.deleteTask();
        }
    }
}
