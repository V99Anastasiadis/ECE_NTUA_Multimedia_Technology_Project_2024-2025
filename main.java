package taskmanagement;
/* import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject; */
//import separate.Category;
// import separate.Priority;
// import separate.Task;
// import separate.TaskManager;
/*import taskmanagement.Category;
//import taskmanagement.Reminder;
import taskmanagement.Priority;
import taskmanagement.Task;*/
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;

public class main {
    public static void main(String[] args) {
        // Δημιουργούμε ένα instance της εξωτερικής κλάσης Main για να έχουμε πρόσβαση στις inner κλάσεις
        //Main instance = new Main();
        //this writes the names to a file
        String[] names = {"john", "mary", "peter", "susan"};
        try{
            //if file doesnt exists it creates it otherwhise it overwrites it
            BufferedWriter writer = new BufferedWriter(new FileWriter("medialab/tasks.json"));
            writer.write("[καυλη]");
            for(String name: names){
                writer.write(name + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //this reads the names from the file
        try{
            BufferedReader reader = Files.newBufferedReader(Paths.get("medialab/tasks.json"));
            String line = null;
            //it reads the file line by line until it finds it is empty
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();

        TaskManager taskManager = instance.new TaskManager();

        // Δοκιμαστική δημιουργία αντικειμένων
        Category category = instance.new Category("Κατηγορία 1");
        Priority priority = instance.new Priority("High");
        Task task = instance.new Task("Task 1", "Περιγραφή Task 1", category, priority, LocalDate.now().plusDays(5), "Open");

        taskManager.addTask(task);

        System.out.println("Αριθμός εργασιών: " + taskManager.getTasks().size());
    }
}
