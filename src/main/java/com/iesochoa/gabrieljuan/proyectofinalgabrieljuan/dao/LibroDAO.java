package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {
    public int agregarLibro(Libro libro) {
        String sql = "INSERT INTO libros (isbn, titulo, autor, ano_publicacion, genero, portada) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, libro.getIsbn());
            statement.setString(2, libro.getTitulo());
            statement.setString(3, libro.getAutor());
            statement.setInt(4, libro.getAnoPublicacion());
            statement.setString(5, libro.getGenero());
            statement.setBytes(6, libro.getPortada());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                int generatedLibroId = rs.getInt(1);
                return generatedLibroId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Libro> obtenerLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, e.disponibles " +
                "FROM libros l LEFT JOIN ejemplares e ON l.libro_id = e.libro_id";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setLibroId(rs.getInt("libro_id"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
                libro.setGenero(rs.getString("genero"));
                libro.setPortada(rs.getBytes("portada"));
                libro.setDisponibles(rs.getInt("disponibles"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public List<Libro> buscarLibroCheck(String criterioBusqueda, boolean buscarPorIsbn, boolean buscarPorTitulo, boolean buscarPorAutor) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE ";
        boolean first = true;

        if (buscarPorIsbn) {
            sql += "isbn LIKE ?";
            first = false;
        }

        if (buscarPorTitulo) {
            if (!first) {
                sql += " OR ";
            }
            sql += "titulo LIKE ?";
            first = false;
        }

        if (buscarPorAutor) {
            if (!first) {
                sql += " OR ";
            }
            sql += "autor LIKE ?";
        }

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            int index = 1;

            if (buscarPorIsbn) {
                statement.setString(index++, criterio);
            }

            if (buscarPorTitulo) {
                statement.setString(index++, criterio);
            }

            if (buscarPorAutor) {
                statement.setString(index++, criterio);
            }

            ResultSet rs = statement.executeQuery();

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

    public List<Libro> obtenerLibrosPrestamo() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, e.disponibles " +
                "FROM libros l LEFT JOIN ejemplares e ON l.libro_id = e.libro_id WHERE e.disponibles > 0";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setLibroId(rs.getInt("libro_id"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
                libro.setGenero(rs.getString("genero"));
                libro.setPortada(rs.getBytes("portada"));
                libro.setDisponibles(rs.getInt("disponibles"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    public void actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET isbn = ?, titulo = ?, autor = ?, ano_publicacion = ?, genero = ?, portada = ? WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar(); PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, libro.getIsbn());
            statement.setString(2, libro.getTitulo());
            statement.setString(3, libro.getAutor());
            statement.setInt(4, libro.getAnoPublicacion());
            statement.setString(5, libro.getGenero());
            statement.setBytes(6, libro.getPortada());
            statement.setInt(7, libro.getLibroId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarLibro(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";

        try (Connection con = ConexionDB.conectar(); PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Libro> buscarLibro(String criterioBusqueda) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM libros WHERE isbn LIKE ? OR titulo LIKE ? OR autor LIKE ? OR ano_publicacion LIKE ? OR genero LIKE ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            statement.setString(1, criterio);
            statement.setString(2, criterio);
            statement.setString(3, criterio);
            statement.setString(4, criterio);
            statement.setString(5, criterio);
            ResultSet rs = statement.executeQuery();

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

    public Libro buscarLibroPorId(int libroId) {
        String sql = "SELECT * FROM libros WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, libroId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Libro libro = new Libro();
                libro.setLibroId(rs.getInt("libro_id"));
                libro.setIsbn(rs.getString("isbn"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setAutor(rs.getString("autor"));
                libro.setAnoPublicacion(rs.getInt("ano_publicacion"));
                libro.setGenero(rs.getString("genero"));
                libro.setPortada(rs.getBytes("portada"));
                libro.setDisponibles(rs.getInt("disponibles"));
                return libro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public int encontrarIdLibro(String isbn) {
        String sql = "SELECT libro_id FROM libros WHERE isbn = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("libro_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}