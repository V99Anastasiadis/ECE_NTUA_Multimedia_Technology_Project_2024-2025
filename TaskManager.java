package taskmanagement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*import taskmanagement.Task;
import taskmanagement.Category;
import taskmanagement.Reminder;
import taskmanagement.Priority;*/
//import taskmanagement.TaskManager;


public class TaskManager {
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