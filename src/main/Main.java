package main;

import javax.swing.SwingUtilities;
import view.*;

public class Main {
	public static void main(String[] args) {

		SwingUtilities.invokeLater(() -> {
			
			ThemeManager.applyTheme();
			MainWindow ventana = new MainWindow();

		});
	}
}