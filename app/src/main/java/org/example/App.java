package org.example;

import java.util.Scanner;

/**
 * Interactive driver program for the TodoList class.
 *
 * The user picks options from a menu. Every input is read as a full line of
 * text and checked before it is used, so invalid input (letters instead of
 * numbers, blank lines, unknown options, out-of-range task numbers, or the
 * input simply ending) never crashes the program.
 */
public class App {

    public static void main(String[] args) {
        run(new Scanner(System.in));
    }

    /** Runs the menu loop until the user quits or the input ends. */
    public static void run(Scanner in) {
        TodoList list = new TodoList();
        System.out.println("Welcome to your To-Do List!");

        while (true) {
            printMenu();
            String choice = readLine(in, "Choose an option (0-6): ");
            if (choice == null) {           // input ended (e.g. Ctrl+D)
                System.out.println();
                System.out.println("Goodbye!");
                return;
            }

            switch (choice) {
                case "1":
                    addTask(in, list);
                    break;
                case "2":
                    completeTask(in, list);
                    break;
                case "3":
                    list.all();
                    break;
                case "4":
                    list.complete();
                    break;
                case "5":
                    list.incomplete();
                    break;
                case "6":
                    clearList(in, list);
                    break;
                case "0":
                    System.out.println("Goodbye!");
                    return;
                default:
                    if (choice.isEmpty()) {
                        System.out.println("You didn't enter anything. Please type a number from 0 to 6.");
                    } else {
                        System.out.println("\"" + choice + "\" is not a valid option. Please type a number from 0 to 6.");
                    }
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("---------- MENU ----------");
        System.out.println("1. Add a task");
        System.out.println("2. Mark a task as complete");
        System.out.println("3. Show all tasks");
        System.out.println("4. Show completed tasks");
        System.out.println("5. Show incomplete tasks");
        System.out.println("6. Clear the list");
        System.out.println("0. Quit");
    }

    private static void addTask(Scanner in, TodoList list) {
        String text = readLine(in, "Enter the task: ");
        if (text == null) {
            return;
        }
        if (list.add(text)) {
            System.out.println("Added: " + text.trim());
        }
    }

    /** Lets the user complete a task by its number or by typing its name. */
    private static void completeTask(Scanner in, TodoList list) {
        if (list.size() == 0) {
            System.out.println("There are no tasks to complete yet.");
            return;
        }
        list.all();
        String input = readLine(in, "Enter the task number or name to complete: ");
        if (input == null) {
            return;
        }

        boolean done;
        if (input.matches("\\d+")) {
            int number;
            try {
                number = Integer.parseInt(input);
            } catch (NumberFormatException e) {   // number too large for an int
                System.out.println("Task number " + input + " does not exist.");
                return;
            }
            done = list.complete(number);
        } else {
            done = list.complete(input);
        }
        if (done) {
            System.out.println("Nice work! Task marked as complete.");
        }
    }

    /** Asks for confirmation before deleting everything. */
    private static void clearList(Scanner in, TodoList list) {
        while (true) {
            String answer = readLine(in, "Are you sure you want to delete ALL tasks? (y/n): ");
            if (answer == null) {
                return;
            }
            answer = answer.toLowerCase();
            if (answer.equals("y") || answer.equals("yes")) {
                list.clear();
                return;
            }
            if (answer.equals("n") || answer.equals("no")) {
                System.out.println("Okay, nothing was deleted.");
                return;
            }
            System.out.println("Please answer y or n.");
        }
    }

    /** Prints a prompt and reads one trimmed line, or returns null if input has ended. */
    private static String readLine(Scanner in, String prompt) {
        System.out.print(prompt);
        if (!in.hasNextLine()) {
            return null;
        }
        return in.nextLine().trim();
    }
}