package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo;

public class Libro {
    private int libroId;
    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacion;
    private String genero;
    private byte[] portada;
    private int disponibles;

    public Libro(int libroId, String isbn, String titulo, String autor, int anoPublicacion, String genero, byte[] portada) {
        this.libroId = libroId;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.genero = genero;
        this.portada = portada;
    }

    public Libro() {

    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getLibroId() {
        return libroId;
    }

    public void setLibroId(int libroId) {
        this.libroId = libroId;
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

    public int getAnoPublicacion() {
        return anoPublicacion;
    }

    public void setAnoPublicacion(int anoPublicacion) {
        this.anoPublicacion = anoPublicacion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public byte[] getPortada() {
        return portada;
    }

    public void setPortada(byte[] portada) {
        this.portada = portada;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "libroId=" + libroId +
                ", isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacion=" + anoPublicacion +
                ", genero='" + genero + '\'' +
                ", disponibles=" + disponibles +
                '}';
    }
}