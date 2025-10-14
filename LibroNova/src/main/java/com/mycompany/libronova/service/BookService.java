/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.exception.EntityNotFoundException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface BookService {
    
    Book createBook(Book book) throws IllegalArgumentException;
    
    Book getBookById(Long id) throws EntityNotFoundException;
    
    List<Book> getAllBooks();
    
    List<Book> getAvailableBooks();
    
    Book updateBook(Book book) throws EntityNotFoundException;
    
    void deleteBook(Long id) throws EntityNotFoundException;
    
    List<Book> searchByTitle(String title);
    
    List<Book> searchByAuthor(String author);
    
    Book getBookByIsbn(String isbn) throws EntityNotFoundException;
    
    boolean existsByIsbn(String isbn);
    
    void updateStock(Long bookId, int newStock) throws EntityNotFoundException;
    
}
