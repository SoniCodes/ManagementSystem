package com.soni.panels;

import com.soni.Main;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends CustomJPanel{
    public MainPanel(Main app) {
        super(app, "MainPanel", false, true);
    }

    @Override
    public void init() {
        setLayout(new MigLayout("", "[grow]", "[grow 30][grow][grow 30]"));

        JTextField titleLabel = new JTextField("Medical Systems");
        titleLabel.setFont(new Font("Bahnschrift", Font.PLAIN, 60));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setEditable(false);
        titleLabel.setFocusable(false);
        add(titleLabel, "cell 0 0,grow");
        titleLabel.setColumns(10);

        JPanel panel = new JPanel();
        add(panel, "cell 0 1,grow");
        panel.setLayout(new GridLayout(1, 0, 0, 0));

        JButton btnClients = new JButton("Clients");
        btnClients.setFont(new Font("Bahnschrift", Font.PLAIN, 60));
        panel.add(btnClients);
        btnClients.addActionListener(e -> instance.createPanelOrSwitch("Clients"));

        JButton btnTasks = new JButton("Tasks");
        btnTasks.setFont(new Font("Bahnschrift", Font.PLAIN, 60));
        panel.add(btnTasks);
        btnTasks.addActionListener(e -> instance.createPanelOrSwitch("TaskShower"));

        JButton btnClose = new JButton("Close Application");
        btnClose.setFont(new Font("Bahnschrift", Font.PLAIN, 60));
        add(btnClose, "cell 0 2,grow");
        btnClose.addActionListener(e -> instance.close());
    }
}

