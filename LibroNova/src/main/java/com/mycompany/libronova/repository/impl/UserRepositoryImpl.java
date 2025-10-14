/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.repository.impl;

import com.mycompany.libronova.dbconnection.DbConnection;
import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.repository.UserRepository;
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
public class UserRepositoryImpl implements UserRepository {
    
    private final DbConnection dbConnection;
    
    public UserRepositoryImpl(DbConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public User crear(User user) {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, rol) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getContraseña());
            stmt.setString(3, user.getNombreCompleto());
            stmt.setString(4, user.getRol());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            return user;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario", e);
        }
    }

    @Override
    public Optional<User> buscarPorId(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por ID", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<User> buscarTodos() {
        String sql = "SELECT * FROM usuarios ORDER BY fecha_registro DESC";
        List<User> users = new ArrayList<>();
        
        try (Connection conn = dbConnection.open();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los usuarios", e);
        }
        
        return users;
    }

    @Override
    public User actualizar(User user) {
        String sql = "UPDATE usuarios SET nombre_usuario = ?, contrasena = ?, nombre_completo = ?, rol = ? WHERE id_usuario = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getContraseña());
            stmt.setString(3, user.getNombreCompleto());
            stmt.setString(4, user.getRol());
            stmt.setLong(5, user.getId());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
            
            return user;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario", e);
        }
    }

    @Override
    public void eliminar(Long id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar usuario", e);
        }
    }

    @Override
    public Optional<User> buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToUser(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario por nombre", e);
        }
        
        return Optional.empty();
    }

    @Override
    public boolean existePorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = ?";
        
        try (Connection conn = dbConnection.open();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombreUsuario);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de usuario", e);
        }
        
        return false;
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id_usuario"));
        user.setNombreUsuario(rs.getString("nombre_usuario"));
        user.setContraseña(rs.getString("contrasena"));
        user.setNombreCompleto(rs.getString("nombre_completo"));
        user.setRol(rs.getString("rol"));
        user.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return user;
    }
    
}
