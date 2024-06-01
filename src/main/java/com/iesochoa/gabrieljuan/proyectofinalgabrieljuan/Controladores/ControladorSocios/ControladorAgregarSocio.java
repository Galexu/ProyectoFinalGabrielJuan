package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ControladorAgregarSocio {
    @FXML
    private TextField campoDireccion;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoTelefono;

    @FXML
    private ImageView imagenSocioView;

    private byte[] imagenSocio;

    @FXML
    void onClickAgregar(ActionEvent event) {
        Socio socio = new Socio();
        socio.setNombre(campoNombre.getText());
        socio.setDireccion(campoDireccion.getText());
        socio.setTelefono(campoTelefono.getText());
        socio.setEmail(campoEmail.getText());
        socio.setSocioFoto(imagenSocio);;

        SocioDAO socioDAO = new SocioDAO();
        socioDAO.agregarSocio(socio);

        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                imagenSocio = Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(new FileInputStream(selectedFile));
                imagenSocioView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}