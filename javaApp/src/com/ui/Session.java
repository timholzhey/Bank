package com.ui;

import java.util.Arrays;

import java.awt.FlowLayout;
import javax.swing.*;

import com.User;

/**
 * The central manager hosting a single user session (from login to logout).
 */
final class Session {
    // Using an internal state machine to manage the current session state
    private enum State {
        LOGIN,
        VIEW_ACCOUNT
    }

    private State currentState;

    /**
     * The page container. Accessible internally so it can be displayed by the object containing the session.
     */
    final JPanel pageContainer = new JPanel();

    Session() {
        // Configure the page container
        this.pageContainer.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Set the initial state and page
        changeState(State.LOGIN);
    }

    public boolean isLoggedIn() {
        return this.currentState != State.LOGIN;
    }

    /**
     * Tries to log into an account with the provided credentials.
     * 
     * @return <code>true</code> if the login was successful, <code>false</code> otherwise.
     */
    boolean login(final String loginName, final char[] password) {
        // Throw an error if this session is already logged in
        if (this.isLoggedIn()) {
            throw new IllegalStateException("Session already logged in to an account.");
        }

        // Actually perform the login and store the result
        int loginResult = -1;
        try {
            loginResult = User.login(loginName, new String(password));
        }
        catch (final Exception e) {
            // TODO Also show a message with the error // Use response codes
            e.printStackTrace();
            // Login failed
            return false;
        }

        // SECURITY: Clear the password after it has been used (no credential residues)
        Arrays.fill(password, (char) 0);

        // Advance the state from LOGIN to VIEW_ACCOUNT if successful
        if (loginResult != 0) {
            changeState(State.VIEW_ACCOUNT);
        }

        return this.isLoggedIn();
    }

    // DANGER: This method does not check for the legality of changes!
    private void changeState(final State newState) {
        // Remove the current page from the container
        this.pageContainer.removeAll();

        // Set the new page
        Page newPage;
        switch (newState) {
            case LOGIN:
                // TODO Log out! (if logged in)
                newPage = new LoginPage(this);
                break;
            case VIEW_ACCOUNT:
                newPage = new AccountPage(this);
                break;
            default:
                // Something's wrong, I can feel it.
                newPage = null;
                throw new IllegalStateException("Unknown state: " + newState);
        }

        // Add the new page to the pageContainer
        pageContainer.add(newPage);

        // Set the new state
        this.currentState = newState;
    }

}
