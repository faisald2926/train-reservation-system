package view;

import model.User;
import util.UIStyle;
import util.DataStore;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private List<JButton> navButtons = new ArrayList<>();

    public MainFrame(User user) {
        this.currentUser = user;
        initUI();
    }

    private void initUI() {
        setTitle("Train Schedule & Reservation Management System");
        setSize(1250, 780);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1050, 680));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BG_MAIN);

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

        JButton logoutBtn = UIStyle.createDangerButton("Logout");
        logoutBtn.addActionListener(e -> logout());
        userPanel.add(logoutBtn);

        topBar.add(userPanel, BorderLayout.EAST);

        // === SIDEBAR ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(UIStyle.BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        // === CONTENT ===
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIStyle.BG_MAIN);

        boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;

        if (isAdmin) {
            sidebar.add(UIStyle.createSidebarSectionLabel("Overview"));
            addNavItem(sidebar, "Dashboard", "dashboard", new DashboardPanel());
        }

        sidebar.add(UIStyle.createSidebarSectionLabel("Management"));
        addNavItem(sidebar, "Trains", "trains", new TrainPanel());
        addNavItem(sidebar, "Passengers", "passengers", new PassengerPanel());
        addNavItem(sidebar, "Reservations", "reservations", new ReservationPanel());

        if (isAdmin) {
            sidebar.add(UIStyle.createSidebarSectionLabel("Analytics"));
            addNavItem(sidebar, "Reports", "reports", new ReportsPanel());
        }

        // Push items to top
        Component glue = Box.createVerticalGlue();
        sidebar.add(glue);

        // Select first nav item
        if (!navButtons.isEmpty()) {
            navButtons.get(0).putClientProperty("sidebar.active", true);
            navButtons.get(0).repaint();
        }

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void addNavItem(JPanel sidebar, String name, String iconType, JPanel panel) {
        ImageIcon icon = UIStyle.createNavIcon(iconType);
        JButton btn = UIStyle.createSidebarButton(name, icon);
        btn.addActionListener(e -> {
            for (JButton b : navButtons) {
                b.putClientProperty("sidebar.active", false);
                b.repaint();
            }
            btn.putClientProperty("sidebar.active", true);
            btn.repaint();
            cardLayout.show(contentPanel, name);
            if (panel instanceof Refreshable) {
                ((Refreshable) panel).refreshData();
            }
        });
        navButtons.add(btn);
        sidebar.add(btn);
        contentPanel.add(panel, name);
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
        int size = 26;
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(2, 5, 22, 12, 6, 6);
        g2.setColor(UIStyle.ACCENT);
        g2.fillRoundRect(5, 7, 6, 6, 2, 2);
        g2.fillRoundRect(14, 7, 6, 6, 2, 2);
        g2.setColor(new Color(200, 210, 225));
        g2.fillOval(5, 18, 5, 5);
        g2.fillOval(16, 18, 5, 5);
        g2.dispose();
        return new ImageIcon(img);
    }

    // Interface for refreshable panels
    public interface Refreshable {
        void refreshData();
    }
}
