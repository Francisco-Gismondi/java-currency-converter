package view;

import com.formdev.flatlaf.*;

import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

public class ThemeManager {

	private static boolean isDarkMode = true;

	private ThemeManager() {
	}

	public static void applyTheme() {
		if (isDarkMode) {
			FlatDarkLaf.setup();
		} else {
			FlatLightLaf.setup();
		}
		applyCustomProperties();
	}

	private static void applyCustomProperties() {
		UIManager.put("Button.arc", 15);
		UIManager.put("Component.arc", 15);
		UIManager.put("Component.arrowType", "triangle");
		UIManager.put("Component.focusWidth", 0);
		UIManager.put("ScrollBar.trackArc", 999);
		UIManager.put("ScrollBar.thumbArc", 999);
		UIManager.put("ScrollBar.trackInsets", new Insets(2, 4, 2, 4));
		UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2));
		UIManager.put("ScrollBar.track", new Color(0xe0e0e0));
		UIManager.put("Component.focusColor", Color.decode("#00FFAA"));
		UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
	}

	public static void toggleTheme(JFrame frame) {
		isDarkMode = !isDarkMode;

		try {
			if (isDarkMode) {
				UIManager.setLookAndFeel(new FlatDarkLaf());
			} else {
				UIManager.setLookAndFeel(new FlatLightLaf());
			}

			applyCustomProperties();

			SwingUtilities.updateComponentTreeUI(frame);

		} catch (Exception ex) {
			System.out.println("Error al cambiar de tema: " + ex.getMessage());
		}
	}

	public static boolean isDarkMode() {
		return isDarkMode;
	}

}
