module com.iesochoa.gabrieljuan.proyectofinalgabrieljuan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires atlantafx.base;

    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.controlador;
    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.controlador to javafx.fxml;
}