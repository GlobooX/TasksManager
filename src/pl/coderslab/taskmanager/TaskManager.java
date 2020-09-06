package pl.coderslab.taskmanager;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class TaskManager {

    static String[][] tasks;
    static final String file = "tasks.csv";
    static final String[] options = {"add", "remove", "list", "exit"};


    public static void main(String[] args) {
        TaskManager.run();
    }


    public static void run() {
        showWelcomeMessage();

        // Ładowanie danych z pliku
        try {
            loadTasks();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
            e.printStackTrace();
        }


        while (true) {
            showMainMenu();
            String userChoice = getUserChoice();
            if (validateUserChoice(userChoice)) {
                executeValidChoice(userChoice);
                if (isExitChoice(userChoice)) {
                    break;
                }
            } else {
                executeInvalidChoice(userChoice);
            }
        }
        showExitMessage();
        saveTasksToFile();
    }


    private static void saveTasksToFile() {
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(file);
            for (String[] taskElement : tasks) {
                for (String oneTask : taskElement) {
                    printWriter.write(oneTask);
                    printWriter.write(", ");
                }
                printWriter.write("\n");
            }
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static String getUserChoice() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next().trim();
    }


    private static boolean validateUserChoice(String userChoice) {
        // Do wykorzystania binarySearch potrzeba posorotowanej tablicy!
        Arrays.sort(options);
        int index = Arrays.binarySearch(options, userChoice);
        return index >= 0;
    }


    private static void executeValidChoice(String userChoice) {
        switch (userChoice) {
            case "add":
                addTask();
                break;
            case "remove":
                removeTask();
                break;
            case "list":
                showTask();
                break;
        }
    }

    private static void removeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE + "Please select number to remove.");

        String n = scanner.nextLine();
        while (!isNumberGreaterEqualZero(n)) {
            System.out.println(ConsoleColors.RED + "Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }

        try {
            tasks = ArrayUtils.remove(tasks, Integer.valueOf(n) - 1);
            System.out.println(ConsoleColors.GREEN + "Task " + n + " was removed correctly!");
        } catch (IndexOutOfBoundsException ex) {
            System.out.println(ConsoleColors.RED + "Tasks does not exists... Type 'list' to check tasks numbers!");
        }
    }


    public static boolean isNumberGreaterEqualZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }


    private static void showTask() {
        if (tasks.length > 0) {
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("  ------- Here is list of Your tasks --------");
            int index = 1;
            for (String[] taskElement : tasks) {
                System.out.print(index + ": ");
                for (String oneTask : taskElement) {
                    System.out.print(oneTask);
                }
                System.out.println("");
                index++;
            }
            System.out.println(" ");
        } else {
            System.out.println("There is no tasks...");
        }
    }


    private static void loadTasks() throws FileNotFoundException {
        tasks = new String[0][4];
        Scanner scanner = new Scanner(new File(file));

        while (scanner.hasNextLine()) {
            String[] task = scanner.nextLine().split(",");
            tasks = Arrays.copyOf(tasks, tasks.length + 1);
            tasks[tasks.length - 1] = task;
        }
    }


    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(ConsoleColors.BLUE + "   [Please add task description]");
        String desc = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE + "   [Please add task due date]");
        String date = scanner.nextLine();
        System.out.println(ConsoleColors.BLUE + "   [Is your task is important: true/false]");
        String important = scanner.nextLine();

        StringBuilder taskString = new StringBuilder();
        taskString.append(desc).append(" ").append(date).append(" ").append(important);
        String[] task = taskString.toString().split(",");
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = task;
    }


    private static boolean isExitChoice(String userChoice) {
        if (userChoice.equalsIgnoreCase("exit")) {
            return true;
        } else {
            return false;
        }
    }


    private static void executeInvalidChoice(String userChoice) {
        clearConsoleScreen();
        System.out.println(ConsoleColors.RED + "  Invalid option: '" + userChoice);
        System.out.println(ConsoleColors.RESET);
    }


    private static void showMainMenu() {
        System.out.println(ConsoleColors.BLUE + "  [Please select an option]");
        System.out.print(ConsoleColors.RESET);
        System.out.print("  add");
        System.out.print(" | remove");
        System.out.print(" | list");
        System.out.println(" | exit");
    }


    private static void showExitMessage() {
        clearConsoleScreen();
        System.out.println(ConsoleColors.GREEN + "+-----------------------------------------+");
        System.out.println(ConsoleColors.GREEN + "  Goodbye and remember to come back soon!");
        System.out.println(ConsoleColors.GREEN + "+-----------------------------------------+\n");
        System.out.print(ConsoleColors.RESET);
    }


    private static void clearConsoleScreen() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append(" \n");
        }
        System.out.println(sb);
    }


    private static void showWelcomeMessage() {
        clearConsoleScreen();
        System.out.println(ConsoleColors.RED + "+---------------------------+");
        System.out.println(ConsoleColors.RED + "   Welcome in Task Manager");
        System.out.println(ConsoleColors.RED + "+---------------------------+\n");
        System.out.print(ConsoleColors.RESET);
    }
}