package ui;

import dao.StationDAO;
import model.Station;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class StationPanel extends JPanel {

    private JTextField idField;
    private JTextField nameField;
    private JTextField locationField;
    private JComboBox<String> typeCombo;

    private JTable table;
    private DefaultTableModel tableModel;

    private final StationDAO stationDAO;

    public StationPanel() {

        stationDAO = new StationDAO();

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

        loadStations();
    }

    private void createHeader() {

        JLabel heading =
                new JLabel("Station Management");

        heading.setFont(
                new Font("Arial", Font.BOLD, 28)
        );

        add(heading, BorderLayout.NORTH);
    }

    private void createForm() {

        JPanel form =
                new JPanel(new GridBagLayout());

        form.setBackground(Color.WHITE);

        form.setBorder(
                BorderFactory.createTitledBorder(
                        "Station Details"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(8, 8, 8, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;

        form.add(new JLabel("Station ID:"), gbc);

        idField = new JTextField(10);

        gbc.gridx = 1;
        form.add(idField, gbc);

        // Name
        gbc.gridx = 2;

        form.add(new JLabel("Station Name:"), gbc);

        nameField = new JTextField(12);

        gbc.gridx = 3;
        form.add(nameField, gbc);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 1;

        form.add(new JLabel("Location:"), gbc);

        locationField = new JTextField(12);

        gbc.gridx = 1;
        form.add(locationField, gbc);

        // Type
        gbc.gridx = 2;

        form.add(new JLabel("Station Type:"), gbc);

        typeCombo =
                new JComboBox<>(
                        new String[]{
                                "JUNCTION",
                                "TERMINAL",
                                "YARD",
                                "INTERCHANGE"
                        }
                );

        gbc.gridx = 3;
        form.add(typeCombo, gbc);

        // Buttons
        JPanel buttons =
                new JPanel(new FlowLayout(
                        FlowLayout.LEFT
                ));

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

        form.add(buttons, gbc);

        add(form, BorderLayout.NORTH);

        add.addActionListener(e -> addStation());
        update.addActionListener(e -> updateStation());
        delete.addActionListener(e -> deleteStation());
        refresh.addActionListener(e -> loadStations());
        clear.addActionListener(e -> clearFields());
    }

    private void createTable() {

        String[] columns = {
                "Station ID",
                "Station Name",
                "Location",
                "Station Type"
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

        table = new JTable(tableModel);

        table.setRowHeight(28);

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Registered Stations"
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

                            nameField.setText(
                                    tableModel
                                            .getValueAt(row, 1)
                                            .toString()
                            );

                            locationField.setText(
                                    tableModel
                                            .getValueAt(row, 2)
                                            .toString()
                            );

                            typeCombo.setSelectedItem(
                                    tableModel
                                            .getValueAt(row, 3)
                                            .toString()
                            );
                        }
                    }
                });
    }

    private void loadStations() {

        tableModel.setRowCount(0);

        try {

            List<Station> stations =
                    stationDAO.getAllStations();

            for (Station station : stations) {

                tableModel.addRow(
                        new Object[]{
                                station.getStationId(),
                                station.getStationName(),
                                station.getLocation(),
                                station.getStationType()
                        }
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load stations.\n"
                    + e.getMessage()
            );
        }
    }

    private void addStation() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String name =
                    nameField.getText().trim();

            String location =
                    locationField.getText().trim();

            String type =
                    typeCombo.getSelectedItem().toString();

            if (name.isBlank() ||
                    location.isBlank()) {

                showError(
                        "Station name and location cannot be empty."
                );

                return;
            }

            stationDAO.addStation(
                    new Station(
                            id,
                            name,
                            location,
                            type
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Station added successfully."
            );

            loadStations();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Station ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to add station.\n"
                    + e.getMessage()
            );
        }
    }

    private void updateStation() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String name =
                    nameField.getText().trim();

            String location =
                    locationField.getText().trim();

            String type =
                    typeCombo.getSelectedItem().toString();

            stationDAO.updateStation(
                    new Station(
                            id,
                            name,
                            location,
                            type
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Station updated successfully."
            );

            loadStations();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Station ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to update station.\n"
                    + e.getMessage()
            );
        }
    }

    private void deleteStation() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            int choice =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Delete Station ID " + id + "?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION
                    );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            stationDAO.deleteStation(id);

            JOptionPane.showMessageDialog(
                    this,
                    "Station deleted successfully."
            );

            loadStations();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Station ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to delete station.\n"
                    + "The station may be referenced by tracks, "
                    + "yards, or manifests.\n\n"
                    + e.getMessage()
            );
        }
    }

    private void clearFields() {

        idField.setText("");
        nameField.setText("");
        locationField.setText("");

        typeCombo.setSelectedIndex(0);

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