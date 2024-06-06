package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo;

import java.util.Arrays;

public class Socio {
    private int socioId;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private byte[] socioFoto;

    public Socio(int socioId, String nombre, String direccion, String telefono, String email, byte[] socioFoto) {
        this.socioId = socioId;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.socioFoto = socioFoto;
    }

    public Socio() {
    }

    public int getSocioId() {
        return socioId;
    }

    public void setSocioId(int socioId) {
        this.socioId = socioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public byte[] getSocioFoto() {
        return socioFoto;
    }

    public void setSocioFoto(byte[] socioFoto) {
        this.socioFoto = socioFoto;
    }

    @Override
    public String toString() {
        return "Socio{" +
                "socioId=" + socioId +
                ", name='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", socioFoto=" + Arrays.toString(socioFoto) +
                '}';
    }
}