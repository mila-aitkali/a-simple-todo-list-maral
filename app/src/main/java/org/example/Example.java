package org.example;

// This file holds the TodoList class (and its nested Task class).
// It is named Example.java to match the project template; because TodoList
// is not public, Java allows it to live in a file with a different name.

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores and manages a user's tasks.
 *
 * Invalid input (null, empty or blank text, duplicates, unknown tasks,
 * out-of-range numbers) never crashes the program. The method prints a
 * short message and returns false instead.
 */
class TodoList {
    /** A single item in a to-do list. */
    public static class Task {
        private final String description;
        private boolean completed;

        public Task(String description) {
            this.description = description;
            this.completed = false;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void markComplete() {
            completed = true;
        }

        @Override
        public String toString() {
            return (completed ? "[x] " : "[ ] ") + description;
        }
    }


    private final List<Task> tasks = new ArrayList<>();

    // ---------- Adding and completing ----------

    /**
     * Adds a new task. Returns true if it was added.
     * Rejects null, blank, and duplicate (case-insensitive) descriptions.
     */
    public boolean add(String description) {
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Cannot add an empty task.");
            return false;
        }
        String cleaned = description.trim();
        if (find(cleaned) != null) {
            System.out.println("\"" + cleaned + "\" is already on the list.");
            return false;
        }
        tasks.add(new Task(cleaned));
        return true;
    }

    /**
     * Marks the task with this description as complete (case-insensitive).
     * Returns true if a task was marked complete.
     */
    public boolean complete(String description) {
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Please enter a task to complete.");
            return false;
        }
        Task task = find(description.trim());
        if (task == null) {
            System.out.println("No task named \"" + description.trim() + "\" was found.");
            return false;
        }
        if (task.isCompleted()) {
            System.out.println("\"" + task.getDescription() + "\" is already complete.");
            return false;
        }
        task.markComplete();
        return true;
    }

    /**
     * Marks a task complete by its position in the list (1 = first task,
     * matching the numbers shown by all()). Returns true on success.
     */
    public boolean complete(int number) {
        if (number < 1 || number > tasks.size()) {
            System.out.println("Task number " + number + " does not exist.");
            return false;
        }
        return complete(tasks.get(number - 1).getDescription());
    }

    // ---------- Viewing ----------

    /** Prints and returns every task. */
    public List<Task> all() {
        printTasks("All tasks", tasks);
        return Collections.unmodifiableList(new ArrayList<>(tasks));
    }

    /** Prints and returns the completed tasks. */
    public List<Task> complete() {
        List<Task> done = filter(true);
        printTasks("Completed tasks", done);
        return done;
    }

    /** Prints and returns the incomplete tasks. */
    public List<Task> incomplete() {
        List<Task> notDone = filter(false);
        printTasks("Incomplete tasks", notDone);
        return notDone;
    }

    // ---------- Clearing ----------

    /** Deletes every task, complete and incomplete. */
    public void clear() {
        tasks.clear();
        System.out.println("The to-do list has been cleared.");
    }

    public int size() {
        return tasks.size();
    }

    // ---------- Helpers ----------

    private Task find(String description) {
        for (Task task : tasks) {
            if (task.getDescription().equalsIgnoreCase(description)) {
                return task;
            }
        }
        return null;
    }

    private List<Task> filter(boolean completed) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isCompleted() == completed) {
                result.add(task);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private void printTasks(String title, List<Task> list) {
        System.out.println("=== " + title + " ===");
        if (list.isEmpty()) {
            System.out.println("  (nothing here - the list is empty)");
        } else {
            for (int i = 0; i < list.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + list.get(i));
            }
        }
        System.out.println();
    }
}