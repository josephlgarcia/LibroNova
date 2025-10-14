/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.repository.impl;

import com.mycompany.libronova.dbconnection.DbConnection;
import com.mycompany.libronova.domain.Loan;
import com.mycompany.libronova.repository.LoanRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public class LoanRepositoryImpl implements LoanRepository {
    
    private final DbConnection dbConnection;
    
    public LoanRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Loan crear(Loan loan) {
        String sql = "INSERT INTO prestamos (id_usuario, id_libro, fecha_devolucion, devuelto) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, loan.getIdUsuario());
            stmt.setLong(2, loan.getIdLibro());
            stmt.setDate(3, new java.sql.Date(loan.getFechaDevolucion().getTime()));
            stmt.setBoolean(4, loan.getDevuelto());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating loan failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    loan.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating loan failed, no ID obtained.");
                }
            }
            
            return loan;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear préstamo", e);
        }
    }

    @Override
    public Optional<Loan> buscarPorId(Long id) {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.id_prestamo = ?
                """;
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToLoan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamo por ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Loan> buscarTodos() {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                ORDER BY p.fecha_prestamo DESC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los préstamos", e);
        }
        
        return loans;
    }

    @Override
    public Loan actualizar(Loan loan) {
        String sql = "UPDATE prestamos SET id_usuario = ?, id_libro = ?, fecha_devolucion = ?, devuelto = ?, fecha_devolucion_real = ? WHERE id_prestamo = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, loan.getIdUsuario());
            stmt.setLong(2, loan.getIdLibro());
            stmt.setDate(3, new java.sql.Date(loan.getFechaDevolucion().getTime()));
            stmt.setBoolean(4, loan.getDevuelto());
            if (loan.getFechaDevolucionReal() != null) {
                stmt.setTimestamp(5, new Timestamp(loan.getFechaDevolucionReal().getTime()));
            } else {
                stmt.setNull(5, Types.TIMESTAMP);
            }
            stmt.setLong(6, loan.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating loan failed, no rows affected.");
            }
            
            return loan;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar préstamo", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM prestamos WHERE id_prestamo = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting loan failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar préstamo", e);
        }
    }

    @Override
    public List<Loan> buscarPorUsuario(Long idUsuario) {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.id_usuario = ?
                ORDER BY p.fecha_prestamo DESC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos por usuario", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarPorLibro(Long idLibro) {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.id_libro = ?
                ORDER BY p.fecha_prestamo DESC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idLibro);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos por libro", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarActivos() {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.devuelto = FALSE
                ORDER BY p.fecha_devolucion ASC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos activos", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarActivosPorUsuario(Long idUsuario) {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.id_usuario = ? AND p.devuelto = FALSE
                ORDER BY p.fecha_devolucion ASC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos activos por usuario", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarVencidos() {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.devuelto = FALSE AND p.fecha_devolucion < CURDATE()
                ORDER BY p.fecha_devolucion ASC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos vencidos", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarDevueltos() {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.devuelto = TRUE
                ORDER BY p.fecha_devolucion_real DESC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                loans.add(mapResultSetToLoan(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos devueltos", e);
        }
        
        return loans;
    }

    @Override
    public List<Loan> buscarDevueltosPorUsuario(Long idUsuario) {
        String sql = """
                SELECT p.*, u.nombre_usuario, l.titulo, l.autor 
                FROM prestamos p 
                JOIN usuarios u ON p.id_usuario = u.id_usuario 
                JOIN libros l ON p.id_libro = l.id_libro 
                WHERE p.id_usuario = ? AND p.devuelto = TRUE
                ORDER BY p.fecha_devolucion_real DESC
                """;
        List<Loan> loans = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loans.add(mapResultSetToLoan(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar préstamos devueltos por usuario", e);
        }
        
        return loans;
    }
    
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setId(rs.getLong("id_prestamo"));
        loan.setIdUsuario(rs.getLong("id_usuario"));
        loan.setIdLibro(rs.getLong("id_libro"));
        loan.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
        loan.setFechaDevolucion(rs.getDate("fecha_devolucion"));
        loan.setDevuelto(rs.getBoolean("devuelto"));
        
        Timestamp fechaDevReal = rs.getTimestamp("fecha_devolucion_real");
        if (fechaDevReal != null) {
            loan.setFechaDevolucionReal(fechaDevReal);
        }
        
        // Información adicional de las joins
        loan.setNombreUsuario(rs.getString("nombre_usuario"));
        loan.setTituloLibro(rs.getString("titulo"));
        loan.setAutorLibro(rs.getString("autor"));
        
        return loan;
    }
    
}
