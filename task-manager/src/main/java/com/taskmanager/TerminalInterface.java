package com.taskmanager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class TerminalInterface {
    private AppData appData;
    private Scanner scanner;
    Priority defaultPriority = new Priority("default");

    public TerminalInterface() {
        this.appData = DataManager.loadData();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        appData.getPriorities().add(defaultPriority);
        while (true) {
            System.out.println("\n=== MediaLab Task Manager ===");
            System.out.println("1. View Tasks");
            System.out.println("2. Add Task");
            System.out.println("3. Modify Task");
            System.out.println("4. Delete Task");
            System.out.println("5. View Categories");
            System.out.println("6. Add Category");
            System.out.println("7. Delete Category");
            System.out.println("8. View Priorities");
            System.out.println("9. Add Priority");
            System.out.println("10. Delete Priority");
            System.out.println("11. View Reminders");
            System.out.println("12. Add Reminder");
            System.out.println("13. Delete Reminder");
            System.out.println("14. Search Tasks");
            System.out.println("15. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Καθαρισμός buffer

            switch (choice) {
                case 1:
                    viewTasks();
                    break;
                case 2:
                    addTask();
                    break;
                case 3:
                    modifyTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    viewCategories();
                    break;
                case 6:
                    addCategory();
                    break;
                case 7:
                    deleteCategory();
                    break;
                case 8:
                    viewPriorities();
                    break;
                case 9:
                    addPriority();
                    break;
                case 10:
                    deletePriority();
                    break;
                case 11:
                    viewReminders();
                    break;
                case 12:
                    addReminder();
                    break;
                case 13:
                    deleteReminder();
                    break;
                case 14:
                    searchTasks();
                    break;
                case 15:
                    DataManager.saveData(appData);
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewTasks() {
        System.out.println("\n=== Εργασίες ===");
        if (appData.getTasks().isEmpty()) {
            System.out.println("Δεν υπάρχουν διαθέσιμες εργασίες.");
            return;
        }
        for (Task task : appData.getTasks()) {
            System.out.println("Τίτλος: " + task.getTitle());
            System.out.println("Περιγραφή: " + task.getDescription());
            System.out.println("Κατηγορία: " + task.getCategory().getName());
            System.out.println("Προτεραιότητα: " + task.getPriority().getName());
            System.out.println("Ημερομηνία Προθεσμίας: " + task.getDueDate());
            System.out.println("Κατάσταση: " + task.getStatus());
            
            if (!task.getReminders().isEmpty()) {
                System.out.println("Υπενθυμίσεις:");
                for (Reminder reminder : task.getReminders()) {
                    System.out.println("  - Ημερομηνία: " + reminder.getReminderDate() + ", Μήνυμα: " + reminder.getMessage());
                }
            } else {
                System.out.println("Δεν υπάρχουν υπενθυμίσεις για αυτή την εργασία.");
            }
    
            System.out.println("--------------------------------");
        }
    }
    
    private boolean isDuplicateTask(String title) {
        return appData.getTasks().stream()
            .anyMatch(task -> task.getTitle().equalsIgnoreCase(title));
    }

    private Category findCategoryByName(String name) {
        return appData.getCategories().stream()
            .filter(category -> category.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    private Priority findPriorityByName(String name) {
        return appData.getPriorities().stream()
            .filter(priority -> priority.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    private void addTask() {
        System.out.print("Enter task title: ");
        String title = scanner.nextLine();
        while(isDuplicateTask(title)) {
            System.out.println("Task with the same title already exists. Please enter a different title.");
            System.out.print("Enter task title: ");
            title = scanner.nextLine();
        }
        System.out.print("Enter task description: ");
        String description = scanner.nextLine();
        System.out.print("Enter task category: ");
        String categoryName = scanner.nextLine();
        System.out.print("Enter task priority: ");
        String priorityName = scanner.nextLine();
        System.out.print("Enter due date (yyyy-MM-dd): ");
        LocalDate dueDate = LocalDate.parse(scanner.nextLine());
        Task task = new Task(title, description, categoryName, priorityName, dueDate, Task.TaskStatus.OPEN);
        appData.getTasks().add(task);
        System.out.println("Task added successfully.");
    }

    private void modifyTask() {
        System.out.print("Enter task title to modify: ");
        String title = scanner.nextLine();
        Task task = findTaskByTitle(title);
        if (task != null) {
            System.out.print("Enter new title: ");
            task.setTitle(scanner.nextLine());
            System.out.print("Enter new description: ");
            task.setDescription(scanner.nextLine());
            System.out.println("Task modified successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private void deleteTask() {
        System.out.print("Enter task title to delete: ");
        String title = scanner.nextLine();
        Task task = findTaskByTitle(title);
        if (task != null) {
            task.deleteTask(); 
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private void viewCategories() {
        System.out.println("\n=== Κατηγορίες ===");
        if (appData.getCategories().isEmpty()) {
            System.out.println("Δεν υπάρχουν διαθέσιμες κατηγορίες.");
            return;
        }
        for (Category category : appData.getCategories()) {
            System.out.println("Όνομα: " + category.getName());
            if (!category.getTasks().isEmpty()) {
                System.out.println("Εργασίες:");
                for (Task task : category.getTasks()) {
                    System.out.println("  - " + task.getTitle());
                }
            } else {
                System.out.println("Δεν υπάρχουν εργασίες σε αυτή την κατηγορία.");
            }
            System.out.println("--------------------------------");
        }
    }    

    private void addCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        appData.getCategories().add(new Category(name));
        System.out.println("Category added successfully.");
    }

    private void deleteCategory() {
        System.out.print("Enter category name to delete: ");
        String name = scanner.nextLine();
        Category category = findCategoryByName(name);
        if (category != null) {
            category.deleteCategory(); 
            appData.getTasks().removeIf(task -> task.getCategory().equals(category));
            System.out.println("Category deleted successfully.");
        } else {
            System.out.println("Category not found.");
        }
    }

    private void viewPriorities() {
        System.out.println("\n=== Προτεραιότητες ===");
        if (appData.getPriorities().isEmpty()) {
            System.out.println("Δεν υπάρχουν διαθέσιμες προτεραιότητες.");
            return;
        }
        for (Priority priority : appData.getPriorities()) {
            System.out.println("Όνομα: " + priority.getName());
            if (!priority.getTasks().isEmpty()) {
                System.out.println("Εργασίες:");
                for (Task task : priority.getTasks()) {
                    System.out.println("  - " + task.getTitle());
                }
            } else {
                System.out.println("Δεν υπάρχουν εργασίες σε αυτή την προτεραιότητα.");
            }
            System.out.println("--------------------------------");
        }
    }    

    private void addPriority() {
        System.out.print("Enter priority name: ");
        String name = scanner.nextLine();
        appData.getPriorities().add(new Priority(name));
        System.out.println("Priority added successfully.");
    }

    private void deletePriority() {
        System.out.print("Enter priority name to delete: ");
        String name = scanner.nextLine();
        Priority priority = findPriorityByName(name);
        if (priority.getName().equals("default")) {
            System.out.println("You can't delete the default");
            return;
        }
        if (priority != null) {
            for (Task task : priority.getTasks()) {  
                task.setPriority(defaultPriority.getName());
            }
            priority.deletePriority(defaultPriority); 
            System.out.println("Priority deleted successfully.");
        } else {
            System.out.println("Priority not found.");
        }
    }

    private void viewReminders() {
        System.out.println("\n=== Υπενθυμίσεις ===");
        boolean foundReminders = false;
    
        for (Task task : appData.getTasks()) {
            if (!task.getReminders().isEmpty()) {
                foundReminders = true;
                System.out.println("Εργασία: " + task.getTitle());
                for (Reminder reminder : task.getReminders()) {
                    System.out.println("  - Ημερομηνία: " + reminder.getReminderDate() + ", Μήνυμα: " + reminder.getMessage());
                }
                System.out.println("--------------------------------");
            }
        }
    
        if (!foundReminders) {
            System.out.println("Δεν υπάρχουν διαθέσιμες υπενθυμίσεις.");
        }
    }
      
    private void addReminder() {
        System.out.print("Enter task title for reminder: ");
        String title = scanner.nextLine();
        Task task = findTaskByTitle(title);
        if (task != null) {
            System.out.print("Enter reminder date (yyyy-MM-dd): ");
            LocalDate reminderDate = LocalDate.parse(scanner.nextLine());
            System.out.print("Enter reminder message: ");
            String message = scanner.nextLine();
            task.setReminder(reminderDate, message); 
            System.out.println("Reminder added successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }
    
    private void deleteReminder() {
        System.out.print("Enter task title for reminder deletion: ");
        String title = scanner.nextLine();
        Task task = findTaskByTitle(title);
        if (task != null) {
            System.out.print("Enter reminder message to delete: ");
            String message = scanner.nextLine();
            Reminder reminderToDelete = null;
            for (Reminder reminder : task.getReminders()) {
                if (reminder.getMessage().equals(message)) {
                    reminderToDelete = reminder;
                    break;
                }
            }
            if (reminderToDelete != null) {
                task.deleteReminder(reminderToDelete); 
                System.out.println("Reminder deleted successfully.");
            } else {
                System.out.println("Reminder not found.");
            }
        } else {
            System.out.println("Task not found.");
        }
    }

    private void searchTasks() {
        System.out.print("Enter search query: ");
        String query = scanner.nextLine().toLowerCase();
        List<Task> results = appData.getTasks().stream()
            .filter(task -> task.getTitle().toLowerCase().contains(query) || 
                            task.getCategory().getName().toLowerCase().contains(query) ||
                            task.getPriority().getName().toLowerCase().contains(query))
            .toList();
        System.out.println("\n=== Search Results ===");
        for (Task task : results) {
            System.out.println(task.getTitle() + " - " + task.getStatus());
        }
    }

    private Task findTaskByTitle(String title) {
        return appData.getTasks().stream()
            .filter(task -> task.getTitle().equalsIgnoreCase(title))
            .findFirst()
            .orElse(null);
    }

    public static void main(String[] args) {
        TerminalInterface terminal = new TerminalInterface();
        terminal.start();
    }
}