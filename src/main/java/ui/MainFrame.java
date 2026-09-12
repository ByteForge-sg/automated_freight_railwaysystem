package ui;

import service.DashboardService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    public MainFrame() {

        setTitle(
                "Automated Freight Railway & Marshalling Yard Simulator"
        );

        setSize(
                1200,
                700
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout()
        );

        createSidebar();

        contentPanel =
                new JPanel(
                        new BorderLayout()
                );

        contentPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        add(
                contentPanel,
                BorderLayout.CENTER
        );

        showDashboard();
    }

    // =========================================================
    // SIDEBAR
    // =========================================================

    private void createSidebar() {

        JPanel sidebar =
                new JPanel();

        sidebar.setPreferredSize(
                new Dimension(
                        220,
                        700
                )
        );

        sidebar.setBackground(
                new Color(
                        30,
                        41,
                        59
                )
        );

        sidebar.setLayout(
                new BoxLayout(
                        sidebar,
                        BoxLayout.Y_AXIS
                )
        );

        JLabel title =
                new JLabel(
                        "<html><center>"
                        + "AUTOMATED<br>"
                        + "FREIGHT RAILWAY"
                        + "</center></html>"
                );

        title.setForeground(
                Color.WHITE
        );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        title.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        10,
                        25,
                        10
                )
        );

        sidebar.add(title);

        addMenuButton(
                sidebar,
                "Dashboard",
                this::showDashboard
        );

        addMenuButton(
                sidebar,
                "Trains",
                this::showTrainManagement
        );

        addMenuButton(
                sidebar,
                "Stations",
                this::showStationManagement
        );

        addMenuButton(
                sidebar,
                "Tracks",
                this::showTrackManagement
        );

        addMenuButton(
                sidebar,
                "Cargo",
                this::showCargoManagement
        );

        addMenuButton(
                sidebar,
                "Route Optimizer",
                this::showRouteOptimizer
        );

        addMenuButton(
                sidebar,
                "Marshalling Yard",
                this::showMarshallingYard
        );

        addMenuButton(
                sidebar,
                "Transactions",
                this::showTransactions
        );

        addMenuButton(
                sidebar,
                "Reports",
                this::showReports
        );

        add(
                sidebar,
                BorderLayout.WEST
        );
    }

    // =========================================================
    // MENU BUTTON
    // =========================================================

    private void addMenuButton(
            JPanel sidebar,
            String text,
            Runnable action) {

        JButton button =
                new JButton(text);

        button.setMaximumSize(
                new Dimension(
                        190,
                        45
                )
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setFocusPainted(
                false
        );

        button.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        button.addActionListener(
                e -> action.run()
        );

        sidebar.add(button);

        sidebar.add(
                Box.createVerticalStrut(8)
        );
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void showDashboard() {

        contentPanel.removeAll();

        JPanel dashboard =
                new JPanel(
                        new BorderLayout()
                );

        dashboard.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        dashboard.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        JLabel heading =
                new JLabel(
                        "Railway Operations Dashboard"
                );

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        dashboard.add(
                heading,
                BorderLayout.NORTH
        );

        JPanel cards =
                new JPanel(
                        new GridLayout(
                                2,
                                3,
                                20,
                                20
                        )
                );

        cards.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        0,
                        30,
                        0
                )
        );

        cards.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        try {

            DashboardService service =
                    new DashboardService();

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getStationCount()
                            ),
                            "Stations"
                    )
            );

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getTrainCount()
                            ),
                            "Trains"
                    )
            );

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getTrackCount()
                            ),
                            "Track Segments"
                    )
            );

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getFreightCarCount()
                            ),
                            "Freight Cars"
                    )
            );

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getCargoCount()
                            ),
                            "Cargo Records"
                    )
            );

            cards.add(
                    createCard(
                            String.valueOf(
                                    service.getHazardousCargoCount()
                            ),
                            "Hazardous Cargo"
                    )
            );

        } catch (SQLException e) {

            cards.add(
                    createCard(
                            "ERR",
                            "Database Error"
                    )
            );

            e.printStackTrace();
        }

        dashboard.add(
                cards,
                BorderLayout.CENTER
        );

        JLabel footer =
                new JLabel(
                        "Database: freight_railway_db  |  Status: Connected"
                );

        footer.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        dashboard.add(
                footer,
                BorderLayout.SOUTH
        );

        contentPanel.add(
                dashboard,
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // CARD
    // =========================================================

    private JPanel createCard(
            String number,
            String label) {

        JPanel card =
                new JPanel(
                        new BorderLayout()
                );

        card.setBackground(
                Color.WHITE
        );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(
                                        220,
                                        225,
                                        230
                                )
                        ),
                        BorderFactory.createEmptyBorder(
                                20,
                                20,
                                20,
                                20
                        )
                )
        );

        JLabel numberLabel =
                new JLabel(
                        number,
                        SwingConstants.CENTER
                );

        numberLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        36
                )
        );

        JLabel textLabel =
                new JLabel(
                        label,
                        SwingConstants.CENTER
                );

        textLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        card.add(
                numberLabel,
                BorderLayout.CENTER
        );

        card.add(
                textLabel,
                BorderLayout.SOUTH
        );

        return card;
    }

    // =========================================================
    // TRAINS
    // =========================================================

    private void showTrainManagement() {

        contentPanel.removeAll();

        contentPanel.add(
                new TrainPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // STATIONS
    // =========================================================

    private void showStationManagement() {

        contentPanel.removeAll();

        contentPanel.add(
                new StationPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // TRACKS
    // =========================================================

    private void showTrackManagement() {

        contentPanel.removeAll();

        contentPanel.add(
                new TrackPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // CARGO
    // =========================================================

    private void showCargoManagement() {

        contentPanel.removeAll();

        contentPanel.add(
                new CargoPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // ROUTE OPTIMIZER
    // =========================================================

    private void showRouteOptimizer() {

        contentPanel.removeAll();

        contentPanel.add(
                new RoutePanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // MARSHALLING YARD
    // =========================================================

    private void showMarshallingYard() {

        contentPanel.removeAll();

        contentPanel.add(
                new MarshallingYardPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // TRANSACTIONS
    // =========================================================

    private void showTransactions() {

        contentPanel.removeAll();

        contentPanel.add(
                new TransactionPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // REPORTS
    // =========================================================

    private void showReports() {

        contentPanel.removeAll();

        contentPanel.add(
                new ReportPanel(),
                BorderLayout.CENTER
        );

        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // =========================================================
    // MAIN
    // =========================================================

    public static void main(
            String[] args) {

        SwingUtilities.invokeLater(
                () -> {

                    MainFrame frame =
                            new MainFrame();

                    frame.setVisible(true);
                }
        );
    }
}