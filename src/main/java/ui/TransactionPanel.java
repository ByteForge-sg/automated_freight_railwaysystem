package ui;

import service.TransactionService;
import service.TransactionService.LogInfo;
import service.TransactionService.TrackInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionPanel extends JPanel {

    private TransactionService service;

    private JTable trackTable;
    private JTable logTable;

    private DefaultTableModel trackModel;
    private DefaultTableModel logModel;

    private JLabel transactionStatusLabel;
    private JLabel transactionIdLabel;

    private JButton beginButton;
    private JButton commitButton;
    private JButton rollbackButton;

    public TransactionPanel() {

        service = new TransactionService();

        setLayout(
                new BorderLayout(15, 15)
        );

        setBackground(
                new Color(245, 247, 250)
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        createTopSection();
        createMainTables();
        createBottomSection();

        loadTracks();
        loadLogs();
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
                new Color(245, 247, 250)
        );

        // -----------------------------------------------------
        // TITLE
        // -----------------------------------------------------

        JLabel title =
                new JLabel(
                        "Transactions & Two-Phase Locking"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        27
                )
        );

        title.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        JLabel subtitle =
                new JLabel(
                        "Transaction-safe railway track allocation using InnoDB row locking"
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

        // -----------------------------------------------------
        // CONTROLS
        // -----------------------------------------------------

        JPanel controls =
                new JPanel(
                        new GridBagLayout()
                );

        controls.setBackground(
                Color.WHITE
        );

        controls.setBorder(
                BorderFactory.createTitledBorder(
                        "Transaction Controls"
                )
        );

        controls.setAlignmentX(
                Component.LEFT_ALIGNMENT
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        5,
                        7,
                        5,
                        7
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weighty = 0;

        // -----------------------------------------------------
        // BEGIN
        // -----------------------------------------------------

        beginButton =
                new JButton(
                        "Begin Transaction"
                );

        beginButton.setPreferredSize(
                new Dimension(
                        160,
                        32
                )
        );

        beginButton.addActionListener(
                e -> beginTransaction()
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        controls.add(
                beginButton,
                gbc
        );

        // -----------------------------------------------------
        // COMMIT
        // -----------------------------------------------------

        commitButton =
                new JButton(
                        "COMMIT"
                );

        commitButton.setPreferredSize(
                new Dimension(
                        100,
                        32
                )
        );

        commitButton.setEnabled(false);

        commitButton.addActionListener(
                e -> commitTransaction()
        );

        gbc.gridx = 1;

        controls.add(
                commitButton,
                gbc
        );

        // -----------------------------------------------------
        // ROLLBACK
        // -----------------------------------------------------

        rollbackButton =
                new JButton(
                        "ROLLBACK"
                );

        rollbackButton.setPreferredSize(
                new Dimension(
                        110,
                        32
                )
        );

        rollbackButton.setEnabled(false);

        rollbackButton.addActionListener(
                e -> rollbackTransaction()
        );

        gbc.gridx = 2;

        controls.add(
                rollbackButton,
                gbc
        );

        // -----------------------------------------------------
        // TRANSACTION ID
        // -----------------------------------------------------

        transactionIdLabel =
                new JLabel(
                        "Transaction: NONE"
                );

        transactionIdLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        controls.add(
                transactionIdLabel,
                gbc
        );

        // -----------------------------------------------------
        // STATUS
        // -----------------------------------------------------

        transactionStatusLabel =
                new JLabel(
                        "Status: No active transaction"
                );

        transactionStatusLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        gbc.gridx = 4;
        gbc.weightx = 1;

        controls.add(
                transactionStatusLabel,
                gbc
        );

        // -----------------------------------------------------
        // REFRESH TRACKS
        // -----------------------------------------------------

        JButton refreshTracksButton =
                new JButton(
                        "Refresh Tracks"
                );

        refreshTracksButton.setPreferredSize(
                new Dimension(
                        135,
                        32
                )
        );

        refreshTracksButton.addActionListener(
                e -> loadTracks()
        );

        gbc.gridx = 5;
        gbc.weightx = 0;

        controls.add(
                refreshTracksButton,
                gbc
        );

        // -----------------------------------------------------
        // REFRESH LOGS
        // -----------------------------------------------------

        JButton refreshLogsButton =
                new JButton(
                        "Refresh Logs"
                );

        refreshLogsButton.setPreferredSize(
                new Dimension(
                        120,
                        32
                )
        );

        refreshLogsButton.addActionListener(
                e -> loadLogs()
        );

        gbc.gridx = 6;

        controls.add(
                refreshLogsButton,
                gbc
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
    // MAIN TABLES
    // =========================================================

    private void createMainTables() {

        JPanel mainPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                2,
                                15,
                                0
                        )
                );

        mainPanel.setBackground(
                new Color(
                        245,
                        247,
                        250
                )
        );

        // =====================================================
        // TRACK TABLE
        // =====================================================

        trackModel =
                new DefaultTableModel(
                        new Object[]{
                                "Select",
                                "Segment ID",
                                "From",
                                "To",
                                "Length",
                                "Max Speed",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public Class<?> getColumnClass(
                            int column) {

                        if (column == 0) {
                            return Boolean.class;
                        }

                        return Object.class;
                    }

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return column == 0;
                    }
                };

        trackTable =
                new JTable(
                        trackModel
                );

        trackTable.setRowHeight(27);

        trackTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        // Set useful column widths

        trackTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        trackTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(80);

        trackTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(55);

        trackTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(55);

        trackTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(75);

        trackTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(80);

        trackTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(90);

        JScrollPane trackScroll =
                new JScrollPane(
                        trackTable
                );

        JPanel trackPanel =
                new JPanel(
                        new BorderLayout()
                );

        trackPanel.setBackground(
                Color.WHITE
        );

        trackPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Track Segments — Select Tracks to Lock"
                )
        );

        trackPanel.add(
                trackScroll,
                BorderLayout.CENTER
        );

        mainPanel.add(
                trackPanel
        );

        // =====================================================
        // LOG TABLE
        // =====================================================

        logModel =
                new DefaultTableModel(
                        new Object[]{
                                "Log ID",
                                "Transaction",
                                "Operation",
                                "Record",
                                "Old",
                                "New",
                                "Status"
                        },
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        logTable =
                new JTable(
                        logModel
                );

        logTable.setRowHeight(27);

        logTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                12
                        )
                );

        logTable.getColumnModel()
                .getColumn(0)
                .setPreferredWidth(55);

        logTable.getColumnModel()
                .getColumn(1)
                .setPreferredWidth(110);

        logTable.getColumnModel()
                .getColumn(2)
                .setPreferredWidth(80);

        logTable.getColumnModel()
                .getColumn(3)
                .setPreferredWidth(70);

        logTable.getColumnModel()
                .getColumn(4)
                .setPreferredWidth(90);

        logTable.getColumnModel()
                .getColumn(5)
                .setPreferredWidth(90);

        logTable.getColumnModel()
                .getColumn(6)
                .setPreferredWidth(90);

        JScrollPane logScroll =
                new JScrollPane(
                        logTable
                );

        JPanel logPanel =
                new JPanel(
                        new BorderLayout()
                );

        logPanel.setBackground(
                Color.WHITE
        );

        logPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Transaction Log"
                )
        );

        logPanel.add(
                logScroll,
                BorderLayout.CENTER
        );

        mainPanel.add(
                logPanel
        );

        add(
                mainPanel,
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

        bottom.setBorder(
                BorderFactory.createEmptyBorder(
                        5,
                        0,
                        0,
                        0
                )
        );

        JLabel explanation =
                new JLabel(
                        "<html>"
                        + "<b>2PL:</b> Locks are acquired before "
                        + "the transaction operation and remain held "
                        + "until COMMIT or ROLLBACK. "
                        + "Tracks are locked in ascending Segment_ID "
                        + "order to reduce deadlock risk."
                        + "</html>"
                );

        explanation.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        bottom.add(
                explanation,
                BorderLayout.CENTER
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // LOAD TRACKS
    // =========================================================

    private void loadTracks() {

        try {

            List<TrackInfo> tracks =
                    service.getAllTracks();

            trackModel.setRowCount(0);

            for (TrackInfo track : tracks) {

                trackModel.addRow(
                        new Object[]{
                                false,
                                track.getSegmentId(),
                                track.getFromStationId(),
                                track.getToStationId(),
                                track.getLengthKm(),
                                track.getMaxSpeed(),
                                track.getStatus()
                        }
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load track segments.",
                    e
            );
        }
    }

    // =========================================================
    // LOAD LOGS
    // =========================================================

    private void loadLogs() {

        try {

            List<LogInfo> logs =
                    service.getTransactionLogs();

            logModel.setRowCount(0);

            for (LogInfo log : logs) {

                logModel.addRow(
                        new Object[]{
                                log.getLogId(),
                                log.getTransactionId(),
                                log.getOperation(),
                                log.getRecordId(),
                                log.getOldValue(),
                                log.getNewValue(),
                                log.getStatus()
                        }
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load transaction logs.",
                    e
            );
        }
    }

    // =========================================================
    // BEGIN TRANSACTION
    // =========================================================

    private void beginTransaction() {

        if (service.isTransactionActive()) {

            JOptionPane.showMessageDialog(
                    this,
                    "A transaction is already active.",
                    "Transaction Active",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<Integer> selectedSegments =
                new ArrayList<>();

        for (
                int row = 0;
                row < trackModel.getRowCount();
                row++
        ) {

            Boolean selected =
                    (Boolean)
                    trackModel.getValueAt(
                            row,
                            0
                    );

            if (Boolean.TRUE.equals(selected)) {

                int segmentId =
                        Integer.parseInt(
                                trackModel
                                        .getValueAt(
                                                row,
                                                1
                                        )
                                        .toString()
                        );

                selectedSegments.add(
                        segmentId
                );
            }
        }

        if (selectedSegments.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Select at least one AVAILABLE track.",
                    "No Track Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            service.beginTransaction(
                    selectedSegments
            );

            transactionIdLabel.setText(
                    "Transaction: "
                    + service.getTransactionId()
            );

            transactionStatusLabel.setText(
                    "Status: LOCKS ACQUIRED"
            );

            beginButton.setEnabled(false);

            commitButton.setEnabled(true);

            rollbackButton.setEnabled(true);

            loadTracks();
            loadLogs();

            JOptionPane.showMessageDialog(
                    this,
                    "Transaction started successfully.\n\n"
                    + "Transaction ID: "
                    + service.getTransactionId()
                    + "\n"
                    + "Tracks locked: "
                    + selectedSegments.size()
                    + "\n\n"
                    + "Locks remain held until COMMIT or ROLLBACK.",
                    "2PL Transaction Started",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            transactionStatusLabel.setText(
                    "Status: TRANSACTION FAILED"
            );

            loadTracks();
            loadLogs();

            showError(
                    "Unable to start transaction.",
                    e
            );
        }
    }

    // =========================================================
    // COMMIT
    // =========================================================

    private void commitTransaction() {

        if (!service.isTransactionActive()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No active transaction.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Commit the transaction?\n\n"
                        + "Selected tracks will change from "
                        + "LOCKED to OCCUPIED.",
                        "Confirm COMMIT",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            service.commitTransaction();

            transactionIdLabel.setText(
                    "Transaction: NONE"
            );

            transactionStatusLabel.setText(
                    "Status: COMMITTED"
            );

            beginButton.setEnabled(true);

            commitButton.setEnabled(false);

            rollbackButton.setEnabled(false);

            loadTracks();
            loadLogs();

            JOptionPane.showMessageDialog(
                    this,
                    "Transaction committed successfully.\n\n"
                    + "Locks have been released.",
                    "COMMIT Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            loadTracks();
            loadLogs();

            showError(
                    "Commit failed.",
                    e
            );
        }
    }

    // =========================================================
    // ROLLBACK
    // =========================================================

    private void rollbackTransaction() {

        if (!service.isTransactionActive()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No active transaction.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Rollback the transaction?\n\n"
                        + "All selected tracks will return "
                        + "to their previous state.",
                        "Confirm ROLLBACK",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            service.rollbackTransaction();

            transactionIdLabel.setText(
                    "Transaction: NONE"
            );

            transactionStatusLabel.setText(
                    "Status: ROLLED BACK"
            );

            beginButton.setEnabled(true);

            commitButton.setEnabled(false);

            rollbackButton.setEnabled(false);

            loadTracks();
            loadLogs();

            JOptionPane.showMessageDialog(
                    this,
                    "Transaction rolled back successfully.\n\n"
                    + "All locks have been released.",
                    "ROLLBACK Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (SQLException e) {

            loadTracks();
            loadLogs();

            showError(
                    "Rollback failed.",
                    e
            );
        }
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