package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Ejemplar;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;

import java.sql.*;

public class EjemplarDAO {

    /**
     * Agrega un nuevo ejemplar a la base de datos.
     *
     * @param ejemplar El ejemplar a agregar.
     */
    public void agregarEjemplar(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplares (libro_id, disponibles) VALUES (?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, ejemplar.getCopiaId());
            statement.setInt(2, ejemplar.getDisponibles());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Actualiza un ejemplar existente en la base de datos.
     *
     * @param ejemplar El ejemplar a actualizar.
     */
    public void actualizarEjemplar(Ejemplar ejemplar) {
        String sql = "UPDATE ejemplares SET disponibles = ? WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, ejemplar.getDisponibles());
            statement.setInt(2, ejemplar.getCopiaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtiene el número de ejemplares disponibles de un libro específico.
     *
     * @param isbn El ISBN del libro.
     * @return El número de ejemplares disponibles.
     */
    public int obtenerDisponibles(String isbn) {
        String sql = "SELECT disponibles FROM ejemplares WHERE libro_id = (SELECT libro_id FROM libros WHERE isbn = ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("disponibles");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Elimina un ejemplar de la base de datos.
     *
     * @param copiaId El ID de la copia del ejemplar a eliminar.
     */
    public void eliminarEjemplar(int copiaId) {
        String sql = "DELETE FROM ejemplares WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, copiaId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reduce en uno el número de ejemplares disponibles de un libro específico.
     *
     * @param isbn El ISBN del libro.
     */
    public void reducirEjemplar(String isbn) {
        String sql = "UPDATE ejemplares SET disponibles = disponibles - 1 WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encuentra el ID de un ejemplar específico.
     *
     * @param isbn El ISBN del libro.
     * @return El ID del ejemplar.
     */
    public int encontrarIdEjemplar(String isbn) {
        String sql = "SELECT copia_id FROM ejemplares WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, isbn);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("copia_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Obtiene un libro por el ID de su copia.
     *
     * @param copiaId El ID de la copia del libro.
     * @return El libro correspondiente al ID de la copia.
     */
    public Libro obtenerLibroPorCopiaId(int copiaId) {
        String sql = "SELECT * FROM libros WHERE libro_id = (SELECT libro_id FROM ejemplares WHERE copia_id = ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, copiaId);
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
                return libro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}