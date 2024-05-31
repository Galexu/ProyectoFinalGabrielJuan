package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo.Ejemplar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO {
    public void agregarEjemplar(Ejemplar ejemplar) {
        String sql = "INSERT INTO ejemplares (isbn, disponible) VALUES (?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, ejemplar.getIsbn());
            statement.setBoolean(2, ejemplar.isDisponible());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Ejemplar> obtenerEjemplares() {
        List<Ejemplar> ejemplares = new ArrayList<>();
        String sql = "SELECT * FROM ejemplares";

        try (Connection con = ConexionDB.conectar();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Ejemplar ejemplar = new Ejemplar();
                ejemplar.setCopiaId(rs.getInt("copia_id"));
                ejemplar.setIsbn(rs.getString("isbn"));
                ejemplar.setDisponible(rs.getBoolean("disponible"));
                ejemplares.add(ejemplar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ejemplares;
    }

    public void actualizarEjemplar(Ejemplar ejemplar) {
        String sql = "UPDATE ejemplares SET isbn = ?, disponible = ? WHERE copia_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, ejemplar.getIsbn());
            statement.setBoolean(2, ejemplar.isDisponible());
            statement.setInt(3, ejemplar.getCopiaId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void eliminarEjemplar(int copiaId) {
        String sql = "DELETE FROM ejemplares WHERE copia_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, copiaId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        Ejemplar ejemplar = new Ejemplar(1, "1234567890123", true);
        ejemplarDAO.agregarEjemplar(ejemplar);
        System.out.println(ejemplarDAO.obtenerEjemplares());
        ejemplar.setDisponible(false);
        ejemplarDAO.actualizarEjemplar(ejemplar);
        System.out.println(ejemplarDAO.obtenerEjemplares());
        ejemplarDAO.eliminarEjemplar(1);
        System.out.println(ejemplarDAO.obtenerEjemplares());
    }
}