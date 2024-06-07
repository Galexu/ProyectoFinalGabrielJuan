module com.iesochoa.gabrieljuan.proyectofinalgabrieljuan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;
    requires java.desktop;
    requires com.google.gson;
    requires jakarta.xml.bind;
    requires opencsv;

    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores to javafx.fxml;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo to javafx.base, com.google.gson, opencsv;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo to com.google.gson, opencsv;
}