package librarymanagement.gui;

import librarymanagement.service.*;
import librarymanagement.util.*;
import javax.swing.*;
import java.awt.*;

/**
 * Registration Frame - GUI for new member registration
 */
public class RegistrationFrame extends JFrame {
    
    private final UserService userService;
    private final BookService bookService;
    private final TransactionService transactionService;
    
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backButton;
    
    public RegistrationFrame(UserService userService, BookService bookService,
                            TransactionService transactionService) {
        this.userService = userService;
        this.bookService = bookService;
        this.transactionService = transactionService;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Register");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(46, 204, 113);
                Color color2 = new Color(39, 174, 96);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        
        // Registration panel
        JPanel regPanel = new JPanel();
        regPanel.setBackground(Color.WHITE);
        regPanel.setLayout(new BoxLayout(regPanel, BoxLayout.Y_AXIS));
        regPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Title
        JLabel titleLabel = new JLabel("Member Registration");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        regPanel.add(titleLabel);
        regPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Name field
        addField(regPanel, "Full Name", nameField = new JTextField(20));
        regPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Email field
        addField(regPanel, "Email Address", emailField = new JTextField(20));
        regPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Password field
        addField(regPanel, "Password (min 6 characters)", 
                passwordField = new JPasswordField(20));
        regPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Confirm password field
        addField(regPanel, "Confirm Password", 
                confirmPasswordField = new JPasswordField(20));
        regPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Register button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        registerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> handleRegistration());
        
        // Back button
        backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backButton.setBackground(Color.WHITE);
        backButton.setForeground(new Color(46, 204, 113));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        backButton.setContentAreaFilled(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> backToLogin());
        
        regPanel.add(registerButton);
        regPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        regPanel.add(backButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel.add(regPanel, gbc);
        
        add(mainPanel);
    }
    
    private void addField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(new Color(52, 73, 94));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
    }
    
    private void handleRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                "Passwords do not match",
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
            confirmPasswordField.setText("");
            return;
        }
        
        try {
            String userId = userService.registerMember(name, email, password);
            
            JOptionPane.showMessageDialog(this,
                "Registration successful!\nYour Member ID: " + userId + 
                "\nYou can now login with your email and password.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            backToLogin();
            
        } catch (LibraryException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Registration Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void backToLogin() {
        new LoginFrame(userService, bookService, transactionService).setVisible(true);
        dispose();
    }
}