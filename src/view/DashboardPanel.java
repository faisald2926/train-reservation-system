package view;

import model.*;
import util.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel implements MainFrame.Refreshable {
    private JPanel statsPanel;
    private JPanel chartPanel;

    public DashboardPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(UIStyle.BG_MAIN);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        buildUI();
    }

    private void buildUI() {
        removeAll();

        DataStore ds = DataStore.getInstance();

        // Title
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setOpaque(false);
        JLabel title = UIStyle.createTitleLabel("Dashboard Overview");
        titlePanel.add(title);
        add(titlePanel, BorderLayout.NORTH);

        // Stats Cards
        statsPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        statsPanel.setOpaque(false);

        statsPanel.add(UIStyle.createStatCard("Total Trains",
            String.valueOf(ds.getTrains().size()), UIStyle.PRIMARY));
        statsPanel.add(UIStyle.createStatCard("Active Trains",
            String.valueOf(ds.getTotalActiveTrains()), UIStyle.ACCENT));
        statsPanel.add(UIStyle.createStatCard("Total Passengers",
            String.valueOf(ds.getPassengers().size()), UIStyle.INFO));
        statsPanel.add(UIStyle.createStatCard("Active Bookings",
            String.valueOf(ds.getTotalConfirmedReservations()), UIStyle.SUCCESS));
        statsPanel.add(UIStyle.createStatCard("Total Revenue",
            String.format("SAR %.0f", ds.getTotalRevenue()), new Color(156, 39, 176)));

        JPanel topSection = new JPanel(new BorderLayout(0, 15));
        topSection.setOpaque(false);
        topSection.add(statsPanel, BorderLayout.NORTH);

        // Charts Area
        JPanel chartsArea = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsArea.setOpaque(false);

        chartsArea.add(createOccupancyChart());
        chartsArea.add(createReservationSummary());

        topSection.add(chartsArea, BorderLayout.CENTER);
        add(topSection, BorderLayout.CENTER);

        // Recent Activity
        add(createRecentActivity(), BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    private JPanel createOccupancyChart() {
        JPanel card = UIStyle.createCard();
        card.setLayout(new BorderLayout(5, 10));

        JLabel title = new JLabel("Train Occupancy Rates");
        title.setFont(UIStyle.FONT_SUBTITLE);
        title.setForeground(UIStyle.PRIMARY);
        card.add(title, BorderLayout.NORTH);

        // Custom bar chart panel
        JPanel barChart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                List<Train> trains = DataStore.getInstance().getTrains();
                if (trains.isEmpty()) {
                    g2.setFont(UIStyle.FONT_BODY);
                    g2.setColor(UIStyle.TEXT_SECONDARY);
                    g2.drawString("No trains to display", getWidth() / 2 - 60, getHeight() / 2);
                    return;
                }

                int maxBars = Math.min(trains.size(), 8);
                int barWidth = Math.max(30, (getWidth() - 80) / maxBars - 10);
                int maxHeight = getHeight() - 60;
                int x = 40;

                for (int i = 0; i < maxBars; i++) {
                    Train t = trains.get(i);
                    double rate = t.getOccupancyRate();
                    int barHeight = (int) (rate / 100.0 * maxHeight);

                    // Bar color based on occupancy
                    Color barColor;
                    if (rate > 80) barColor = UIStyle.DANGER;
                    else if (rate > 50) barColor = UIStyle.WARNING;
                    else barColor = UIStyle.ACCENT;

                    g2.setColor(barColor);
                    g2.fillRoundRect(x, getHeight() - 30 - barHeight, barWidth, barHeight, 5, 5);

                    // Label
                    g2.setColor(UIStyle.TEXT_PRIMARY);
                    g2.setFont(UIStyle.FONT_SMALL);
                    String label = t.getTrainId().replace("TRN-", "T");
                    g2.drawString(label, x + barWidth / 2 - 10, getHeight() - 12);

                    // Percentage
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    g2.drawString(String.format("%.0f%%", rate), x + barWidth / 2 - 12, getHeight() - 35 - barHeight);

                    x += barWidth + 10;
                }
            }
        };
        barChart.setBackground(Color.WHITE);
        barChart.setPreferredSize(new Dimension(0, 200));
        card.add(barChart, BorderLayout.CENTER);
        return card;
    }

    private JPanel createReservationSummary() {
        JPanel card = UIStyle.createCard();
        card.setLayout(new BorderLayout(5, 10));

        JLabel title = new JLabel("Reservation Summary");
        title.setFont(UIStyle.FONT_SUBTITLE);
        title.setForeground(UIStyle.PRIMARY);
        card.add(title, BorderLayout.NORTH);

        DataStore ds = DataStore.getInstance();
        List<Reservation> reservations = ds.getReservations();
        int confirmed = 0, cancelled = 0, pending = 0;
        for (Reservation r : reservations) {
            switch (r.getStatus()) {
                case CONFIRMED: confirmed++; break;
                case CANCELLED: cancelled++; break;
                case PENDING: pending++; break;
            }
        }

        // Pie chart simulation
        final int fc = confirmed, fcc = cancelled, fp = pending;
        JPanel piePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int total = fc + fcc + fp;
                if (total == 0) {
                    g2.setFont(UIStyle.FONT_BODY);
                    g2.setColor(UIStyle.TEXT_SECONDARY);
                    g2.drawString("No reservations yet", getWidth() / 2 - 60, getHeight() / 2);
                    return;
                }

                int size = Math.min(getWidth(), getHeight()) - 60;
                int cx = getWidth() / 2 - size / 2 - 40;
                int cy = (getHeight() - size) / 2;

                int startAngle = 0;
                Color[] colors = {UIStyle.SUCCESS, UIStyle.DANGER, UIStyle.WARNING};
                int[] values = {fc, fcc, fp};
                String[] labels = {"Confirmed", "Cancelled", "Pending"};

                for (int i = 0; i < 3; i++) {
                    if (values[i] > 0) {
                        int angle = (int) Math.round(360.0 * values[i] / total);
                        if (i == 2) angle = 360 - startAngle; // ensure full circle
                        g2.setColor(colors[i]);
                        g2.fillArc(cx, cy, size, size, startAngle, angle);
                        startAngle += angle;
                    }
                }

                // Legend
                int lx = cx + size + 20;
                int ly = cy + 30;
                g2.setFont(UIStyle.FONT_SMALL);
                for (int i = 0; i < 3; i++) {
                    g2.setColor(colors[i]);
                    g2.fillRect(lx, ly, 14, 14);
                    g2.setColor(UIStyle.TEXT_PRIMARY);
                    g2.drawString(labels[i] + ": " + values[i], lx + 20, ly + 12);
                    ly += 25;
                }
            }
        };
        piePanel.setBackground(Color.WHITE);
        card.add(piePanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createRecentActivity() {
        JPanel card = UIStyle.createCard();
        card.setLayout(new BorderLayout(5, 5));
        card.setPreferredSize(new Dimension(0, 160));

        JLabel title = new JLabel("Recent Reservations");
        title.setFont(UIStyle.FONT_SUBTITLE);
        title.setForeground(UIStyle.PRIMARY);
        card.add(title, BorderLayout.NORTH);

        List<Reservation> reservations = DataStore.getInstance().getReservations();
        String[] columns = {"Reservation ID", "Passenger", "Train", "Date", "Seats", "Price", "Status"};
        int rows = Math.min(reservations.size(), 5);
        Object[][] data = new Object[rows][7];

        int start = Math.max(0, reservations.size() - 5);
        for (int i = 0; i < rows; i++) {
            Reservation r = reservations.get(start + i);
            data[i] = new Object[]{r.getReservationId(), r.getPassengerName(), r.getTrainName(),
                r.getTravelDate(), r.getSeatCount(), String.format("SAR %.2f", r.getTotalPrice()),
                r.getStatus().toString()};
        }

        JTable table = new JTable(data, columns);
        UIStyle.styleTable(table);
        table.setEnabled(false);

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        card.add(sp, BorderLayout.CENTER);
        return card;
    }

    @Override
    public void refreshData() {
        buildUI();
    }
}
