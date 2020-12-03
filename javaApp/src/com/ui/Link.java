package com.ui;

import java.util.Map;
import java.util.HashMap;
import java.awt.font.TextAttribute;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;

/**
 * A UI component similar to a web link.
 */
class Link extends JLabel {
    // Only one action listener allowed
    private ActionListener actionListener;
    
    Link(final String text) {
        super(text);

        // Set the underline font
        Map<TextAttribute, Integer> fontAttributes = new HashMap<TextAttribute, Integer>();
        fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        this.setFont(this.getFont().deriveFont(fontAttributes));
        
        // Add the mouse listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Call the action listener (without an action command)
                actionListener.actionPerformed(new ActionEvent(Link.this, ActionEvent.ACTION_PERFORMED, null));
            }
        });

        // Set the cursor to hand
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void setActionListener(final ActionListener listener) {
        this.actionListener = listener;
    }

    public ActionListener getActionListener() {
        return this.actionListener;
    }

}
