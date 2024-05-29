package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int anoPublicacion;
    private String genero;
    private int ejemplares;
    private String foto; // por pensar todavia como hacer el codigo para la foto


    public Libro(String isbn, String titulo, String autor, int anoPublicacion, String genero, int ejemplares, String foto) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anoPublicacion = anoPublicacion;
        this.genero = genero;
        this.ejemplares = ejemplares;
        this.foto = foto;
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

    public int getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(int ejemplares) {
        this.ejemplares = ejemplares;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "isbn='" + isbn + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", anoPublicacion=" + anoPublicacion +
                ", genero='" + genero + '\'' +
                ", ejemplares=" + ejemplares +
                '}';
    }
}