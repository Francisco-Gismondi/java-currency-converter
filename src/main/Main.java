package main;

import javax.swing.SwingUtilities;

import view.MainWindow;
import view.ThemeManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ThemeManager.applyTheme();
            MainWindow window = new MainWindow();
        });
    }
}