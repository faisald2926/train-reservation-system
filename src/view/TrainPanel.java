package view;

import controller.TrainController;
import model.Train;
import model.User;
import util.DataStore;
import util.UIStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TrainPanel extends JPanel implements MainFrame.Refreshable {
    private TrainController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private JTextField nameField, routeField, depStationField, arrStationField;
    private JTextField depTimeField, arrTimeField, seatsField, priceField;
    private String selectedTrainId = null;

    public TrainPanel() {
        controller = new TrainController();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
        loadTableData();
    }

    private void buildUI() {
        // Title
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(UIStyle.createTitleLabel("Train & Schedule Management"));
        topPanel.add(titlePanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Split: Table left, Form right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.65);
        splitPane.setBackground(UIStyle.BG_MAIN);

        // === TABLE with search ===
        JPanel tablePanel = new JPanel(new BorderLayout(0, 8));
        tablePanel.setOpaque(false);

        String[] columns = {"Train ID", "Name", "Route", "From", "To", "Departure", "Arrival", "Seats", "Available", "Price (SAR)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIStyle.styleTable(table);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                populateForm(table.getSelectedRow());
            }
        });

        searchField = UIStyle.addTableSearch(table, tableModel);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        tablePanel.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_COLOR));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(tablePanel);

        // === FORM ===
        JPanel formCard = UIStyle.createCard();
        formCard.setLayout(new BorderLayout(0, 10));

        JLabel formTitle = new JLabel("Train Details");
        formTitle.setFont(UIStyle.FONT_SUBTITLE);
        formTitle.setForeground(UIStyle.PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = UIStyle.createTextField();
        routeField = UIStyle.createTextField();
        depStationField = UIStyle.createTextField();
        arrStationField = UIStyle.createTextField();
        depTimeField = UIStyle.createTextField();
        arrTimeField = UIStyle.createTextField();
        seatsField = UIStyle.createTextField();
        priceField = UIStyle.createTextField();

        String[] labels = {"Train Name:", "Route:", "Departure Stn:", "Arrival Stn:",
                           "Departure Time:", "Arrival Time:", "Total Seats:", "Price (SAR):"};
        JTextField[] fields = {nameField, routeField, depStationField, arrStationField,
                               depTimeField, arrTimeField, seatsField, priceField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            formFields.add(UIStyle.createLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            formFields.add(fields[i], gbc);
        }

        formCard.add(formFields, BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        btnPanel.setBackground(Color.WHITE);

        JButton addBtn = UIStyle.createAccentButton("Add");
        JButton updateBtn = UIStyle.createWarningButton("Update");
        JButton deleteBtn = UIStyle.createDangerButton("Delete");
        JButton clearBtn = UIStyle.createPrimaryButton("Clear");

        addBtn.addActionListener(e -> addTrain());
        updateBtn.addActionListener(e -> updateTrain());
        deleteBtn.addActionListener(e -> deleteTrain());
        clearBtn.addActionListener(e -> clearForm());

        User current = DataStore.getInstance().getCurrentUser();
        boolean isAdmin = current != null && current.getRole() == User.Role.ADMIN;
        if (isAdmin) {
            btnPanel.add(addBtn);
            btnPanel.add(updateBtn);
            btnPanel.add(deleteBtn);
        }
        btnPanel.add(clearBtn);

        formCard.add(btnPanel, BorderLayout.SOUTH);
        splitPane.setRightComponent(formCard);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Train> trains = controller.getAllTrains();
        for (Train t : trains) {
            tableModel.addRow(new Object[]{
                t.getTrainId(), t.getTrainName(), t.getRoute(),
                t.getDepartureStation(), t.getArrivalStation(),
                t.getDepartureTime(), t.getArrivalTime(),
                t.getTotalSeats(), t.getAvailableSeats(),
                String.format("%.2f", t.getTicketPrice()), t.getStatus()
            });
        }
        UIStyle.applyStatusRenderer(table, 10);
    }

    private void populateForm(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        selectedTrainId = tableModel.getValueAt(modelRow, 0).toString();
        nameField.setText(tableModel.getValueAt(modelRow, 1).toString());
        routeField.setText(tableModel.getValueAt(modelRow, 2).toString());
        depStationField.setText(tableModel.getValueAt(modelRow, 3).toString());
        arrStationField.setText(tableModel.getValueAt(modelRow, 4).toString());
        depTimeField.setText(tableModel.getValueAt(modelRow, 5).toString());
        arrTimeField.setText(tableModel.getValueAt(modelRow, 6).toString());
        seatsField.setText(tableModel.getValueAt(modelRow, 7).toString());
        priceField.setText(tableModel.getValueAt(modelRow, 9).toString());
    }

    private void addTrain() {
        try {
            int seats = Integer.parseInt(seatsField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String error = controller.addTrain(
                nameField.getText().trim(), routeField.getText().trim(),
                depStationField.getText().trim(), arrStationField.getText().trim(),
                depTimeField.getText().trim(), arrTimeField.getText().trim(),
                seats, price
            );
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UIStyle.showToast(this, "Train added successfully!", UIStyle.SUCCESS);
                clearForm();
                loadTableData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats must be integer, Price must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTrain() {
        if (selectedTrainId == null) {
            JOptionPane.showMessageDialog(this, "Select a train to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int seats = Integer.parseInt(seatsField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            String error = controller.updateTrain(selectedTrainId,
                nameField.getText().trim(), routeField.getText().trim(),
                depStationField.getText().trim(), arrStationField.getText().trim(),
                depTimeField.getText().trim(), arrTimeField.getText().trim(),
                seats, price
            );
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UIStyle.showToast(this, "Train updated successfully!", UIStyle.SUCCESS);
                clearForm();
                loadTableData();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seats must be integer, Price must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTrain() {
        if (selectedTrainId == null) {
            JOptionPane.showMessageDialog(this, "Select a train to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this train?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String error = controller.deleteTrain(selectedTrainId);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UIStyle.showToast(this, "Train deleted.", UIStyle.DANGER);
                clearForm();
                loadTableData();
            }
        }
    }

    private void clearForm() {
        selectedTrainId = null;
        nameField.setText(""); routeField.setText("");
        depStationField.setText(""); arrStationField.setText("");
        depTimeField.setText(""); arrTimeField.setText("");
        seatsField.setText(""); priceField.setText("");
        table.clearSelection();
    }

    @Override
    public void refreshData() { loadTableData(); }
}
