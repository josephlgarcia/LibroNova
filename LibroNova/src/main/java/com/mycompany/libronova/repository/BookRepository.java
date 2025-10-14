/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.repository;

import com.mycompany.libronova.domain.Book;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public interface BookRepository extends Repository<Book, Long>{
    
    Optional<Book> buscarPorIsbn(String isbn);
    
    List<Book> buscarPorTitulo(String titulo);
    
    List<Book> buscarPorAutor(String autor);
    
    List<Book> buscarDisponibles();
    
    boolean existePorIsbn(String isbn);
    
}
