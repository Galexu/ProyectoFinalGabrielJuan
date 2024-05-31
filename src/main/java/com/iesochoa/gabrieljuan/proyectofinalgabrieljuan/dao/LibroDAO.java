package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo.Libro;

import java.sql.*;

public class LibroDAO {
    public void agregarLibro(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, ano_publicacion, genero, portada) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, libro.getIsbn());
            statement.setString(2, libro.getTitulo());
            statement.setString(3, libro.getAutor());
            statement.setInt(4, libro.getAnoPublicacion());
            statement.setString(5, libro.getGenero());
            statement.setBytes(6, libro.getPortada());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}