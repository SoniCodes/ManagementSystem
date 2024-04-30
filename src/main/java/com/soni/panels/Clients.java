package com.soni.panels;

import com.soni.Main;
import com.soni.Settings;
import com.soni.panels.data.ClientPanel;
import com.soni.panels.data.ReportPanel;
import com.soni.util.*;
import lombok.Getter;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import org.bson.Document;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clients extends CustomJPanel
{
    @Getter
    private static Clients cinstance;
    public Clients(Main app) {
        super(app, "Clients", true, true);
        cinstance = this;
    }
    private JTextField textField;
    private JTextField txtClients;
    private JTextField txtOptions;
    private JPanel clientJPanel, reportJPanel;
    private CustomHashTable<UUID, ClientPanel> clientPanels;
    private CustomHashTable<UUID, ReportPanel> reportPanels;

    @Getter
    private UUID currClient;
    @Getter
    @Setter
    private JTextField lastFocused;

    @Override
    public void init() {
        setLayout(new MigLayout("", "[grow][grow 200]", "[grow 120][grow 140][grow 800][grow 140]"));

        txtClients = new JTextField();
        txtClients.setHorizontalAlignment(SwingConstants.CENTER);
        txtClients.setEditable(false);
        txtClients.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        txtClients.setText("Clients");
        add(txtClients, "cell 0 0,grow");
        txtClients.setColumns(10);

        txtOptions = new JTextField();
        txtOptions.setHorizontalAlignment(SwingConstants.CENTER);
        txtOptions.setEditable(false);
        txtOptions.setFont(new Font("Bahnschrift", Font.PLAIN, 40));
        txtOptions.setText("Options");
        add(txtOptions, "cell 1 0,grow");
        txtOptions.setColumns(10);

        textField = new JTextField();
        textField.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(textField, "cell 0 1,grow");
        textField.setColumns(10);
        textField.getDocument().addDocumentListener(new DocumentListener(){
            @Override
            public void insertUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e)
            {
                changedUpdate(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e)
            {
                loadClients();
            }
        });

        JPanel reportSide = new JPanel();
        reportSide.setLayout(new MigLayout("insets 0", "[grow]", "[grow][grow 800][grow]"));
        add(reportSide, "cell 1 1 1 3,grow");

        JButton btnCreateTask = new JButton("Create Local Task");
        btnCreateTask.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        reportSide.add(btnCreateTask, "cell 0 0,grow");
        btnCreateTask.addActionListener(e -> {
            if(currClient != null)
            {
                instance.createPanelOrSwitch("CreateTask");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select a client first!");
            }
        });

        JScrollPane jScrollpane_2 = new JScrollPane();
        jScrollpane_2.getVerticalScrollBar().setUnitIncrement(30);
        reportSide.add(jScrollpane_2, "cell 0 1,grow");

        reportJPanel = new JPanel();
        jScrollpane_2.setViewportView(reportJPanel);

        JButton btnCreateReport = new JButton("Create new Report");
        btnCreateReport.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        reportSide.add(btnCreateReport, "cell 0 2,grow");
        btnCreateReport.addActionListener(e -> {
            if(currClient != null)
            {
                instance.createPanelOrSwitch("InsertReport");
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Please select a client first!");
            }
        });

        JScrollPane jScrollpane = new JScrollPane();
        jScrollpane.getVerticalScrollBar().setUnitIncrement(30);
        add(jScrollpane, "cell 0 2,grow");

        clientJPanel = new JPanel();
        jScrollpane.setViewportView(clientJPanel);

        JButton btnNewClient = new JButton("Add New Client");
        btnNewClient.setFont(new Font("Bahnschrift", Font.PLAIN, 20));
        add(btnNewClient, "cell 0 3,grow");
        btnNewClient.addActionListener(e -> instance.createPanelOrSwitch("InsertClient"));

        clientPanels = new CustomHashTable<>(520);
        reportPanels = new CustomHashTable<>(520);


        ArrayList<Document> clients = MongoDBConnection.getInstance().getClients();
        for(Document client : clients)
        {
            ClientPanel clientPanel = new ClientPanel(client, this);
            clientPanels.put(clientPanel.getUuid(), clientPanel);
        }

        ArrayList<Document> reports = MongoDBConnection.getInstance().getReports();
        for(Document report : reports)
        {
            ReportPanel reportPanel = new ReportPanel(report);
            reportPanels.put(reportPanel.getUuid(), reportPanel);
        }


        loadClients();
    }

    public void loadClients()
    {
        lastFocused = null;
        String currSearch = textField.getText();
        List<ClientPanel> clients = FilterUtils.filter(clientPanels.values(), (clientPanel -> {
            if((clientPanel.getFirstName() + " " + clientPanel.getLastName()).toLowerCase().contains(currSearch.toLowerCase()))
            {
                return true;
            }
            if(clientPanel.getUuid().toString().contains(currSearch))
            {
                return true;
            }
            return false;
        }));

        clients = Sorting.sortClients(clients, Settings.getInstance().getClientSortBy());

        clientJPanel.removeAll();
        clientJPanel.setLayout(new BoxLayout(clientJPanel, BoxLayout.Y_AXIS));
        for(ClientPanel client : clients)
        {
            clientJPanel.add(client);
        }
        repaint();
        revalidate();
    }



    public void setCurrClient(ClientPanel clientPanel)
    {
        if(clientPanels.hasKey(currClient))
        {
            clientPanels.get(currClient).deselect();
        }
        currClient = clientPanel.getUuid();
        loadReports();
    }

    public void loadReports()
    {
        List<ReportPanel> reports = FilterUtils.filter(reportPanels.values(), (reportPanel -> {
            if(currClient == null)
            {
                return false;
            }
            if(reportPanel.getClient().equals(currClient))
            {
                return true;
            }
            return false;
        }));
        reports = Sorting.sortReports(reports, Settings.getInstance().getReportSortBy());
        reportJPanel.removeAll();
        reportJPanel.setLayout(new BoxLayout(reportJPanel, BoxLayout.Y_AXIS));
        for(ReportPanel report : reports)
        {
            reportJPanel.add(report);
        }
        repaint();
        revalidate();
    }

    public void reloadReports(){
        reportPanels = new CustomHashTable<>(520);


        ArrayList<Document> reports = MongoDBConnection.getInstance().getReports();
        for(Document report : reports)
        {
            ReportPanel reportPanel = new ReportPanel(report);
            reportPanels.put(reportPanel.getUuid(), reportPanel);
        }
        loadReports();
    }
}
