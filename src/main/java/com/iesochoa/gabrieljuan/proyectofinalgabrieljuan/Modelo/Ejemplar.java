package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo;

public class Ejemplar {
    private int copiaId;
    private String isbn;
    private boolean disponible;

    public Ejemplar(int copiaId, String isbn, boolean disponible) {
        this.copiaId = copiaId;
        this.isbn = isbn;
        this.disponible = disponible;
    }

    public Ejemplar() {
    }

    public int getCopiaId() {
        return copiaId;
    }

    public void setCopiaId(int copiaId) {
        this.copiaId = copiaId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "copiaId=" + copiaId +
                ", isbn='" + isbn + '\'' +
                ", disponible=" + disponible +
                '}';
    }
}