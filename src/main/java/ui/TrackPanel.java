package ui;

import dao.TrackSegmentDAO;
import model.TrackSegment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TrackPanel extends JPanel {

    private JTextField idField;
    private JTextField fromField;
    private JTextField toField;
    private JTextField lengthField;
    private JTextField speedField;

    private JComboBox<String> statusCombo;

    private JTable table;
    private DefaultTableModel tableModel;

    private final TrackSegmentDAO trackDAO;

    public TrackPanel() {

        trackDAO = new TrackSegmentDAO();

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

        loadTracks();
    }

    private void createHeader() {

        JLabel heading =
                new JLabel("Track Segment Management");

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
                        "Track Segment Details"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(6, 6, 6, 6);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Segment ID
        gbc.gridx = 0;
        gbc.gridy = 0;

        form.add(
                new JLabel("Segment ID:"),
                gbc
        );

        idField = new JTextField(8);

        gbc.gridx = 1;
        form.add(idField, gbc);

        // From station
        gbc.gridx = 2;

        form.add(
                new JLabel("From Station ID:"),
                gbc
        );

        fromField = new JTextField(8);

        gbc.gridx = 3;
        form.add(fromField, gbc);

        // To station
        gbc.gridx = 0;
        gbc.gridy = 1;

        form.add(
                new JLabel("To Station ID:"),
                gbc
        );

        toField = new JTextField(8);

        gbc.gridx = 1;
        form.add(toField, gbc);

        // Length
        gbc.gridx = 2;

        form.add(
                new JLabel("Length (KM):"),
                gbc
        );

        lengthField = new JTextField(8);

        gbc.gridx = 3;
        form.add(lengthField, gbc);

        // Speed
        gbc.gridx = 0;
        gbc.gridy = 2;

        form.add(
                new JLabel("Max Speed:"),
                gbc
        );

        speedField = new JTextField(8);

        gbc.gridx = 1;
        form.add(speedField, gbc);

        // Status
        gbc.gridx = 2;

        form.add(
                new JLabel("Status:"),
                gbc
        );

        statusCombo =
                new JComboBox<>(
                        new String[]{
                                "AVAILABLE",
                                "OCCUPIED",
                                "LOCKED",
                                "MAINTENANCE"
                        }
                );

        gbc.gridx = 3;
        form.add(statusCombo, gbc);

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
                new JButton("UPDATE STATUS");

        JButton refresh =
                new JButton("REFRESH");

        JButton clear =
                new JButton("CLEAR");

        buttons.add(add);
        buttons.add(update);
        buttons.add(refresh);
        buttons.add(clear);

        gbc.gridx = 0;
        gbc.gridy = 3;

        gbc.gridwidth = 4;

        form.add(buttons, gbc);

        add(form, BorderLayout.NORTH);

        add.addActionListener(
                e -> addTrack()
        );

        update.addActionListener(
                e -> updateTrackStatus()
        );

        refresh.addActionListener(
                e -> loadTracks()
        );

        clear.addActionListener(
                e -> clearFields()
        );
    }

    private void createTable() {

        String[] columns = {
                "Segment ID",
                "From",
                "To",
                "Length KM",
                "Max Speed",
                "Status"
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
                        "Railway Track Network"
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

                            fromField.setText(
                                    tableModel
                                            .getValueAt(row, 1)
                                            .toString()
                            );

                            toField.setText(
                                    tableModel
                                            .getValueAt(row, 2)
                                            .toString()
                            );

                            lengthField.setText(
                                    tableModel
                                            .getValueAt(row, 3)
                                            .toString()
                            );

                            speedField.setText(
                                    tableModel
                                            .getValueAt(row, 4)
                                            .toString()
                            );

                            statusCombo.setSelectedItem(
                                    tableModel
                                            .getValueAt(row, 5)
                                            .toString()
                            );
                        }
                    }
                });
    }

    private void loadTracks() {

        tableModel.setRowCount(0);

        try {

            List<TrackSegment> tracks =
                    trackDAO.getAllTrackSegments();

            for (TrackSegment track : tracks) {

                tableModel.addRow(
                        new Object[]{
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
                    "Unable to load tracks.\n"
                    + e.getMessage()
            );
        }
    }

    private void addTrack() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            int from =
                    Integer.parseInt(
                            fromField.getText().trim()
                    );

            int to =
                    Integer.parseInt(
                            toField.getText().trim()
                    );

            double length =
                    Double.parseDouble(
                            lengthField.getText().trim()
                    );

            int speed =
                    Integer.parseInt(
                            speedField.getText().trim()
                    );

            String status =
                    statusCombo.getSelectedItem().toString();

            if (length <= 0 || speed <= 0) {

                showError(
                        "Length and speed must be positive."
                );

                return;
            }

            trackDAO.addTrackSegment(
                    new TrackSegment(
                            id,
                            from,
                            to,
                            length,
                            speed,
                            status
                    )
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Track segment added successfully."
            );

            loadTracks();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Please enter valid numeric values."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to add track.\n"
                    + "Check that the From/To Station IDs exist.\n\n"
                    + e.getMessage()
            );
        }
    }

    private void updateTrackStatus() {

        try {

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String status =
                    statusCombo.getSelectedItem().toString();

            trackDAO.updateStatus(
                    id,
                    status
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Track status updated successfully."
            );

            loadTracks();
            clearFields();

        } catch (NumberFormatException e) {

            showError(
                    "Segment ID must be a valid number."
            );

        } catch (SQLException e) {

            showError(
                    "Unable to update track.\n"
                    + e.getMessage()
            );
        }
    }

    private void clearFields() {

        idField.setText("");
        fromField.setText("");
        toField.setText("");
        lengthField.setText("");
        speedField.setText("");

        statusCombo.setSelectedIndex(0);

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