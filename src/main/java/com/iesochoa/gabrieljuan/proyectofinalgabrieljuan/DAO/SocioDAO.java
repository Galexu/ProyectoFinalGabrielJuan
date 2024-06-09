package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocioDAO {

    /**
     * Agrega un nuevo socio a la base de datos.
     *
     * @param socio El socio a agregar.
     */
    public void agregarSocio(Socio socio) {
        String sql = "INSERT INTO socios (socio_id, nombre, direccion, telefono, email, socio_foto) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, socio.getSocioId());
            statement.setString(2, socio.getNombre());
            statement.setString(3, socio.getDireccion());
            statement.setString(4, socio.getTelefono());
            statement.setString(5, socio.getEmail());
            statement.setBytes(6, socio.getSocioFoto());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca socios en la base de datos según varios criterios.
     *
     * @param criterioBusqueda El criterio de búsqueda.
     * @param buscarPorId Si se debe buscar por ID del socio.
     * @param buscarPorNombre Si se debe buscar por nombre.
     * @param buscarPorDireccion Si se debe buscar por dirección.
     * @param buscarPorTelefono Si se debe buscar por teléfono.
     * @param buscarPorEmail Si se debe buscar por email.
     * @return Una lista de socios que coinciden con los criterios de búsqueda.
     */
    public List<Socio> buscarSocioCheck(String criterioBusqueda, boolean buscarPorId, boolean buscarPorNombre, boolean buscarPorDireccion, boolean buscarPorTelefono, boolean buscarPorEmail) {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios WHERE ";

        boolean first = true;

        if (buscarPorId) {
            sql += "socio_id LIKE ?";
            first = false;
        }

        if (buscarPorNombre) {
            if (!first) {
                sql += " OR ";
            }
            sql += "nombre LIKE ?";
            first = false;
        }

        if (buscarPorDireccion) {
            if (!first) {
                sql += " OR ";
            }
            sql += "direccion LIKE ?";
            first = false;
        }

        if (buscarPorTelefono) {
            if (!first) {
                sql += " OR ";
            }
            sql += "telefono LIKE ?";
            first = false;
        }

        if (buscarPorEmail) {
            if (!first) {
                sql += " OR ";
            }
            sql += "email LIKE ?";
        }

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            int index = 1;

            if (buscarPorId) {
                statement.setString(index++, criterio);
            }

            if (buscarPorNombre) {
                statement.setString(index++, criterio);
            }

            if (buscarPorDireccion) {
                statement.setString(index++, criterio);
            }

            if (buscarPorTelefono) {
                statement.setString(index++, criterio);
            }

            if (buscarPorEmail) {
                statement.setString(index++, criterio);
            }

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Socio socio = new Socio();
                socio.setSocioId(rs.getInt("socio_id"));
                socio.setNombre(rs.getString("nombre"));
                socio.setDireccion(rs.getString("direccion"));
                socio.setTelefono(rs.getString("telefono"));
                socio.setEmail(rs.getString("email"));
                socio.setSocioFoto(rs.getBytes("socio_foto"));
                socios.add(socio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socios;
    }


    /**
     * Obtiene todos los socios de la base de datos.
     *
     * @return Una lista de socios.
     */
    public List<Socio> obtenerSocios() {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios";

        try (Connection con = ConexionDB.conectar();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                Socio socio = new Socio();
                socio.setSocioId(rs.getInt("socio_id"));
                socio.setNombre(rs.getString("nombre"));
                socio.setDireccion(rs.getString("direccion"));
                socio.setTelefono(rs.getString("telefono"));
                socio.setEmail(rs.getString("email"));
                socio.setSocioFoto(rs.getBytes("socio_foto"));
                socios.add(socio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socios;
    }

    /**
     * Actualiza un socio existente en la base de datos.
     *
     * @param socio El socio a actualizar.
     */
    public void actualizarSocio(Socio socio) {
        String sql = "UPDATE socios SET nombre = ?, direccion = ?, telefono = ?, email = ?, socio_foto = ? WHERE socio_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, socio.getNombre());
            statement.setString(2, socio.getDireccion());
            statement.setString(3, socio.getTelefono());
            statement.setString(4, socio.getEmail());
            statement.setBytes(5, socio.getSocioFoto());
            statement.setInt(6, socio.getSocioId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Elimina un socio de la base de datos.
     *
     * @param socioId El ID del socio a eliminar.
     */
    public void eliminarSocio(String socioId) {
        String sql = "DELETE FROM socios WHERE socio_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setString(1, socioId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Busca socios en la base de datos según un criterio de búsqueda.
     *
     * @param criterioBusqueda El criterio de búsqueda.
     * @return Una lista de socios que coinciden con el criterio de búsqueda.
     */
    public List<Socio> buscarSocios(String criterioBusqueda) {
        List<Socio> socios = new ArrayList<>();
        String sql = "SELECT * FROM socios WHERE nombre LIKE ? OR direccion LIKE ? OR telefono LIKE ? OR email LIKE ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            String criterio = "%" + criterioBusqueda + "%";
            statement.setString(1, criterio);
            statement.setString(2, criterio);
            statement.setString(3, criterio);
            statement.setString(4, criterio);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Socio socio = new Socio();
                socio.setSocioId(rs.getInt("socio_id"));
                socio.setNombre(rs.getString("nombre"));
                socio.setDireccion(rs.getString("direccion"));
                socio.setTelefono(rs.getString("telefono"));
                socio.setEmail(rs.getString("email"));
                socios.add(socio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return socios;
    }

    /**
     * Obtiene un socio por su ID.
     *
     * @param socioId El ID del socio.
     * @return El socio correspondiente al ID.
     */
    public Socio obtenerSocioPorId(int socioId) {
        String sql = "SELECT * FROM socios WHERE socio_id = ?";

        try (Connection con = ConexionDB.conectar();
             PreparedStatement statement = con.prepareStatement(sql)) {
            statement.setInt(1, socioId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Socio socio = new Socio();
                socio.setSocioId(rs.getInt("socio_id"));
                socio.setNombre(rs.getString("nombre"));
                socio.setSocioFoto(rs.getBytes("socio_foto"));
                return socio;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}