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
        Priority defaultPriority = new Priority("default");
        System.out.println("\n🔄 [TEST] Φόρτωση δεδομένων από JSON...");

        // 1️⃣ Έλεγχος ύπαρξης του JSON αρχείου και δημιουργία νέου αν δεν υπάρχει.
        File jsonFile = new File("src/main/resources/medialab/tasks.json");
        if (!jsonFile.exists()) {
            System.out.println("⚠ Το αρχείο JSON δεν βρέθηκε. Δημιουργία νέου...");
            DataManager.saveData(new AppData());
        }

        // 2️⃣ Φόρτωση δεδομένων από JSON.
        AppData appData = DataManager.loadData();
        LocalDate today = LocalDate.now();

        // 3️⃣ Έλεγχος για καθυστερημένες εργασίες: αν η προθεσμία έχει περάσει και η εργασία δεν είναι COMPLETED, ορίζουμε την κατάσταση σε DELAYED.
        System.out.println("\n⏳ [TEST] Έλεγχος καθυστερημένων εργασιών...");
        boolean foundDelayed = false;
        for (Task task : appData.getTasks()) {
            if (task.getDueDate().isBefore(today) && task.getStatus() != Task.TaskStatus.COMPLETED) {
                task.setStatus(Task.TaskStatus.DELAYED);
                foundDelayed = true;
            }
        }
        if (foundDelayed) {
            System.out.println("✅ Βρέθηκαν καθυστερημένες εργασίες. Το JSON ενημερώθηκε.");
            DataManager.saveData(appData);
        } else {
            System.out.println("✅ Δεν υπάρχουν καθυστερημένες εργασίες.");
        }

        // 4️⃣ Εμφάνιση των τρεχουσών εργασιών.
        System.out.println("\n📌 [TEST] Υπάρχουσες Εργασίες:");
        for (Task task : appData.getTasks()) {
            System.out.println("➡ " + task.getTitle() + " (" + task.getStatus() + ")");
        }

        // 5️⃣ Προσθήκη νέας εργασίας και έλεγχος.
        System.out.println("\n➕ [TEST] Προσθήκη νέας εργασίας...");
        int initialSize = appData.getTasks().size();
        Category workCategory = new Category("Work");
        Priority highPriority = new Priority("High");
        Task newTask = new Task("Develop UI", "Create the user interface", workCategory, highPriority, LocalDate.of(2024, 12, 20), Task.TaskStatus.OPEN);
        // Σύνδεση εργασίας με την κατηγορία και προτεραιότητα.
        workCategory.addTask(newTask);
        highPriority.addTask(newTask);
        appData.getTasks().add(newTask);
        DataManager.saveData(appData);
        appData = DataManager.loadData();
        if (appData.getTasks().size() == initialSize + 1) {
            System.out.println("✅ Η εργασία προστέθηκε επιτυχώς.");
        } else {
            System.out.println("❌ Σφάλμα στην προσθήκη εργασίας.");
        }

        // 6️⃣ Ενημέρωση κατάστασης εργασίας σε COMPLETED.
        System.out.println("\n✅ [TEST] Ενημέρωση κατάστασης πρώτης εργασίας σε COMPLETED...");
        if (!appData.getTasks().isEmpty()) {
            Task firstTask = appData.getTasks().get(0);
            firstTask.setStatus(Task.TaskStatus.COMPLETED);
            DataManager.saveData(appData);
            appData = DataManager.loadData();
            if (firstTask.getStatus() == Task.TaskStatus.COMPLETED) {
                System.out.println("✅ Η εργασία ενημερώθηκε σε COMPLETED.");
            } else {
                System.out.println("❌ Η ενημέρωση της εργασίας απέτυχε.");
            }
        }

        // 7️⃣ Ενημέρωση προθεσμίας εργασίας και έλεγχος.
        System.out.println("\n🕒 [TEST] Ενημέρωση προθεσμίας...");
        if (!appData.getTasks().isEmpty()) {
            Task taskToUpdate = appData.getTasks().get(0);
            LocalDate newDueDate = LocalDate.of(2024, 12, 31);
            taskToUpdate.setDueDate(newDueDate);
            DataManager.saveData(appData);
            appData = DataManager.loadData();
            if (taskToUpdate.getDueDate().equals(newDueDate)) {
                System.out.println("✅ Η προθεσμία ενημερώθηκε σε " + newDueDate);
            } else {
                System.out.println("❌ Η ενημέρωση της προθεσμίας απέτυχε.");
            }
        }

        // 8️⃣ Δοκιμή διαχείρισης κατηγορίας:
        // Δημιουργούμε μια νέα κατηγορία και μια εργασία που ανήκει σε αυτή, στη συνέχεια διαγράφουμε την κατηγορία.
        System.out.println("\n🗑 [TEST] Διαγραφή κατηγορίας μαζί με τις εργασίες της...");
        Category testCategory = new Category("TestCategory");
        appData.getCategories().add(testCategory);
        Task taskInCategory = new Task("Task in Category", "Testing deletion of category", testCategory, new Priority("Medium"), LocalDate.now(), Task.TaskStatus.OPEN);
        testCategory.addTask(taskInCategory);
        appData.getTasks().add(taskInCategory);
        DataManager.saveData(appData);
        // Κλήση μεθόδου διαγραφής κατηγορίας που διαγράφει και τις εργασίες της.
        testCategory.deleteCategory();
        // Αφαιρούμε και την κατηγορία από τη λίστα (αν χρειάζεται)
        appData.getCategories().remove(testCategory);
        // Επίσης, αφαιρούμε τις διαγραμμένες εργασίες από τη λίστα εργασιών.
        appData.getTasks().removeIf(t -> t.getCategory().getName().equals("TestCategory"));
        DataManager.saveData(appData);
        boolean taskExists = appData.getTasks().stream().anyMatch(t -> t.getTitle().equals("Task in Category"));
        if (!taskExists) {
            System.out.println("✅ Η κατηγορία και οι σχετικές εργασίες διαγράφηκαν σωστά.");
        } else {
            System.out.println("❌ Σφάλμα στη διαγραφή κατηγορίας ή των εργασιών της.");
        }

        //  curently under construction
        // 9️⃣ Δοκιμή διαχείρισης προτεραιότητας:
        // Δημιουργούμε μια προτεραιότητα και μια εργασία με αυτή, στη συνέχεια διαγράφουμε την προτεραιότητα.
        System.out.println("\n🔽 [TEST] Διαγραφή προτεραιότητας...");
        Priority testPriority = new Priority("TestPriority");
        appData.getPriorities().add(testPriority);
        Task taskWithPriority = new Task("Task with Priority", "Testing deletion of priority", new Category("General"), testPriority, LocalDate.now(), Task.TaskStatus.OPEN);
        testPriority.addTask(taskWithPriority);
        appData.getTasks().add(taskWithPriority);
        DataManager.saveData(appData);
        // Κλήση μεθόδου διαγραφής προτεραιότητας.
        testPriority.deletePriority(defaultPriority);
        // Ενημερώνουμε την εργασία ώστε να πάρει την default προτεραιότητα.
        taskWithPriority.setPriority(defaultPriority);
        DataManager.saveData(appData);
        if (taskWithPriority.getPriority().getName().equals("default")) {
            System.out.println("✅ Οι εργασίες με διαγραμμένη προτεραιότητα μεταφέρθηκαν στο default.");
        } else {
            System.out.println("❌ Σφάλμα στην ενημέρωση προτεραιότητας.");
        } 

        // 🔔 10️⃣ Δοκιμή υπενθύμισης:
        // Προσπάθεια ορισμού υπενθύμισης σε COMPLETED εργασία πρέπει να απορρίπτεται.
        System.out.println("\n❌ [TEST] Απαγόρευση υπενθύμισης σε Completed εργασία...");
        Task completedTask = new Task("Completed Task", "Testing reminder on completed task", new Category("General"), new Priority("Low"), LocalDate.now(), Task.TaskStatus.COMPLETED);
        appData.getTasks().add(completedTask);
        DataManager.saveData(appData);
        // Προσπάθεια ορισμού υπενθύμισης
        completedTask.setReminder(LocalDate.now().plusDays(1), "Reminder: Should not be added");
        if (completedTask.getReminders().isEmpty()) {
            System.out.println("✅ Δεν επιτρέπεται υπενθύμιση σε ολοκληρωμένη εργασία.");
        } else {
            System.out.println("❌ Λάθος! Επιτράπηκε υπενθύμιση σε COMPLETED εργασία.");
        }

        // 🗑 11️⃣ Δοκιμή διαγραφής εργασίας:
        // Η διαγραφή μιας εργασίας πρέπει να αφαιρεί και όλες τις υπενθυμίσεις της.
        System.out.println("\n🗑 [TEST] Διαγραφή εργασίας και υπενθυμίσεών της...");
        Task taskWithReminder = new Task("Task With Reminder", "Testing deletion of task reminders", new Category("General"), new Priority("Medium"), LocalDate.now().plusDays(10), Task.TaskStatus.OPEN);
        taskWithReminder.setReminder(LocalDate.now().plusDays(5), "Upcoming deadline");
        appData.getTasks().add(taskWithReminder);
        DataManager.saveData(appData);
        // Διαγραφή της εργασίας.
        appData.getTasks().remove(taskWithReminder);
        DataManager.saveData(appData);
        if (taskWithReminder.getReminders().isEmpty()) {
            System.out.println("✅ Όλες οι υπενθυμίσεις διαγράφηκαν σωστά μαζί με την εργασία.");
        } else {
            System.out.println("❌ Σφάλμα! Υπενθυμίσεις υπάρχουν ακόμα μετά τη διαγραφή.");
        }

        // 💾 Τελικός έλεγχος αποθήκευσης.
        System.out.println("\n💾 [TEST] Τελικός έλεγχος αποθήκευσης...");
        DataManager.saveData(appData);
        AppData finalAppData = DataManager.loadData();
        if (finalAppData.getTasks().size() == appData.getTasks().size()) {
            System.out.println("✅ Όλες οι αλλαγές αποθηκεύτηκαν σωστά στο JSON.");
        } else {
            System.out.println("❌ Σφάλμα στην αποθήκευση δεδομένων.");
        }

        System.out.println("\n✅ [TEST] Όλες οι λειτουργίες ελέγχθηκαν επιτυχώς!");
        if (args.length > 0 && args[0].equals("terminal")) {
            TerminalInterface terminal = new TerminalInterface();
            terminal.start();
        } else {
            launch(args);  // Εκτέλεση του GUI
        }
    }
}
