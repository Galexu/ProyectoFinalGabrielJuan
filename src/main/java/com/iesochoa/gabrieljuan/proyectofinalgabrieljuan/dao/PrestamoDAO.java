package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.modelo.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    public void agregarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO prestamos (copia_id, socio_id, fecha_prestamo, fecha_devolucion, fecha_limite, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, prestamo.getCopiaId());
            pstmt.setInt(2, prestamo.getSocioId());
            pstmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            pstmt.setDate(4, new java.sql.Date(prestamo.getFechaDevolucion().getTime()));
            pstmt.setDate(5, new java.sql.Date(prestamo.getFechaLimite().getTime()));
            pstmt.setString(6, prestamo.getEstado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Prestamo> obtenerPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM prestamos";

        try (Connection conn = ConexionDB.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setPrestamoId(rs.getInt("prestamo_id"));
                prestamo.setCopiaId(rs.getInt("copia_id"));
                prestamo.setSocioId(rs.getInt("socio_id"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                prestamo.setEstado(rs.getString("estado"));
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }

    public void actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE prestamos SET copia_id = ?, socio_id = ?, fecha_prestamo = ?, fecha_devolucion = ?, fecha_limite = ?, estado = ? WHERE prestamo_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, prestamo.getCopiaId());
            statement.setInt(2, prestamo.getSocioId());
            statement.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            statement.setDate(4, new java.sql.Date(prestamo.getFechaDevolucion().getTime()));
            statement.setDate(5, new java.sql.Date(prestamo.getFechaLimite().getTime()));
            statement.setString(6, prestamo.getEstado());
            statement.setInt(7, prestamo.getPrestamoId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarPrestamo(int prestamoId) {
        String sql = "DELETE FROM prestamos WHERE prestamo_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, prestamoId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        Prestamo prestamo = new Prestamo();
        prestamo.setCopiaId(1);
        prestamo.setSocioId(1);
        prestamo.setFechaPrestamo(new Date(System.currentTimeMillis()));
        prestamo.setFechaDevolucion(new Date(System.currentTimeMillis()));
        prestamo.setFechaLimite(new Date(System.currentTimeMillis()));
        prestamo.setEstado("Prestado");

        prestamoDAO.agregarPrestamo(prestamo);
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }

        prestamo.setEstado("Devuelto");
        prestamoDAO.actualizarPrestamo(prestamo);
        prestamos = prestamoDAO.obtenerPrestamos();
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }

        prestamoDAO.eliminarPrestamo(prestamo.getPrestamoId());
        prestamos = prestamoDAO.obtenerPrestamos();
        for (Prestamo p : prestamos) {
            System.out.println(p);
        }
    }
}