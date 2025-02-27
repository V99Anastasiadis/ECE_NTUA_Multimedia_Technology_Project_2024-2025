package com.taskmanager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DataManager {

    private static final String FILE_PATH = "src/main/resources/medialab/tasks.json";

    /**
     * Αποθηκεύει τα δεδομένα σε JSON, με πολυγραμμική μορφοποίηση (pretty print).
     */
    public static void saveData(AppData appData) {
        // Κεντρικό αντικείμενο JSON
        JSONObject root = new JSONObject();

        // 1) Αποθήκευση Tasks
        JSONArray tasksArray = new JSONArray();
        for (Task task : appData.getTasks()) {
            tasksArray.add(task.toJSON());  // toJSON() επιστρέφει JSONObject με reminders, κτλ
        }
        root.put("tasks", tasksArray);

        // 2) Αποθήκευση Categories
        JSONArray categoriesArray = new JSONArray();
        for (Category category : appData.getCategories()) {
            JSONObject catObj = new JSONObject();
            catObj.put("name", category.getName());
            // Αποθηκεύουμε τους τίτλους των tasks που ανήκουν σε αυτήν την κατηγορία
            JSONArray catTasks = new JSONArray();
            for (Task t : category.getTasks()) {
                catTasks.add(t.getTitle());
            }
            catObj.put("tasks", catTasks);
            categoriesArray.add(catObj);
        }
        root.put("categories", categoriesArray);

        // 3) Αποθήκευση Priorities
        JSONArray prioritiesArray = new JSONArray();
        for (Priority priority : appData.getPriorities()) {
            JSONObject prioObj = new JSONObject();
            prioObj.put("name", priority.getName());
            // Αποθηκεύουμε τους τίτλους των tasks που ανήκουν σε αυτή την προτεραιότητα
            JSONArray prioTasks = new JSONArray();
            for (Task t : priority.getTasks()) {
                prioTasks.add(t.getTitle());
            }
            prioObj.put("tasks", prioTasks);
            prioritiesArray.add(prioObj);
        }
        root.put("priorities", prioritiesArray);

        // Χρήση Gson για pretty-print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(root);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(prettyJson);
            writer.flush();
            System.out.println(" Δεδομένα αποθηκεύτηκαν (pretty JSON)!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Φορτώνει τα δεδομένα από το JSON.
     * - Φορτώνει tasks, categories, priorities
     * - Επανασυνδέει τις εργασίες στις κατηγορίες και στις προτεραιότητες βάσει των τίτλων.
     */
    public static AppData loadData() {
        AppData appData = new AppData();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONObject root = (JSONObject) parser.parse(reader);

            // 1) Φόρτωση Tasks
            JSONArray tasksArray = (JSONArray) root.get("tasks");
            if (tasksArray != null) {
                for (Object obj : tasksArray) {
                    JSONObject taskJSON = (JSONObject) obj;
                    Task task = Task.fromJSON(taskJSON); 
                    appData.getTasks().add(task);
                }
            }

            // 2) Φόρτωση Categories
            JSONArray categoriesArray = (JSONArray) root.get("categories");
            if (categoriesArray != null) {
                for (Object catObj : categoriesArray) {
                    JSONObject catJSON = (JSONObject) catObj;
                    String catName = (String) catJSON.get("name");
                    Category category = new Category(catName);
                    appData.getCategories().add(category);

                    // Ανασύνδεση tasks
                    JSONArray catTasks = (JSONArray) catJSON.get("tasks");
                    if (catTasks != null) {
                        for (Object t : catTasks) {
                            String taskTitle = (String) t;
                            // Βρίσκουμε το αντίστοιχο Task
                            Task foundTask = appData.getTasks().stream()
                                    .filter(tt -> tt.getTitle().equalsIgnoreCase(taskTitle))
                                    .findFirst()
                                    .orElse(null);
                            if (foundTask != null) {
                                category.addTask(foundTask);
                                // Ενημερώνουμε και το Task αν χρειάζεται
                                // foundTask.setCategory(catName); 
                            }
                        }
                    }
                }
            }

            // 3) Φόρτωση Priorities
            JSONArray prioritiesArray = (JSONArray) root.get("priorities");
            if (prioritiesArray != null) {
                for (Object prioObj : prioritiesArray) {
                    JSONObject prioJSON = (JSONObject) prioObj;
                    String prioName = (String) prioJSON.get("name");
                    Priority priority = new Priority(prioName);
                    appData.getPriorities().add(priority);

                    JSONArray prioTasks = (JSONArray) prioJSON.get("tasks");
                    if (prioTasks != null) {
                        for (Object t : prioTasks) {
                            String taskTitle = (String) t;
                            // Βρίσκουμε το αντίστοιχο Task
                            Task foundTask = appData.getTasks().stream()
                                    .filter(tt -> tt.getTitle().equalsIgnoreCase(taskTitle))
                                    .findFirst()
                                    .orElse(null);
                            if (foundTask != null) {
                                priority.addTask(foundTask);
                                // foundTask.setPriority(prioName);
                            }
                        }
                    }
                }
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println("⚠ Δεν βρέθηκε ή δεν φορτώθηκε σωστά το JSON. Επιστρέφεται κενό AppData.");
        }

        return appData;
    }
}
