/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.repository;

import com.mycompany.libronova.domain.User;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public interface UserRepository extends Repository<User, Long> {
    
    Optional<User> buscarPorNombreUsuario(String nombreUsuario);
    
    boolean existePorNombreUsuario(String nombreUsuario);
    
}
