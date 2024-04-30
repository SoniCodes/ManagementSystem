package com.soni.panels.data;

import com.soni.panels.Clients;
import com.soni.panels.MainPanel;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class ClientPanel extends JPanel
{
    @Getter
    private Document client;
    private JButton btnSelect;
    private JTextField textField;
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String gender;
    @Getter
    private String email;
    @Getter
    private int age;
    @Getter
    private UUID uuid;
    public ClientPanel(Document client, Clients instance)
    {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        this.client = client;
        this.firstName = client.getString("First-Name");
        this.lastName = client.getString("Last-Name");
        this.gender = client.getString("Gender");
        this.email = client.getString("Email");
        this.age = client.getInteger("Age");

        this.uuid = UUID.fromString(client.getString("UUID"));
        setLayout(new MigLayout("", "[grow,fill][32px:50px:50px,grow,fill]", "[grow,fill]"));

        btnSelect = new JButton(" ");
        btnSelect.addActionListener(e -> {
            select(instance);
        });
        String displayString = client.getString("First-Name") + " " + client.getString("Last-Name") + " - " + client.getInteger("Age");

        textField = new JTextField(displayString);
        textField.setHorizontalAlignment(SwingConstants.LEADING);
        textField.setEditable(false);
        textField.setFont(new Font("Bahnschrift", Font.PLAIN, 25));
        textField.setColumns(10);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if(instance.getLastFocused() != null) {
                    instance.getLastFocused().setFocusable(true);
                }
                select(instance);
                ((JTextField)evt.getSource()).setFocusable(false);
                instance.setLastFocused((JTextField)evt.getSource());
            }
        });
        add(textField, "cell 0 0,grow");
        add(btnSelect, "cell 1 0,grow");

    }

    private void select(Clients instance)
    {
        btnSelect.setBackground(Color.GREEN);
        instance.setCurrClient(this);
    }

    public void deselect()
    {
        btnSelect.setBackground(new JButton().getBackground());
    }
}
