package com.soni.panels;

import com.soni.Main;
import com.soni.util.MongoDBConnection;
import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class InsertClient extends CustomJPanel
{
    private JTextField txtClient;
    private JTextField txtFirstname;
    private JTextField textField_1;
    private JTextField txtLastname;
    private JTextField textField_3;
    private JTextField txtEmail;
    private JTextField textField_5;
    private JTextField txtGender;
    private JTextField textField_7;
    private JTextField txtAge;


    public InsertClient(Main app) {
        super(app, "InsertClient", true, true);
    }

    @Override
    public void init()
    {
        setLayout(new MigLayout("", "[grow][grow 1250,fill]", "[grow 50][grow,fill][grow,fill][grow,fill][grow,fill][grow,fill][grow 50]"));

        txtClient = new JTextField();
        txtClient.setHorizontalAlignment(SwingConstants.CENTER);
        txtClient.setText("New Client");
        txtClient.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        txtClient.setEditable(false);
        add(txtClient, "cell 0 0 2 1,grow");
        txtClient.setColumns(10);

        txtFirstname = new JTextField();
        txtFirstname.setText("First-Name");
        txtFirstname.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        txtFirstname.setEditable(false);
        add(txtFirstname, "cell 0 1,growx");
        txtFirstname.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField_1, "cell 1 1,growx");
        textField_1.setColumns(10);

        txtLastname = new JTextField();
        txtLastname.setText("Last-Name");
        txtLastname.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        txtLastname.setEditable(false);
        add(txtLastname, "cell 0 2,growx");
        txtLastname.setColumns(10);

        textField_3 = new JTextField();
        textField_3.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField_3, "cell 1 2,growx");
        textField_3.setColumns(10);

        txtEmail = new JTextField();
        txtEmail.setText("E-Mail");
        txtEmail.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        txtEmail.setEditable(false);
        add(txtEmail, "cell 0 3,growx");
        txtEmail.setColumns(10);

        textField_5 = new JTextField();
        textField_5.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField_5, "cell 1 3,growx");
        textField_5.setColumns(10);

        txtGender = new JTextField();
        txtGender.setText("Gender");
        txtGender.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        txtGender.setEditable(false);
        add(txtGender, "cell 0 4,growx");
        txtGender.setColumns(10);

        textField_7 = new JTextField();
        textField_7.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField_7, "cell 1 4,growx");
        textField_7.setColumns(10);

        txtAge = new JTextField();
        txtAge.setText("Age");
        txtAge.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        txtAge.setEditable(false);
        add(txtAge, "cell 0 5,growx");
        txtAge.setColumns(10);

        JComboBox comboBox = new JComboBox();
        for(int i = 0; i < 100; i++) {
            comboBox.addItem(i);
        }
        add(comboBox, "cell 1 5,growx");

        JButton btnCreateClient = new JButton("Create new Client");
        btnCreateClient.setFont(new Font("Bahnschrift", Font.PLAIN, 30));
        add(btnCreateClient, "cell 0 6 2 1,grow");

        btnCreateClient.addActionListener(e ->
        {
            String firstName = textField_1.getText();
            String lastName = textField_3.getText();
            String email = textField_5.getText();
            String gender = textField_7.getText();
            int age = (int) comboBox.getSelectedItem();

            if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || gender.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill out all fields", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Document client = new Document();
            client.append("First-Name", firstName);
            client.append("Last-Name", lastName);
            client.append("Email", email);
            client.append("Gender", gender);
            client.append("Age", age);

            MongoDBConnection.getInstance().insertClientDocument(UUID.randomUUID(), client);
            JOptionPane.showMessageDialog(null, "Client created successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

            textField_1.setText("");
            textField_3.setText("");
            textField_5.setText("");
            textField_7.setText("");

            Main.removeTab("Clients");
            instance.createPanelOrSwitch("Clients");
        });
    }
}
