package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo;

public class Ejemplar {
    private int copiaId;
    private int disponibles;

    public Ejemplar() {}

    public int getCopiaId() {
        return copiaId;
    }

    public void setCopiaId(int copiaId) {
        this.copiaId = copiaId;
    }

    public int getDisponibles() { return disponibles; }

    public void setDisponibles(int disponibles) { this.disponibles = disponibles; }

    @Override
    public String toString() {
        return "Ejemplar{" +
                "copiaId=" + copiaId +
                ", disponibles=" + disponibles +
                '}';
    }
}