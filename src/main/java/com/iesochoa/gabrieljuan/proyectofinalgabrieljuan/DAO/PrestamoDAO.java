package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Prestamo;

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

    public List<Prestamo> buscarPrestamoCheck(String criterioBusqueda, boolean buscarPorId, boolean buscarPorCopiaId, boolean buscarPorSocioId, boolean buscarPorFechaPrestamo, boolean buscarPorFechaDevolucion, boolean buscarPorFechaLimite, boolean buscarPorEstado) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT p.*, l.titulo, s.nombre FROM prestamos p " +
                "JOIN ejemplares e ON p.copia_id = e.copia_id " +
                "JOIN libros l ON e.libro_id = l.libro_id " +
                "JOIN socios s ON p.socio_id = s.socio_id WHERE ";

        boolean first = true;

        if (buscarPorId) {
            sql += "p.prestamo_id LIKE ?";
            first = false;
        }

        if (buscarPorCopiaId) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.copia_id LIKE ?";
        }

        if (buscarPorSocioId) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.socio_id LIKE ?";
        }

        if (buscarPorFechaPrestamo) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.fecha_prestamo LIKE ?";
        }

        if (buscarPorFechaDevolucion) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.fecha_devolucion LIKE ?";
        }

        if (buscarPorFechaLimite) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.fecha_limite LIKE ?";
        }

        if (buscarPorEstado) {
            if (!first) {
                sql += " OR ";
            }
            sql += "p.estado LIKE ?";
        }

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            int index = 1;

            if (buscarPorId) {
                statement.setString(index++, criterio);
            }

            if (buscarPorCopiaId) {
                statement.setString(index++, criterio);
            }

            if (buscarPorSocioId) {
                statement.setString(index++, criterio);
            }

            if (buscarPorFechaPrestamo) {
                statement.setString(index++, criterio);
            }

            if (buscarPorFechaDevolucion) {
                statement.setString(index++, criterio);
            }

            if (buscarPorFechaLimite) {
                statement.setString(index++, criterio);
            }

            if (buscarPorEstado) {
                statement.setString(index++, criterio);
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setPrestamoId(rs.getInt("prestamo_id"));
                prestamo.setCopiaId(rs.getInt("copia_id"));
                prestamo.setSocioId(rs.getInt("socio_id"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                prestamo.setEstado(rs.getString("estado"));
                prestamo.setTituloLibro(rs.getString("titulo"));
                prestamo.setNombreSocio(rs.getString("nombre"));
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public boolean existePrestamoParaLibro(int libroId) {
        String query = "SELECT COUNT(*) FROM prestamos WHERE copia_id IN (SELECT copia_id FROM ejemplares WHERE libro_id = ?)";
        try (PreparedStatement statement = ConexionDB.conectar().prepareStatement(query)) {
            statement.setInt(1, libroId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existePrestamoParaSocio(int socioId) {
        String query = "SELECT COUNT(*) FROM prestamos WHERE socio_id = ?";
        try (PreparedStatement statement = ConexionDB.conectar().prepareStatement(query)) {
            statement.setInt(1, socioId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Prestamo> obtenerPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT prestamos.*, libros.titulo AS tituloLibro, socios.nombre AS nombreSocio " +
                "FROM prestamos " +
                "JOIN ejemplares ON prestamos.copia_id = ejemplares.copia_id " +
                "JOIN libros ON ejemplares.libro_id = libros.libro_id " +
                "JOIN socios ON prestamos.socio_id = socios.socio_id";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setPrestamoId(rs.getInt("prestamo_id"));
                prestamo.setCopiaId(rs.getInt("copia_id"));
                prestamo.setSocioId(rs.getInt("socio_id"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                prestamo.setEstado(rs.getString("estado"));
                prestamo.setTituloLibro(rs.getString("tituloLibro"));
                prestamo.setNombreSocio(rs.getString("nombreSocio"));
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return prestamos;
    }

    public List<Prestamo> buscarPrestamos(String criterioBusqueda) {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT prestamos.*, libros.titulo AS tituloLibro, socios.nombre AS nombreSocio " +
                "FROM prestamos " +
                "JOIN ejemplares ON prestamos.copia_id = ejemplares.copia_id " +
                "JOIN libros ON ejemplares.libro_id = libros.libro_id " +
                "JOIN socios ON prestamos.socio_id = socios.socio_id " +
                "WHERE prestamos.prestamo_id LIKE ? OR prestamos.copia_id LIKE ? OR prestamos.socio_id LIKE ? OR prestamos.fecha_prestamo LIKE ? OR prestamos.fecha_devolucion LIKE ? OR prestamos.fecha_limite LIKE ? OR prestamos.estado LIKE ? OR libros.titulo LIKE ? OR socios.nombre LIKE ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            for (int i = 1; i <= 9; i++) {
                statement.setString(i, criterio);
            }
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo();
                prestamo.setPrestamoId(rs.getInt("prestamo_id"));
                prestamo.setCopiaId(rs.getInt("copia_id"));
                prestamo.setSocioId(rs.getInt("socio_id"));
                prestamo.setFechaPrestamo(rs.getDate("fecha_prestamo"));
                prestamo.setFechaDevolucion(rs.getDate("fecha_devolucion"));
                prestamo.setFechaLimite(rs.getDate("fecha_limite"));
                prestamo.setEstado(rs.getString("estado"));
                prestamo.setTituloLibro(rs.getString("tituloLibro"));
                prestamo.setNombreSocio(rs.getString("nombreSocio"));
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
}