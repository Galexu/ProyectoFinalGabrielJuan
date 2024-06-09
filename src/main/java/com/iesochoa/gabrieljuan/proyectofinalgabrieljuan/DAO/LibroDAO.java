package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    /**
     * Agrega un nuevo libro a la base de datos.
     *
     * @param libro El libro a agregar.
     * @return El ID del libro generado.
     */
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

    /**
     * Obtiene todos los libros de la base de datos.
     *
     * @return Una lista de libros.
     */
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

    /**
     * Busca libros en la base de datos según varios criterios.
     *
     * @param criterioBusqueda El criterio de búsqueda.
     * @param buscarPorIsbn Si se debe buscar por ISBN.
     * @param buscarPorTitulo Si se debe buscar por título.
     * @param buscarPorAutor Si se debe buscar por autor.
     * @param buscarPorAno Si se debe buscar por año de publicación.
     * @param buscarPorGenero Si se debe buscar por género.
     * @return Una lista de libros que coinciden con los criterios de búsqueda.
     */
    public List<Libro> buscarLibroCheck(String criterioBusqueda, boolean buscarPorIsbn, boolean buscarPorTitulo, boolean buscarPorAutor, boolean buscarPorAno, boolean buscarPorGenero) {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT l.*, e.disponibles FROM libros l LEFT JOIN ejemplares e ON l.libro_id = e.libro_id WHERE ";

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
        }

        if (buscarPorAutor) {
            if (!first) {
                sql += " OR ";
            }
            sql += "autor LIKE ?";
        }

        if (buscarPorAno) {
            if (!first) {
                sql += " OR ";
            }
            sql += "ano_publicacion LIKE ?";
        }

        if (buscarPorGenero) {
            if (!first) {
                sql += " OR ";
            }
            sql += "genero LIKE ?";
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

            if (buscarPorAno) {
                statement.setString(index++, criterio);
            }

            if (buscarPorGenero) {
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
                libro.setDisponibles(rs.getInt("disponibles"));
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    /**
     * Obtiene los libros disponibles para préstamo.
     *
     * @return Una lista de libros disponibles para préstamo.
     */
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

    /**
     * Actualiza un libro existente en la base de datos.
     *
     * @param libro El libro a actualizar.
     */
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

    /**
     * Elimina un libro de la base de datos.
     *
     * @param isbn El ISBN del libro a eliminar.
     */
    public void eliminarLibro(String isbn) {
        String sql = "DELETE FROM libros WHERE isbn = ?";

        try (Connection con = ConexionDB.conectar(); PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca libros en la base de datos según un criterio de búsqueda.
     *
     * @param criterioBusqueda El criterio de búsqueda.
     * @return Una lista de libros que coinciden con el criterio de búsqueda.
     */
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

    /**
     * Busca el ID de un libro por su ISBN.
     *
     * @param isbn El ISBN del libro.
     * @return El ID del libro.
     */
    public int buscarLibroIdPorIsbn(String isbn) {
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