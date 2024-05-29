package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo.Libro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    public void agregarLibro(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, año_publicacion, genero, ejemplares, foto) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, libro.getIsbn());
            pstmt.setString(2, libro.getTitulo());
            pstmt.setString(3, libro.getAutor());
            pstmt.setInt(4, libro.getAnoPublicacion());
            pstmt.setString(5, libro.getGenero());
            pstmt.setInt(6, libro.getEjemplares());
            pstmt.setString(7, libro.getFoto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String isbn = rs.getString("isbn");
                String titulo = rs.getString("titulo");
                String autor = rs.getString("autor");
                int añoPublicacion = rs.getInt("año_publicacion");
                String genero = rs.getString("genero");
                int ejemplares = rs.getInt("ejemplares");
                String foto = rs.getString("foto");

                Libro libro = new Libro(isbn, titulo, autor, añoPublicacion, genero, ejemplares, foto);
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libros;
    }

    // por añadir metodos para actualizar y eliminar libros
}
