package ui;

import service.MarshallingYardService;
import service.MarshallingYardService.WagonInfo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarshallingYardPanel extends JPanel {

    private JComboBox<String> sourceTrainCombo;
    private JComboBox<String> destinationTrainCombo;

    private JTable wagonTable;
    private DefaultTableModel tableModel;

    private MarshallingYardService service;

    private JLabel statusLabel;

    public MarshallingYardPanel() {

        service = new MarshallingYardService();

        setLayout(new BorderLayout(15, 15));

        setBackground(
                new Color(245, 247, 250)
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 30, 25, 30
                )
        );

        createTopSection();
        createTable();
        createBottomPanel();

        loadTrains();
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

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(
                new Color(245, 247, 250)
        );

        JLabel title =
                new JLabel(
                        "Marshalling Yard Operations"
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Sort and transfer freight wagons between trains"
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        header.add(
                title,
                BorderLayout.NORTH
        );

        header.add(
                subtitle,
                BorderLayout.SOUTH
        );

        topSection.add(header);

        topSection.add(
                Box.createVerticalStrut(15)
        );

        // -----------------------------------------------------
        // CONTROLS
        // -----------------------------------------------------

        JPanel controlPanel =
                new JPanel(
                        new GridBagLayout()
                );

        controlPanel.setBackground(
                Color.WHITE
        );

        controlPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Marshalling Controls"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // -----------------------------------------------------
        // INBOUND TRAIN
        // -----------------------------------------------------

        JLabel sourceLabel =
                new JLabel(
                        "Inbound Train:"
                );

        sourceTrainCombo =
                new JComboBox<>();

        sourceTrainCombo.setPreferredSize(
                new Dimension(
                        230,
                        32
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;

        controlPanel.add(
                sourceLabel,
                gbc
        );

        gbc.gridx = 1;
        gbc.weightx = 1;

        controlPanel.add(
                sourceTrainCombo,
                gbc
        );

        // -----------------------------------------------------
        // OUTBOUND TRAIN
        // -----------------------------------------------------

        JLabel destinationLabel =
                new JLabel(
                        "Outbound Train:"
                );

        destinationTrainCombo =
                new JComboBox<>();

        destinationTrainCombo.setPreferredSize(
                new Dimension(
                        230,
                        32
                )
        );

        gbc.gridx = 2;
        gbc.weightx = 0;

        controlPanel.add(
                destinationLabel,
                gbc
        );

        gbc.gridx = 3;
        gbc.weightx = 1;

        controlPanel.add(
                destinationTrainCombo,
                gbc
        );

        // -----------------------------------------------------
        // LOAD BUTTON
        // -----------------------------------------------------

        JButton loadButton =
                new JButton(
                        "Load Wagons"
                );

        loadButton.setPreferredSize(
                new Dimension(
                        130,
                        32
                )
        );

        loadButton.addActionListener(
                e -> loadWagons()
        );

        gbc.gridx = 4;
        gbc.weightx = 0;

        controlPanel.add(
                loadButton,
                gbc
        );

        // -----------------------------------------------------
        // SECOND ROW - ACTION BUTTONS
        // -----------------------------------------------------

        JButton moveButton =
                new JButton(
                        "Move Selected Wagons"
                );

        moveButton.setPreferredSize(
                new Dimension(
                        180,
                        32
                )
        );

        moveButton.addActionListener(
                e -> moveSelectedWagons()
        );

        JButton refreshButton =
                new JButton(
                        "Refresh"
                );

        refreshButton.setPreferredSize(
                new Dimension(
                        100,
                        32
                )
        );

        refreshButton.addActionListener(
                e -> loadTrains()
        );

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 0;

        controlPanel.add(
                moveButton,
                gbc
        );

        gbc.gridx = 3;
        gbc.gridwidth = 2;

        controlPanel.add(
                refreshButton,
                gbc
        );

        topSection.add(
                controlPanel
        );

        add(
                topSection,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // TABLE
    // =========================================================

    private void createTable() {

        tableModel =
                new DefaultTableModel(
                        new Object[]{
                                "Select",
                                "Sequence",
                                "Wagon ID",
                                "Wagon Type",
                                "Capacity (Tons)",
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

        wagonTable =
                new JTable(
                        tableModel
                );

        wagonTable.setRowHeight(28);

        wagonTable.getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                13
                        )
                );

        wagonTable.setAutoResizeMode(
                JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        wagonTable
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
                        "Freight Wagon Composition"
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
    // BOTTOM STATUS
    // =========================================================

    private void createBottomPanel() {

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

        statusLabel =
                new JLabel(
                        "Select an inbound train to view its wagons."
                );

        statusLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        bottom.add(
                statusLabel,
                BorderLayout.WEST
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // LOAD TRAINS
    // =========================================================

    private void loadTrains() {

        try {

            List<String> trains =
                    service.getTrainList();

            sourceTrainCombo.removeAllItems();

            destinationTrainCombo.removeAllItems();

            for (String train : trains) {

                sourceTrainCombo.addItem(
                        train
                );

                destinationTrainCombo.addItem(
                        train
                );
            }

            tableModel.setRowCount(0);

            statusLabel.setText(
                    "Train list loaded successfully."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to load trains.",
                    e
            );
        }
    }

    // =========================================================
    // LOAD WAGONS
    // =========================================================

    private void loadWagons() {

        String selectedTrain =
                (String)
                sourceTrainCombo.getSelectedItem();

        int trainId =
                service.extractTrainId(
                        selectedTrain
                );

        if (trainId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an inbound train.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            List<WagonInfo> wagons =
                    service.getTrainComposition(
                            trainId
                    );

            tableModel.setRowCount(0);

            for (WagonInfo wagon : wagons) {

                tableModel.addRow(
                        new Object[]{
                                false,
                                wagon.getSequenceNo(),
                                wagon.getWagonId(),
                                wagon.getWagonType(),
                                wagon.getCapacity(),
                                wagon.getStatus()
                        }
                );
            }

            statusLabel.setText(
                    "Loaded "
                    + wagons.size()
                    + " wagon(s) from Train "
                    + trainId
                    + "."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to load train composition.",
                    e
            );
        }
    }

    // =========================================================
    // MOVE SELECTED WAGONS
    // =========================================================

    private void moveSelectedWagons() {

        String sourceText =
                (String)
                sourceTrainCombo.getSelectedItem();

        String destinationText =
                (String)
                destinationTrainCombo.getSelectedItem();

        int sourceTrainId =
                service.extractTrainId(
                        sourceText
                );

        int destinationTrainId =
                service.extractTrainId(
                        destinationText
                );

        if (sourceTrainId == -1 ||
                destinationTrainId == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select both trains.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (sourceTrainId ==
                destinationTrainId) {

            JOptionPane.showMessageDialog(
                    this,
                    "Source and destination trains must be different.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        List<Integer> selectedWagons =
                new ArrayList<>();

        for (
                int row = 0;
                row < tableModel.getRowCount();
                row++
        ) {

            Boolean selected =
                    (Boolean)
                    tableModel.getValueAt(
                            row,
                            0
                    );

            if (Boolean.TRUE.equals(selected)) {

                int wagonId =
                        Integer.parseInt(
                                tableModel
                                        .getValueAt(
                                                row,
                                                2
                                        )
                                        .toString()
                        );

                selectedWagons.add(
                        wagonId
                );
            }
        }

        if (selectedWagons.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select at least one wagon.",
                    "No Wagons Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Move "
                        + selectedWagons.size()
                        + " wagon(s) from Train "
                        + sourceTrainId
                        + " to Train "
                        + destinationTrainId
                        + "?",
                        "Confirm Marshalling Operation",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice !=
                JOptionPane.YES_OPTION) {

            return;
        }

        try {

            service.moveWagons(
                    sourceTrainId,
                    destinationTrainId,
                    selectedWagons
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Marshalling operation completed successfully.\n"
                    + selectedWagons.size()
                    + " wagon(s) transferred.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadWagons();

            statusLabel.setText(
                    "Successfully transferred "
                    + selectedWagons.size()
                    + " wagon(s) to Train "
                    + destinationTrainId
                    + "."
            );

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Marshalling operation failed.\n\n"
                    + e.getMessage()
                    + "\n\nThe transaction was rolled back.",
                    "Transaction Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // ERROR HANDLER
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