package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo;

public class Libro {
    private String isbn;
    private String titulo;
    private String autor;
    private int añoPublicacion;
    private String genero;
    private int ejemplares;
    private String foto; // por pensar todavia como hacer el codigo para la foto


    public Libro(String isbn, String titulo, String autor, int añoPublicacion, String genero, int ejemplares, String foto) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.añoPublicacion = añoPublicacion;
        this.genero = genero;
        this.ejemplares = ejemplares;
        this.foto = foto;
    }

}