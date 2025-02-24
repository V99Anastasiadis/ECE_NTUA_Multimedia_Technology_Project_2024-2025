package com.taskmanager;

import java.io.File;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("\n🔄 [TEST] Φόρτωση δεδομένων από JSON...");

        // **1️⃣ Έλεγχος αν υπάρχει το JSON αρχείο**
        File jsonFile = new File("src/main/resources/medialab/tasks.json");
        if (!jsonFile.exists()) {
            System.out.println("⚠ Το αρχείο JSON δεν βρέθηκε. Δημιουργία νέου...");
            DataManager.saveData(new AppData());
        }

        // **2️⃣ Φόρτωση δεδομένων από JSON**
        AppData appData = DataManager.loadData();
        LocalDate today = LocalDate.now();

        // **3️⃣ Έλεγχος για καθυστερημένες εργασίες**
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

        // **4️⃣ Εμφάνιση εργασιών**
        System.out.println("\n📌 [TEST] Υπάρχουσες Εργασίες:");
        for (Task task : appData.getTasks()) {
            System.out.println("➡ " + task.getTitle() + " (" + task.getStatus() + ")");
        }

        // **5️⃣ Προσθήκη νέας εργασίας και έλεγχος**
        System.out.println("\n➕ [TEST] Προσθήκη νέας εργασίας...");
        int initialSize = appData.getTasks().size();
        Category workCategory = new Category("Work");
        Priority highPriority = new Priority("High");
        Task newTask = new Task("Develop UI", "Create the user interface", workCategory, highPriority, LocalDate.of(2024, 12, 20), Task.TaskStatus.OPEN);
        appData.getTasks().add(newTask);
        DataManager.saveData(appData);
        appData = DataManager.loadData();

        if (appData.getTasks().size() == initialSize + 1) {
            System.out.println("✅ Η εργασία προστέθηκε επιτυχώς.");
        } else {
            System.out.println("❌ Σφάλμα στην προσθήκη εργασίας.");
        }

        // **6️⃣ Ενημέρωση κατάστασης εργασίας**
        System.out.println("\n✅ [TEST] Ενημέρωση κατάστασης πρώτης εργασίας...");
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

        // **7️⃣ Ενημέρωση προθεσμίας και επιβεβαίωση**
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

        // **8️⃣ Προσθήκη υπενθύμισης και έλεγχος**
        System.out.println("\n🔔 [TEST] Προσθήκη υπενθύμισης...");
        if (!appData.getTasks().isEmpty()) {
            Task taskWithReminder = appData.getTasks().get(0);
            int reminderCount = taskWithReminder.getReminders().size();
            taskWithReminder.setReminder(LocalDate.of(2024, 12, 15), "Reminder: Finish UI Design!");
            DataManager.saveData(appData);
            appData = DataManager.loadData();

            if (taskWithReminder.getReminders().size() == reminderCount + 1) {
                System.out.println("✅ Η υπενθύμιση προστέθηκε επιτυχώς.");
            } else {
                System.out.println("❌ Σφάλμα στην προσθήκη υπενθύμισης.");
            }
        }

        // **9️⃣ Διαγραφή εργασίας και επιβεβαίωση**
        System.out.println("\n🗑 [TEST] Διαγραφή πρώτης εργασίας...");
        if (!appData.getTasks().isEmpty()) {
            Task taskToDelete = appData.getTasks().get(0);
            String taskTitle = taskToDelete.getTitle();
            appData.getTasks().remove(taskToDelete);
            DataManager.saveData(appData);
            appData = DataManager.loadData();

            boolean exists = appData.getTasks().stream().anyMatch(t -> t.getTitle().equals(taskTitle));
            if (!exists) {
                System.out.println("✅ Η εργασία διαγράφηκε επιτυχώς.");
            } else {
                System.out.println("❌ Η εργασία δεν διαγράφηκε σωστά.");
            }
        }

        // **💾 Επαλήθευση τελικής αποθήκευσης**
        System.out.println("\n💾 [TEST] Τελικός έλεγχος αποθήκευσης...");
        DataManager.saveData(appData);
        AppData finalAppData = DataManager.loadData();
        if (finalAppData.getTasks().size() == appData.getTasks().size()) {
            System.out.println("✅ Όλες οι αλλαγές αποθηκεύτηκαν σωστά στο JSON.");
        } else {
            System.out.println("❌ Σφάλμα στην αποθήκευση δεδομένων.");
        }

        System.out.println("\n✅ [TEST] Όλες οι λειτουργίες ελέγχθηκαν επιτυχώς!");
    }
}
