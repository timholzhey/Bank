package com.ui;

import javax.swing.JPanel;
import javax.swing.JButton;

/**
 * A single UI page.
 * The page is the central element of the UI/UX flow.
 * Each page contains all the components necessary to perform a specific function.
 */
abstract class Page extends JPanel {
    // The session is stored so sub-pages can access it
    protected Session session;

    public Page(final Session session) {
        this.session = session;
    }

	public JButton getDefaultButton() {
		return null;
	}

}
