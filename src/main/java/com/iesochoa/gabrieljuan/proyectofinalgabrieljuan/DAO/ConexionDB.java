package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://db-biblioteca-gabriel.cbeqau42k82u.us-east-1.rds.amazonaws.com:3306/ProyectoFinalGabrielJuan";
    private static final String USER = "galexu";
    private static final String PASSWORD = "12341234";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}