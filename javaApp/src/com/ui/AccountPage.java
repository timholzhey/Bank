package com.ui;

import javax.swing.*;

import com.User;

class AccountPage extends Page {
    
    AccountPage(final Session session) {
        super(session);

        // TODO Expand account view
        add(new JLabel("Welcome, " + User.getUsername() + "! Balance: " + User.getBalance()));
    }

}
