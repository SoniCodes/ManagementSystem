package com.soni.panels;

import com.soni.Main;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public abstract class CustomJPanel extends JPanel
{
    public Main instance;
    private String name;
    public CustomJPanel(Main app, String name, boolean closeable, boolean init) {
        super();
        this.instance = app;
        this.name = name;
        Main.addTab(name, this);
        if(closeable)
        {
            KeyStroke ctrlWKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_W, KeyEvent.CTRL_DOWN_MASK);
            this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlWKeyStroke, "closeTab");
            this.getActionMap().put("closeTab", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    Main.removeTab(name);
                }
            });
        }
        Main.requestFocus(name);
        if(init)
        {
            init();
        }
    }
    public abstract void init();
}
