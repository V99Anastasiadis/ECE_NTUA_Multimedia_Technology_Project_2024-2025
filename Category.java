import java.util.List;
public class Category {
    private List<Task> tasks;
    private String name;

    //Ο χρήστης θα μπορεί να ορίζει νέες κατηγορίες, δίνοντας το σχετικό όνομα.
    public Category(String name){
        this.name = name;
    }

    public String getName() { return name; }
    public List<Task> getTasks() { return tasks; }

    //Επιπλέον, θα μπορεί να τροποποιήσει τον όνομα μιας κατηγορίας.
    public void setName(String name) { this.name = name; }
    
    //each time a task is added to this category, it will be added to the list of tasks
    public void addTask(Task task) {
        tasks.add(task);
    }

    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    //Επίσης, θα μπορεί να διαγράφει μια κατηγορία μαζί με την αυτόματη διαγραφή όλων των εργασιών που ανήκουν σε αυτή.
    //when you delete a category it deletes all the assowciated tasks
    public void deleteCategory() {
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.deleteTask();
        }
    }
}
