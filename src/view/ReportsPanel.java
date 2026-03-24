package view;

import model.*;
import util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class ReportsPanel extends JPanel implements MainFrame.Refreshable {
    private JPanel reportContent;
    private JTextField fromDateField, toDateField;

    public ReportsPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        titlePanel.add(UIStyle.createTitleLabel("Reports & Analytics"));
        add(titlePanel, BorderLayout.NORTH);

        // Controls Panel
        JPanel controlsCard = UIStyle.createCard();
        controlsCard.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 8));
        controlsCard.setPreferredSize(new Dimension(0, 80));

        controlsCard.add(UIStyle.createLabel("From:"));
        fromDateField = UIStyle.createTextField();
        fromDateField.setPreferredSize(new Dimension(130, 34));
        fromDateField.setText(LocalDate.now().minusMonths(1).toString());
        controlsCard.add(fromDateField);

        controlsCard.add(UIStyle.createLabel("To:"));
        toDateField = UIStyle.createTextField();
        toDateField.setPreferredSize(new Dimension(130, 34));
        toDateField.setText(LocalDate.now().toString());
        controlsCard.add(toDateField);

        JButton bookingReport = UIStyle.createPrimaryButton("Booking Report");
        bookingReport.addActionListener(e -> generateBookingReport());
        controlsCard.add(bookingReport);

        JButton revenueReport = UIStyle.createAccentButton("Revenue Report");
        revenueReport.addActionListener(e -> generateRevenueReport());
        controlsCard.add(revenueReport);

        JButton utilizationReport = UIStyle.createButton("Utilization Report", UIStyle.INFO);
        utilizationReport.addActionListener(e -> generateUtilizationReport());
        controlsCard.add(utilizationReport);

        JButton passengerReport = UIStyle.createWarningButton("Passenger Report");
        passengerReport.addActionListener(e -> generatePassengerReport());
        controlsCard.add(passengerReport);

        // Report Content Area
        reportContent = new JPanel(new BorderLayout());
        reportContent.setBackground(Color.WHITE);
        reportContent.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.BORDER_COLOR),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel placeholder = new JLabel("Select a report type to generate", SwingConstants.CENTER);
        placeholder.setFont(UIStyle.FONT_SUBTITLE);
        placeholder.setForeground(UIStyle.TEXT_SECONDARY);
        reportContent.add(placeholder, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(controlsCard, BorderLayout.NORTH);
        centerPanel.add(reportContent, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private void generateBookingReport() {
        reportContent.removeAll();

        DataStore ds = DataStore.getInstance();
        List<Reservation> reservations = filterByDate(ds.getReservations());

        // Summary
        int total = reservations.size();
        int confirmed = 0, cancelled = 0;
        double totalRevenue = 0;
        int totalSeats = 0;

        for (Reservation r : reservations) {
            if (r.getStatus() == Reservation.Status.CONFIRMED) {
                confirmed++;
                totalRevenue += r.getTotalPrice();
                totalSeats += r.getSeatCount();
            } else if (r.getStatus() == Reservation.Status.CANCELLED) {
                cancelled++;
            }
        }

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel title = new JLabel("Booking Report");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.PRIMARY);
        header.add(title, BorderLayout.NORTH);

        JPanel statsRow = new JPanel(new GridLayout(1, 4, 10, 0));
        statsRow.setOpaque(false);
        statsRow.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        statsRow.add(UIStyle.createStatCard("Total Bookings", String.valueOf(total), UIStyle.PRIMARY));
        statsRow.add(UIStyle.createStatCard("Confirmed", String.valueOf(confirmed), UIStyle.SUCCESS));
        statsRow.add(UIStyle.createStatCard("Cancelled", String.valueOf(cancelled), UIStyle.DANGER));
        statsRow.add(UIStyle.createStatCard("Revenue", String.format("SAR %.0f", totalRevenue), UIStyle.ACCENT));
        header.add(statsRow, BorderLayout.CENTER);

        reportContent.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"Res. ID", "Passenger", "Train", "Booking Date", "Travel Date", "Seats", "Total (SAR)", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Reservation r : reservations) {
            model.addRow(new Object[]{
                r.getReservationId(), r.getPassengerName(), r.getTrainName(),
                r.getBookingDate(), r.getTravelDate(), r.getSeatCount(),
                String.format("%.2f", r.getTotalPrice()), r.getStatus()
            });
        }
        JTable table = new JTable(model);
        UIStyle.styleTable(table);
        UIStyle.applyStatusRenderer(table, 7);
        reportContent.add(new JScrollPane(table), BorderLayout.CENTER);

        reportContent.revalidate();
        reportContent.repaint();
    }

    private void generateRevenueReport() {
        reportContent.removeAll();

        DataStore ds = DataStore.getInstance();
        List<Reservation> reservations = filterByDate(ds.getReservations());

        // Group by train
        Map<String, double[]> trainRevenue = new LinkedHashMap<>(); // trainName -> [revenue, bookings]
        for (Reservation r : reservations) {
            if (r.getStatus() == Reservation.Status.CONFIRMED) {
                double[] data = trainRevenue.getOrDefault(r.getTrainName(), new double[]{0, 0});
                data[0] += r.getTotalPrice();
                data[1] += 1;
                trainRevenue.put(r.getTrainName(), data);
            }
        }

        JLabel title = new JLabel("Revenue Analysis Report");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        reportContent.add(title, BorderLayout.NORTH);

        // Revenue by train table
        String[] columns = {"Train", "Total Revenue (SAR)", "Bookings", "Avg Revenue/Booking"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        double grandTotal = 0;

        for (Map.Entry<String, double[]> entry : trainRevenue.entrySet()) {
            double rev = entry.getValue()[0];
            int bookings = (int) entry.getValue()[1];
            double avg = bookings > 0 ? rev / bookings : 0;
            grandTotal += rev;
            model.addRow(new Object[]{
                entry.getKey(), String.format("%.2f", rev),
                bookings, String.format("%.2f", avg)
            });
        }
        model.addRow(new Object[]{"TOTAL", String.format("%.2f", grandTotal), "", ""});

        JTable table = new JTable(model);
        UIStyle.styleTable(table);

        // Bar chart
        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (trainRevenue.isEmpty()) {
                    g2.setFont(UIStyle.FONT_BODY);
                    g2.setColor(UIStyle.TEXT_SECONDARY);
                    g2.drawString("No revenue data", 60, getHeight() / 2);
                    return;
                }

                double maxRev = trainRevenue.values().stream().mapToDouble(d -> d[0]).max().orElse(1);
                int barWidth = Math.max(40, (getWidth() - 60) / trainRevenue.size() - 15);
                int x = 40;
                Color[] colors = {UIStyle.PRIMARY, UIStyle.ACCENT, UIStyle.INFO, new Color(156,39,176), UIStyle.SUCCESS};
                int ci = 0;

                for (Map.Entry<String, double[]> entry : trainRevenue.entrySet()) {
                    int barH = (int) (entry.getValue()[0] / maxRev * (getHeight() - 60));
                    g2.setColor(colors[ci % colors.length]);
                    g2.fillRoundRect(x, getHeight() - 30 - barH, barWidth, barH, 5, 5);
                    g2.setColor(UIStyle.TEXT_PRIMARY);
                    g2.setFont(UIStyle.FONT_SMALL);

                    String label = entry.getKey().length() > 10 ? entry.getKey().substring(0, 10) + ".." : entry.getKey();
                    g2.drawString(label, x, getHeight() - 12);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2.drawString(String.format("%.0f", entry.getValue()[0]), x, getHeight() - 35 - barH);

                    x += barWidth + 15;
                    ci++;
                }
            }
        };
        chart.setBackground(Color.WHITE);
        chart.setPreferredSize(new Dimension(0, 180));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(chart, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        reportContent.add(centerPanel, BorderLayout.CENTER);
        reportContent.revalidate();
        reportContent.repaint();
    }

    private void generateUtilizationReport() {
        reportContent.removeAll();

        DataStore ds = DataStore.getInstance();
        List<Train> trains = ds.getTrains();

        JLabel title = new JLabel("Train Utilization Report");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        reportContent.add(title, BorderLayout.NORTH);

        String[] columns = {"Train ID", "Train Name", "Route", "Total Seats", "Booked Seats",
                            "Available", "Occupancy Rate", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Train t : trains) {
            int booked = t.getTotalSeats() - t.getAvailableSeats();
            String occupancy = String.format("%.1f%%", t.getOccupancyRate());
            model.addRow(new Object[]{
                t.getTrainId(), t.getTrainName(), t.getRoute(),
                t.getTotalSeats(), booked, t.getAvailableSeats(),
                occupancy, t.getStatus()
            });
        }

        JTable table = new JTable(model);
        UIStyle.styleTable(table);
        UIStyle.applyStatusRenderer(table, 7);

        // Summary stats
        double avgOccupancy = ds.getAverageOccupancy();
        JPanel summary = new JPanel(new GridLayout(1, 3, 10, 0));
        summary.setOpaque(false);
        summary.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        summary.add(UIStyle.createStatCard("Total Trains", String.valueOf(trains.size()), UIStyle.PRIMARY));
        summary.add(UIStyle.createStatCard("Avg Occupancy", String.format("%.1f%%", avgOccupancy), UIStyle.ACCENT));

        int highUtil = 0;
        for (Train t : trains) if (t.getOccupancyRate() > 75) highUtil++;
        summary.add(UIStyle.createStatCard("High Utilization (>75%)", String.valueOf(highUtil), UIStyle.DANGER));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(summary, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        reportContent.add(centerPanel, BorderLayout.CENTER);
        reportContent.revalidate();
        reportContent.repaint();
    }

    private void generatePassengerReport() {
        reportContent.removeAll();

        DataStore ds = DataStore.getInstance();
        List<Passenger> passengers = ds.getPassengers();

        JLabel title = new JLabel("Passenger Activity Report");
        title.setFont(UIStyle.FONT_TITLE);
        title.setForeground(UIStyle.PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        reportContent.add(title, BorderLayout.NORTH);

        String[] columns = {"Passenger ID", "Name", "Email", "Phone", "Total Bookings",
                            "Active Bookings", "Total Spent (SAR)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Passenger p : passengers) {
            List<Reservation> bookings = ds.getReservationsByPassenger(p.getPassengerId());
            int totalBookings = bookings.size();
            int active = 0;
            double totalSpent = 0;
            for (Reservation r : bookings) {
                if (r.getStatus() == Reservation.Status.CONFIRMED) {
                    active++;
                    totalSpent += r.getTotalPrice();
                }
            }
            model.addRow(new Object[]{
                p.getPassengerId(), p.getFullName(), p.getEmail(), p.getPhone(),
                totalBookings, active, String.format("%.2f", totalSpent)
            });
        }

        JTable table = new JTable(model);
        UIStyle.styleTable(table);

        JPanel summary = new JPanel(new GridLayout(1, 3, 10, 0));
        summary.setOpaque(false);
        summary.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        summary.add(UIStyle.createStatCard("Total Passengers", String.valueOf(passengers.size()), UIStyle.PRIMARY));
        summary.add(UIStyle.createStatCard("Total Reservations",
            String.valueOf(ds.getReservations().size()), UIStyle.ACCENT));
        summary.add(UIStyle.createStatCard("Revenue",
            String.format("SAR %.0f", ds.getTotalRevenue()), UIStyle.SUCCESS));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(summary, BorderLayout.NORTH);
        centerPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        reportContent.add(centerPanel, BorderLayout.CENTER);
        reportContent.revalidate();
        reportContent.repaint();
    }

    private List<Reservation> filterByDate(List<Reservation> reservations) {
        String from = fromDateField.getText().trim();
        String to = toDateField.getText().trim();

        List<Reservation> filtered = new ArrayList<>();
        for (Reservation r : reservations) {
            String bookDate = r.getBookingDate();
            if (bookDate == null) continue;
            try {
                if (bookDate.compareTo(from) >= 0 && bookDate.compareTo(to) <= 0) {
                    filtered.add(r);
                }
            } catch (Exception e) {
                filtered.add(r); // include if date parsing fails
            }
        }
        return filtered.isEmpty() ? reservations : filtered;
    }

    @Override
    public void refreshData() {
        // Reset content
        reportContent.removeAll();
        JLabel placeholder = new JLabel("Select a report type to generate", SwingConstants.CENTER);
        placeholder.setFont(UIStyle.FONT_SUBTITLE);
        placeholder.setForeground(UIStyle.TEXT_SECONDARY);
        reportContent.add(placeholder, BorderLayout.CENTER);
        reportContent.revalidate();
        reportContent.repaint();
    }
}
