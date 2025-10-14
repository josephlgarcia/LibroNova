/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.service;

import com.mycompany.libronova.domain.Loan;
import com.mycompany.libronova.exception.EntityNotFoundException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface LoanService {
    
    Loan createLoan(Long userId, Long bookId, Date returnDate) throws EntityNotFoundException, IllegalStateException;
    
    Loan getLoanById(Long id) throws EntityNotFoundException;
    
    List<Loan> getAllLoans();
    
    List<Loan> getActiveLoansByUser(Long userId);
    
    List<Loan> getLoanHistoryByUser(Long userId);
    
    List<Loan> getActiveLoans();
    
    List<Loan> getOverdueLoans();
    
    Loan returnBook(Long loanId) throws EntityNotFoundException, IllegalStateException;
    
    void deleteLoan(Long id) throws EntityNotFoundException;
    
    List<Loan> getReturnedLoansByUser(Long userId);
    
    boolean canUserBorrowBook(Long userId, Long bookId);
    
    int getActiveLoansCountByUser(Long userId);
    
}
