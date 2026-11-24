package hu.szkristof.BFEditor.menu;

import javax.swing.*;

/** Main menu bar containing the File, Edit, and Run menus. */
public class MenuBar extends JMenuBar {

	/** Creates the application's main menu bar. */
    public MenuBar() {
        add(new FileMenu());
		add(new EditMenu());
		add(new RunMenu());
    }

}
