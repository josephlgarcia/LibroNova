/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.exception.AuthenticationException;
import com.mycompany.libronova.exception.EntityNotFoundException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface UserService {
    
    User authenticate(String username, String password) throws AuthenticationException;
    
    User createUser(User user) throws IllegalArgumentException;
    
    User getUserById(Long id) throws EntityNotFoundException;
    
    List<User> getAllUsers();
    
    User updateUser(User user) throws EntityNotFoundException;
    
    void deleteUser(Long id) throws EntityNotFoundException;
    
    boolean existsByUsername(String username);
    
    List<User> getUsersByRole(String role);
    
}
