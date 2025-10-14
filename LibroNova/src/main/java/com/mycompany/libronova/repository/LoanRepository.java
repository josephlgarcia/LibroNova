/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.libronova.repository;

import com.mycompany.libronova.domain.Loan;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface LoanRepository extends Repository<Loan, Long> {
    
    List<Loan> buscarPorUsuario(Long idUsuario);
    
    List<Loan> buscarPorLibro(Long idLibro);
    
    List<Loan> buscarActivos();
    
    List<Loan> buscarActivosPorUsuario(Long idUsuario);
    
    List<Loan> buscarVencidos();
    
    List<Loan> buscarDevueltos();
    
    List<Loan> buscarDevueltosPorUsuario(Long idUsuario);
    
}
