package hu.szkristof.BFEditor.menu;

import hu.szkristof.BFEditor.BFEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

public class FileMenu extends JMenu {

    public FileMenu(){
        super("File");

        JMenuItem newMenuItem = new JMenuItem("New");
        add(newMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("Save As");
        add(saveAsMenuItem);

        addSeparator();

        JMenuItem exitMenuItem = new JMenuItem(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BFEditor.getInstance().requestClose();
            }
        });
        add(exitMenuItem);
    }

}
