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
    private static final String FILE_PATH = "src/main/resources/medialab/tasks_copy.json";

    public static AppData loadData() {
        AppData appData = new AppData();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            JSONObject root = (JSONObject) parser.parse(reader);

            JSONArray tasksArray = (JSONArray) root.get("tasks");
            for (Object obj : tasksArray) {
                JSONObject taskJSON = (JSONObject) obj;
                Task task = Task.fromJSON(taskJSON);
                appData.getTasks().add(task);
            }
            JSONArray categoriesArray = (JSONArray) root.get("categories");
        if (categoriesArray != null) {
            for (Object obj : categoriesArray) {
                JSONObject categoryJSON = (JSONObject) obj;
                String categoryName = (String) categoryJSON.get("name");
                Category category = new Category(categoryName);
                appData.getCategories().add(category);
            }
        }
        JSONArray prioritiesArray = (JSONArray) root.get("priorities");
        if (prioritiesArray != null) {
            for (Object obj : prioritiesArray) {
                JSONObject priorityJSON = (JSONObject) obj;
                String priorityName = (String) priorityJSON.get("name");
                Priority priority = new Priority(priorityName);
                appData.getPriorities().add(priority);
            }
        }
        } catch (IOException | ParseException e) {
            System.out.println("Δεν βρέθηκε αρχείο, δημιουργία νέου.");
        }
        return appData;
    }

    public static void saveData(AppData appData) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JSONObject root = new JSONObject();

        JSONArray tasksArray = new JSONArray();
        for (Task task : appData.getTasks()) {
            tasksArray.add(task.toJSON());
        }
        root.put("tasks", tasksArray);
        JSONArray categoriesArray = new JSONArray();
        for (Category category : appData.getCategories()) {
            JSONObject categoryJSON = new JSONObject();
            categoryJSON.put("name", category.getName());
            categoriesArray.add(categoryJSON);
        }
        root.put("categories", categoriesArray);
        JSONArray prioritiesArray = new JSONArray();
        for (Priority priority : appData.getPriorities()) {
            JSONObject priorityJSON = new JSONObject();
            priorityJSON.put("name", priority.getName());
            prioritiesArray.add(priorityJSON);
        }
        root.put("priorities", prioritiesArray);


        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(gson.toJson(root));  // Χρήση Gson για pretty print JSON
            file.flush();
            System.out.println("✅ Δεδομένα αποθηκεύτηκαν με μορφοποιημένο JSON!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
