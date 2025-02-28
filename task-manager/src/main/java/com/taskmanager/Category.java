package com.taskmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση Category αναπαριστά μια κατηγορία εργασιών μέσα στο σύστημα διαχείρισης εργασιών.
 * Κάθε κατηγορία έχει ένα όνομα και μια λίστα εργασιών που της ανήκουν.
 */
public class Category {
    private String name;
    private List<Task> tasks;

    /**
     * Δημιουργεί μια νέα κατηγορία με το δοσμένο όνομα.
     * @param name Το όνομα της κατηγορίας.
     */
    public Category(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    /**
     * Επιστρέφει το όνομα της κατηγορίας.
     * @return Το όνομα της κατηγορίας.
     */
    public String getName() {
        return name;
    }

    /**
     * Ορίζει ένα νέο όνομα για την κατηγορία.
     * @param name Το νέο όνομα της κατηγορίας.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Επιστρέφει τη λίστα των εργασιών που ανήκουν σε αυτή την κατηγορία.
     * @return Μια λίστα με τις εργασίες της κατηγορίας.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Προσθέτει μια εργασία στην κατηγορία.
     * @param task Η εργασία που θα προστεθεί.
     */
    public void addTask(Task task) {
        if (!tasks.contains(task)) {
            tasks.add(task);
        }
    }

    /**
     * Αφαιρεί μια εργασία από την κατηγορία.
     * @param task Η εργασία που θα αφαιρεθεί.
     */
    public void deleteTask(Task task) {
        tasks.remove(task);
    }

    /**
     * Διαγράφει την κατηγορία και όλες τις εργασίες που περιέχει.
     */
    public void deleteCategory() {
        tasks.clear();
    }
}
