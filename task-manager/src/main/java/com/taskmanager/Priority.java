package com.taskmanager;
import java.util.ArrayList;
import java.util.List;
public class Priority {
    private List<Task> tasks;
    private String name;
    public Priority(String string) {
        this.name = string;
        this.tasks = new ArrayList<>();
    }

    public String getName() { return name; }
    public List<Task> getTasks() { return tasks; }

    public void setName(String name) { 
        if(this.name.equals("default")){
            System.out.println("You can't change the name of the default");
            return;
        } else if (name.equals("default")) { //not sure who to handle new priorities
                //not sure if this is the correct way to handle the default
                System.out.println("You can't use the name default");
                return;
            } 
        else this.name = name; 
    }

    //problem with the default that it is required to be "default"
    public void addTask(Task task) { 
        tasks.add(task); 
    }

    public void deleteTask(Task task) { tasks.remove(task); }

    public void deletePriority(Priority defaultPriority) {
        String priorityName = this.getName();
        if (!priorityName.equals("default")) {
            for (Task task : new ArrayList<>(tasks)) {  
                task.setPriority(defaultPriority);
            }
            tasks.clear();
        } else {
            System.out.println("You can't delete the default");
        }
    }
}
