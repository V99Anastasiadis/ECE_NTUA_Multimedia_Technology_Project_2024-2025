/* import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject; */

//import separate.Category;
// import separate.Priority;
// import separate.Task;
// import separate.TaskManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class main {
    /*------------------ Priority ------------------*/
    class Priority {
        private String name;

        public Priority(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    /*------------------ Category ------------------*/
    class Category {
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

    /*------------------ Reminder ------------------*/
    class Reminder {
        private String type; // "1 day before", "1 week before", "1 month before", "Custom date"
        private String reminderDate;

        public Reminder(String type, String reminderDate) {
            this.type = type;
            this.reminderDate = reminderDate;
        }

        public String getType() {
            return type;
        }

        public String getReminderDate() {
            return reminderDate;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    /*------------------ TaskStatus ------------------*/
    private enum TaskStatus {
        OPEN,
        IN_PROGRESS,
        POSTPONED,
        COMPLETED,
        DELAYED;
    }

    /*------------------ Task ------------------*/
    class Task {
        private String title;
        private String description;
        private Category category;
        private Priority priority;
        private LocalDate dueDate;
        private TaskStatus status; // "Open", "In Progress", "Postponed", "Completed", "Delayed"
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
        }

        // Getters
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public Category getCategory() { return category; }
        public Priority getPriority() { return priority; }
        public LocalDate getDueDate() { return dueDate; }
        public TaskStatus getStatus() { return status; }
        public List<Reminder> getReminders() { return reminders; }

        // Setters
        public void setTitle(String title) { this.title = title; }
        public void setDescription(String description) { this.description = description; }
        public void setCategory(Category category) { this.category = category; }
        public void setPriority(Priority priority) { this.priority = priority; }
        public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
        public void setStatus(TaskStatus status) { this.status = status; }
        public void setReminders(List<Reminder> reminders) { this.reminders = reminders; }

        //Ο χρήστης θα μπορεί να τροποποιήσει όλα τα στοιχεία μιας εργασίας 
        public void updateTitle(String newTitle) { this.title = newTitle; }
        public void updateDescription(String newDescription) { this.description = newDescription; }
        public void updateCategory(Category newCategory) { this.category = newCategory; }
        public void updatePriority(Priority newPriority) { this.priority = newPriority; }
        public void updateDueDate(LocalDate newDueDate) { this.dueDate = newDueDate; }
        public void updateStatus(TaskStatus newStatus) { this.status = newStatus; }
        public void addReminder(Reminder reminder) { reminders.add(reminder); }
        public void removeReminder(Reminder reminder) { reminders.remove(reminder); }


        //Ο χρήστης θα μπορεί να προχωρήσει στην διαγραφή εργασιών.
        public void deleteTask() { category.removeTask(this);}
    }

    /*------------------ TaskManager ------------------*/
    class TaskManager {
        //tasks -> all tasks except completed, tasksCompleted -> all completed tasks
        private List<Task> tasks, tasksCompleted;//
        private List<Category> categories;
        private List<Priority> priorities;

        //constructor
        public TaskManager() {
            tasks = new ArrayList<>();
            tasksCompleted = new ArrayList<>();
            categories = new ArrayList<>();
            priorities = new ArrayList<>();
            //υπο αμφισβήτηση
            // Προκαθορισμένο επίπεδο προτεραιότητας "Default"
            priorities.add(new Priority("Default"));
        }

        // CRUD μέθοδοι για εργασίες, κατηγορίες και προτεραιότητες
        public void addTask(Task task) {
            if(task.getStatus().equals(TaskStatus.COMPLETED)){
                tasksCompleted.add(task);
            }
            else tasks.add(task);
            task.getCategory().addTask(task);
        }

        public void removeTask(Task task) {
            tasks.remove(task);
            task.getCategory().removeTask(task);
            // Αφαίρεση υπενθυμίσεων που σχετίζονται με την εργασία
            task.getReminders().clear();
        }

        public void addCategory(Category category) {
            categories.add(category);
        }

        public void removeCategory(Category category) {
            categories.remove(category);
            // Αφαίρεση όλων των εργασιών της κατηγορίας
            List<Task> tasksToRemove = new ArrayList<>(category.getTasks());
            for (Task task : tasksToRemove) {
                removeTask(task);
            }
        }

        public void addPriority(Priority priority) {
            priorities.add(priority);
        }

        public void removePriority(Priority priority) {
            if (!priority.getName().equals("Default")) {
                priorities.remove(priority);
                // Αντιστοίχιση των εργασιών με το "Default"
                for (Task task : tasks) {
                    if (task.getPriority().getName().equals(priority.getName())) {
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

        public List<Task> getTasks() {
            return tasks;
        }
        
        public List<Task> getTasksCompleted() {
            return tasksCompleted;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public List<Priority> getPriorities() {
            return priorities;
        }
        //Κατά την αρχικοποίηση της εφαρμογής θα
        //πρέπει να εντοπίζονται οι εργασίες που δεν είναι “Completed” και έχει περάσει η
        //προθεσμία ολοκλήρωσης και να αλλάζει αυτόματα η κατάσταση σε “Delayed”
        public void turnToDelayedTasks() {
            for (Task task : tasks) {
                if (task.getDueDate().isBefore(LocalDate.now())) {
                    task.changeStatus(TaskStatus.DELAYED);
                }
            }
        }
    }

    public static void main(String[] args) {
        // Δημιουργούμε ένα instance της εξωτερικής κλάσης Main για να έχουμε πρόσβαση στις inner κλάσεις
        Main instance = new Main();
        TaskManager taskManager = instance.new TaskManager();

        // Δοκιμαστική δημιουργία αντικειμένων
        Category category = instance.new Category("Κατηγορία 1");
        Priority priority = instance.new Priority("High");
        Task task = instance.new Task("Task 1", "Περιγραφή Task 1", category, priority, LocalDate.now().plusDays(5), "Open");

        taskManager.addTask(task);

        System.out.println("Αριθμός εργασιών: " + taskManager.getTasks().size());
    }
}
