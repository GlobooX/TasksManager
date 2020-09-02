package pl.coderslab.taskmanager;

import java.io.File;
import java.io.FileNotFoundException;
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
            System.err.println("Nie odnaleziono pliku!");
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
                addTask();
                break;
            case "list":
                showTask();
                break;
        }
    }

    private static void showTask() {
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
        System.out.println("Please add task description");
        String desc = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
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
        System.out.println(ConsoleColors.RED_BACKGROUND + "Invalid option: '"
                + userChoice + "'. Please choose a valid option");
        System.out.println(ConsoleColors.RESET);
    }

    
    

    private static void showMainMenu() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        System.out.print(ConsoleColors.RESET);
        System.out.println("\tadd");
        System.out.println("\tremove");
        System.out.println("\tlist");
        System.out.println("\texit");
    }


    
    
    private static void showExitMessage() {
        System.out.println(ConsoleColors.RED + "Goodbye and remember to come back soon!");
        System.out.print(ConsoleColors.RESET);
    }


    
    
    private static void showWelcomeMessage() {
        System.out.println(ConsoleColors.RED + "Welcome in Task Manager");
        System.out.print(ConsoleColors.RESET);
    }
}