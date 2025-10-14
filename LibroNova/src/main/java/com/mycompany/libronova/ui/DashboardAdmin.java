/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.ui;

import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.domain.Loan;
import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.exception.EntityNotFoundException;
import com.mycompany.libronova.infra.config.ApplicationContext;
import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.service.LoanService;
import com.mycompany.libronova.service.UserService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Coder
 */
public class DashboardAdmin extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(DashboardAdmin.class.getName());

    private final User currentUser;
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;

    // Componentes UI
    private JTabbedPane tabbedPane;
    private JTable usersTable;
    private JTable booksTable;
    private JTable loansTable;
    private DefaultTableModel usersModel;
    private DefaultTableModel booksModel;
    private DefaultTableModel loansModel;

    public DashboardAdmin(User user) {
        this.currentUser = user;
        ApplicationContext context = ApplicationContext.getInstance();
        this.userService = context.getUserService();
        this.bookService = context.getBookService();
        this.loanService = context.getLoanService();

        initComponents();
        loadData();
    }

    private void initComponents() {
        setTitle("LibroNova - Panel de Administrador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        headerPanel.setBackground(new Color(70, 130, 180));

        JLabel welcomeLabel = new JLabel("Bienvenido, " + currentUser.getNombreCompleto() + " (Administrador)");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> logout());
        headerPanel.add(logoutButton, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Usuarios", createUsersPanel());
        tabbedPane.addTab("Libros", createBooksPanel());
        tabbedPane.addTab("Préstamos", createLoansPanel());

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Usuario");
        JButton editButton = new JButton("Editar Usuario");
        JButton deleteButton = new JButton("Eliminar Usuario");
        JButton refreshButton = new JButton("Actualizar");
        JButton importButton = new JButton("Importar CSV");
        JButton exportButton = new JButton("Exportar CSV");

        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> editSelectedUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> loadUsersData());
        importButton.addActionListener(e -> importUsersFromCSV());
        exportButton.addActionListener(e -> exportUsersToCSV());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "Usuario", "Nombre Completo", "Rol", "Fecha Registro"};
        usersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        usersTable = new JTable(usersModel);
        usersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Agregar Libro");
        JButton editButton = new JButton("Editar Libro");
        JButton deleteButton = new JButton("Eliminar Libro");
        JButton refreshButton = new JButton("Actualizar");
        JButton importButton = new JButton("Importar CSV");
        JButton exportButton = new JButton("Exportar CSV");

        addButton.addActionListener(e -> showAddBookDialog());
        editButton.addActionListener(e -> editSelectedBook());
        deleteButton.addActionListener(e -> deleteSelectedBook());
        refreshButton.addActionListener(e -> loadBooksData());
        importButton.addActionListener(e -> importBooksFromCSV());
        exportButton.addActionListener(e -> exportBooksToCSV());
        

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "ISBN", "Título", "Autor", "Stock", "Disponible"};
        booksModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(booksModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createLoansPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshButton = new JButton("Actualizar");
        JButton viewOverdueButton = new JButton("Ver Vencidos");
        JButton viewActiveButton = new JButton("Ver Activos");
        JButton viewAllButton = new JButton("Ver Todos");
        JButton exportButton = new JButton("Exportar CSV");

        refreshButton.addActionListener(e -> loadLoansData());
        viewOverdueButton.addActionListener(e -> showOverdueLoans());
        viewActiveButton.addActionListener(e -> showActiveLoans());
        viewAllButton.addActionListener(e -> loadLoansData());
        exportButton.addActionListener(e -> exportLoansToCSV());

        buttonPanel.add(refreshButton);
        buttonPanel.add(viewActiveButton);
        buttonPanel.add(viewOverdueButton);
        buttonPanel.add(viewAllButton);
        buttonPanel.add(exportButton);

        panel.add(buttonPanel, BorderLayout.NORTH);

        // Tabla
        String[] columns = {"ID", "Usuario", "Libro", "Fecha Préstamo", "Fecha Devolución", "Devuelto", "Fecha Devolución Real"};
        loansModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        loansTable = new JTable(loansModel);
        loansTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(loansTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Métodos para cargar datos
    private void loadData() {
        loadUsersData();
        loadBooksData();
        loadLoansData();
    }

    private void loadUsersData() {
        usersModel.setRowCount(0);
        try {
            List<User> users = userService.getAllUsers();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (User user : users) {
                Object[] row = {
                    user.getId(),
                    user.getNombreUsuario(),
                    user.getNombreCompleto(),
                    user.getRol(),
                    user.getFechaRegistro() != null ? sdf.format(user.getFechaRegistro()) : ""
                };
                usersModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    private void loadBooksData() {
        booksModel.setRowCount(0);
        try {
            List<Book> books = bookService.getAllBooks();

            for (Book book : books) {
                Object[] row = {
                    book.getId(),
                    book.getIsbn(),
                    book.getTitulo(),
                    book.getAutor(),
                    book.getStock(),
                    book.getDisponible() ? "Sí" : "No"
                };
                booksModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar libros: " + e.getMessage());
        }
    }

    private void loadLoansData() {
        loansModel.setRowCount(0);
        try {
            List<Loan> loans = loanService.getAllLoans();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Loan loan : loans) {
                Object[] row = {
                    loan.getId(),
                    loan.getNombreUsuario(),
                    loan.getTituloLibro() + " - " + loan.getAutorLibro(),
                    loan.getFechaPrestamo() != null ? sdfTime.format(loan.getFechaPrestamo()) : "",
                    loan.getFechaDevolucion() != null ? sdf.format(loan.getFechaDevolucion()) : "",
                    loan.getDevuelto() ? "Sí" : "No",
                    loan.getFechaDevolucionReal() != null ? sdfTime.format(loan.getFechaDevolucionReal()) : ""
                };
                loansModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar préstamos: " + e.getMessage());
        }
    }

    // Métodos para usuarios
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Agregar Usuario", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JTextField nameField = new JTextField(15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"USUARIO", "ADMIN"});

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(roleCombo, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            try {
                User user = new User();
                user.setNombreUsuario(usernameField.getText().trim());
                user.setContraseña(new String(passwordField.getPassword()));
                user.setNombreCompleto(nameField.getText().trim());
                user.setRol((String) roleCombo.getSelectedItem());

                userService.createUser(user);
                loadUsersData();
                dialog.dispose();
                showInfo("Usuario creado exitosamente");
            } catch (Exception ex) {
                showError("Error al crear usuario: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un usuario para editar");
            return;
        }

        Long userId = (Long) usersModel.getValueAt(selectedRow, 0);
        try {
            User user = userService.getUserById(userId);
            showEditUserDialog(user);
        } catch (EntityNotFoundException e) {
            showError("Usuario no encontrado: " + e.getMessage());
        }
    }

    private void showEditUserDialog(User user) {
        JDialog dialog = new JDialog(this, "Editar Usuario", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField usernameField = new JTextField(user.getNombreUsuario(), 15);
        JPasswordField passwordField = new JPasswordField(user.getContraseña(), 15);
        JTextField nameField = new JTextField(user.getNombreCompleto(), 15);
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"USUARIO", "ADMIN"});
        roleCombo.setSelectedItem(user.getRol());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Nombre Completo:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Rol:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(roleCombo, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            try {
                user.setNombreUsuario(usernameField.getText().trim());
                user.setContraseña(new String(passwordField.getPassword()));
                user.setNombreCompleto(nameField.getText().trim());
                user.setRol((String) roleCombo.getSelectedItem());

                userService.updateUser(user);
                loadUsersData();
                dialog.dispose();
                showInfo("Usuario actualizado exitosamente");
            } catch (Exception ex) {
                showError("Error al actualizar usuario: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedUser() {
        int selectedRow = usersTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un usuario para eliminar");
            return;
        }

        Long userId = (Long) usersModel.getValueAt(selectedRow, 0);
        String username = (String) usersModel.getValueAt(selectedRow, 1);

        int result = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el usuario '" + username + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                userService.deleteUser(userId);
                loadUsersData();
                showInfo("Usuario eliminado exitosamente");
            } catch (Exception e) {
                showError("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    // Métodos para libros
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Agregar Libro", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField isbnField = new JTextField(15);
        JTextField titleField = new JTextField(15);
        JTextField authorField = new JTextField(15);
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(stockSpinner, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            try {
                Book book = new Book();
                book.setIsbn(isbnField.getText().trim());
                book.setTitulo(titleField.getText().trim());
                book.setAutor(authorField.getText().trim());
                book.setStock((Integer) stockSpinner.getValue());
                book.setDisponible(true);

                bookService.createBook(book);
                loadBooksData();
                dialog.dispose();
                showInfo("Libro creado exitosamente");
            } catch (Exception ex) {
                showError("Error al crear libro: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void editSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un libro para editar");
            return;
        }

        Long bookId = (Long) booksModel.getValueAt(selectedRow, 0);
        try {
            Book book = bookService.getBookById(bookId);
            showEditBookDialog(book);
        } catch (EntityNotFoundException e) {
            showError("Libro no encontrado: " + e.getMessage());
        }
    }

    private void showEditBookDialog(Book book) {
        JDialog dialog = new JDialog(this, "Editar Libro", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField isbnField = new JTextField(book.getIsbn(), 15);
        JTextField titleField = new JTextField(book.getTitulo(), 15);
        JTextField authorField = new JTextField(book.getAutor(), 15);
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(book.getStock().intValue(), 0, 999, 1));
        JCheckBox availableCheckbox = new JCheckBox("Disponible", book.getDisponible());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel("Stock:"), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(stockSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(new JLabel(""), gbc);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        dialog.add(availableCheckbox, gbc);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Guardar");
        JButton cancelButton = new JButton("Cancelar");

        saveButton.addActionListener(e -> {
            try {
                book.setIsbn(isbnField.getText().trim());
                book.setTitulo(titleField.getText().trim());
                book.setAutor(authorField.getText().trim());
                book.setStock((Integer) stockSpinner.getValue());
                book.setDisponible(availableCheckbox.isSelected());

                bookService.updateBook(book);
                loadBooksData();
                dialog.dispose();
                showInfo("Libro actualizado exitosamente");
            } catch (Exception ex) {
                showError("Error al actualizar libro: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void deleteSelectedBook() {
        int selectedRow = booksTable.getSelectedRow();
        if (selectedRow == -1) {
            showInfo("Por favor seleccione un libro para eliminar");
            return;
        }

        Long bookId = (Long) booksModel.getValueAt(selectedRow, 0);
        String title = (String) booksModel.getValueAt(selectedRow, 2);

        int result = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el libro '" + title + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            try {
                bookService.deleteBook(bookId);
                loadBooksData();
                showInfo("Libro eliminado exitosamente");
            } catch (Exception e) {
                showError("Error al eliminar libro: " + e.getMessage());
            }
        }
    }

    // Métodos para préstamos
    private void showActiveLoans() {
        loansModel.setRowCount(0);
        try {
            List<Loan> loans = loanService.getActiveLoans();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Loan loan : loans) {
                Object[] row = {
                    loan.getId(),
                    loan.getNombreUsuario(),
                    loan.getTituloLibro() + " - " + loan.getAutorLibro(),
                    loan.getFechaPrestamo() != null ? sdfTime.format(loan.getFechaPrestamo()) : "",
                    loan.getFechaDevolucion() != null ? sdf.format(loan.getFechaDevolucion()) : "",
                    loan.getDevuelto() ? "Sí" : "No",
                    loan.getFechaDevolucionReal() != null ? sdfTime.format(loan.getFechaDevolucionReal()) : ""
                };
                loansModel.addRow(row);
            }
        } catch (Exception e) {
            showError("Error al cargar préstamos activos: " + e.getMessage());
        }
    }

    private void showOverdueLoans() {
        loansModel.setRowCount(0);
        try {
            List<Loan> loans = loanService.getOverdueLoans();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat sdfTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            for (Loan loan : loans) {
                Object[] row = {
                    loan.getId(),
                    loan.getNombreUsuario(),
                    loan.getTituloLibro() + " - " + loan.getAutorLibro(),
                    loan.getFechaPrestamo() != null ? sdfTime.format(loan.getFechaPrestamo()) : "",
                    loan.getFechaDevolucion() != null ? sdf.format(loan.getFechaDevolucion()) : "",
                    loan.getDevuelto() ? "Sí" : "No",
                    loan.getFechaDevolucionReal() != null ? sdfTime.format(loan.getFechaDevolucionReal()) : ""
                };
                loansModel.addRow(row);
            }

            if (loans.isEmpty()) {
                showInfo("No hay préstamos vencidos");
            }
        } catch (Exception e) {
            showError("Error al cargar préstamos vencidos: " + e.getMessage());
        }
    }

    // Métodos de utilidad
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

    private void importUsersFromCSV() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int imported = 0;
                int errors = 0;

                // Saltar encabezado
                br.readLine();

                while ((line = br.readLine()) != null) {
                    try {
                        String[] data = line.split(",");
                        if (data.length >= 4) {
                            User user = new User();
                            user.setNombreUsuario(data[0].trim());
                            user.setContraseña(data[1].trim());
                            user.setNombreCompleto(data[2].trim());
                            user.setRol(data[3].trim());

                            userService.createUser(user);
                            imported++;
                        }
                    } catch (Exception e) {
                        errors++;
                        logger.warning("Error al importar línea: " + line + " - " + e.getMessage());
                    }
                }

                loadUsersData();
                showInfo("Importación completada. Usuarios importados: " + imported + ", Errores: " + errors);
            } catch (IOException e) {
                showError("Error al leer el archivo CSV: " + e.getMessage());
            }

        }
    }

    private void exportUsersToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        fileChooser.setSelectedFile(new File("usuarios_" + System.currentTimeMillis() + ".csv"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            // Asegurar que el archivo tenga extensión .csv
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                // Escribir encabezado
                bw.write("ID,Usuario,Nombre Completo,Rol,Fecha Registro");
                bw.newLine();

                // Escribir datos
                for (int i = 0; i < usersModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < usersModel.getColumnCount(); j++) {
                        Object value = usersModel.getValueAt(i, j);
                        sb.append(value != null ? value.toString() : "");
                        if (j < usersModel.getColumnCount() - 1) {
                            sb.append(",");
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }

                showInfo("Datos exportados exitosamente a: " + file.getAbsolutePath());
            } catch (IOException e) {
                showError("Error al exportar CSV: " + e.getMessage());
            }
        }
    }
    
    // Métodos para importar/exportar CSV - LIBROS
    private void importBooksFromCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                int imported = 0;
                int errors = 0;
                
                // Saltar encabezado
                br.readLine();
                
                while ((line = br.readLine()) != null) {
                    try {
                        String[] data = line.split(",");
                        if (data.length >= 4) {
                            Book book = new Book();
                            book.setIsbn(data[0].trim());
                            book.setTitulo(data[1].trim());
                            book.setAutor(data[2].trim());
                            book.setStock(Integer.parseInt(data[3].trim()));
                            book.setDisponible(data.length > 4 ? 
                                Boolean.parseBoolean(data[3].trim()) : true);
                            
                            bookService.createBook(book);
                            imported++;
                        }
                    } catch (Exception e) {
                        errors++;
                        logger.warning("Error al importar línea: " + line + " - " + e.getMessage());
                    }
                }
                
                loadBooksData();
                showInfo("Importación completada. Libros importados: " + imported + ", Errores: " + errors);
            } catch (IOException e) {
                showError("Error al leer el archivo CSV: " + e.getMessage());
            }
        }
    }
    
    private void exportBooksToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        fileChooser.setSelectedFile(new File("libros_" + System.currentTimeMillis() + ".csv"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Asegurar que el archivo tenga extensión .csv
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                // Escribir encabezado
                bw.write("ID,ISBN,Título,Autor,Stock,Disponible");
                bw.newLine();
                
                // Escribir datos
                for (int i = 0; i < booksModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < booksModel.getColumnCount(); j++) {
                        Object value = booksModel.getValueAt(i, j);
                        sb.append(value != null ? value.toString() : "");
                        if (j < booksModel.getColumnCount() - 1) {
                            sb.append(",");
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
                
                showInfo("Datos exportados exitosamente a: " + file.getAbsolutePath());
            } catch (IOException e) {
                showError("Error al exportar CSV: " + e.getMessage());
            }
        }
    }
    
    // Métodos para exportar CSV - PRÉSTAMOS (solo exportación)
    private void exportLoansToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos CSV", "csv"));
        fileChooser.setSelectedFile(new File("prestamos_" + System.currentTimeMillis() + ".csv"));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Asegurar que el archivo tenga extensión .csv
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getAbsolutePath() + ".csv");
            }
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                // Escribir encabezado
                bw.write("ID,Usuario,Libro,Fecha Préstamo,Fecha Devolución,Devuelto,Fecha Devolución Real");
                bw.newLine();
                
                // Escribir datos
                for (int i = 0; i < loansModel.getRowCount(); i++) {
                    StringBuilder sb = new StringBuilder();
                    for (int j = 0; j < loansModel.getColumnCount(); j++) {
                        Object value = loansModel.getValueAt(i, j);
                        String valueStr = value != null ? value.toString() : "";
                        // Escapar comas dentro de campos
                        if (valueStr.contains(",")) {
                            valueStr = "\"" + valueStr + "\"";
                        }
                        sb.append(valueStr);
                        if (j < loansModel.getColumnCount() - 1) {
                            sb.append(",");
                        }
                    }
                    bw.write(sb.toString());
                    bw.newLine();
                }
                
                showInfo("Datos exportados exitosamente a: " + file.getAbsolutePath());
            } catch (IOException e) {
                showError("Error al exportar CSV: " + e.getMessage());
            }
        }
    }

}
