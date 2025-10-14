/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.repository.impl;

import com.mycompany.libronova.dbconnection.DbConnection;
import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.repository.BookRepository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public class BookRepositoryImpl implements BookRepository {
    
    private final DbConnection dbConnection;
    
    public BookRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public Book crear(Book book) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, stock, disponible) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitulo());
            stmt.setString(3, book.getAutor());
            stmt.setInt(4, book.getStock());
            stmt.setBoolean(5, book.getDisponible());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
            
            return book;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear libro", e);
        }
    }

    @Override
    public Optional<Book> buscarPorId(Long id) {
        String sql = "SELECT * FROM libros WHERE id_libro = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro por ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Book> buscarTodos() {
        String sql = "SELECT * FROM libros ORDER BY fecha_creacion DESC";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los libros", e);
        }
        
        return books;
    }

    @Override
    public Book actualizar(Book book) {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, stock = ?, disponible = ? WHERE id_libro = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitulo());
            stmt.setString(3, book.getAutor());
            stmt.setInt(4, book.getStock());
            stmt.setBoolean(5, book.getDisponible());
            stmt.setLong(6, book.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating book failed, no rows affected.");
            }
            
            return book;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar libro", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM libros WHERE id_libro = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting book failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar libro", e);
        }
    }

    @Override
    public Optional<Book> buscarPorIsbn(String isbn) {
        String sql = "SELECT * FROM libros WHERE isbn = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToBook(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libro por ISBN", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Book> buscarPorTitulo(String titulo) {
        String sql = "SELECT * FROM libros WHERE titulo LIKE ? ORDER BY titulo";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + titulo + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros por título", e);
        }
        
        return books;
    }

    @Override
    public List<Book> buscarPorAutor(String autor) {
        String sql = "SELECT * FROM libros WHERE autor LIKE ? ORDER BY autor, titulo";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + autor + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros por autor", e);
        }
        
        return books;
    }

    @Override
    public List<Book> buscarDisponibles() {
        String sql = "SELECT * FROM libros WHERE disponible = TRUE AND stock > 0 ORDER BY titulo";
        List<Book> books = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar libros disponibles", e);
        }
        
        return books;
    }

    @Override
    public boolean existePorIsbn(String isbn) {
        String sql = "SELECT COUNT(*) FROM libros WHERE isbn = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, isbn);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de libro", e);
        }
        
        return false;
    }
    
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getLong("id_libro"));
        book.setIsbn(rs.getString("isbn"));
        book.setTitulo(rs.getString("titulo"));
        book.setAutor(rs.getString("autor"));
        book.setStock(rs.getInt("stock"));
        book.setDisponible(rs.getBoolean("disponible"));
        book.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
        return book;
    }
    
}
