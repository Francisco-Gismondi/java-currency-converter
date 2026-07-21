package view;

import java.util.Scanner;
import service.ConversionService;
import util.NumberFormatter;

public class ConsoleMenu {

	private ConversionService service;
	private Scanner scanner;

	public ConsoleMenu() {
		this.service = new ConversionService();
		this.scanner = new Scanner(System.in);
	}

	public void start() {
		boolean exit = false;

		System.out.println("======================================");
		System.out.println("   BIENVENIDO AL CONVERSOR DE DIVISAS ");
		System.out.println("======================================");

		while (!exit) {
			try {
				System.out.println("\n¿Qué deseas hacer?");
				System.out.println("1. Realizar una conversión");
				System.out.println("2. Salir");
				System.out.print("Elige una opción: ");

				String option = scanner.nextLine();

				if (option.equals("1")) {
					executeConversion();
				} else if (option.equals("2")) {
					System.out.println("¡Gracias por usar el conversor! Hasta luego.");
					exit = true;
				} else {
					System.out.println("Opción no válida. Intenta de nuevo.");
				}
			} catch (Exception e) {
				System.out.println("Ocurrió un error: " + e.getMessage());
			}
		}
		scanner.close();
	}

	private void executeConversion() {
		System.out.print("\nIngresa la moneda base (Ej. USD, ARS, EUR): ");
		String base = scanner.nextLine().trim().toUpperCase();

		System.out.print("Ingresa la moneda a la que quieres convertir (Ej. ARS, BRL): ");
		String target = scanner.nextLine().trim().toUpperCase();

		System.out.print("Ingresa el monto a convertir: ");
		double amount = Double.parseDouble(scanner.nextLine().trim());

		System.out.println("Consultando tipos de cambio en tiempo real...");

		double result = service.convert(base, target, amount);

		String formattedAmount = NumberFormatter.formatForDisplay(amount);
		String formattedResult = NumberFormatter.formatForDisplay(result);

		System.out.println("\n--------------------------------------");
		System.out.println("RESULTADO: " + formattedAmount + " " + base + " = " + formattedResult + " " + target);
		System.out.println("--------------------------------------");
	}
}