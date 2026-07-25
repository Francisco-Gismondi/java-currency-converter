package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	private JTextField amountField;
	private JTextField resultField;
	private JComboBox<String> sourceCurrencyBox;
	private JComboBox<String> targetCurrencyBox;

	private ConversionService conversionService;

	public MainWindow() {
		this.conversionService = new ConversionService();
		initComponents();
		setupListeners();
		this.frame.setVisible(true);
		this.frame.setResizable(false);
	}


	private void initComponents() {
		frame = new JFrame("Currency Converter");
		frame.setSize(600, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		mainPanel = new JPanel(new BorderLayout(20, 20));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

		formPanel = new JPanel(new GridLayout(5, 2, 15, 5));

		String[] currencyList = CurrencyLoader.obtenerMonedasFormateadas();

		sourceCurrencyBox = new JComboBox<>(currencyList);
		targetCurrencyBox = new JComboBox<>(currencyList);
		amountField = new JTextField();
		resultField = new JTextField();
		resultField.setEditable(false);

		convertButton = new JButton("Convert");
		exitButton = new JButton("Exit");

		JLabel lblSource = new JLabel("From:");
		JLabel lblTarget = new JLabel("To:");
		JLabel lblAmount = new JLabel("Amount:");
		JLabel lblResult = new JLabel("Result:");

		formPanel.add(lblSource);
		formPanel.add(lblTarget);
		formPanel.add(sourceCurrencyBox);
		formPanel.add(targetCurrencyBox);
		formPanel.add(lblAmount);
		formPanel.add(lblResult);
		formPanel.add(amountField);
		formPanel.add(resultField);
		formPanel.add(convertButton);
		formPanel.add(exitButton);

		mainPanel.add(formPanel, BorderLayout.CENTER);
		frame.add(mainPanel);
	}


	private void setupListeners() {

		exitButton.addActionListener(e -> frame.dispose());

		convertButton.addActionListener(e -> performConversion());

		amountField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != '.') {
					e.consume();
				}
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
}