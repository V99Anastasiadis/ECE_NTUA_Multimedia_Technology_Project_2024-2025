import java.time.LocalDate;
import java.util.ArrayList;

//import javax.json.Json;
//import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        ArrayList<Category> categories = new ArrayList<>();
        ArrayList<Task> tasks = new ArrayList<>();
        ArrayList<Priority> priorities = new ArrayList<>();
        //at the begining of the program we wiil check if we have any tasks that are delayed
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            LocalDate dueDate = task.getDueDate();
            if ( dueDate.isBefore(today) && task.getStatus() != Task.TaskStatus.COMPLETED) {
                task.setStatus(Task.TaskStatus.DELAYED);
            }
        }
        priorities.add(new Priority("Default"));
        categories.add(new Category("Personal"));

        Task task = new Task("Finish the project", "Complete the project by the deadline", categories.get(0), LocalDate.of(2021, 12, 31), Task.TaskStatus.OPEN);
        tasks.add(task);
        // task.setTitle("Finish the project");
        // task.setDescription("Complete the project by the deadline");
        // task.setCategory(Category.WORK);
        // task.setDueDate(LocalDate.of(2021, 12, 31));
        // task.setStatus(Task.TaskStatus.OPEN);
        System.out.println(task);
        //JSONObject jo = new JSONObject();

    }
}
