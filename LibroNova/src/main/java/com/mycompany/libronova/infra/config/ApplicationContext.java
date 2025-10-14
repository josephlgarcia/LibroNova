/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.infra.config;

import com.mycompany.libronova.dbconnection.DbConnection;
import com.mycompany.libronova.repository.BookRepository;
import com.mycompany.libronova.repository.LoanRepository;
import com.mycompany.libronova.repository.UserRepository;
import com.mycompany.libronova.repository.impl.BookRepositoryImpl;
import com.mycompany.libronova.repository.impl.LoanRepositoryImpl;
import com.mycompany.libronova.repository.impl.UserRepositoryImpl;
import com.mycompany.libronova.service.BookService;
import com.mycompany.libronova.service.LoanService;
import com.mycompany.libronova.service.UserService;
import com.mycompany.libronova.service.impl.BookServiceImpl;
import com.mycompany.libronova.service.impl.LoanServiceImpl;
import com.mycompany.libronova.service.impl.UserServiceImpl;

/**
 *
 * @author Coder
 */
public class ApplicationContext {
    
    private static ApplicationContext instance;
    
    // Configuración
    private final AppConfig appConfig;
    private final DbConnection dbConnection;
    
    // Repositories
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    
    // Services
    private final UserService userService;
    private final BookService bookService;
    private final LoanService loanService;
    
    private ApplicationContext() {
        // Inicializar configuración
        this.appConfig = new AppConfig();
        this.dbConnection = new DbConnection(appConfig);
        
        // Inicializar repositories
        this.userRepository = new UserRepositoryImpl(dbConnection);
        this.bookRepository = new BookRepositoryImpl(dbConnection);
        this.loanRepository = new LoanRepositoryImpl(dbConnection);
        
        // Inicializar services
        this.userService = new UserServiceImpl(userRepository);
        this.bookService = new BookServiceImpl(bookRepository);
        this.loanService = new LoanServiceImpl(loanRepository, bookRepository, userRepository);
    }
    
    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }
    
    public UserService getUserService() {
        return userService;
    }
    
    public BookService getBookService() {
        return bookService;
    }
    
    public LoanService getLoanService() {
        return loanService;
    }
    
    public UserRepository getUserRepository() {
        return userRepository;
    }
    
    public BookRepository getBookRepository() {
        return bookRepository;
    }
    
    public LoanRepository getLoanRepository() {
        return loanRepository;
    }
    
    public DbConnection getDbConnection() {
        return dbConnection;
    }
    
    public AppConfig getAppConfig() {
        return appConfig;
    }
    
}
