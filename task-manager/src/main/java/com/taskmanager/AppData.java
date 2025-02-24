package com.taskmanager;

import java.util.ArrayList;
import java.util.List;

public class AppData {
    private List<Task> tasks = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();
    private List<Priority> priorities = new ArrayList<>();

    public List<Task> getTasks() { return tasks; }
    public List<Category> getCategories() { return categories; }
    public List<Priority> getPriorities() { return priorities; }
}
