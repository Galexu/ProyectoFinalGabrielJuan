package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ControladorModificarSocio {

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

    private Socio socio;

    private Runnable onSocioChangeListener;

    public void setOnSocioChangeListener(Runnable onLibroChangeListener) {
        this.onSocioChangeListener = onLibroChangeListener;
    }

    @FXML
    void onClickModificar(ActionEvent event) {
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

        socio.setNombre(campoNombre.getText());
        socio.setDireccion(campoDireccion.getText());
        socio.setTelefono(campoTelefono.getText());
        socio.setEmail(campoEmail.getText());
        socio.setSocioFoto(imagenSocio);

        SocioDAO socioDAO = new SocioDAO();
        socioDAO.actualizarSocio(socio);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    public void posicionarSocio(Socio socio) {
        this.socio = socio;

        if (socio != null) {
            imagenSocio = socio.getSocioFoto();

            byte[] portadaBytes = socio.getSocioFoto();
            if (portadaBytes != null) {
                Image image = new Image(new ByteArrayInputStream(portadaBytes));
                imagenSocioView.setImage(image);
            }

            campoNombre.setText(socio.getNombre());
            campoDireccion.setText(socio.getDireccion());
            campoTelefono.setText(socio.getTelefono());
            campoEmail.setText(socio.getEmail());
        }
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

    @FXML
    void initialize() {
        if (socio != null) {
            campoNombre.setText(socio.getNombre());
            campoDireccion.setText(socio.getDireccion());
            campoTelefono.setText(socio.getTelefono());
            campoEmail.setText(socio.getEmail());
        }
    }

    private boolean validarCampos() {
        if (campoNombre.getText().isEmpty() || campoDireccion.getText().isEmpty() || campoTelefono.getText().isEmpty() || campoEmail.getText().isEmpty()) {
            return false;
        }


        if (!campoTelefono.getText().matches("^\\d{9}$")) {
            return false;
        }

        String regexEmail = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        if (!campoEmail.getText().matches(regexEmail)) {
            return false;
        }

        return true;
    }

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, introduzca los campos correctamente.");

        alert.showAndWait();
    }
}