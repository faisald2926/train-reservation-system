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
        setSize(480, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(420, 460));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIStyle.BG_MAIN);

        // Header
        JPanel headerPanel = new JPanel(new GridBagLayout());
        headerPanel.setBackground(UIStyle.PRIMARY);
        headerPanel.setPreferredSize(new Dimension(0, 85));

        JPanel headerContent = new JPanel();
        headerContent.setOpaque(false);
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));

        JLabel headerTitle = new JLabel("Train Reservation System");
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 21));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContent.add(headerTitle);
        headerContent.add(Box.createVerticalStrut(4));

        JLabel headerSub = new JLabel("Schedule & Booking Management");
        headerSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        headerSub.setForeground(new Color(180, 200, 230));
        headerSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerContent.add(headerSub);

        headerPanel.add(headerContent);

        // Login Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_MAIN);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 50, 20, 50));

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
        loginBtn.setPreferredSize(new Dimension(240, 42));
        formPanel.add(loginBtn, gbc);

        gbc.gridy = 5;
        gbc.insets = new Insets(8, 8, 8, 8);
        JButton registerBtn = UIStyle.createAccentButton("Create Account");
        registerBtn.setPreferredSize(new Dimension(240, 38));
        formPanel.add(registerBtn, gbc);

        // Hint label
        gbc.gridy = 6;
        gbc.insets = new Insets(12, 8, 8, 8);
        JLabel hintLabel = new JLabel("<html><center>Default accounts:<br/>Admin: admin / admin123 &nbsp; | &nbsp; Staff: staff / staff123</center></html>");
        hintLabel.setFont(UIStyle.FONT_SMALL);
        hintLabel.setForeground(UIStyle.TEXT_SECONDARY);
        hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(hintLabel, gbc);

        // Actions
        loginBtn.addActionListener(e -> performLogin());
        registerBtn.addActionListener(e -> openRegistrationDialog());
        passwordField.addActionListener(e -> performLogin());
        usernameField.addActionListener(e -> performLogin());

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footer.setBackground(UIStyle.BG_MAIN);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel versionLabel = new JLabel("v1.0  |  CS1350 Software Engineering");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(new Color(170, 180, 190));
        footer.add(versionLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);
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

    private void openRegistrationDialog() {
        JDialog dialog = new JDialog(this, "Create Customer Account", true);
        dialog.setSize(460, 520);
        dialog.setLocationRelativeTo(this);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIStyle.BG_MAIN);

        JPanel header = new JPanel(new GridBagLayout());
        header.setBackground(UIStyle.PRIMARY);
        header.setPreferredSize(new Dimension(0, 60));
        JLabel headerLabel = new JLabel("Create Your Account");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        headerLabel.setForeground(Color.WHITE);
        header.add(headerLabel);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyle.BG_MAIN);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        JTextField nameField = UIStyle.createTextField();
        JTextField unameField = UIStyle.createTextField();
        JPasswordField pwField = UIStyle.createPasswordField();
        JTextField emailField = UIStyle.createTextField();
        JTextField phoneField = UIStyle.createTextField();
        JTextField nidField = UIStyle.createTextField();

        String[] labels = {"Full Name:", "Username:", "Password:", "Email:", "Phone:", "National ID:"};
        JComponent[] fields = {nameField, unameField, pwField, emailField, phoneField, nidField};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(UIStyle.createLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1.0;
            form.add(fields[i], gbc);
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setBackground(UIStyle.BG_MAIN);
        JButton cancelBtn = UIStyle.createDangerButton("Cancel");
        JButton submitBtn = UIStyle.createAccentButton("Create Account");
        buttons.add(cancelBtn);
        buttons.add(submitBtn);

        content.add(header, BorderLayout.NORTH);
        content.add(form, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);
        dialog.setContentPane(content);

        cancelBtn.addActionListener(e -> dialog.dispose());
        submitBtn.addActionListener(e -> {
            String error = authController.register(
                unameField.getText(),
                new String(pwField.getPassword()),
                nameField.getText(),
                emailField.getText(),
                phoneField.getText(),
                nidField.getText()
            );
            if (error != null) {
                JOptionPane.showMessageDialog(dialog, error, "Registration Failed", JOptionPane.ERROR_MESSAGE);
                return;
            }
            JOptionPane.showMessageDialog(dialog,
                "Account created! You can now log in.",
                "Welcome", JOptionPane.INFORMATION_MESSAGE);
            usernameField.setText(unameField.getText().trim());
            passwordField.setText(new String(pwField.getPassword()));
            dialog.dispose();
            performLogin();
        });

        dialog.setVisible(true);
    }
}
