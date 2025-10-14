/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.ui;

import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.domain.Loan;
import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.infra.config.ApplicationContext;
import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.service.LoanService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Coder
 */
public class DashboardUser extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardUser.class.getName());

    private final User currentUser;
    private final BookService bookService;
    private final LoanService loanService;

    // Componentes UI
    private JTabbedPane tabbedPane;
    private JTable availableBooksTable;
    private JTable myLoansTable;
    private JTable loanHistoryTable;
    private DefaultTableModel availableBooksModel;
    private DefaultTableModel myLoansModel;
    private DefaultTableModel loanHistoryModel;

    public DashboardUser(User user) {
        this.currentUser = user;
        ApplicationContext context = ApplicationContext.getInstance();
        this.bookService = context.getBookService();
        this.loanService = context.getLoanService();
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("LibroNova - Panel de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setBackground(new Color(70, 130, 180));

        JLabel welcomeLabel = new JLabel("Bienvenido, " + currentUser.getNombreCompleto());
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JPanel rightPanel = new JPanel(new FlowLayout());
        rightPanel.setOpaque(false);
        
        // Mostrar cantidad de préstamos activos
        int activeLoansCount = loanService.getActiveLoansCountByUser(currentUser.getId());
        JLabel loansCountLabel = new JLabel("Préstamos activos: " + activeLoansCount + "/3");
        loansCountLabel.setForeground(Color.WHITE);
        loansCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(loansCountLabel);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> logout());
        rightPanel.add(logoutButton);

        headerPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Libros Disponibles", createAvailableBooksPanel());
        tabbedPane.addTab("Mis Préstamos", createMyLoansPanel());
        tabbedPane.addTab("Historial", createLoanHistoryPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createAvailableBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchTitleButton = new JButton("Buscar por Título");
        JButton searchAuthorButton = new JButton("Buscar por Autor");
        JButton showAllButton = new JButton("Mostrar Todos");
        
        searchTitleButton.addActionListener(e -> searchBooksByTitle(searchField.getText().trim()));
        searchAuthorButton.addActionListener(e -> searchBooksByAuthor(searchField.getText().trim()));
        showAllButton.addActionListener(e -> loadAvailableBooksData());
        
        searchPanel.add(new JLabel("Búsqueda:"));
        searchPanel.add(searchField);
        searchPanel.add(searchTitleButton);
        searchPanel.add(searchAuthorButton);
        searchPanel.add(showAllButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton borrowButton = new JButton("Solicitar Préstamo");
        JButton refreshButton = new JButton("Actualizar");

        borrowButton.addActionListener(e -> requestLoan());
        refreshButton.addActionListener(e -> loadAvailableBooksData());

        buttonPanel.add(borrowButton);
        buttonPanel.add(refreshButton);

        // Crear panel que contenga búsqueda y botones
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "ISBN", "Título", "Autor", "Stock"};
        availableBooksModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        availableBooksTable = new JTable(availableBooksModel);
        availableBooksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(availableBooksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createMyLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton returnButton = new JButton("Devolver Libro");
        JButton refreshButton = new JButton("Actualizar");

        returnButton.addActionListener(e -> returnSelectedBook());
        refreshButton.addActionListener(e -> loadMyLoansData());

        buttonPanel.add(returnButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "Libro", "Autor", "Fecha Préstamo", "Fecha Devolución", "Estado"};
        myLoansModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myLoansTable = new JTable(myLoansModel);
        myLoansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(myLoansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLoanHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton deleteButton = new JButton("Eliminar Registro");
        JButton refreshButton = new JButton("Actualizar");

        deleteButton.addActionListener(e -> deleteSelectedLoanRecord());
        refreshButton.addActionListener(e -> loadLoanHistoryData());

        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "Libro", "Autor", "Fecha Préstamo", "Fecha Devolución", "Fecha Devuelto"};
        loanHistoryModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loanHistoryTable = new JTable(loanHistoryModel);
        loanHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(loanHistoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Métodos para cargar datos
    private void loadData() {
        loadAvailableBooksData();
        loadMyLoansData();
        loadLoanHistoryData();
    }

    private void loadAvailableBooksData() {
        availableBooksModel.setRowCount(0);
        try {
            List<Book> books = bookService.getAvailableBooks();
            
            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getIsbn(),
                    book.getTitulo(),
                    book.getAutor(),
                    book.getStock()
                };
                availableBooksModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar libros disponibles: " + e.getMessage());
        }
    }

    private void loadMyLoansData() {
        myLoansModel.setRowCount(0);
        try {
            List<Loan> loans = loanService.getActiveLoansByUser(currentUser.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Loan loan : loans) {
                String status = "Activo";
                if (loan.isOverdue()) {
                    status = "VENCIDO";
                }
                
                Object[] row = {
                    loan.getId(),
                    loan.getTituloLibro(),
                    loan.getAutorLibro(),
                    loan.getFechaPrestamo() != null ? sdfTime.format(loan.getFechaPrestamo()) : "",
                    loan.getFechaDevolucion() != null ? sdf.format(loan.getFechaDevolucion()) : "",
                    status
                };
                myLoansModel.addRow(row);
            }
            
            // Actualizar contador en header
            updateLoansCounter();
            
        } catch (Exception e) {
            showError("Error al cargar mis préstamos: " + e.getMessage());
        }
    }

    private void loadLoanHistoryData() {
        loanHistoryModel.setRowCount(0);
        try {
            List<Loan> loans = loanService.getReturnedLoansByUser(currentUser.getId());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            for (Loan loan : loans) {
                Object[] row = {
                    loan.getId(),
                    loan.getTituloLibro(),
                    loan.getAutorLibro(),
                    loan.getFechaPrestamo() != null ? sdfTime.format(loan.getFechaPrestamo()) : "",
                    loan.getFechaDevolucion() != null ? sdf.format(loan.getFechaDevolucion()) : "",
                    loan.getFechaDevolucionReal() != null ? sdfTime.format(loan.getFechaDevolucionReal()) : ""
                };
                loanHistoryModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar historial de préstamos: " + e.getMessage());
        }
    }

    // Funcionalidades de búsqueda
    private void searchBooksByTitle(String title) {
        if (title.isEmpty()) {
            loadAvailableBooksData();
            return;
        }
        
        availableBooksModel.setRowCount(0);
        try {
            List<Book> books = bookService.searchByTitle(title);
            // Filtrar solo disponibles
            books = books.stream()
                    .filter(book -> book.getDisponible() && book.getStock() > 0)
                    .toList();
            
            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getIsbn(),
                    book.getTitulo(),
                    book.getAutor(),
                    book.getStock()
                };
                availableBooksModel.addRow(row);
            }
            
            if (books.isEmpty()) {
                showInfo("No se encontraron libros disponibles con ese título");
            }
        } catch (Exception e) {
            showError("Error al buscar libros por título: " + e.getMessage());
        }
    }

    private void searchBooksByAuthor(String author) {
        if (author.isEmpty()) {
            loadAvailableBooksData();
            return;
        }
        
        availableBooksModel.setRowCount(0);
        try {
            List<Book> books = bookService.searchByAuthor(author);
            // Filtrar solo disponibles
            books = books.stream()
                    .filter(book -> book.getDisponible() && book.getStock() > 0)
                    .toList();
            
            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getIsbn(),
                    book.getTitulo(),
                    book.getAutor(),
                    book.getStock()
                };
                availableBooksModel.addRow(row);
            }
            
            if (books.isEmpty()) {
                showInfo("No se encontraron libros disponibles de ese autor");
            }
        } catch (Exception e) {
            showError("Error al buscar libros por autor: " + e.getMessage());
        }
    }

    // Funcionalidades de préstamo
    private void requestLoan() {
        int selectedRow = availableBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un libro para solicitar el préstamo");
            return;
        }

        Long bookId = (Long) availableBooksModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) availableBooksModel.getValueAt(selectedRow, 2);
        
        // Verificar si el usuario puede pedir prestado el libro
        if (!loanService.canUserBorrowBook(currentUser.getId(), bookId)) {
            showError("No puede pedir prestado este libro. Posibles razones:\n" +
                     "- Ha alcanzado el límite de 3 préstamos activos\n" +
                     "- El libro ya no está disponible");
            return;
        }

        showRequestLoanDialog(bookId, bookTitle);
    }

    private void showRequestLoanDialog(Long bookId, String bookTitle) {
        JDialog dialog = new JDialog(this, "Solicitar Préstamo", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Información del libro
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JLabel bookLabel = new JLabel("Libro: " + bookTitle);
        bookLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dialog.add(bookLabel, gbc);

        // Fecha de devolución
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Días de préstamo:"), gbc);
        
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(7, 1, 30, 1));
        dialog.add(daysSpinner, gbc);

        // Fecha estimada de devolución (solo informativa)
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Fecha límite de devolución:"), gbc);
        
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        JLabel dateLabel = new JLabel();
        dateLabel.setFont(new Font("Arial", Font.BOLD, 12));
        dateLabel.setForeground(Color.BLUE);
        dialog.add(dateLabel, gbc);

        // Actualizar fecha cuando cambie el spinner
        daysSpinner.addChangeListener(e -> {
            int days = (Integer) daysSpinner.getValue();
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, days);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dateLabel.setText(sdf.format(cal.getTime()));
        });
        
        // Inicializar fecha
        daysSpinner.getChangeListeners()[0].stateChanged(null);

        // Botones
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("Confirmar Préstamo");
        JButton cancelButton = new JButton("Cancelar");

        confirmButton.addActionListener(e -> {
            try {
                int days = (Integer) daysSpinner.getValue();
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, days);
                Date returnDate = cal.getTime();

                loanService.createLoan(currentUser.getId(), bookId, returnDate);
                
                dialog.dispose();
                loadData(); // Recargar todas las tablas
                showInfo("Préstamo realizado exitosamente");
            } catch (Exception ex) {
                showError("Error al realizar préstamo: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void returnSelectedBook() {
        int selectedRow = myLoansTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un préstamo para devolver");
            return;
        }

        Long loanId = (Long) myLoansModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) myLoansModel.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this,
                "¿Confirma la devolución del libro '" + bookTitle + "'?",
                "Confirmar devolución",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                loanService.returnBook(loanId);
                loadData(); // Recargar todas las tablas
                showInfo("Libro devuelto exitosamente");
            } catch (Exception e) {
                showError("Error al devolver libro: " + e.getMessage());
            }
        }
    }

    private void deleteSelectedLoanRecord() {
        int selectedRow = loanHistoryTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un registro para eliminar");
            return;
        }

        Long loanId = (Long) loanHistoryModel.getValueAt(selectedRow, 0);
        String bookTitle = (String) loanHistoryModel.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el registro del libro '" + bookTitle + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                loanService.deleteLoan(loanId);
                loadLoanHistoryData();
                showInfo("Registro eliminado exitosamente");
            } catch (Exception e) {
                showError("Error al eliminar registro: " + e.getMessage());
            }
        }
    }

    // Métodos de utilidad
    private void updateLoansCounter() {
        // Buscar el label del contador en el header y actualizarlo
        SwingUtilities.invokeLater(() -> {
            int activeLoansCount = loanService.getActiveLoansCountByUser(currentUser.getId());
            // Buscar en el header panel
            JPanel headerPanel = (JPanel) ((JPanel) getContentPane().getComponent(0)).getComponent(0);
            JPanel rightPanel = (JPanel) headerPanel.getComponent(1);
            if (rightPanel.getComponentCount() > 0) {
                JLabel loansLabel = (JLabel) rightPanel.getComponent(0);
                loansLabel.setText("Préstamos activos: " + activeLoansCount + "/3");
            }
        });
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de cerrar sesión?",
                "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            this.setVisible(false);
            new Login().setVisible(true);
            this.dispose();
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Información", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
