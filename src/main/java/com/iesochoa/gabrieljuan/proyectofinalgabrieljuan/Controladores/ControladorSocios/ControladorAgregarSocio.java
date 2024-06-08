package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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

    private Runnable onSocioChangeListener;

    @FXML
    private StackPane stackPaneImagenSocio;


    public void setOnSocioChangeListener(Runnable onLibroChangeListener) {
        this.onSocioChangeListener = onLibroChangeListener;
    }

    @FXML
    void onClickAgregar(ActionEvent event) {
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

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

    @FXML
    void initialize() {
        stackPaneImagenSocio.getStyleClass().add("border-default");
    }
}