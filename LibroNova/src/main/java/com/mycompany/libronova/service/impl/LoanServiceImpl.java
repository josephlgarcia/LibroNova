/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.service.impl;

import com.mycompany.libronova.domain.Book;
import com.mycompany.libronova.domain.Loan;
import com.mycompany.libronova.domain.User;
import com.mycompany.libronova.exception.EntityNotFoundException;
import com.mycompany.libronova.repository.BookRepository;
import com.mycompany.libronova.repository.LoanRepository;
import com.mycompany.libronova.repository.UserRepository;
import com.mycompany.libronova.service.LoanService;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Coder
 */
public class LoanServiceImpl implements LoanService {
    
    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private static final int MAX_LOANS_PER_USER = 3;
    
    public LoanServiceImpl(LoanRepository loanRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan createLoan(Long userId, Long bookId, Date returnDate) throws EntityNotFoundException, IllegalStateException {
        if (userId == null || bookId == null || returnDate == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser null");
        }

        // Verificar que el usuario existe
        Optional<User> userOpt = userRepository.buscarPorId(userId);
        if (userOpt.isEmpty()) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        // Verificar que el libro existe
        Optional<Book> bookOpt = bookRepository.buscarPorId(bookId);
        if (bookOpt.isEmpty()) {
            throw new EntityNotFoundException("Libro no encontrado con ID: " + bookId);
        }

        Book book = bookOpt.get();

        // Verificar disponibilidad del libro
        if (!book.getDisponible() || book.getStock() <= 0) {
            throw new IllegalStateException("El libro no está disponible para préstamo");
        }

        // Verificar límite de préstamos por usuario
        int activeLoansCount = getActiveLoansCountByUser(userId);
        if (activeLoansCount >= MAX_LOANS_PER_USER) {
            throw new IllegalStateException("El usuario ha alcanzado el límite máximo de " + MAX_LOANS_PER_USER + " préstamos activos");
        }

        // Verificar que la fecha de devolución no sea en el pasado
        if (returnDate.before(new Date())) {
            throw new IllegalArgumentException("La fecha de devolución no puede ser en el pasado");
        }

        // Crear el préstamo
        Loan loan = new Loan(userId, bookId, returnDate);
        loan = loanRepository.crear(loan);

        // Actualizar stock del libro
        book.setStock(book.getStock() - 1);
        if (book.getStock() <= 0) {
            book.setDisponible(false);
        }
        bookRepository.actualizar(book);

        return loan;
    }

    @Override
    public Loan getLoanById(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Optional<Loan> loanOpt = loanRepository.buscarPorId(id);
        if (loanOpt.isEmpty()) {
            throw new EntityNotFoundException("Préstamo no encontrado con ID: " + id);
        }

        return loanOpt.get();
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.buscarTodos();
    }

    @Override
    public List<Loan> getActiveLoansByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }
        return loanRepository.buscarActivosPorUsuario(userId);
    }

    @Override
    public List<Loan> getLoanHistoryByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }
        return loanRepository.buscarPorUsuario(userId);
    }

    @Override
    public List<Loan> getActiveLoans() {
        return loanRepository.buscarActivos();
    }

    @Override
    public List<Loan> getOverdueLoans() {
        return loanRepository.buscarVencidos();
    }

    @Override
    public Loan returnBook(Long loanId) throws EntityNotFoundException, IllegalStateException {
        if (loanId == null) {
            throw new IllegalArgumentException("El ID del préstamo no puede ser null");
        }

        Loan loan = getLoanById(loanId);
        
        if (loan.getDevuelto()) {
            throw new IllegalStateException("El libro ya ha sido devuelto");
        }

        // Marcar como devuelto
        loan.setDevuelto(true);
        loan.setFechaDevolucionReal(new Date());
        
        // Actualizar préstamo
        loan = loanRepository.actualizar(loan);

        // Actualizar stock del libro
        Optional<Book> bookOpt = bookRepository.buscarPorId(loan.getIdLibro());
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setStock(book.getStock() + 1);
            book.setDisponible(true);
            bookRepository.actualizar(book);
        }

        return loan;
    }

    @Override
    public void deleteLoan(Long id) throws EntityNotFoundException {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        Loan loan = getLoanById(id);
        
        // Solo permitir eliminar préstamos ya devueltos
        if (!loan.getDevuelto()) {
            throw new IllegalStateException("Solo se pueden eliminar préstamos ya devueltos");
        }

        loanRepository.eliminar(id);
    }

    @Override
    public List<Loan> getReturnedLoansByUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("El ID del usuario no puede ser null");
        }
        return loanRepository.buscarDevueltosPorUsuario(userId);
    }

    @Override
    public boolean canUserBorrowBook(Long userId, Long bookId) {
        if (userId == null || bookId == null) {
            return false;
        }

        try {
            // Verificar que el usuario existe
            Optional<User> userOpt = userRepository.buscarPorId(userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            // Verificar que el libro existe y está disponible
            Optional<Book> bookOpt = bookRepository.buscarPorId(bookId);
            if (bookOpt.isEmpty()) {
                return false;
            }
            
            Book book = bookOpt.get();
            if (!book.getDisponible() || book.getStock() <= 0) {
                return false;
            }

            // Verificar límite de préstamos
            int activeLoansCount = getActiveLoansCountByUser(userId);
            if (activeLoansCount >= MAX_LOANS_PER_USER) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getActiveLoansCountByUser(Long userId) {
        if (userId == null) {
            return 0;
        }
        return loanRepository.buscarActivosPorUsuario(userId).size();
    }
    
}
