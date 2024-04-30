package com.soni;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.soni.panels.*;
import com.soni.panels.task.CreateTask;
import com.soni.panels.task.TaskShower;
import com.soni.util.ClientSortBy;
import com.soni.util.MongoDBConnection;
import com.soni.util.ReportSortBy;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.HashMap;

public class Main extends JFrame
{
    @Getter
    private static Main instance;
    @Getter
    private final Settings settings;
    private JTabbedPane tabbedPane;
    private HashMap<String, JPanel> panels = new HashMap<>();

    public Main()
    {
        instance = this;
        settings = new Settings();
        new TaskHolder();

        MongoDBConnection.startConnection();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1920, 1080);

        {
            JMenuBar menuBar = new JMenuBar();
            setJMenuBar(menuBar);

            JMenu mnSettings = new JMenu("Settings");
            menuBar.add(mnSettings);

            //Client Sort By menu
            JMenu mnClientSortBy = new JMenu("ClientSortBy");
            mnSettings.add(mnClientSortBy);

            ButtonGroup clientSortGroup = new ButtonGroup();
            JRadioButtonMenuItem rbClientAge = new JRadioButtonMenuItem("Age");
            JRadioButtonMenuItem rbClientName = new JRadioButtonMenuItem("Name");
            JRadioButtonMenuItem rbClientNone = new JRadioButtonMenuItem("None");

            clientSortGroup.add(rbClientAge);
            clientSortGroup.add(rbClientName);
            clientSortGroup.add(rbClientNone);

            mnClientSortBy.add(rbClientAge);
            mnClientSortBy.add(rbClientName);
            mnClientSortBy.add(rbClientNone);

            //Report Sort By menu
            JMenu mnReportSortBy = new JMenu("ReportSortBy");
            mnSettings.add(mnReportSortBy);

            ButtonGroup reportSortGroup = new ButtonGroup();
            JRadioButtonMenuItem rbReportDate = new JRadioButtonMenuItem("Date");
            JRadioButtonMenuItem rbReportDateReversed = new JRadioButtonMenuItem("Date Reversed");
            JRadioButtonMenuItem rbReportNone = new JRadioButtonMenuItem("None");

            reportSortGroup.add(rbReportDate);
            reportSortGroup.add(rbReportDateReversed);
            reportSortGroup.add(rbReportNone);

            mnReportSortBy.add(rbReportDate);
            mnReportSortBy.add(rbReportDateReversed);
            mnReportSortBy.add(rbReportNone);

            switch(Settings.getInstance().getClientSortBy())
            {
                case AGE -> rbClientAge.setSelected(true);
                case NAME -> rbClientName.setSelected(true);
                case NONE -> rbClientNone.setSelected(true);
            }
            switch(Settings.getInstance().getReportSortBy())
            {
                case NEWESTFIRST -> rbReportDate.setSelected(true);
                case OLDESTFIRST -> rbReportDateReversed.setSelected(true);
                case NONE -> rbReportNone.setSelected(true);
            }


            rbClientAge.addActionListener(e ->
            {
                Settings.getInstance().setClientSortBy(ClientSortBy.AGE);
                updateClients();
            });
            rbClientName.addActionListener(e ->
            {
                Settings.getInstance().setClientSortBy(ClientSortBy.NAME);
                updateClients();
            });
            rbClientNone.addActionListener(e ->
            {
                Settings.getInstance().setClientSortBy(ClientSortBy.NONE);
                updateClients();
            });

            rbReportDate.addActionListener(e -> {
                Settings.getInstance().setReportSortBy(ReportSortBy.NEWESTFIRST);
                updateReports();
            });
            rbReportDateReversed.addActionListener(e -> {
                Settings.getInstance().setReportSortBy(ReportSortBy.OLDESTFIRST);
                updateReports();
            });
            rbReportNone.addActionListener(e -> {
                Settings.getInstance().setReportSortBy(ReportSortBy.NONE);
                updateReports();
            });
        }

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0,0,0,0));



        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        contentPane.add(tabbedPane, "cell 0 0,grow");
        setVisible(true);


        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                close();
            }
        });

        new MainPanel(this);
    }

    private void updateReports()
    {
        for(JPanel panel : panels.values())
        {
            if(panel instanceof Clients clients)
            {
                clients.loadReports();
            }
        }
    }


    private void updateClients()
    {
        for(JPanel panel : panels.values())
        {
            if(panel instanceof Clients clients)
            {
                clients.loadClients();
            }
        }
    }

    public static void addTab(String title, JPanel panel) {
        getInstance().panels.put(title, panel);
        getInstance().tabbedPane.addTab(title, panel);
    }

    public static void removeTab(int index) {
        getInstance().panels.remove(getInstance().tabbedPane.getTitleAt(index));
        getInstance().tabbedPane.removeTabAt(index);
    }

    public static void removeTab(String title) {
        getInstance().panels.remove(title);
        getInstance().tabbedPane.removeTabAt(getInstance().tabbedPane.indexOfTab(title));
    }

    public static void requestFocus(String title) {
        getInstance().tabbedPane.setSelectedIndex(getInstance().tabbedPane.indexOfTab(title));
    }

    public void createPanelOrSwitch(String title)
    {
        if(panels.containsKey(title))
        {
            System.out.println("Switching to Panel: " + title);
            tabbedPane.setSelectedIndex(tabbedPane.indexOfComponent(panels.get(title)));
        }
        else
        {
            System.out.println("Creating Panel: " + title);
            switch (title)
            {
                case "Clients" -> {
                    new Clients(this);
                }
                case "Tasks" -> {
                    new Tasks(this);
                }
                case "InsertReport" -> {
                    new InsertReport(this);
                }
                case "InsertClient" -> {
                    new InsertClient(this);
                }
                case "CreateTask" -> {
                    new CreateTask(this);
                }
                case "TaskShower" -> {
                    new TaskShower(this);
                }
            }
        }
        repaint();
        revalidate();

    }

    public void close()
    {
        System.out.println("Closing");
        Settings.getInstance().saveSettings();
        MongoDBConnection.stopConnection();
        System.exit(0);
    }
    public static void main(String[] args) throws UnsupportedLookAndFeelException
    {
        UIManager.setLookAndFeel(new FlatDarculaLaf());
        new Main();
    }
}