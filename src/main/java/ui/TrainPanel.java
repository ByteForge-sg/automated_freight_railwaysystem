package ui;

import dao.TrainDAO;
import model.Train;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TrainPanel extends JPanel {

    private JTextField trainIdField;
    private JTextField trainNumberField;
    private JComboBox<String> trainTypeCombo;
    private JComboBox<String> statusCombo;

    private JTable trainTable;
    private DefaultTableModel tableModel;

    private final TrainDAO trainDAO;

    public TrainPanel() {

        trainDAO = new TrainDAO();

        setLayout(new BorderLayout(15, 15));

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 30, 25, 30
                )
        );

        setBackground(
                new Color(245, 247, 250)
        );

        createHeader();
        createForm();
        createTable();

        loadTrains();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JLabel heading =
                new JLabel("Train Management");

        heading.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        28
                )
        );

        add(
                heading,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // FORM
    // =========================================================

    private void createForm() {

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Train Details"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Train ID
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel("Train ID:"),
                gbc
        );

        trainIdField =
                new JTextField(12);

        gbc.gridx = 1;

        formPanel.add(
                trainIdField,
                gbc
        );

        // Train Number
        gbc.gridx = 2;

        formPanel.add(
                new JLabel("Train Number:"),
                gbc
        );

        trainNumberField =
                new JTextField(12);

        gbc.gridx = 3;

        formPanel.add(
                trainNumberField,
                gbc
        );

        // Train Type
        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                new JLabel("Train Type:"),
                gbc
        );

        trainTypeCombo =
                new JComboBox<>(
                        new String[]{
                                "FREIGHT",
                                "EXPRESS_FREIGHT"
                        }
                );

        gbc.gridx = 1;

        formPanel.add(
                trainTypeCombo,
                gbc
        );

        // Status
        gbc.gridx = 2;

        formPanel.add(
                new JLabel("Status:"),
                gbc
        );

        statusCombo =
                new JComboBox<>(
                        new String[]{
                                "SCHEDULED",
                                "RUNNING",
                                "AT_YARD",
                                "MAINTENANCE"
                        }
                );

        gbc.gridx = 3;

        formPanel.add(
                statusCombo,
                gbc
        );

        // Buttons
        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttonPanel.setBackground(Color.WHITE);

        JButton addButton =
                new JButton("ADD");

        JButton updateButton =
                new JButton("UPDATE");

        JButton deleteButton =
                new JButton("DELETE");

        JButton refreshButton =
                new JButton("REFRESH");

        JButton clearButton =
                new JButton("CLEAR");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = 4;

        formPanel.add(
                buttonPanel,
                gbc
        );

        add(
                formPanel,
                BorderLayout.NORTH
        );

        // Button actions

        addButton.addActionListener(
                e -> addTrain()
        );

        updateButton.addActionListener(
                e -> updateTrain()
        );

        deleteButton.addActionListener(
                e -> deleteTrain()
        );

        refreshButton.addActionListener(
                e -> loadTrains()
        );

        clearButton.addActionListener(
                e -> clearFields()
        );
    }

    // =========================================================
    // TABLE
    // =========================================================

    private void createTable() {

        String[] columns = {
                "Train ID",
                "Train Number",
                "Train Type",
                "Current Status"
        };

        tableModel =
                new DefaultTableModel(
                        columns,
                        0
                ) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {

                        return false;
                    }
                };

        trainTable =
                new JTable(tableModel);

        trainTable.setRowHeight(28);

        trainTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(trainTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Registered Trains"
                )
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        // Select table row → populate form
        trainTable.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row =
                                trainTable.getSelectedRow();

                        if (row >= 0) {

                            trainIdField.setText(
                                    tableModel.getValueAt(
                                            row, 0
                                    ).toString()
                            );

                            trainNumberField.setText(
                                    tableModel.getValueAt(
                                            row, 1
                                    ).toString()
                            );

                            trainTypeCombo.setSelectedItem(
                                    tableModel.getValueAt(
                                            row, 2
                                    ).toString()
                            );

                            statusCombo.setSelectedItem(
                                    tableModel.getValueAt(
                                            row, 3
                                    ).toString()
                            );
                        }
                    }
                });
    }

    // =========================================================
    // LOAD TRAINS
    // =========================================================

    private void loadTrains() {

        tableModel.setRowCount(0);

        try {

            List<Train> trains =
                    trainDAO.getAllTrains();

            for (Train train : trains) {

                tableModel.addRow(
                        new Object[]{
                                train.getTrainId(),
                                train.getTrainNumber(),
                                train.getTrainType(),
                                train.getCurrentStatus()
                        }
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load trains.\n"
                    + e.getMessage()
            );
        }
    }

    // =========================================================
    // ADD
    // =========================================================

    private void addTrain() {

        try {

            int id =
                    Integer.parseInt(
                            trainIdField.getText().trim()
                    );

            String number =
                    trainNumberField.getText().trim();

            String type =
                    trainTypeCombo
                            .getSelectedItem()
                            .toString();

            String status =
                    statusCombo
                            .getSelectedItem()
                            .toString();

            if (number.isBlank()) {

                showError(
                        "Train number cannot be empty."
                );

                return;
            }

            Train train =
                    new Train(
                            id,
                            number,
                            type,
                            status
                    );

            trainDAO.addTrain(train);

            JOptionPane.showMessageDialog(
                    this,
                    "Train added successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrains();

            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Train ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to add train.\n"
                    + e.getMessage()
            );
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    private void updateTrain() {

        try {

            int id =
                    Integer.parseInt(
                            trainIdField.getText().trim()
                    );

            String status =
                    statusCombo
                            .getSelectedItem()
                            .toString();

            trainDAO.updateStatus(
                    id,
                    status
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Train status updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrains();

            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Train ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to update train.\n"
                    + e.getMessage()
            );
        }
    }

    // =========================================================
    // DELETE
    // =========================================================

    private void deleteTrain() {

        try {

            int id =
                    Integer.parseInt(
                            trainIdField.getText().trim()
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Delete Train ID " + id + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            trainDAO.deleteTrain(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Train deleted successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            loadTrains();

            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Train ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to delete train.\n"
                    + "The train may have related records "
                    + "in MANIFEST, SCHEDULE, or other tables.\n\n"
                    + e.getMessage()
            );
        }
    }

    // =========================================================
    // CLEAR
    // =========================================================

    private void clearFields() {

        trainIdField.setText("");

        trainNumberField.setText("");

        trainTypeCombo.setSelectedIndex(0);

        statusCombo.setSelectedIndex(0);

        trainTable.clearSelection();
    }

    // =========================================================
    // ERROR MESSAGE
    // =========================================================

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}