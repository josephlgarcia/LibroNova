/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public interface Repository<T, ID> {
    
    T crear(T t);

    Optional<T> buscarPorId(ID id);

    List<T> buscarTodos();

    T actualizar(T t);

    void eliminar(ID id);
    
}
