package view;

import controller.AuthController;
import model.User;
import util.UIStyle;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private AuthController authController;

    public LoginFrame() {
        authController = new AuthController();
        initUI();
    }

    private void initUI() {
        setTitle("Train Reservation System - Login");
        setSize(480, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BG_MAIN);

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(UIStyle.PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 80));
        headerPanel.setLayout(new GridBagLayout());

        JLabel headerTitle = new JLabel("Train Reservation System");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle);

        // Login Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_MAIN);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel loginTitle = new JLabel("Sign In");
        loginTitle.setFont(UIStyle.FONT_TITLE);
        loginTitle.setForeground(UIStyle.PRIMARY);
        loginTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(loginTitle, gbc);

        JLabel subLabel = new JLabel("Enter your credentials to access the system");
        subLabel.setFont(UIStyle.FONT_SMALL);
        subLabel.setForeground(UIStyle.TEXT_SECONDARY);
        subLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        formPanel.add(subLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 2; gbc.gridx = 0;
        formPanel.add(UIStyle.createLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = UIStyle.createTextField();
        usernameField.setPreferredSize(new Dimension(240, 36));
        formPanel.add(usernameField, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        formPanel.add(UIStyle.createLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = UIStyle.createPasswordField();
        passwordField.setPreferredSize(new Dimension(240, 36));
        formPanel.add(passwordField, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        JButton loginBtn = UIStyle.createPrimaryButton("Login");
        loginBtn.setPreferredSize(new Dimension(240, 40));
        formPanel.add(loginBtn, gbc);

        // Hint label
        gbc.gridy = 5;
        gbc.insets = new Insets(15, 8, 8, 8);
        JLabel hintLabel = new JLabel("<html><center>Default accounts:<br/>Admin: admin / admin123<br/>Staff: staff / staff123</center></html>");
        hintLabel.setFont(UIStyle.FONT_SMALL);
        hintLabel.setForeground(UIStyle.TEXT_SECONDARY);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(hintLabel, gbc);

        // Action
        loginBtn.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        User user = authController.login(username, password);

        if (user != null) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame(user);
                mainFrame.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid username or password.",
                "Login Failed", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
}
