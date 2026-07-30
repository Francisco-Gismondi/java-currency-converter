package view;

import java.util.Scanner;

import service.ConversionService;
import util.NumberFormatter;

public class ConsoleMenu {

    private final ConversionService service;
    private final Scanner scanner;

    public ConsoleMenu() {
        this.service = new ConversionService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;

        System.out.println("======================================");
        System.out.println("   WELCOME TO THE CURRENCY CONVERTER ");
        System.out.println("======================================");

        while (!exit) {
            try {
                System.out.println("\nWhat do you want to do?");
                System.out.println("1. Make a conversion");
                System.out.println("2. Exit");
                System.out.print("Choose an option: ");

                String option = scanner.nextLine();

                if (option.equals("1")) {
                    executeConversion();
                } else if (option.equals("2")) {
                    System.out.println("Thank you for using the converter. See you soon.");
                    exit = true;
                } else {
                    System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void executeConversion() {
        System.out.print("\nEnter the base currency (e.g. USD, ARS, EUR): ");
        String base = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the currency you want to convert to (e.g. ARS, BRL): ");
        String target = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter the amount to convert: ");
        double amount = Double.parseDouble(scanner.nextLine().trim());

        System.out.println("Checking exchange rates in real time...");

        double result = service.convert(base, target, amount);

        String formattedAmount = NumberFormatter.formatForDisplay(amount);
        String formattedResult = NumberFormatter.formatForDisplay(result);

        System.out.println("\n--------------------------------------");
        System.out.println("RESULT: " + formattedAmount + " " + base + " = " + formattedResult + " " + target);
        System.out.println("--------------------------------------");
    }
}