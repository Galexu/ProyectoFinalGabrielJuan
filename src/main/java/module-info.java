module com.iesochoa.gabrieljuan.proyectofinalgabrieljuan {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.iesochoa.gabrieljuan.proyectofinalgabrieljuan to javafx.fxml;
    exports com.iesochoa.gabrieljuan.proyectofinalgabrieljuan;
}