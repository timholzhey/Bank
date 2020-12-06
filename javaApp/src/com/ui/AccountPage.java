package com.ui;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import com.User;

class AccountPage extends Page {

    private JButton logoutButton = new JButton("Logout");
    private JButton transferButton = new JButton("Transfer money");
    
    AccountPage(final Session session) {
        super(session);

        // Using a 5x1 grid layout with a 5px vertical gap
        this.setLayout(new GridLayout(5, 1, 0, 5));

        // TODO Expand account view
        add(new JLabel("Account: " + User.getUsername() + "!"));
        add(new JLabel("Balance: " + User.getBalance() + " €"));
        add(new JLabel("Name: " + User.getName()));
        add(new JLabel("EMail: " + User.getEmail()));

        // :BUTTONS

        final JPanel buttonPanel = new JPanel();
        this.add(buttonPanel);

        // A simple FlowLayout for the buttons (with no gaps)
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

        logoutButton.addActionListener(e -> onLogout());
        transferButton.addActionListener(e -> onTransfer());

        buttonPanel.add(logoutButton);
        buttonPanel.add(transferButton);
    }

    private void onLogout() {
        // Attempt logging out the session
        final boolean logoutSuccess = session.logout();
        if(logoutSuccess) {
            JOptionPane.showMessageDialog(
                this,
                "Logged out.",
                "Logged out",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public void onTransfer() {
        // Attempt money transfer
        // TODO: transfer
    }

    public JButton getDefaultButton() {
        return logoutButton;
    }

}
