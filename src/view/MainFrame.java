package view;

import model.User;
import util.UIStyle;
import util.DataStore;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;
    private JTabbedPane tabbedPane;

    public MainFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Train Schedule & Reservation Management System");
        setSize(1200, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 650));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // === TOP BAR ===
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(UIStyle.PRIMARY);
        topBar.setPreferredSize(new Dimension(0, 55));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel titleLabel = new JLabel("  Train Reservation System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIcon(createTrainIcon());
        topBar.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        userPanel.setOpaque(false);

        JLabel userLabel = new JLabel("Welcome, " + currentUser.getFullName()
                + "  |  Role: " + currentUser.getRole());
        userLabel.setFont(UIStyle.FONT_SMALL);
        userLabel.setForeground(new Color(200, 215, 240));
        userPanel.add(userLabel);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(UIStyle.FONT_BUTTON);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(UIStyle.DANGER);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> logout());
        userPanel.add(logoutBtn);

        topBar.add(userPanel, BorderLayout.EAST);

        // === TABBED PANE ===
        tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabbedPane.setBackground(UIStyle.BG_MAIN);

        tabbedPane.addTab("  Dashboard  ", new DashboardPanel());
        tabbedPane.addTab("  Trains  ", new TrainPanel());
        tabbedPane.addTab("  Passengers  ", new PassengerPanel());
        tabbedPane.addTab("  Reservations  ", new ReservationPanel());
        tabbedPane.addTab("  Reports  ", new ReportsPanel());

        // Refresh panels on tab change
        tabbedPane.addChangeListener(e -> {
            Component selected = tabbedPane.getSelectedComponent();
            if (selected instanceof Refreshable) {
                ((Refreshable) selected).refreshData();
            }
        });

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            DataStore.getInstance().setCurrentUser(null);
            dispose();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
        }
    }

    private ImageIcon createTrainIcon() {
        // Create a simple colored square as icon placeholder
        int size = 24;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255, 255, 255));
        g2.fillRoundRect(2, 6, 20, 14, 5, 5);
        g2.setColor(UIStyle.ACCENT);
        g2.fillRect(4, 8, 5, 10);
        g2.fillRect(11, 8, 5, 10);
        g2.setColor(new Color(200, 200, 200));
        g2.fillOval(4, 18, 5, 5);
        g2.fillOval(15, 18, 5, 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    // Interface for refreshable panels
    public interface Refreshable {
        void refreshData();
    }
}
