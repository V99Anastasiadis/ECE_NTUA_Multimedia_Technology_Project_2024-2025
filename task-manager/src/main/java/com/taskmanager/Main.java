package com.taskmanager;

import java.io.File;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Φόρτωση του FXML αρχείου
        Parent root = FXMLLoader.load(getClass().getResource("/MediaLabAssistant.fxml"));

        // Δημιουργία της σκηνής
        Scene scene = new Scene(root, 800, 600);

        // Ρύθμιση του παραθύρου
        primaryStage.setTitle("MediaLab Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        File jsonFile = new File("src/main/resources/medialab/tasks.json");
        if (!jsonFile.exists()) {
            DataManager.saveData(new AppData());
        }
        AppData appData = DataManager.loadData();
        LocalDate today = LocalDate.now();

        // Έλεγχος για καθυστερημένες εργασίες: αν η προθεσμία έχει περάσει και η εργασία δεν είναι COMPLETED, ορίζουμε την κατάσταση σε DELAYED.
        for (Task task : appData.getTasks()) {
            if (task.getDueDate().isBefore(today) && task.getStatus() != Task.TaskStatus.COMPLETED) {
                task.setStatus(Task.TaskStatus.DELAYED);
                //needs to print for cli or set alert for gui
                //System.out.println("⚠️ Η εργασία '" + task.getTitle() + "' είναι καθυστερημένη!");
            }
        }

        for (Task task : appData.getTasks()) {
            //needs modifications to show alla the task details
            System.out.println("➡ " + task.getTitle() + " (" + task.getStatus() + ")");
        }

        DataManager.saveData(appData);
        AppData finalAppData = DataManager.loadData();

        if (args.length > 0 && args[0].equals("terminal")) {
            TerminalInterface terminal = new TerminalInterface();
            terminal.start();
        } else {
            launch(args);  // Εκτέλεση του GUI
        }
    }
}
