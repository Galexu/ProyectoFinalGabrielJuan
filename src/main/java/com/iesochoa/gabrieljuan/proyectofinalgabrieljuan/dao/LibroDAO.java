package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection con = ConexionDB.conectar();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
                libro.setGenero(rs.getString("genero"));
                libro.setPortada(rs.getBytes("portada"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public void actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, ano_publicacion = ?, genero = ?, portada = ? WHERE isbn = ?";

        try (Connection con = ConexionDB.conectar(); PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setInt(3, libro.getAnoPublicacion());
            statement.setString(4, libro.getGenero());
            statement.setBytes(5, libro.getPortada());
            statement.setString(6, libro.getIsbn());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}