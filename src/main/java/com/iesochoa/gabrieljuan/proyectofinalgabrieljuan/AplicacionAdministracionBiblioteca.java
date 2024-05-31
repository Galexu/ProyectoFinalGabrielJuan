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
        FXMLLoader fxmlLoader = new FXMLLoader(AplicacionAdministracionBiblioteca.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
        stage.getIcons().add(new Image(AplicacionAdministracionBiblioteca.class.getResourceAsStream("/imagenes/favicon.png")));

        stage.setTitle("Gestion de Biblioteca");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
