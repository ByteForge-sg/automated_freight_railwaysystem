package ui;

import dao.StationDAO;
import model.Station;
import model.TrackSegment;
import service.RouteService;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class RoutePanel extends JPanel {

    private JComboBox<StationItem> sourceCombo;
    private JComboBox<StationItem> destinationCombo;

    private JTextArea resultArea;
    private JLabel distanceLabel;

    private final StationDAO stationDAO;
    private final RouteService routeService;

    public RoutePanel() {

        stationDAO = new StationDAO();
        routeService = new RouteService();

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
        createControls();
        createResultArea();

        loadStations();
    }

    private void createHeader() {

        JLabel heading =
                new JLabel("Route Optimizer");

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

    private void createControls() {

        JPanel controlPanel =
                new JPanel(
                        new GridBagLayout()
                );

        controlPanel.setBackground(Color.WHITE);

        controlPanel.setBorder(
                BorderFactory.createTitledBorder(
                        "Route Selection"
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(10, 10, 10, 10);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        // Source
        gbc.gridx = 0;
        gbc.gridy = 0;

        controlPanel.add(
                new JLabel("Source Station:"),
                gbc
        );

        sourceCombo =
                new JComboBox<>();

        gbc.gridx = 1;
        gbc.weightx = 1;

        controlPanel.add(
                sourceCombo,
                gbc
        );

        // Destination
        gbc.gridx = 2;
        gbc.weightx = 0;

        controlPanel.add(
                new JLabel("Destination Station:"),
                gbc
        );

        destinationCombo =
                new JComboBox<>();

        gbc.gridx = 3;
        gbc.weightx = 1;

        controlPanel.add(
                destinationCombo,
                gbc
        );

        // Button
        JButton findButton =
                new JButton(
                        "FIND SHORTEST ROUTE"
                );

        findButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        gbc.gridx = 0;
        gbc.gridy = 1;

        gbc.gridwidth = 4;

        gbc.weightx = 0;

        controlPanel.add(
                findButton,
                gbc
        );

        findButton.addActionListener(
                e -> findRoute()
        );

        add(
                controlPanel,
                BorderLayout.NORTH
        );
    }

    private void createResultArea() {

        JPanel resultPanel =
                new JPanel(
                        new BorderLayout(10, 10)
                );

        resultPanel.setBackground(
                new Color(245, 247, 250)
        );

        resultArea =
                new JTextArea();

        resultArea.setEditable(false);

        resultArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        resultArea.setMargin(
                new Insets(15, 15, 15, 15)
        );

        JScrollPane scrollPane =
                new JScrollPane(resultArea);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Optimized Route"
                )
        );

        resultPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        distanceLabel =
                new JLabel(
                        "Total Distance: --"
                );

        distanceLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        resultPanel.add(
                distanceLabel,
                BorderLayout.SOUTH
        );

        add(
                resultPanel,
                BorderLayout.CENTER
        );
    }

    private void loadStations() {

        try {

            List<Station> stations =
                    stationDAO.getAllStations();

            sourceCombo.removeAllItems();

            destinationCombo.removeAllItems();

            for (Station station : stations) {

                StationItem item =
                        new StationItem(
                                station.getStationId(),
                                station.getStationName()
                        );

                sourceCombo.addItem(item);

                destinationCombo.addItem(
                        new StationItem(
                                station.getStationId(),
                                station.getStationName()
                        )
                );
            }

        } catch (SQLException e) {

            showError(
                    "Unable to load stations.\n"
                    + e.getMessage()
            );
        }
    }

    private void findRoute() {

        StationItem source =
                (StationItem)
                        sourceCombo.getSelectedItem();

        StationItem destination =
                (StationItem)
                        destinationCombo.getSelectedItem();

        if (source == null || destination == null) {

            showError(
                    "Please select both source and destination."
            );

            return;
        }

        if (source.id == destination.id) {

            showError(
                    "Source and destination must be different."
            );

            return;
        }

        try {

            List<TrackSegment> route =
                    routeService.findShortestRoute(
                            source.id,
                            destination.id
                    );

            resultArea.setText("");

            if (route.isEmpty()) {

                resultArea.append(
                        "NO AVAILABLE ROUTE FOUND\n\n"
                );

                resultArea.append(
                        source.name
                        + " → "
                        + destination.name
                );

                distanceLabel.setText(
                        "Total Distance: --"
                );

                return;
            }

            resultArea.append(
                    "SHORTEST AVAILABLE ROUTE\n"
            );

            resultArea.append(
                    "====================================\n\n"
            );

            resultArea.append(
                    source.name
                    + " (Station "
                    + source.id
                    + ")\n"
            );

            double totalDistance = 0;

            for (TrackSegment track : route) {

                resultArea.append(
                        "   ↓ Segment "
                        + track.getSegmentId()
                        + " | "
                        + track.getLengthKm()
                        + " km | Max Speed "
                        + track.getMaxSpeed()
                        + " km/h\n"
                );

                resultArea.append(
                        "   ↓ Station "
                        + track.getToStationId()
                        + "\n"
                );

                totalDistance +=
                        track.getLengthKm();
            }

            resultArea.append(
                    "\nDestination: "
                    + destination.name
                    + "\n"
            );

            resultArea.append(
                    "\nNumber of segments: "
                    + route.size()
                    + "\n"
            );

            distanceLabel.setText(
                    String.format(
                            "Total Distance: %.1f km",
                            totalDistance
                    )
            );

        } catch (SQLException e) {

            showError(
                    "Unable to calculate route.\n"
                    + e.getMessage()
            );
        }
    }

    private void showError(String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "Route Optimizer",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // STATION COMBO BOX ITEM
    // =========================================================

    private static class StationItem {

        private final int id;
        private final String name;

        public StationItem(
                int id,
                String name) {

            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {

            return id + " - " + name;
        }
    }
}