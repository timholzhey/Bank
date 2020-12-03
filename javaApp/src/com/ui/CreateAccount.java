package com.ui;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

// TODO Modernize (away from a static implementation)
class CreateAccount {

    public static JPanel pnPanel0;
    public static JLabel lbMessage;

    public static JPanel panel() {
        JPanel panel = new JPanel();

        JEditorPane jEditorPane = new JEditorPane();
        jEditorPane.setEditable(false);

        try {
             URL url = new File("html/sign-up.html").toURI().toURL();
             jEditorPane.setPage(url);
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) { 
            jEditorPane.setContentType("text/html");
            jEditorPane.setText("<html>Page not found.</html>");
        }

        JScrollPane jScrollPane = new JScrollPane(jEditorPane);
        jScrollPane.setPreferredSize(new Dimension(1440, 810));      

        panel.add(jScrollPane);

        return panel;
    }

}
