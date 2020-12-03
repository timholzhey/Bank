package com.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

class LoginPage extends Page {
    // Made final to avoid accidental override errors
    private final JTextField nameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private JButton loginButton = new JButton("Login");

    LoginPage(Session session) {
        super(session);

        // Using a 3x2 grid layout with a 5px vertical gap
        this.setLayout(new GridLayout(3, 2, 0, 5));
        
        // :INPUT FIELDS

        add(new JLabel("Account Name:"));
        add(nameField);
        add(new JLabel("Password:"));
        add(passwordField);

        // :LINKS

        final JPanel linkPanel = new JPanel();
        this.add(linkPanel);

        // A simple FlowLayout for the link panel (with no horizontal gaps)
        linkPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

        // Create the 'Sign Up' link
        final Link createAccountLink = new Link("Sign Up");
        createAccountLink.setActionListener(e -> onCreateAccount());

        linkPanel.add(new JLabel("Don't have an account? "));
        linkPanel.add(createAccountLink);
        
        // :BUTTONS

        final JPanel buttonPanel = new JPanel();
        this.add(buttonPanel);

        // A simple FlowLayout for the buttons (with no gaps)
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        
        loginButton.addActionListener(e -> onLogin());

        buttonPanel.add(loginButton);
    }

    public JButton getDefaultButton() {
        return loginButton;
    }

    private void onLogin() {
        // Attempt to log in
        final boolean loginSuccess = session.login(nameField.getText(), passwordField.getPassword());
        // In any case, clear the password field
        passwordField.setText("");
        // If the login was unsuccessful, show an error message
        if (!loginSuccess) {
            JOptionPane.showMessageDialog(
                this,
                "The login name or password you entered are incorrect.",
                "Incorrect Credentials",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void onCreateAccount() {
        session.createAccountPage();
    }

}
