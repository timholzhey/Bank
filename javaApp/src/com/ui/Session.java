package com.ui;

import java.util.ArrayList;
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
        VIEW_ACCOUNT,
        CREATE_ACCOUNT
    }

    private State currentState;
    private Page currentPage;

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
    public boolean login(final String loginName, final char[] password) {
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

    public boolean logout() {
        // Throw an error if this session isn't logged in
        if (!this.isLoggedIn()) {
            throw new IllegalStateException("Session is not logged into an account.");
        }

        // TODO: Maybe autosave changes before logging out?
        // perform logout
        try {
            User.logout();
        }
        catch (final Exception e) {
            e.printStackTrace();
            // Logout failed
            return false;
        }

        // Change the state back to LOGIN if successful
        changeState(State.LOGIN);
        setDefaultButton();

        return true;
    }

    public void createAccountPage() {
        changeState(State.CREATE_ACCOUNT);
        setDefaultButton();
    }

    public void loginPage() {
        changeState(State.LOGIN);
        setDefaultButton();
    }

    public ArrayList<String> createAccount(
        String username,
        char[] password,
        char[] repeatPassword,
        String firstName,
        String lastName,
        String emailAddress,
        String telNumber,
        String streetAndHouseNum,
        String zipCodeAndCity
    ) {
        // attempt creating a new account
        final ArrayList<String> inputErrors = User.createAccount(username, password, repeatPassword, firstName, lastName, emailAddress, telNumber, streetAndHouseNum, zipCodeAndCity);

        if(inputErrors.size() == 0) {
            changeState(State.LOGIN);
        } else {
            System.out.println("Input values could not be validated.");
        }

        return inputErrors;
    }

    // DANGER: This method does not check for the legality of changes!
    private void changeState(final State newState) {
        // Remove the current page from the container
        this.pageContainer.removeAll();

        // Set the new page
        switch (newState) {
            case LOGIN:
                currentPage = new LoginPage(this);
                break;
            case VIEW_ACCOUNT:
                currentPage = new AccountPage(this);
                break;
            case CREATE_ACCOUNT:
                currentPage = new CreateAccountPage(this);
                break;
            default:
                // Something's wrong, I can feel it.
                currentPage = null;
                throw new IllegalStateException("Unknown state: " + newState);
        }

        // Add the new page to the pageContainer
        pageContainer.add(currentPage);

        // Update pageContainer
        pageContainer.revalidate();
        pageContainer.repaint();

        // Set the new state
        this.currentState = newState;

        if(newState != State.LOGIN) {
            setDefaultButton();
        }
    }

    public void setDefaultButton() {
        // Set default button on default keypress 'ENTER'
        JButton defaultButton = currentPage.getDefaultButton();
        if(defaultButton != null) {
            pageContainer.getRootPane().setDefaultButton(currentPage.getDefaultButton());
        } else {
            // No default button defined
        }
    }

}
