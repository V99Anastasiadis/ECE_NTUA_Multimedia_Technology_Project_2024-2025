
/*//import taskmanagement.Task;
import taskmanagement.Category;
import taskmanagement.Reminder;
import taskmanagement.Priority;
import taskmanagement.TaskManager;*/
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
public class Task {

    public enum TaskStatus {
        OPEN,
        IN_PROGRESS,
        POSTPONED,
        COMPLETED,
        DELAYED;
    }
    private static final String FILE_PATH = "medialab/tasks.json";
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
        addTaskToJson(this);
    }    
    private void addTaskToJson(Task newTask) {
        Gson gson = new Gson();
        List<Task> tasks;
    
        try {
            // Διαβάζουμε το υπάρχον JSON
            if (!Files.exists(Paths.get(FILE_PATH))) {
                tasks = new ArrayList<>();
            } else {
                try (Reader reader = new FileReader(FILE_PATH)) {
                    tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
                    if (tasks == null) tasks = new ArrayList<>();
                }
            }
            tasks.add(newTask);    
            // Ξαναγράφουμε το JSON αρχείο
            try (Writer writer = new FileWriter(FILE_PATH)) {
                gson.toJson(tasks, writer);
            }
            //System.out.println("New task added successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters
    public Object JSONSearch (String searchTitle , int Searchflag){
        try (JsonReader reader = new JsonReader(new FileReader(FILE_PATH))) {
            Gson gson = new Gson(); 
            reader.beginArray();
            while (reader.hasNext()) { 
                Task task = gson.fromJson(reader, Task.class); 
                if (task.title.equals(searchTitle)) {
                    return switch (Searchflag) {
                        case 1 -> task.reminders;
                        case 2 -> task.description;
                        case 3 -> task.category;
                        case 4 -> task.priority;
                        case 5 -> task.dueDate;
                        case 6 -> task.status;
                        default -> task.title;
                    };
                }
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Category getCategory() { return category; }
    public Priority getPriority() { return priority; }
    public LocalDate getDueDate() { return dueDate; }
    public TaskStatus getStatus() { return status; }
    public List<Reminder> getReminders() { return reminders; }

    public void JSONAction(String searchTitle, int searchFlag, Object newValue) {
    try {
        Gson gson = new Gson();
        List<Task> tasks;
        try (Reader reader = new FileReader(FILE_PATH)) {
            tasks = gson.fromJson(reader, new TypeToken<List<Task>>() {}.getType());
        }
        if (tasks == null) return;
        for (Task task : tasks) {
            if (task.getTitle().equals(searchTitle)) {
                switch (searchFlag) {
                    case 1 -> task.setReminders((List<Reminder>) newValue);
                    case 2 -> task.setDescription((String) newValue);
                    case 3 -> task.setCategory((Category) newValue);
                    case 4 -> task.setPriority((Priority) newValue);
                    case 5 -> task.setDueDate((LocalDate) newValue);
                    case 6 -> task.setStatus((TaskStatus) newValue);
                    default -> task.setTitle((String) newValue);
                }
                break; 
            }
        }
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(tasks, writer);
        }
        //System.out.println("Task updated successfully.");
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    //Ο χρήστης θα μπορεί να τροποποιήσει όλα τα στοιχεία μιας εργασίας 
    public void updateTitle(String searchTitle, String newTitle) { JSONAction(searchTitle, 0, newTitle); }
    public void updateDescription(String searchTitle, String newDescription) { JSONAction(searchTitle,2,newDescription); }
    public void updateCategory(String searchTitle, Category newCategory) { JSONAction(searchTitle,3,newCategory); }
    public void updatePriority(String searchTitle, Priority newPriority) { JSONAction(searchTitle,4,newPriority); }
    public void updateDueDate(String searchTitle ,LocalDate newDueDate) { JSONAction(searchTitle,5,newDueDate); }
    public void updateStatus(String searchTitle ,TaskStatus newStatus) { JSONAction(searchTitle,6,newStatus); }
    public void addReminder(String searchTitle ,Reminder reminder) { JSONAction(searchTitle,1,reminder); }
    public void removeReminder(String searchTitle ,Reminder reminder) { reminders.remove(reminder); }

    //Ο χρήστης θα μπορεί να προχωρήσει στην διαγραφή εργασιών.
    public void deleteTask(String searchTitle) { category.removeTask(this);}
}