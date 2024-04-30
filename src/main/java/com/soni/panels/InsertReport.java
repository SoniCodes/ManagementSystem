package com.soni.panels;

import com.soni.Main;
import com.soni.util.MongoDBConnection;
import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.UUID;

public class InsertReport extends CustomJPanel
{
    private JTextField txtClient;
    private JTextField txtFirstname;
    private JTextField textField_1;
    private JTextField txtLastname;
    private JScrollPane scrollPane;
    private JTextArea textArea;

    public InsertReport(Main app) {
        super(app, "InsertReport", true, true);
    }

    @Override
    public void init()
    {
        setLayout(new MigLayout("", "[grow][grow 600,fill]", "[grow 50][grow,fill][grow 400,fill][grow,fill]"));

        txtClient = new JTextField();
        txtClient.setHorizontalAlignment(SwingConstants.CENTER);
        txtClient.setText("Report");
        txtClient.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        txtClient.setEditable(false);
        add(txtClient, "cell 0 0 2 1,grow");
        txtClient.setColumns(10);

        txtFirstname = new JTextField();
        txtFirstname.setColumns(8);
        txtFirstname.setText("Title");
        txtFirstname.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        txtFirstname.setEditable(false);
        add(txtFirstname, "cell 0 1,growx");

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField_1, "cell 1 1,growx");
        textField_1.setColumns(10);

        txtLastname = new JTextField();
        txtLastname.setColumns(8);
        txtLastname.setText("Content");
        txtLastname.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        txtLastname.setEditable(false);
        add(txtLastname, "cell 0 2,growx");

        scrollPane = new JScrollPane();
        add(scrollPane, "cell 1 2,grow");

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane.setViewportView(textArea);

        JButton btnNewButton = new JButton("Create Report");
        btnNewButton.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        add(btnNewButton, "cell 0 3 2 1,grow");
        btnNewButton.addActionListener(e -> {
            if(textField_1.getText().isEmpty() || textArea.getText().isEmpty())
            {
                JOptionPane.showMessageDialog(null, "Please fill out all fields!");
            }
            else
            {
                Document doc = new Document();
                doc.append("Title", textField_1.getText());
                doc.append("Report", textArea.getText());
                doc.append("Date", new Date());
                doc.append("Client-UUID", Clients.getCinstance().getCurrClient().toString());
                MongoDBConnection.getInstance().insertReportDocument(UUID.randomUUID(), doc);
                Main.removeTab("Clients");
                Clients.getCinstance().instance.createPanelOrSwitch("Clients");
                Main.removeTab("InsertReport");
            }
        });
    }
}
