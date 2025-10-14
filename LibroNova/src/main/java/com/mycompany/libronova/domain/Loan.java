/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.libronova.domain;

import java.util.Date;

/**
 *
 * @author Coder
 */
public class Loan {
    
    private Long id;
    private Long idUsuario;
    private Long idLibro;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private Boolean devuelto;
    private Date fechaDevolucionReal;
    
    // Para mostrar información completa en las vistas
    private String nombreUsuario;
    private String tituloLibro;
    private String autorLibro;
    
    public Loan() {}
    
    public Loan(Long idUsuario, Long idLibro, Date fechaDevolucion) {
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = false;
        this.fechaPrestamo = new Date();
    }
    
    public Loan(Long id, Long idUsuario, Long idLibro, Date fechaPrestamo, Date fechaDevolucion, Boolean devuelto, Date fechaDevolucionReal) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idLibro = idLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.devuelto = devuelto;
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(Date fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public Boolean getDevuelto() {
        return devuelto;
    }

    public void setDevuelto(Boolean devuelto) {
        this.devuelto = devuelto;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTituloLibro() {
        return tituloLibro;
    }

    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    public String getAutorLibro() {
        return autorLibro;
    }

    public void setAutorLibro(String autorLibro) {
        this.autorLibro = autorLibro;
    }
    
    public boolean isOverdue() {
        if (devuelto) return false;
        return new Date().after(fechaDevolucion);
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", idLibro=" + idLibro +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", devuelto=" + devuelto +
                ", fechaDevolucionReal=" + fechaDevolucionReal +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", tituloLibro='" + tituloLibro + '\'' +
                '}';
    }
    
}
