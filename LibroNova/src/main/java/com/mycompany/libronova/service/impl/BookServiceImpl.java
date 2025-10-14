/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.exception.EntityNotFoundException;
import com.mycompany.libronova.repository.BookRepository;
import com.mycompany.libronova.service.BookService;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book createBook(Book book) throws IllegalArgumentException {
        validateBook(book);
        
        if (bookRepository.existePorIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
        }

        if (book.getStock() == null || book.getStock() < 0) {
            book.setStock(1);
        }
        
        if (book.getDisponible() == null) {
            book.setDisponible(true);
        }

        return bookRepository.crear(book);
    }

    @Override
    public Book getBookById(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Optional<Book> bookOpt = bookRepository.buscarPorId(id);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Libro no encontrado con ID: " + id);
        }

        return bookOpt.get();
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.buscarTodos();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.buscarDisponibles();
    }

    @Override
    public Book updateBook(Book book) throws EntityNotFoundException {
        if (book.getId() == null) {
            throw new IllegalArgumentException("El ID del libro no puede ser null para actualizar");
        }

        validateBook(book);

        // Verificar que el libro existe
        Optional<Book> existingBookOpt = bookRepository.buscarPorId(book.getId());
        if (existingBookOpt.isEmpty()) {
            throw new EntityNotFoundException("Libro no encontrado con ID: " + book.getId());
        }

        Book existingBook = existingBookOpt.get();
        
        // Si el ISBN cambió, verificar que no exista otro libro con ese ISBN
        if (!existingBook.getIsbn().equals(book.getIsbn())) {
            if (bookRepository.existePorIsbn(book.getIsbn())) {
                throw new IllegalArgumentException("Ya existe un libro con ese ISBN");
            }
        }

        if (book.getStock() == null || book.getStock() < 0) {
            book.setStock(0);
        }

        return bookRepository.actualizar(book);
    }

    @Override
    public void deleteBook(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Optional<Book> bookOpt = bookRepository.buscarPorId(id);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Libro no encontrado con ID: " + id);
        }

        bookRepository.eliminar(id);
    }

    @Override
    public List<Book> searchByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.buscarPorTitulo(title.trim());
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            return List.of();
        }
        return bookRepository.buscarPorAutor(author.trim());
    }

    @Override
    public Book getBookByIsbn(String isbn) throws EntityNotFoundException {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }

        Optional<Book> bookOpt = bookRepository.buscarPorIsbn(isbn.trim());
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Libro no encontrado con ISBN: " + isbn);
        }

        return bookOpt.get();
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }
        return bookRepository.existePorIsbn(isbn.trim());
    }

    @Override
    public void updateStock(Long bookId, int newStock) throws EntityNotFoundException {
        Book book = getBookById(bookId);
        book.setStock(newStock);
        
        // Si el stock es 0, marcar como no disponible
        if (newStock <= 0) {
            book.setDisponible(false);
        } else {
            book.setDisponible(true);
        }
        
        bookRepository.actualizar(book);
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("El libro no puede ser null");
        }
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        if (book.getTitulo() == null || book.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (book.getAutor() == null || book.getAutor().trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
    }
    
}
