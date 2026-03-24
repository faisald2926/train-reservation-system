package view;

import controller.PassengerController;
import model.Passenger;
import util.UIStyle;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PassengerPanel extends JPanel implements MainFrame.Refreshable {
    private PassengerController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private JTextField nameField, emailField, phoneField, nationalIdField;
    private String selectedPassengerId = null;

    public PassengerPanel() {
        controller = new PassengerController();
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
        titlePanel.add(UIStyle.createTitleLabel("Passenger Management"));
        topPanel.add(titlePanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);
        splitPane.setBackground(UIStyle.BG_MAIN);

        // === TABLE with search ===
        JPanel tablePanel = new JPanel(new BorderLayout(0, 8));
        tablePanel.setOpaque(false);

        String[] columns = {"Passenger ID", "Full Name", "Email", "Phone", "National ID", "Reg. Date"};
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

        JLabel formTitle = new JLabel("Passenger Details");
        formTitle.setFont(UIStyle.FONT_SUBTITLE);
        formTitle.setForeground(UIStyle.PRIMARY);
        formCard.add(formTitle, BorderLayout.NORTH);

        JPanel formFields = new JPanel(new GridBagLayout());
        formFields.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        nameField = UIStyle.createTextField();
        emailField = UIStyle.createTextField();
        phoneField = UIStyle.createTextField();
        nationalIdField = UIStyle.createTextField();

        String[] labels = {"Full Name:", "Email:", "Phone:", "National ID:"};
        JTextField[] fields = {nameField, emailField, phoneField, nationalIdField};

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

        addBtn.addActionListener(e -> addPassenger());
        updateBtn.addActionListener(e -> updatePassenger());
        deleteBtn.addActionListener(e -> deletePassenger());
        clearBtn.addActionListener(e -> clearForm());

        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);

        formCard.add(btnPanel, BorderLayout.SOUTH);
        splitPane.setRightComponent(formCard);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Passenger> passengers = controller.getAllPassengers();
        for (Passenger p : passengers) {
            tableModel.addRow(new Object[]{
                p.getPassengerId(), p.getFullName(), p.getEmail(),
                p.getPhone(), p.getNationalId(), p.getRegistrationDate()
            });
        }
    }

    private void populateForm(int viewRow) {
        int modelRow = table.convertRowIndexToModel(viewRow);
        selectedPassengerId = tableModel.getValueAt(modelRow, 0).toString();
        nameField.setText(tableModel.getValueAt(modelRow, 1).toString());
        emailField.setText(tableModel.getValueAt(modelRow, 2).toString());
        phoneField.setText(tableModel.getValueAt(modelRow, 3).toString());
        nationalIdField.setText(tableModel.getValueAt(modelRow, 4).toString());
    }

    private void addPassenger() {
        String error = controller.addPassenger(
            nameField.getText().trim(), emailField.getText().trim(),
            phoneField.getText().trim(), nationalIdField.getText().trim()
        );
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            UIStyle.showToast(this, "Passenger registered successfully!", UIStyle.SUCCESS);
            clearForm();
            loadTableData();
        }
    }

    private void updatePassenger() {
        if (selectedPassengerId == null) {
            JOptionPane.showMessageDialog(this, "Select a passenger to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String error = controller.updatePassenger(selectedPassengerId,
            nameField.getText().trim(), emailField.getText().trim(),
            phoneField.getText().trim(), nationalIdField.getText().trim()
        );
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            UIStyle.showToast(this, "Passenger updated successfully!", UIStyle.SUCCESS);
            clearForm();
            loadTableData();
        }
    }

    private void deletePassenger() {
        if (selectedPassengerId == null) {
            JOptionPane.showMessageDialog(this, "Select a passenger to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this passenger?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String error = controller.deletePassenger(selectedPassengerId);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UIStyle.showToast(this, "Passenger deleted.", UIStyle.DANGER);
                clearForm();
                loadTableData();
            }
        }
    }

    private void clearForm() {
        selectedPassengerId = null;
        nameField.setText(""); emailField.setText("");
        phoneField.setText(""); nationalIdField.setText("");
        table.clearSelection();
    }

    @Override
    public void refreshData() { loadTableData(); }
}
