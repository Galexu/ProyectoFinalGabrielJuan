package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo;

import java.time.LocalDate;

public class Prestamo {
    private Libro libro;
    private Socio socio;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;
    private String estado;
    private int idPrestamo;

    public Prestamo(Libro libro, Socio socio, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista, String estado, int idPrestamo) {
        this.libro = libro;
        this.socio = socio;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
        this.estado = estado;
        this.idPrestamo = idPrestamo;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(int idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "libro=" + libro +
                ", socio=" + socio +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucionPrevista=" + fechaDevolucionPrevista +
                ", estado='" + estado + '\'' +
                '}';
    }
}