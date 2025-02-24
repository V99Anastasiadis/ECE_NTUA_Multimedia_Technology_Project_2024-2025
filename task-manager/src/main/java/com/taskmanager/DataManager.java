package com.taskmanager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataManager {
    private static final String FILE_PATH = "src/main/resources/medialab/tasks.json";

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

        try (FileWriter file = new FileWriter(FILE_PATH)) {
            file.write(gson.toJson(root));  // Χρήση Gson για pretty print JSON
            file.flush();
            System.out.println("✅ Δεδομένα αποθηκεύτηκαν με μορφοποιημένο JSON!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
