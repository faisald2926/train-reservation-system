package view;

import controller.ReservationController;
import model.*;
import util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReservationPanel extends JPanel implements MainFrame.Refreshable {
    private ReservationController controller;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;

    private JComboBox<String> passengerCombo, trainCombo;
    private JLabel customerLabel;
    private JTextField travelDateField, seatCountField;
    private JLabel priceLabel, availableLabel;

    private final User currentUser;
    private final boolean customerMode;

    public ReservationPanel() {
        this(null);
    }

    public ReservationPanel(User user) {
        this.currentUser = user;
        this.customerMode = user != null && user.getRole() == User.Role.CUSTOMER;
        controller = new ReservationController();
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
        loadTableData();
    }

    private void buildUI() {
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(UIStyle.createTitleLabel("Reservation & Ticketing"));
        add(titlePanel, BorderLayout.NORTH);

        // Top: Booking Form
        JPanel bookingCard = UIStyle.createCard();
        bookingCard.setLayout(new BorderLayout(10, 10));
        // preferredSize removed — layout sizes naturally for resizability

        JLabel bookTitle = new JLabel("New Booking");
        bookTitle.setFont(UIStyle.FONT_SUBTITLE);
        bookTitle.setForeground(UIStyle.PRIMARY);
        bookingCard.add(bookTitle, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: Passenger + Train
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIStyle.createLabel("Passenger:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        if (customerMode) {
            Passenger me = DataStore.getInstance().getPassengerById(currentUser.getLinkedPassengerId());
            String meText = me != null
                ? me.getPassengerId() + " - " + me.getFullName()
                : currentUser.getFullName();
            customerLabel = new JLabel(meText);
            customerLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            customerLabel.setForeground(UIStyle.PRIMARY);
            formPanel.add(customerLabel, gbc);
        } else {
            passengerCombo = new JComboBox<>();
            passengerCombo.setFont(UIStyle.FONT_BODY);
            passengerCombo.setPreferredSize(new Dimension(250, 36));
            loadPassengerCombo();
            formPanel.add(passengerCombo, gbc);
        }

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(UIStyle.createLabel("Train:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        trainCombo = new JComboBox<>();
        trainCombo.setFont(UIStyle.FONT_BODY);
        trainCombo.setPreferredSize(new Dimension(250, 36));
        loadTrainCombo();
        trainCombo.addActionListener(e -> updateTrainInfo());
        formPanel.add(trainCombo, gbc);

        // Row 2: Travel Date + Seat Count
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(UIStyle.createLabel("Travel Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        travelDateField = UIStyle.createTextField();
        formPanel.add(travelDateField, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        formPanel.add(UIStyle.createLabel("Number of Seats:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1.0;
        seatCountField = UIStyle.createTextField();
        seatCountField.setText("1");
        formPanel.add(seatCountField, gbc);

        // Row 3: Info labels
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(UIStyle.createLabel("Available Seats:"), gbc);
        gbc.gridx = 1;
        availableLabel = new JLabel("-");
        availableLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        availableLabel.setForeground(UIStyle.SUCCESS);
        formPanel.add(availableLabel, gbc);

        gbc.gridx = 2;
        formPanel.add(UIStyle.createLabel("Price per Seat:"), gbc);
        gbc.gridx = 3;
        priceLabel = new JLabel("-");
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(UIStyle.PRIMARY);
        formPanel.add(priceLabel, gbc);

        bookingCard.add(formPanel, BorderLayout.CENTER);

        // Booking Buttons
        JPanel bookBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        bookBtnPanel.setBackground(Color.WHITE);

        JButton refreshBtn = UIStyle.createPrimaryButton("Refresh");
        refreshBtn.addActionListener(e -> refreshAll());

        JButton bookBtn = UIStyle.createAccentButton("Book Ticket");
        bookBtn.addActionListener(e -> bookTicket());

        JButton cancelBtn = UIStyle.createDangerButton("Cancel Selected");
        cancelBtn.addActionListener(e -> cancelReservation());

        JButton confirmBtn = UIStyle.createButton("View Confirmation", UIStyle.INFO);
        confirmBtn.addActionListener(e -> viewConfirmation());

        bookBtnPanel.add(refreshBtn);
        bookBtnPanel.add(bookBtn);
        bookBtnPanel.add(cancelBtn);
        bookBtnPanel.add(confirmBtn);

        bookingCard.add(bookBtnPanel, BorderLayout.SOUTH);

        // Bottom: Search + Reservations Table
        JPanel tableSection = new JPanel(new BorderLayout(0, 8));
        tableSection.setOpaque(false);

        String[] columns = {"Res. ID", "Passenger", "Train", "Booking Date", "Travel Date",
                            "Seats", "Total (SAR)", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIStyle.styleTable(table);

        searchField = UIStyle.addTableSearch(table, tableModel);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        searchPanel.setOpaque(false);
        searchPanel.add(searchField);
        tableSection.add(searchPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UIStyle.BORDER_COLOR));
        tableSection.add(scrollPane, BorderLayout.CENTER);

        // Resizable split between booking form and reservations table
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, bookingCard, tableSection);
        splitPane.setResizeWeight(0.35);
        splitPane.setBorder(null);
        splitPane.setBackground(UIStyle.BG_MAIN);
        splitPane.setDividerSize(6);
        add(splitPane, BorderLayout.CENTER);
        updateTrainInfo();
    }

    private void loadPassengerCombo() {
        if (passengerCombo == null) return;
        passengerCombo.removeAllItems();
        List<Passenger> passengers = DataStore.getInstance().getPassengers();
        for (Passenger p : passengers) {
            passengerCombo.addItem(p.getPassengerId() + " - " + p.getFullName());
        }
    }

    private void loadTrainCombo() {
        trainCombo.removeAllItems();
        List<Train> trains = DataStore.getInstance().getTrains();
        for (Train t : trains) {
            if ("Active".equals(t.getStatus())) {
                trainCombo.addItem(t.getTrainId() + " - " + t.getTrainName() + " (" + t.getRoute() + ")");
            }
        }
    }

    private void updateTrainInfo() {
        String selected = (String) trainCombo.getSelectedItem();
        if (selected != null && selected.contains(" - ")) {
            String trainId = selected.split(" - ")[0].trim();
            Train train = DataStore.getInstance().getTrainById(trainId);
            if (train != null) {
                availableLabel.setText(String.valueOf(train.getAvailableSeats()));
                priceLabel.setText("SAR " + String.format("%.2f", train.getTicketPrice()));
                availableLabel.setForeground(train.getAvailableSeats() > 0 ? UIStyle.SUCCESS : UIStyle.DANGER);
                return;
            }
        }
        availableLabel.setText("-");
        priceLabel.setText("-");
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
        List<Reservation> reservations = customerMode
            ? controller.getReservationsByPassenger(currentUser.getLinkedPassengerId())
            : controller.getAllReservations();
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                r.getReservationId(), r.getPassengerName(), r.getTrainName(),
                r.getBookingDate(), r.getTravelDate(), r.getSeatCount(),
                String.format("%.2f", r.getTotalPrice()), r.getStatus().toString()
            });
        }
        UIStyle.applyStatusRenderer(table, 7);
    }

    private void bookTicket() {
        String trainSel = (String) trainCombo.getSelectedItem();

        String passengerId;
        if (customerMode) {
            passengerId = currentUser.getLinkedPassengerId();
            if (passengerId == null) {
                JOptionPane.showMessageDialog(this, "Your account is not linked to a passenger profile.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            String passengerSel = (String) passengerCombo.getSelectedItem();
            if (passengerSel == null) {
                JOptionPane.showMessageDialog(this, "Please select a passenger.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            passengerId = passengerSel.split(" - ")[0].trim();
        }

        if (trainSel == null) {
            JOptionPane.showMessageDialog(this, "Please select a train.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String trainId = trainSel.split(" - ")[0].trim();
        String travelDate = travelDateField.getText().trim();

        int seatCount;
        try {
            seatCount = Integer.parseInt(seatCountField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Seat count must be a number.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Train train = DataStore.getInstance().getTrainById(trainId);
        double estimatedTotal = train != null ? train.getTicketPrice() * Math.max(seatCount, 0) : 0;

        if (!showPaymentDialog(estimatedTotal)) {
            return;
        }

        String error = controller.bookTicket(passengerId, trainId, travelDate, seatCount);
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Booking Failed", JOptionPane.ERROR_MESSAGE);
        } else {
            List<Reservation> all = controller.getAllReservations();
            Reservation last = all.get(all.size() - 1);
            String confirmation = controller.getBookingConfirmation(last.getReservationId());

            JTextArea textArea = new JTextArea(confirmation);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
                "Payment Successful - Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

            refreshAll();
        }
    }

    private boolean showPaymentDialog(double amount) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = (owner instanceof Frame)
            ? new JDialog((Frame) owner, "SecurePay Gateway", true)
            : new JDialog((Dialog) owner, "SecurePay Gateway", true);
        dialog.setSize(380, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(0, 12));
        content.setBackground(Color.WHITE);
        content.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = new JLabel("Processing payment via SecurePay…");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(UIStyle.PRIMARY);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel amountLabel = new JLabel(String.format("Amount: SAR %.2f", amount));
        amountLabel.setFont(UIStyle.FONT_BODY);
        amountLabel.setForeground(UIStyle.TEXT_PRIMARY);
        amountLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JProgressBar bar = new JProgressBar();
        bar.setIndeterminate(true);
        bar.setPreferredSize(new Dimension(0, 18));

        JLabel hint = new JLabel("Please do not close this window.");
        hint.setFont(UIStyle.FONT_SMALL);
        hint.setForeground(UIStyle.TEXT_SECONDARY);
        hint.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        amountLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bar.setAlignmentX(Component.CENTER_ALIGNMENT);
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);
        center.add(title);
        center.add(Box.createVerticalStrut(8));
        center.add(amountLabel);
        center.add(Box.createVerticalStrut(10));
        center.add(bar);
        center.add(Box.createVerticalStrut(8));
        center.add(hint);

        content.add(center, BorderLayout.CENTER);
        dialog.setContentPane(content);

        Timer timer = new Timer(1500, e -> dialog.dispose());
        timer.setRepeats(false);
        timer.start();
        dialog.setVisible(true);
        return true;
    }

    private void cancelReservation() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a reservation to cancel.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        String resId = tableModel.getValueAt(modelRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to cancel reservation " + resId + "?",
            "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String error = controller.cancelReservation(resId);
            if (error != null) {
                JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                UIStyle.showToast(this, "Reservation cancelled. Seats released.", UIStyle.DANGER);
                refreshAll();
            }
        }
    }

    private void viewConfirmation() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Select a reservation.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        String resId = tableModel.getValueAt(modelRow, 0).toString();
        String confirmation = controller.getBookingConfirmation(resId);

        JTextArea textArea = new JTextArea(confirmation);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea),
            "Booking Confirmation", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshAll() {
        loadPassengerCombo();
        loadTrainCombo();
        loadTableData();
        updateTrainInfo();
    }

    @Override
    public void refreshData() { refreshAll(); }
}
