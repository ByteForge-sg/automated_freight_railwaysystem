package ui;

import service.ReportService;
import service.ReportService.ReportData;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

public class ReportPanel extends JPanel {

    private ReportService service;

    private JTable reportTable;

    private DefaultTableModel tableModel;

    private JLabel reportStatusLabel;

    private JComboBox<String> reportSelector;

    public ReportPanel() {

        service =
                new ReportService();

        setLayout(
                new BorderLayout(
                        15,
                        15
                )
        );

        setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        25,
                        20,
                        25
                )
        );

        createTopSection();

        createReportTable();

        createBottomSection();

        loadDefaultReport();
    }

    // =========================================================
    // TOP SECTION
    // =========================================================

    private void createTopSection() {

        JPanel topSection =
                new JPanel();

        topSection.setLayout(
                new BoxLayout(
                        topSection,
                        BoxLayout.Y_AXIS
                )
        );

        topSection.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        JLabel title =
                new JLabel(
                        "Railway Operations Reports"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Database reports using joins, views, aggregates and grouped queries"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        subtitle.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        topSection.add(title);
        topSection.add(subtitle);

        topSection.add(
                Box.createVerticalStrut(12)
        );

        JPanel controls =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT,
                                10,
                                8
                        )
                );

        controls.setBackground(
                Color.WHITE
        );

        controls.setBorder(
                BorderFactory.createTitledBorder(
                        "Report Selection"
                )
        );

        controls.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel reportLabel =
                new JLabel(
                        "Select Report:"
                );

        reportSelector =
                new JComboBox<>(
                        new String[]{
                                "Train Overview",
                                "Hazardous Cargo",
                                "Train Composition",
                                "Maintenance Records",
                                "Track Utilization",
                                "Cargo Summary",
                                "Train Status Summary"
                        }
                );

        reportSelector.setPreferredSize(
                new Dimension(
                        230,
                        32
                )
        );

        JButton generateButton =
                new JButton(
                        "Generate Report"
                );

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        generateButton.addActionListener(
                e -> generateSelectedReport()
        );

        refreshButton.addActionListener(
                e -> generateSelectedReport()
        );

        controls.add(
                reportLabel
        );

        controls.add(
                reportSelector
        );

        controls.add(
                generateButton
        );

        controls.add(
                refreshButton
        );

        topSection.add(
                controls
        );

        add(
                topSection,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // REPORT TABLE
    // =========================================================

    private void createReportTable() {

        tableModel =
                new DefaultTableModel();

        reportTable =
                new JTable(
                        tableModel
                );

        reportTable.setRowHeight(
                28
        );

        reportTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        reportTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        reportTable
                );

        JPanel tablePanel =
                new JPanel(
                        new BorderLayout()
                );

        tablePanel.setBackground(
                Color.WHITE
        );

        tablePanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Report Results"
                )
        );

        tablePanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                tablePanel,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // BOTTOM SECTION
    // =========================================================

    private void createBottomSection() {

        JPanel bottom =
                new JPanel(
                        new BorderLayout()
                );

        bottom.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        reportStatusLabel =
                new JLabel(
                        "Ready to generate report."
                );

        reportStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        bottom.add(
                reportStatusLabel,
                BorderLayout.WEST
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // DEFAULT REPORT
    // =========================================================

    private void loadDefaultReport() {

        reportSelector.setSelectedIndex(
                0
        );

        generateSelectedReport();
    }

    // =========================================================
    // GENERATE SELECTED REPORT
    // =========================================================

    private void generateSelectedReport() {

        String selected =
                (String)
                reportSelector.getSelectedItem();

        if (selected == null) {
            return;
        }

        try {

            ReportData data;

            switch (selected) {

                case "Train Overview":

                    data =
                            service.getTrainOverview();

                    break;

                case "Hazardous Cargo":

                    data =
                            service.getHazardousCargo();

                    break;

                case "Train Composition":

                    data =
                            service.getTrainComposition();

                    break;

                case "Maintenance Records":

                    data =
                            service.getMaintenanceReport();

                    break;

                case "Track Utilization":

                    data =
                            service.getTrackUtilization();

                    break;

                case "Cargo Summary":

                    data =
                            service.getCargoSummary();

                    break;

                case "Train Status Summary":

                    data =
                            service.getTrainStatusSummary();

                    break;

                default:

                    return;
            }

            displayReport(
                    data
            );

        } catch (SQLException e) {

            showError(
                    "Unable to generate report.",
                    e
            );
        }
    }

    // =========================================================
    // DISPLAY REPORT
    // =========================================================

    private void displayReport(
            ReportData data) {

        tableModel.setDataVector(
                data.getRows().toArray(
                        new Object[0][]
                ),
                data.getColumns()
        );

        reportStatusLabel.setText(
                "Report generated successfully — "
                + data.getRows().size()
                + " record(s)."
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message,
            SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                message
                + "\n\n"
                + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );

        e.printStackTrace();
    }
}