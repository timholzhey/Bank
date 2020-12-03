package com.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.*;

class CreateAccountPage extends Page {
    // Made final to avoid accidental override errors
    private final JTextField nameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField repeatPasswordField = new JPasswordField(20);
    private final JTextField firstNameField = new JTextField(20);
    private final JTextField lastNameField = new JTextField(20);
    private final JTextField emailAddressField = new JTextField(20);
    private final JTextField telNumberField = new JTextField(20);
    private final JTextField streetAndHouseNumField = new JTextField(20);
    private final JTextField zipCodeAndCityField = new JTextField(20);
    
    private JButton createAccountButton = new JButton("Create account");
    private JButton loginButton = new JButton("Login");

    CreateAccountPage(Session session) {
        super(session);

        // Using a 10x2 grid layout with a 5px vertical gap
        this.setLayout(new GridLayout(10, 2, 0, 5));
        
        // :INPUT FIELDS

        add(new JLabel("Account Name:"));
        add(nameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(new JLabel("Repeat Password:"));
        add(repeatPasswordField);
        add(new JLabel("First Name"));
        add(firstNameField);
        add(new JLabel("Last Name:"));
        add(lastNameField);
        add(new JLabel("Email Address:"));
        add(emailAddressField);
        add(new JLabel("Telephone Number:"));
        add(telNumberField);
        add(new JLabel("Street and House Number:"));
        add(streetAndHouseNumField);
        add(new JLabel("Postal Code and City:"));
        add(zipCodeAndCityField);
        
        // :BUTTONS

        final JPanel buttonPanel = new JPanel();
        this.add(buttonPanel);

        // A simple FlowLayout for the buttons (with no gaps)
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        createAccountButton.addActionListener(e -> onCreateAccount());
        loginButton.addActionListener(e -> onLogin());

        buttonPanel.add(createAccountButton);
        buttonPanel.add(loginButton);
    }

    public JButton getDefaultButton() {
        return createAccountButton;
    }

    private void onCreateAccount() {
        // Attempt to log in
        final ArrayList<String> inputErrors = session.createAccount(
            nameField.getText(),
            passwordField.getPassword(),
            repeatPasswordField.getPassword(),
            firstNameField.getText(),
            lastNameField.getText(),
            emailAddressField.getText(),
            telNumberField.getText(),
            streetAndHouseNumField.getText(),
            zipCodeAndCityField.getText()
        );
        // In any case, clear the password field
        passwordField.setText("");
        repeatPasswordField.setText("");
        // If the login was unsuccessful, show an error message
        if(inputErrors.size() != 0) {
            String errorMessage = "Requirements were not satisfied:";
            for(int i = 0; i < inputErrors.size(); i++) {
                errorMessage += "\n - " + inputErrors.get(i);
            }

            JOptionPane.showMessageDialog(
                this,
                errorMessage,
                "Incorrect Credentials",
                JOptionPane.ERROR_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Successfully created a new account.\n",
                "Incorrect Credentials",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onLogin() {
        session.loginPage();
    }

}
