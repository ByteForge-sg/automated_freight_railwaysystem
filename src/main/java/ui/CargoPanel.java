package ui;

import dao.CargoDAO;
import model.Cargo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class CargoPanel extends JPanel {

    private JTextField idField;
    private JTextField descriptionField;
    private JTextField weightField;

    private JComboBox<String> hazardCombo;

    private JTable table;
    private DefaultTableModel tableModel;

    private final CargoDAO cargoDAO;

    public CargoPanel() {

        cargoDAO = new CargoDAO();

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

        loadCargo();
    }

    private void createHeader() {

        JLabel heading =
                new JLabel("Cargo Management");

        heading.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        add(
                heading,
                BorderLayout.NORTH
        );
    }

    private void createForm() {

        JPanel form =
                new JPanel(new GridBagLayout());

        form.setBackground(Color.WHITE);

        form.setBorder(
                BorderFactory.createTitledBorder(
                        "Cargo Details"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Cargo ID
        gbc.gridx = 0;
        gbc.gridy = 0;

        form.add(
                new JLabel("Cargo ID:"),
                gbc
        );

        idField =
                new JTextField(10);

        gbc.gridx = 1;

        form.add(
                idField,
                gbc
        );

        // Description
        gbc.gridx = 2;

        form.add(
                new JLabel("Description:"),
                gbc
        );

        descriptionField =
                new JTextField(12);

        gbc.gridx = 3;

        form.add(
                descriptionField,
                gbc
        );

        // Weight
        gbc.gridx = 0;
        gbc.gridy = 1;

        form.add(
                new JLabel("Weight (Tons):"),
                gbc
        );

        weightField =
                new JTextField(10);

        gbc.gridx = 1;

        form.add(
                weightField,
                gbc
        );

        // Hazard
        gbc.gridx = 2;

        form.add(
                new JLabel("Hazard Class:"),
                gbc
        );

        hazardCombo =
                new JComboBox<>(
                        new String[]{
                                "NON_HAZARDOUS",
                                "HAZARDOUS-2",
                                "HAZARDOUS-3",
                                "HAZARDOUS-5",
                                "HAZARDOUS-8"
                        }
                );

        gbc.gridx = 3;

        form.add(
                hazardCombo,
                gbc
        );

        // Buttons
        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        buttons.setBackground(Color.WHITE);

        JButton add =
                new JButton("ADD");

        JButton update =
                new JButton("UPDATE");

        JButton delete =
                new JButton("DELETE");

        JButton refresh =
                new JButton("REFRESH");

        JButton clear =
                new JButton("CLEAR");

        buttons.add(add);
        buttons.add(update);
        buttons.add(delete);
        buttons.add(refresh);
        buttons.add(clear);

        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = 4;

        form.add(
                buttons,
                gbc
        );

        add(
                form,
                BorderLayout.NORTH
        );

        add.addActionListener(
                e -> addCargo()
        );

        update.addActionListener(
                e -> updateCargo()
        );

        delete.addActionListener(
                e -> deleteCargo()
        );

        refresh.addActionListener(
                e -> loadCargo()
        );

        clear.addActionListener(
                e -> clearFields()
        );
    }

    private void createTable() {

        String[] columns = {
                "Cargo ID",
                "Description",
                "Weight Tons",
                "Hazard Class"
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

        table =
                new JTable(tableModel);

        table.setRowHeight(28);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Cargo Inventory"
                )
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        int row =
                                table.getSelectedRow();

                        if (row >= 0) {

                            idField.setText(
                                    tableModel
                                            .getValueAt(row, 0)
                                            .toString()
                            );

                            descriptionField.setText(
                                    tableModel
                                            .getValueAt(row, 1)
                                            .toString()
                            );

                            weightField.setText(
                                    tableModel
                                            .getValueAt(row, 2)
                                            .toString()
                            );

                            hazardCombo.setSelectedItem(
                                    tableModel
                                            .getValueAt(row, 3)
                                            .toString()
                            );
                        }
                    }
                });
    }

    private void loadCargo() {

        tableModel.setRowCount(0);

        try {

            List<Cargo> cargoList =
                    cargoDAO.getAllCargo();

            for (Cargo cargo : cargoList) {

                tableModel.addRow(
                        new Object[]{
                                cargo.getCargoId(),
                                cargo.getDescription(),
                                cargo.getWeightTons(),
                                cargo.getHazardClass()
                        }
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load cargo.\n"
                    + e.getMessage()
            );
        }
    }

    private void addCargo() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String description =
                    descriptionField.getText().trim();

            double weight =
                    Double.parseDouble(
                            weightField.getText().trim()
                    );

            String hazard =
                    hazardCombo.getSelectedItem().toString();

            if (description.isBlank()) {

                showError(
                        "Cargo description cannot be empty."
                );

                return;
            }

            if (weight <= 0) {

                showError(
                        "Weight must be greater than zero."
                );

                return;
            }

            cargoDAO.addCargo(
                    new Cargo(
                            id,
                            description,
                            weight,
                            hazard
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Cargo added successfully."
            );

            loadCargo();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Cargo ID and weight must be valid numbers."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to add cargo.\n"
                    + e.getMessage()
            );
        }
    }

    private void updateCargo() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String description =
                    descriptionField.getText().trim();

            double weight =
                    Double.parseDouble(
                            weightField.getText().trim()
                    );

            String hazard =
                    hazardCombo.getSelectedItem().toString();

            cargoDAO.updateCargo(
                    new Cargo(
                            id,
                            description,
                            weight,
                            hazard
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Cargo updated successfully."
            );

            loadCargo();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Cargo ID and weight must be valid numbers."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to update cargo.\n"
                    + e.getMessage()
            );
        }
    }

    private void deleteCargo() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Delete Cargo ID " + id + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            cargoDAO.deleteCargo(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Cargo deleted successfully."
            );

            loadCargo();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Cargo ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to delete cargo.\n"
                    + "The cargo may be referenced by CAR_CARGO.\n\n"
                    + e.getMessage()
            );
        }
    }

    private void clearFields() {

        idField.setText("");
        descriptionField.setText("");
        weightField.setText("");

        hazardCombo.setSelectedIndex(0);

        table.clearSelection();
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}