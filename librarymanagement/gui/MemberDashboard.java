package librarymanagement.gui;

import librarymanagement.model.*;
import librarymanagement.service.*;
import librarymanagement.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Member Dashboard - Main GUI for library members
 */
public class MemberDashboard extends JFrame {
    
    private final User currentUser;
    private final BookService bookService;
    private final TransactionService transactionService;
    
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public MemberDashboard(User user, BookService bookService, 
                          TransactionService transactionService) {
        this.currentUser = user;
        this.bookService = bookService;
        this.transactionService = transactionService;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Library Management System - Member Dashboard");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        mainPanel.add(createSidebar(), BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(236, 240, 241));
        
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(createBrowseBooksPanel(), "browse");
        contentPanel.add(createMyBooksPanel(), "mybooks");
        contentPanel.add(createHistoryPanel(), "history");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(41, 128, 185));
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Welcome, " + currentUser.getName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(41, 128, 185));
        rightPanel.add(userLabel);
        rightPanel.add(logoutButton);
        
        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(rightPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(52, 152, 219));
        sidebar.setPreferredSize(new Dimension(180, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        addMenuItem(sidebar, "Dashboard", "dashboard");
        addMenuItem(sidebar, "Browse Books", "browse");
        addMenuItem(sidebar, "My Books", "mybooks");
        addMenuItem(sidebar, "History", "history");
        
        return sidebar;
    }
    
    private void addMenuItem(JPanel sidebar, String text, String cardName) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(160, 40));
        button.setBackground(new Color(52, 152, 219));
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
            case "browse" -> refreshBrowseTable();
            case "mybooks" -> refreshMyBooksTable();
            case "history" -> refreshHistoryTable();
        }
    }
    
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        Member member = (Member) currentUser;
        int booksIssued = member.getBooksIssued();
        int booksAvailable = 3 - booksIssued;
        int totalAvailable = bookService.getAvailableBooks().size();
        
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setOpaque(false);
        
        statsPanel.add(createStatCard("Books Issued", String.valueOf(booksIssued), 
                new Color(230, 126, 34)));
        statsPanel.add(createStatCard("Can Issue", String.valueOf(booksAvailable), 
                new Color(46, 204, 113)));
        statsPanel.add(createStatCard("Books Available", String.valueOf(totalAvailable), 
                new Color(52, 152, 219)));
        
        GridBagConstraints gbc = new GridBagConstraints();
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
    
    private JTable browseTable;
    private DefaultTableModel browseTableModel;
    
    private JPanel createBrowseBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("Available Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        String[] columns = {"ID", "Title", "Author", "Category", "Available"};
        browseTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        browseTable = new JTable(browseTableModel);
        browseTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(browseTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        JButton issueButton = new JButton("Issue Selected Book");
        issueButton.setBackground(new Color(46, 204, 113));
        issueButton.setForeground(Color.WHITE);
        issueButton.setFocusPainted(false);
        issueButton.addActionListener(e -> issueSelectedBook());
        
        buttonPanel.add(issueButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshBrowseTable();
        
        return panel;
    }
    
    private void refreshBrowseTable() {
        browseTableModel.setRowCount(0);
        List<Book> books = bookService.getAvailableBooks();
        for (Book book : books) {
            browseTableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getAvailableQuantity()
            });
        }
    }
    
    private void issueSelectedBook() {
        int selectedRow = browseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to issue");
            return;
        }
        
        String bookId = (String) browseTable.getValueAt(selectedRow, 0);
        String title = (String) browseTable.getValueAt(selectedRow, 1);
        
        Member member = (Member) currentUser;
        if (!member.canIssueBook()) {
            JOptionPane.showMessageDialog(this,
                "You have reached the maximum book limit (3 books).\nPlease return a book first.",
                "Limit Reached", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Issue book: " + title + "?",
            "Confirm Issue", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String txnId = transactionService.issueBook(bookId, currentUser.getUserId());
                BookTransaction txn = transactionService.getTransactionById(txnId);
                
                JOptionPane.showMessageDialog(this,
                    "Book issued successfully!\n" +
                    "Transaction ID: " + txnId + "\n" +
                    "Due Date: " + txn.getDueDate() + "\n" +
                    "Please return by the due date to avoid fines.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                refreshBrowseTable();
                
            } catch (LibraryException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JTable myBooksTable;
    private DefaultTableModel myBooksTableModel;
    
    private JPanel createMyBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("My Issued Books");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        String[] columns = {"Transaction ID", "Book Title", "Issue Date", "Due Date", "Status"};
        myBooksTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        myBooksTable = new JTable(myBooksTableModel);
        myBooksTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        myBooksTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(myBooksTable);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(236, 240, 241));
        
        JButton returnButton = new JButton("Return Selected Book");
        returnButton.setBackground(new Color(230, 126, 34));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.addActionListener(e -> returnSelectedBook());
        
        buttonPanel.add(returnButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        refreshMyBooksTable();
        
        return panel;
    }
    
    private void refreshMyBooksTable() {
        myBooksTableModel.setRowCount(0);
        List<BookTransaction> transactions = transactionService
                .getActiveTransactionsByUser(currentUser.getUserId());
        
        for (BookTransaction txn : transactions) {
            try {
                Book book = bookService.getBookById(txn.getBookId());
                String status = txn.isOverdue() ? "OVERDUE" : "Active";
                
                myBooksTableModel.addRow(new Object[]{
                    txn.getTransactionId(),
                    book.getTitle(),
                    txn.getIssueDate(),
                    txn.getDueDate(),
                    status
                });
            } catch (LibraryException e) {
                // Skip if book not found
            }
        }
    }
    
    private void returnSelectedBook() {
        int selectedRow = myBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }
        
        String txnId = (String) myBooksTable.getValueAt(selectedRow, 0);
        String title = (String) myBooksTable.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Return book: " + title + "?",
            "Confirm Return", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                double fine = transactionService.returnBook(txnId);
                
                String message = "Book returned successfully!";
                if (fine > 0) {
                    message += "\n\nLate Return Fine: ₹" + String.format("%.2f", fine) +
                              "\nPlease pay at the counter.";
                } else {
                    message += "\nNo fine. Thank you for returning on time!";
                }
                
                JOptionPane.showMessageDialog(this, message, 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                refreshMyBooksTable();
                
            } catch (LibraryException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private JTable historyTable;
    private DefaultTableModel historyTableModel;
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(236, 240, 241));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titleLabel = new JLabel("Transaction History");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        
        String[] columns = {"Transaction ID", "Book Title", "Issue Date", "Return Date", "Fine"};
        historyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        historyTable = new JTable(historyTableModel);
        historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        historyTable.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        refreshHistoryTable();
        
        return panel;
    }
    
    private void refreshHistoryTable() {
        historyTableModel.setRowCount(0);
        List<BookTransaction> transactions = transactionService
                .getTransactionHistoryByUser(currentUser.getUserId());
        
        for (BookTransaction txn : transactions) {
            try {
                Book book = bookService.getBookById(txn.getBookId());
                String returnDate = txn.getReturnDate() != null ? 
                        txn.getReturnDate().toString() : "Not Returned";
                String fine = txn.getFine() > 0 ? "₹" + String.format("%.2f", txn.getFine()) : "-";
                
                historyTableModel.addRow(new Object[]{
                    txn.getTransactionId(),
                    book.getTitle(),
                    txn.getIssueDate(),
                    returnDate,
                    fine
                });
            } catch (LibraryException e) {
                // Skip if book not found
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame(null, bookService, transactionService).setVisible(true);
            dispose();
        }
    }
}