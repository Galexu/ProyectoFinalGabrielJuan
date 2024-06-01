package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Ejemplar;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;

import java.sql.*;

public class EjemplarDAO {
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

    public Libro obtenerLibroPorId(int libroId) {
        String sql = "SELECT * FROM libros WHERE libro_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, libroId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Libro libro = new Libro();
                libro.setLibroId(rs.getInt("libro_id"));
                libro.setTitulo(rs.getString("titulo"));
                libro.setPortada(rs.getBytes("portada"));
                // Set other Libro properties as needed
                return libro;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}