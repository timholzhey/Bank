package com.ui;

import java.awt.BorderLayout;
import javax.swing.*;

/**
 * The main application frame.
 * Currently only supports a single session,
 * but should eventually be able to support multiple sessions via tabs.
 */
public class MainFrame extends JFrame {
    private Session session = new Session();

    public MainFrame() {
        this.setLayout(new BorderLayout());

        // Add the session page container (wrapped in a scroll pane)
        this.add(new JScrollPane(session.pageContainer), BorderLayout.CENTER);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Set the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (Exception e) {
            System.err.println("Could not set look and feel:");
            e.printStackTrace();
        }
    }

}
