package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import service.ConversionService;
import util.CurrencyLoader;
import util.NumberFormatter;

public class MainWindow {

	private JFrame frame;
	private JPanel mainPanel;
	private JPanel formPanel;
	private JButton convertButton;
	private JButton exitButton;
	private JButton changeButton;
	private JTextField amountField;
	private JTextField resultField;
	private JComboBox<String> sourceCurrencyBox;
	private JComboBox<String> targetCurrencyBox;
	private final ConversionService conversionService;
	private JMenuItem themeMenuItem;
	private JLabel statusLabel;
	
	public MainWindow() {
		this.conversionService = new ConversionService();
		initComponents();
		setupListeners();

		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	private void initComponents() {
		frame = new JFrame("Currency Converter");
		frame.setSize(600, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			java.net.URL iconURL = getClass().getResource("/icon.png");
			if (iconURL != null) {
				ImageIcon icon = new ImageIcon(iconURL);
				frame.setIconImage(icon.getImage());
			} else {
				System.out.println("Icon file not found.");
			}
		} catch (Exception e) {
			System.out.println("Error loading the icon: " + e.getMessage());
		}

		JMenuBar menuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Settings");
		themeMenuItem = new JMenuItem("Light mode");

		optionsMenu.add(themeMenuItem);
		menuBar.add(optionsMenu);
		frame.setJMenuBar(menuBar);

		mainPanel = new JPanel(new BorderLayout(20, 20));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		String[] currencyList = CurrencyLoader.getFormattedCurrencies();

		sourceCurrencyBox = new JComboBox<>(currencyList);
		targetCurrencyBox = new JComboBox<>(currencyList);

		sourceCurrencyBox.setPreferredSize(new Dimension(180, 30));
		targetCurrencyBox.setPreferredSize(new Dimension(180, 30));

		amountField = new JTextField();
		resultField = new JTextField();
		resultField.setEditable(false);

		amountField.setPreferredSize(new Dimension(180, 30));
		resultField.setPreferredSize(new Dimension(180, 30));

		convertButton = new JButton("Convert");
		exitButton = new JButton("Exit");
		changeButton = new JButton("↔");

		formPanel = new JPanel();
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

		JPanel currencyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
		currencyPanel.add(createVerticalBlock("From:", sourceCurrencyBox));

		JPanel buttonPanel = new JPanel(new BorderLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
		buttonPanel.add(changeButton, BorderLayout.CENTER);

		currencyPanel.add(buttonPanel);
		currencyPanel.add(createVerticalBlock("To:", targetCurrencyBox));

		JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
		amountPanel.add(createVerticalBlock("Amount:", amountField));

		JPanel arrowPanel = new JPanel(new BorderLayout());
		arrowPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10));
		arrowPanel.add(new JLabel("→"), BorderLayout.CENTER);

		amountPanel.add(arrowPanel);
		amountPanel.add(createVerticalBlock("Result:", resultField));

		JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		buttonContainer.add(convertButton);
		buttonContainer.add(exitButton);

		formPanel.add(currencyPanel);
		formPanel.add(amountPanel);
		formPanel.add(buttonContainer);

		mainPanel.add(formPanel, BorderLayout.CENTER);

		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(JLabel.CENTER);

		if (conversionService.isOfflineMode()) {
			statusLabel.setText("Offline mode. Last update: " + conversionService.getLastUpdateTime());
			statusLabel.setForeground(Color.RED);
		} else {
			statusLabel.setText("Online mode. Rates are up to date.");
			statusLabel.setForeground(new Color(16, 185, 129));
		}
		mainPanel.add(statusLabel, BorderLayout.SOUTH);

		frame.add(mainPanel);
		frame.setLocationRelativeTo(null);
	}

	private JPanel createVerticalBlock(String labelText, JComponent component) {
		JPanel block = new JPanel(new BorderLayout(0, 5));
		block.add(new JLabel(labelText), BorderLayout.NORTH);
		block.add(component, BorderLayout.CENTER);
		return block;
	}

	private void setupListeners() {
		exitButton.addActionListener(e -> frame.dispose());
		changeButton.addActionListener(e -> swapCurrencies());
		convertButton.addActionListener(e -> performConversion());

		amountField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char character = e.getKeyChar();
				if (!Character.isDigit(character) && character != KeyEvent.VK_BACK_SPACE && character != '.') {
					e.consume();
				}
			}
		});

		themeMenuItem.addActionListener(e -> {
			ThemeManager.toggleTheme(frame);

			if (ThemeManager.isDarkMode()) {
				themeMenuItem.setText("Light mode");
			} else {
				themeMenuItem.setText("Dark mode");
			}
		});
	}

	private void performConversion() {
		String inputText = amountField.getText().trim();

		if (inputText.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Please enter an amount to convert.", "Input Error",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			double amount = Double.parseDouble(inputText);

			String sourceSelection = (String) sourceCurrencyBox.getSelectedItem();
			String targetSelection = (String) targetCurrencyBox.getSelectedItem();

			String sourceCode = sourceSelection.split(" - ")[0].trim();
			String targetCode = targetSelection.split(" - ")[0].trim();

			double rawResult = conversionService.convert(sourceCode, targetCode, amount);

			double roundedResult = NumberFormatter.round(rawResult);
			resultField.setText(NumberFormatter.formatForDisplay(roundedResult));
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(frame, "Invalid number format.", "Format Error", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(frame, "Error calculating conversion: " + ex.getMessage(), "System Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void swapCurrencies() {
		int sourceIndex = sourceCurrencyBox.getSelectedIndex();
		int targetIndex = targetCurrencyBox.getSelectedIndex();
		sourceCurrencyBox.setSelectedIndex(targetIndex);
		targetCurrencyBox.setSelectedIndex(sourceIndex);
	}
}