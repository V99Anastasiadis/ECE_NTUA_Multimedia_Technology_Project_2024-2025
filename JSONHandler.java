import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JSONHandler {
    private static final String DATA_DIRECTORY = "medialab";

    public static void loadData(TaskManager taskManager) {
        loadPriorities(taskManager);
        loadCategories(taskManager);
        loadTasks(taskManager);
    }

    private static void loadPriorities(TaskManager taskManager) {
        JsonArray prioritiesJson = readJsonArray("priorities.json");
        for (int i = 0; i < prioritiesJson.size(); i++) {
            JsonObject priorityJson = prioritiesJson.get(i).getAsJsonObject();
            String name = priorityJson.get("name").getAsString();
            taskManager.addPriority(new Priority(name));
        }
    }

    private static void loadCategories(TaskManager taskManager) {
        JsonArray categoriesJson = readJsonArray("categories.json");
        for (int i = 0; i < categoriesJson.size(); i++) {
            JsonObject categoryJson = categoriesJson.get(i).getAsJsonObject();
            String name = categoryJson.get("name").getAsString();
            Category category = new Category(name);
            taskManager.addCategory(category);
        }
    }

    private static void loadTasks(TaskManager taskManager) {
        JsonArray tasksJson = readJsonArray("tasks.json");
        for (int i = 0; i < tasksJson.size(); i++) {
            JsonObject taskJson = tasksJson.get(i).getAsJsonObject();
            String title = taskJson.get("title").getAsString();
            String description = taskJson.get("description").getAsString();
            String categoryName = taskJson.get("category").getAsString();
            String priorityName = taskJson.get("priority").getAsString();
            String dueDate = taskJson.get("dueDate").getAsString();
            String status = taskJson.get("status").getAsString();

            Category category = taskManager.getCategory(categoryName);
            Priority priority = taskManager.getPriority(priorityName);
            Task task = new Task(title, description, category, priority, LocalDate.parse(dueDate), status);
            taskManager.addTask(task);

            // Load reminders
            JsonArray remindersJson = taskJson.getAsJsonArray("reminders");
            for (int j = 0; j < remindersJson.size(); j++) {
                JsonObject reminderJson = remindersJson.get(j).getAsJsonObject();
                String reminderType = reminderJson.get("type").getAsString();
                String reminderDate = reminderJson.get("date").getAsString();
                Reminder reminder = new Reminder(reminderType, reminderDate);
                task.addReminder(reminder);
            }
        }
    }

    public static void saveData(TaskManager taskManager) {
        saveCategories(taskManager.getCategories());
        savePriorities(taskManager.getPriorities());
        saveTasks(taskManager.getTasks());
    }

    private static void saveCategories(List<Category> categories) {
        JsonArray categoriesJson = new JsonArray();
        for (Category category : categories) {
            JsonObject categoryJson = new JsonObject();
            categoryJson.addProperty("name", category.getName());
            categoriesJson.add(categoryJson);
        }
        writeJsonArray("categories.json", categoriesJson);
    }

    private static void savePriorities(List<Priority> priorities) {
        JsonArray prioritiesJson = new JsonArray();
        for (Priority priority : priorities) {
            JsonObject priorityJson = new JsonObject();
            priorityJson.addProperty("name", priority.getName());
            prioritiesJson.add(priorityJson);
        }
        writeJsonArray("priorities.json", prioritiesJson);
    }

    private static void saveTasks(List<Task> tasks) {
        JsonArray tasksJson = new JsonArray();
        for (Task task : tasks) {
            JsonObject taskJson = new JsonObject();
            taskJson.addProperty("title", task.getTitle());
            taskJson.addProperty("description", task.getDescription());
            taskJson.addProperty("category", task.getCategory().getName());
            taskJson.addProperty("priority", task.getPriority().getName());
            taskJson.addProperty("dueDate", task.getDueDate().toString());
            taskJson.addProperty("status", task.getStatus());

            JsonArray remindersJson = new JsonArray();
            for (Reminder reminder : task.getReminders()) {
                JsonObject reminderJson = new JsonObject();
                reminderJson.addProperty("type", reminder.getType());
                reminderJson.addProperty("date", reminder.getReminderDate());
                remindersJson.add(reminderJson);
            }
            taskJson.add("reminders", remindersJson);

            tasksJson.add(taskJson);
        }
        writeJsonArray("tasks.json", tasksJson);
    }

    private static JsonArray readJsonArray(String filename) {
        try {
            String path = Paths.get(DATA_DIRECTORY, filename).toString();
            String json = new String(Files.readAllBytes(Paths.get(path)));
            return new Gson().fromJson(json, JsonArray.class);
        } catch (IOException e) {
            return new JsonArray();
        }
    }

    private static void writeJsonArray(String filename, JsonArray jsonArray) {
        try {
            String path = Paths.get(DATA_DIRECTORY, filename).toString();
            Files.createDirectories(Paths.get(DATA_DIRECTORY));
            FileWriter writer = new FileWriter(path);
            writer.write(new Gson().toJson(jsonArray));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}