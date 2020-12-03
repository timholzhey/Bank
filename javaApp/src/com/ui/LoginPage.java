package com.ui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

class LoginPage extends Page {
    // Made final to avoid accidental override errors
    private final JTextField nameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);

    LoginPage(Session session) {
        super(session);

        // Using a 3x2 grid layout with a 5px vertical gap
        this.setLayout(new GridLayout(3, 2, 0, 5));
        
        // :INPUT FIELDS

        add(new JLabel("Login Name:"));
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

        final JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> onLogin());

        buttonPanel.add(loginButton);
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
        // Open the dummy CreateAccount component in a new modal dialog
        final JDialog caDialog = new JDialog((JFrame) SwingUtilities.getRoot(this), "Sign Up", true);
        // Wrap the CreateAccount panel in a ScrollPane
        caDialog.add(new JScrollPane(CreateAccount.panel()));
        
        // Closing the dialog means completely disposing it
        caDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        
        // Pack and make visible
        caDialog.pack();
        caDialog.setVisible(true);
    }

}
