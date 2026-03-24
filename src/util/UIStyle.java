package util;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class UIStyle {
    // ===== Color Palette =====
    public static final Color PRIMARY = new Color(25, 55, 109);
    public static final Color PRIMARY_LIGHT = new Color(42, 87, 154);
    public static final Color ACCENT = new Color(0, 150, 136);
    public static final Color ACCENT_HOVER = new Color(0, 121, 107);
    public static final Color BG_MAIN = new Color(240, 243, 247);
    public static final Color BG_CARD = Color.WHITE;
    public static final Color BG_SIDEBAR = new Color(20, 44, 84);
    public static final Color TEXT_PRIMARY = new Color(33, 37, 41);
    public static final Color TEXT_SECONDARY = new Color(108, 117, 125);
    public static final Color BORDER_COLOR = new Color(222, 226, 230);
    public static final Color SUCCESS = new Color(40, 167, 69);
    public static final Color DANGER = new Color(220, 53, 69);
    public static final Color WARNING = new Color(255, 193, 7);
    public static final Color INFO = new Color(23, 162, 184);

    // ===== Fonts =====
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_TABLE_HEADER = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SIDEBAR = new Font("Segoe UI", Font.PLAIN, 14);

    // ===== Buttons (custom-painted, rounded, with hover/press effects) =====

    public static JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color c = bg;
                if (!isEnabled()) c = new Color(200, 200, 200);
                else if (getModel().isPressed()) c = bg.darker();
                else if (getModel().isRollover()) c = brighten(bg, 0.12f);
                g2.setColor(c);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMargin(new Insets(8, 20, 8, 20));
        return btn;
    }

    public static JButton createPrimaryButton(String text) { return createButton(text, PRIMARY); }
    public static JButton createAccentButton(String text) { return createButton(text, ACCENT); }
    public static JButton createDangerButton(String text) { return createButton(text, DANGER); }
    public static JButton createWarningButton(String text) { return createButton(text, new Color(230, 160, 0)); }

    // ===== Sidebar Navigation =====

    public static JButton createSidebarButton(String text, ImageIcon icon) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean active = Boolean.TRUE.equals(getClientProperty("sidebar.active"));
                if (active) {
                    g2.setColor(new Color(255, 255, 255, 15));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    g2.setColor(ACCENT);
                    g2.fillRoundRect(0, 6, 4, getHeight() - 12, 4, 4);
                    setForeground(Color.WHITE);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 8));
                    g2.fillRoundRect(8, 2, getWidth() - 16, getHeight() - 4, 8, 8);
                    setForeground(new Color(210, 220, 235));
                } else {
                    setForeground(new Color(160, 175, 200));
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        if (icon != null) btn.setIcon(icon);
        btn.setIconTextGap(12);
        btn.setFont(FONT_SIDEBAR);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 24, 0, 20));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        return btn;
    }

    public static JLabel createSidebarSectionLabel(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("Segoe UI", Font.BOLD, 10));
        label.setForeground(new Color(100, 120, 155));
        label.setBorder(BorderFactory.createEmptyBorder(18, 28, 6, 20));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        return label;
    }

    public static ImageIcon createNavIcon(String type) {
        int size = 16;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(160, 175, 200));
        switch (type) {
            case "dashboard":
                g2.fillRoundRect(0, 0, 7, 7, 2, 2);
                g2.fillRoundRect(9, 0, 7, 7, 2, 2);
                g2.fillRoundRect(0, 9, 7, 7, 2, 2);
                g2.fillRoundRect(9, 9, 7, 7, 2, 2);
                break;
            case "trains":
                g2.fillRoundRect(0, 3, 16, 8, 4, 4);
                g2.fillOval(2, 12, 4, 4);
                g2.fillOval(10, 12, 4, 4);
                break;
            case "passengers":
                g2.fillOval(4, 0, 8, 8);
                g2.fillRoundRect(2, 9, 12, 7, 6, 6);
                break;
            case "reservations":
                g2.fillRoundRect(1, 0, 14, 16, 3, 3);
                g2.setColor(BG_SIDEBAR);
                g2.fillRect(4, 4, 8, 2);
                g2.fillRect(4, 8, 8, 2);
                g2.fillRect(4, 12, 5, 2);
                break;
            case "reports":
                g2.fillRect(0, 10, 4, 6);
                g2.fillRect(6, 5, 4, 11);
                g2.fillRect(12, 0, 4, 16);
                break;
        }
        g2.dispose();
        return new ImageIcon(img);
    }

    // ===== Text Fields (with focus highlight) =====

    public static JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(220, 36));
        applyFieldBorder(field, false);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { applyFieldBorder(field, true); }
            public void focusLost(FocusEvent e) { applyFieldBorder(field, false); }
        });
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(220, 36));
        applyFieldBorder(field, false);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { applyFieldBorder(field, true); }
            public void focusLost(FocusEvent e) { applyFieldBorder(field, false); }
        });
        return field;
    }

    public static JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(TEXT_SECONDARY);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(placeholder, ins.left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            }
        };
        field.setFont(FONT_BODY);
        field.setPreferredSize(new Dimension(280, 36));
        applyFieldBorder(field, false);
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { applyFieldBorder(field, true); field.repaint(); }
            public void focusLost(FocusEvent e) { applyFieldBorder(field, false); field.repaint(); }
        });
        return field;
    }

    private static void applyFieldBorder(JComponent field, boolean focused) {
        if (focused) {
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT, 2),
                BorderFactory.createEmptyBorder(3, 7, 3, 7)
            ));
        } else {
            field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)
            ));
        }
    }

    // ===== Labels =====

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_BODY);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(PRIMARY);
        return label;
    }

    // ===== Combo Box =====

    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setFont(FONT_BODY);
        combo.setPreferredSize(new Dimension(220, 36));
        combo.setBackground(Color.WHITE);
        return combo;
    }

    // ===== Cards (with subtle shadow) =====

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 1, new Color(0, 0, 0, 15)),
                BorderFactory.createLineBorder(new Color(228, 231, 236))
            ),
            BorderFactory.createEmptyBorder(18, 20, 18, 20)
        ));
        return card;
    }

    // ===== Tables =====

    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(36);
        table.setGridColor(new Color(245, 245, 245));
        table.setSelectionBackground(new Color(210, 225, 245));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setShowGrid(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Custom header renderer for consistent styling
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                label.setBackground(PRIMARY);
                label.setForeground(Color.WHITE);
                label.setFont(FONT_TABLE_HEADER);
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 0, 1, PRIMARY_LIGHT),
                    BorderFactory.createEmptyBorder(0, 10, 0, 10)
                ));
                label.setOpaque(true);
                label.setPreferredSize(new Dimension(label.getPreferredSize().width, 40));
                return label;
            }
        });

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    public static void applyStatusRenderer(JTable table, int statusColumn) {
        if (statusColumn >= table.getColumnCount()) return;
        table.getColumnModel().getColumn(statusColumn).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(tbl, value, isSelected, hasFocus, row, column);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected && value != null) {
                    String s = value.toString().toUpperCase();
                    if (s.contains("CONFIRMED") || s.contains("ACTIVE")) {
                        setForeground(SUCCESS);
                    } else if (s.contains("CANCELLED")) {
                        setForeground(DANGER);
                    } else if (s.contains("PENDING")) {
                        setForeground(WARNING.darker());
                    } else if (s.contains("COMPLETED")) {
                        setForeground(new Color(156, 39, 176));
                    } else {
                        setForeground(TEXT_PRIMARY);
                    }
                }
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 252));
                }
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
    }

    // ===== Search / Filter for tables =====

    public static JTextField addTableSearch(JTable table, DefaultTableModel model) {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        JTextField searchField = createSearchField("Search...");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + java.util.regex.Pattern.quote(text)));
                }
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });
        return searchField;
    }

    // ===== Stat Cards (with colored top accent) =====

    public static JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(4, 0, 0, 0, color),
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 1, 1, new Color(228, 231, 236)),
                BorderFactory.createEmptyBorder(14, 20, 14, 20)
            )
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FONT_SMALL);
        titleLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        valueLabel.setForeground(color);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // ===== Toast Notifications =====

    public static void showToast(Component parent, String message, Color bgColor) {
        Window window = SwingUtilities.getWindowAncestor(parent);
        if (window == null && parent instanceof Window) window = (Window) parent;
        if (window == null) return;

        JWindow toast = new JWindow(window);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 22, 10, 22)
        ));

        JLabel label = new JLabel(message);
        label.setFont(FONT_BODY);
        label.setForeground(Color.WHITE);
        panel.add(label);

        toast.setContentPane(panel);
        toast.pack();

        final Window anchor = window;
        int x = anchor.getX() + anchor.getWidth() - toast.getWidth() - 25;
        int y = anchor.getY() + anchor.getHeight() - toast.getHeight() - 45;
        toast.setLocation(x, y);
        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        Timer timer = new Timer(2500, e -> {
            toast.setVisible(false);
            toast.dispose();
        });
        timer.setRepeats(false);
        timer.start();
    }

    // ===== Utility =====

    private static Color brighten(Color c, float factor) {
        int r = Math.min(255, (int)(c.getRed() + 255 * factor));
        int g = Math.min(255, (int)(c.getGreen() + 255 * factor));
        int b = Math.min(255, (int)(c.getBlue() + 255 * factor));
        return new Color(r, g, b);
    }
}
