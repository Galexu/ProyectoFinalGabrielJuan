package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo;

import java.time.LocalDate;
import java.util.Date;

public class Prestamo {
    private int prestamoId;
    private int copiaId;
    private int socioId;
    private Date fechaPrestamo;
    private Date fechaDevolucion;
    private Date fechaLimite;
    private String estado;

    public Prestamo(int prestamoId, int copiaId, int socioId, Date fechaPrestamo, Date fechaDevolucion, Date fechaLimite, String estado) {
        this.prestamoId = prestamoId;
        this.copiaId = copiaId;
        this.socioId = socioId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
    }

    public int getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(int prestamoId) {
        this.prestamoId = prestamoId;
    }

    public int getCopiaId() {
        return copiaId;
    }

    public void setCopiaId(int copiaId) {
        this.copiaId = copiaId;
    }

    public int getSocioId() {
        return socioId;
    }

    public void setSocioId(int socioId) {
        this.socioId = socioId;
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

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "prestamoId=" + prestamoId +
                ", copiaId=" + copiaId +
                ", socioId=" + socioId +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", fechaLimite=" + fechaLimite +
                ", estado='" + estado + '\'' +
                '}';
    }
}