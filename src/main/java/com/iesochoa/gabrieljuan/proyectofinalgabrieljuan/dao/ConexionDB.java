package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/proyectofinalgabrieljuan";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

//    public static void conectar() throws SQLException {
//        try (final Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
//            System.out.println(connection.getCatalog());
//        } catch (SQLException ex) {
//            System.out.println("SQLException: " + ex.getMessage());
//            System.out.println("SQLState: " + ex.getSQLState());
//            System.out.println("VendorError: " + ex.getErrorCode());
//        }
//    }

    public static void main(String[] args) {
        try (final Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}