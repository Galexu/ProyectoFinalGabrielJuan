package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores;

import atlantafx.base.theme.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ControladorInicio {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private Hyperlink guiaUsuarioLink;

    @FXML
    void onClickLightPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    @FXML
    void onClickDarkPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    @FXML
    void onClickDracula(ActionEvent event) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }

    @FXML
    void onClickNordDark(ActionEvent event) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    @FXML
    void onClickLibro(ActionEvent event) {
        loadView("libro-view.fxml");
    }

    @FXML
    void onClickPrestamo(ActionEvent event) {
        loadView("prestamo-view.fxml");
    }

    @FXML
    void onClickSocio(ActionEvent event) {
        loadView("socio-view.fxml");
    }

    private void loadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/" + viewName));
            Node view = loader.load();
            mainPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void abrirEnlace(ActionEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/Galexu?tab=repositories"));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
