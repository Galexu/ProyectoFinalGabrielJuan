package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores;

import atlantafx.base.theme.*;
import atlantafx.base.util.Animations;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javafx.scene.image.ImageView;

public class ControladorInicio {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private Hyperlink guiaUsuarioLink;

    @FXML
    private ImageView logoPortada;

    /**
     * Cambia el tema de la aplicación a Light Prime.
     * @param event
     */
    @FXML
    void onClickLightPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Dark Prime.
     * @param event
     */
    @FXML
    void onClickDarkPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Dracula.
     * @param event
     */
    @FXML
    void onClickDracula(ActionEvent event) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Nord Dark.
     * @param event
     */
    @FXML
    void onClickNordDark(ActionEvent event) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    /**
     * Carga la vista de libro.
     * @param event
     */
    @FXML
    void onClickLibro(ActionEvent event) {
        cargarVista("libro-view.fxml");
    }

    /**
     * Carga la vista de prestamo.
     * @param event
     */
    @FXML
    void onClickPrestamo(ActionEvent event) {
        cargarVista("prestamo-view.fxml");
    }

    /**
     * Carga la vista de socio.
     * @param event
     */
    @FXML
    void onClickSocio(ActionEvent event) {
        cargarVista("socio-view.fxml");
    }

    /**
     * Este método se utiliza para abrir un enlace en el navegador predeterminado del sistema.
     * Primero, obtiene el escritorio del sistema y luego intenta abrir el URI especificado en el navegador.
     * Si se produce una IOException o URISyntaxException durante la apertura del URI, imprime la traza de la excepción.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void abrirEnlace(ActionEvent event) {
        try {
            // Obtener el escritorio del sistema y abrir el URI especificado en el navegador
            Desktop.getDesktop().browse(new URI("https://github.com/Galexu/Readme-Proyecto-Final"));
        } catch (IOException | URISyntaxException e) {
            // Imprimir la traza de la excepción si se produce una IOException o URISyntaxException
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se inicializa el controlador después de que se haya cargado su archivo FXML.
     * Primero, crea dos animaciones para el logo de la portada.
     * Luego, establece que cuando la primera animación termine, la segunda animación comienza desde el principio.
     * Finalmente, inicia la primera animación desde el principio.
     */
    @FXML
    void initialize() {
        // Crear dos animaciones para el logo de la portada
        Timeline a = Animations.fadeInDown(logoPortada, Duration.seconds(10));
        Timeline b = Animations.pulse(logoPortada);

        // Establecer que cuando la primera animación termine, la segunda animación comienza desde el principio
        a.setOnFinished(event -> b.playFromStart());
        // Iniciar la primera animación desde el principio
        a.playFromStart();
    }

    /**
     * Este método se utiliza para cargar una vista en el panel principal.
     * Primero, crea un FXMLLoader y establece su ubicación a la ruta de la vista especificada.
     * Luego, carga la vista y la establece como el único hijo del panel principal.
     * Si se produce una IOException durante la carga de la vista, imprime la traza de la excepción.
     *
     * @param viewName el nombre de la vista a cargar. Debe ser el nombre de un archivo .fxml en el paquete /com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/.
     */
    private void cargarVista(String viewName) {
        try {
            // Crear un FXMLLoader y establecer su ubicación a la ruta de la vista especificada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/" + viewName));
            // Cargar la vista
            Node view = loader.load();
            // Establecer la vista como el único hijo del panel principal
            mainPane.getChildren().setAll(view);
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }
}
