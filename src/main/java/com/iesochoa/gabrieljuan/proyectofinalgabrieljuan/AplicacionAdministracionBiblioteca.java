package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;

public class AplicacionAdministracionBiblioteca extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionAdministracionBiblioteca.class.getResource("inicio-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Establecer el tema de la aplicación utilizando el tema Dracula
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        // Añadir un icono a la ventana de la aplicación
        stage.getIcons().add(new Image(AplicacionAdministracionBiblioteca.class.getResourceAsStream("/imagenes/favicon.png")));

        // Establecer el título de la ventana de la aplicación
        stage.setTitle("Gestion de Biblioteca");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
