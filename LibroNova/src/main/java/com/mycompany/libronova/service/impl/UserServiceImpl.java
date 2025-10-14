/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.exception.AuthenticationException;
import com.mycompany.libronova.exception.EntityNotFoundException;
import com.mycompany.libronova.repository.UserRepository;
import com.mycompany.libronova.service.UserService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author Coder
 */
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authenticate(String username, String password) throws AuthenticationException {
        if (username == null || username.trim().isEmpty()) {
            throw new AuthenticationException("El nombre de usuario no puede estar vacío");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new AuthenticationException("La contraseña no puede estar vacía");
        }

        Optional<User> userOpt = userRepository.buscarPorNombreUsuario(username.trim());
        if (userOpt.isEmpty()) {
            throw new AuthenticationException("Usuario no encontrado");
        }

        User user = userOpt.get();
        if (!password.equals(user.getContraseña())) {
            throw new AuthenticationException("Contraseña incorrecta");
        }

        return user;
    }

    @Override
    public User createUser(User user) throws IllegalArgumentException {
        validateUser(user);
        
        if (userRepository.existePorNombreUsuario(user.getNombreUsuario())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario");
        }

        // Validar rol
        if (!"ADMIN".equals(user.getRol()) && !"USUARIO".equals(user.getRol())) {
            throw new IllegalArgumentException("El rol debe ser ADMIN o USUARIO");
        }

        return userRepository.crear(user);
    }

    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Optional<User> userOpt = userRepository.buscarPorId(id);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }

        return userOpt.get();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.buscarTodos();
    }

    @Override
    public User updateUser(User user) throws EntityNotFoundException {
        if (user.getId() == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null para actualizar");
        }

        validateUser(user);

        // Verificar que el usuario existe
        Optional<User> existingUserOpt = userRepository.buscarPorId(user.getId());
        if (existingUserOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + user.getId());
        }

        User existingUser = existingUserOpt.get();
        
        // Si el nombre de usuario cambió, verificar que no exista otro usuario con ese nombre
        if (!existingUser.getNombreUsuario().equals(user.getNombreUsuario())) {
            if (userRepository.existePorNombreUsuario(user.getNombreUsuario())) {
                throw new IllegalArgumentException("Ya existe un usuario con ese nombre de usuario");
            }
        }

        // Validar rol
        if (!"ADMIN".equals(user.getRol()) && !"USUARIO".equals(user.getRol())) {
            throw new IllegalArgumentException("El rol debe ser ADMIN o USUARIO");
        }

        return userRepository.actualizar(user);
    }

    @Override
    public void deleteUser(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Optional<User> userOpt = userRepository.buscarPorId(id);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }

        userRepository.eliminar(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userRepository.existePorNombreUsuario(username.trim());
    }

    @Override
    public List<User> getUsersByRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            return List.of();
        }
        
        return userRepository.buscarTodos()
                .stream()
                .filter(user -> role.equals(user.getRol()))
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        if (user.getNombreUsuario() == null || user.getNombreUsuario().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (user.getContraseña() == null || user.getContraseña().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        if (user.getNombreCompleto() == null || user.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede estar vacío");
        }
        if (user.getRol() == null || user.getRol().trim().isEmpty()) {
            throw new IllegalArgumentException("El rol no puede estar vacío");
        }

        // Validar longitud mínima de contraseña
        if (user.getContraseña().length() < 3) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 3 caracteres");
        }
    }
    
}
