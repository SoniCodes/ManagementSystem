package com.soni.panels.data;

import com.soni.panels.Clients;
import com.soni.util.MongoDBConnection;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.UUID;

public class ReportPanel extends JPanel
{
    @Getter
    private Document reportDoc;
    private JButton btnRemove;
    private JButton btnSeeContent;
    private JTextField textField;
    @Getter
    private String title;
    @Getter
    private String report;
    @Getter
    private Date date;
    @Getter
    private UUID client;
    @Getter
    private UUID uuid;
    public ReportPanel(Document reportDoc)
    {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        this.reportDoc = reportDoc;

        this.title = reportDoc.getString("Title");
        this.report = reportDoc.getString("Report");
        this.date = reportDoc.getDate("Date");
        this.client = UUID.fromString(reportDoc.getString("Client-UUID"));

        this.uuid = UUID.fromString(reportDoc.getString("UUID"));

        setLayout(new MigLayout("", "[grow 140,fill][grow 100,fill][grow 100,fill]", "[grow,fill]"));

        btnRemove = new JButton("Remove");
        btnRemove.addActionListener(e -> {
            MongoDBConnection.getInstance().removeReport(this.uuid);
            Clients.getCinstance().reloadReports();
        });

        btnSeeContent = new JButton("See Content");
        btnSeeContent.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, this.report, this.title, JOptionPane.INFORMATION_MESSAGE);
        });

        textField = new JTextField(title + " | " + date.toString());
        textField.setHorizontalAlignment(SwingConstants.LEADING);
        textField.setEditable(false);
        textField.setFont(new Font("Bahnschrift", Font.PLAIN, 25));
        textField.setColumns(10);
        add(textField, "cell 0 0,grow");
        add(btnSeeContent, "cell 1 0,grow");
        add(btnRemove, "cell 2 0,grow");

    }
}
