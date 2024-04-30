package com.soni.panels.task;

import com.soni.Main;
import com.soni.Task;
import com.soni.TaskHolder;
import com.soni.panels.Clients;
import com.soni.panels.CustomJPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class TaskShower extends CustomJPanel
{
    private JTextField txtClient;
    private JTextField txtFirstname;
    private JTextField textField_1;
    private JTextField txtLastname;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    public TaskShower(Main app) {
        super(app, "TaskShower", true, true);
    }

    @Override
    public void init()
    {
        setLayout(new MigLayout("", "[grow][grow 600,fill]", "[grow 50][grow,fill][grow 400,fill][grow,fill]"));

        txtClient = new JTextField();
        txtClient.setHorizontalAlignment(SwingConstants.CENTER);
        txtClient.setText("Task");
        txtClient.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        txtClient.setEditable(false);
        add(txtClient, "cell 0 0 2 1,grow");
        txtClient.setColumns(10);

        txtFirstname = new JTextField();
        txtFirstname.setColumns(8);
        txtFirstname.setText("Name");
        txtFirstname.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        txtFirstname.setEditable(false);
        add(txtFirstname, "cell 0 1,growx");

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        textField_1.setEditable(false);
        add(textField_1, "cell 1 1,growx");
        textField_1.setColumns(10);

        txtLastname = new JTextField();
        txtLastname.setColumns(8);
        txtLastname.setText("Description");
        txtLastname.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        txtLastname.setEditable(false);
        add(txtLastname, "cell 0 2,growx");

        scrollPane = new JScrollPane();
        add(scrollPane, "cell 1 2,grow");

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JButton btnNewButton = new JButton("Copy UUID of Client");
        btnNewButton.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        add(btnNewButton, "cell 0 3 1 1,grow");
        btnNewButton.addActionListener(e -> {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(TaskHolder.getInstance().peekTask().getClientId().toString()), null);
            JOptionPane.showMessageDialog(null, "Copied UUID of Client to Clipboard!");
        });

        JButton btnNewButton2 = new JButton("Next Task");
        btnNewButton2.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        add(btnNewButton2, "cell 1 3 1 1,grow");
        btnNewButton2.addActionListener(e -> {
            TaskHolder.getInstance().popTask();
            updateCurrTask();
        });

        updateCurrTask();
    }

    private void updateCurrTask()
    {
        Task task = TaskHolder.getInstance().peekTask();
        if(task == null)
        {
            JOptionPane.showMessageDialog(null, "No more tasks!");
            instance.createPanelOrSwitch("Clients");
        }
        else
        {
            textField_1.setText(task.getName());
            textArea.setText(task.getDescription());
        }
    }
}
