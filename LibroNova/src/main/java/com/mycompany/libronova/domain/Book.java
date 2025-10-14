/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.domain;

import java.sql.Timestamp;

/**
 *
 * @author Coder
 */
public class Book {
    
    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private Integer stock;
    private Boolean disponible;
    private Timestamp fechaCreacion;
    
    public Book() {}
    
    public Book(String isbn, String titulo, String autor, Integer stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.stock = stock;
        this.disponible = true;
    }
    
    public Book(Long id, String isbn, String titulo, String autor, Integer stock, Boolean disponible, Timestamp fechaCreacion) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.stock = stock;
        this.disponible = disponible;
        this.fechaCreacion = fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", stock=" + stock +
                ", disponible=" + disponible +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
    
}
