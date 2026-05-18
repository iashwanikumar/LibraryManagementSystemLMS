package librarymanagement.gui;

import librarymanagement.model.*;
import librarymanagement.service.*;
import librarymanagement.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Admin Dashboard - Main GUI for administrators
 */
public class AdminDashboard extends JFrame {
    
    private final User currentUser;
    private final BookService bookService;
    private final UserService userService;
    private final TransactionService transactionService;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public AdminDashboard(User user, BookService bookService, 
                         UserService userService, TransactionService transactionService) {
        this.currentUser = user;
        this.bookService = bookService;
        this.userService = userService;
        this.transactionService = transactionService;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Admin Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Top bar
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        
        // Sidebar
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        
        // Content area with CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(236, 240, 241));
        
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createBooksPanel(), "books");
        contentPanel.add(createAddBookPanel(), "addBook");
        contentPanel.add(createMembersPanel(), "members");
        contentPanel.add(createTransactionsPanel(), "transactions");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(44, 62, 80));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Admin: " + currentUser.getName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(189, 195, 199));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(44, 62, 80));
        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);
        
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        addMenuItem(sidebar, "Dashboard", "dashboard");
        addMenuItem(sidebar, "All Books", "books");
        addMenuItem(sidebar, "Add Book", "addBook");
        addMenuItem(sidebar, "Members", "members");
        addMenuItem(sidebar, "Transactions", "transactions");
        
        return sidebar;
    }
    
    private void addMenuItem(JPanel sidebar, String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.setBackground(new Color(52, 73, 94));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            refreshCurrentPanel(cardName);
        });
        
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private void refreshCurrentPanel(String cardName) {
        switch (cardName) {
            case "books" -> refreshBooksTable();
            case "members" -> refreshMembersTable();
            case "transactions" -> refreshTransactionsTable();
        }
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setOpaque(false);
        
        int totalBooks = bookService.getAllBooks().size();
        int availableBooks = bookService.getAvailableBooks().size();
        int totalMembers = userService.getAllMembers().size();
        int activeTransactions = transactionService.getAllActiveTransactions().size();
        
        statsPanel.add(createStatCard("Total Books", String.valueOf(totalBooks), 
                new Color(52, 152, 219)));
        statsPanel.add(createStatCard("Available Books", String.valueOf(availableBooks), 
                new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Total Members", String.valueOf(totalMembers), 
                new Color(155, 89, 182)));
        statsPanel.add(createStatCard("Active Issues", String.valueOf(activeTransactions), 
                new Color(230, 126, 34)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(statsPanel, gbc);
        
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(new Color(127, 140, 141));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        card.add(valueLabel);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLabel);
        
        return card;
    }
    
    private JTable booksTable;
    private DefaultTableModel booksTableModel;
    
    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("All Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 73, 94));
        
        String[] columns = {"ID", "Title", "Author", "Category", "Total", "Available"};
        booksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        booksTable = new JTable(booksTableModel);
        booksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        booksTable.setRowHeight(25);
        booksTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(booksTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteSelectedBook());
        
        buttonPanel.add(deleteButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshBooksTable();
        
        return panel;
    }
    
    private void refreshBooksTable() {
        booksTableModel.setRowCount(0);
        List<Book> books = bookService.getAllBooks();
        for (Book book : books) {
            booksTableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalQuantity(),
                book.getAvailableQuantity()
            });
        }
    }
    
    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete");
            return;
        }
        
        String bookId = (String) booksTable.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this book?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                bookService.deleteBook(bookId);
                JOptionPane.showMessageDialog(this, "Book deleted successfully");
                refreshBooksTable();
            } catch (LibraryException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JPanel createAddBookPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JLabel titleLabel = new JLabel("Add New Book");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JTextField titleField = createFormField(formPanel, "Title");
        JTextField authorField = createFormField(formPanel, "Author");
        JTextField publisherField = createFormField(formPanel, "Publisher");
        JTextField categoryField = createFormField(formPanel, "Category");
        JTextField isbnField = createFormField(formPanel, "ISBN");
        JTextField quantityField = createFormField(formPanel, "Quantity");
        
        JButton addButton = new JButton("Add Book");
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addButton.setBackground(new Color(52, 152, 219));
        addButton.setForeground(Color.WHITE);
        addButton.setFocusPainted(false);
        addButton.setBorderPainted(false);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        addButton.addActionListener(e -> {
            try {
                String bookId = bookService.addBook(
                    titleField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    categoryField.getText(),
                    isbnField.getText(),
                    Integer.parseInt(quantityField.getText())
                );
                JOptionPane.showMessageDialog(this, 
                    "Book added successfully!\nBook ID: " + bookId);
                titleField.setText("");
                authorField.setText("");
                publisherField.setText("");
                categoryField.setText("");
                isbnField.setText("");
                quantityField.setText("");
            } catch (LibraryException | NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(addButton);
        
        GridBagConstraints gbc = new GridBagConstraints();
        panel.add(formPanel, gbc);
        
        return panel;
    }
    
    private JTextField createFormField(JPanel panel, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return field;
    }
    
    private JTable membersTable;
    private DefaultTableModel membersTableModel;
    
    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("All Members");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        String[] columns = {"Member ID", "Name", "Email"};
        membersTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        membersTable = new JTable(membersTableModel);
        membersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        membersTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(membersTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshMembersTable();
        
        return panel;
    }
    
    private void refreshMembersTable() {
        membersTableModel.setRowCount(0);
        List<User> members = userService.getAllMembers();
        for (User member : members) {
            membersTableModel.addRow(new Object[]{
                member.getUserId(),
                member.getName(),
                member.getEmail()
            });
        }
    }
    
    private JTable transactionsTable;
    private DefaultTableModel transactionsTableModel;
    
    private JPanel createTransactionsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("All Transactions");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        String[] columns = {"Transaction ID", "Book ID", "User ID", "Issue Date", "Status"};
        transactionsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        transactionsTable = new JTable(transactionsTableModel);
        transactionsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        transactionsTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshTransactionsTable();
        
        return panel;
    }
    
    private void refreshTransactionsTable() {
        transactionsTableModel.setRowCount(0);
        List<BookTransaction> transactions = transactionService.getAllTransactions();
        for (BookTransaction txn : transactions) {
            transactionsTableModel.addRow(new Object[]{
                txn.getTransactionId(),
                txn.getBookId(),
                txn.getUserId(),
                txn.getIssueDate(),
                txn.getStatus()
            });
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame(userService, bookService, transactionService).setVisible(true);
            dispose();
        }
    }
}