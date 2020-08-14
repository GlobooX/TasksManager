import pl.coderslab.ConsoleColors;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    static String[][] tasks;
    static final String file = "tasks.csv";
    static final String[] options = {"add", "remove", "list", "exit"};

    public static void main(String[] args) {

        // Ładowanie danych z pliku
        try {
            loadTasks();
        } catch (FileNotFoundException e) {
            System.err.println("Nie odnaleziono pliku!");
            e.printStackTrace();
        }

        // Wyświetlanie dostępnych opcji
        // Wyświetlanie dostępnych opcji
        showOptions();




        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();

        switch (input) {
            case "add":
                addTask();
                break;
            case "remove":
                addTask();
                break;
            case "list":
                addTask();
                break;
            case "exit":
                break;
            default:
                System.out.println("Please select a correct option.");
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
        System.out.println("Please add task description");
        String desc = scanner.nextLine();
        System.out.println("Please add task due date");
        String date = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
        String important = scanner.nextLine();

        StringBuilder taskString = new StringBuilder();
        taskString.append(desc).append(date).append(important);
        String[] task = scanner.nextLine().split(",");

    }


    private static void showOptions() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:");
        System.out.print(ConsoleColors.RESET);
        for (String option : options) {
            System.out.println(option);
        }
    }


}
