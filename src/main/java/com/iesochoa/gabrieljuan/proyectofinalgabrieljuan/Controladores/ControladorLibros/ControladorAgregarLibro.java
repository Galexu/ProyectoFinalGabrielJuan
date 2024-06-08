package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Ejemplar;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
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

public class ControladorAgregarLibro {

    @FXML
    private TextField campoIsbn;

    @FXML
    private TextField campoTitulo;

    @FXML
    private TextField campoAutor;

    @FXML
    private TextField campoAnoPublicacion;

    @FXML
    private TextField campoEjemplares;

    @FXML
    private MenuButton generoMenuButton;

    @FXML
    private ImageView imagenLibroView;

    private byte[] imagenLibro;

    private Runnable onLibroChangeListener;

    @FXML
    private StackPane stackPaneImagenLibro;

    public void setOnLibroChangeListener(Runnable onLibroChangeListener) {
        this.onLibroChangeListener = onLibroChangeListener;
    }

    @FXML
    void onClickAgregar(ActionEvent event) {
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

        Libro libro = new Libro();
        Ejemplar ejemplar = new Ejemplar();
        LibroDAO libroDAO = new LibroDAO();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();

        libro.setIsbn(campoIsbn.getText());
        libro.setTitulo(campoTitulo.getText());
        libro.setAutor(campoAutor.getText());
        libro.setAnoPublicacion(Integer.parseInt(campoAnoPublicacion.getText()));
        libro.setGenero(generoMenuButton.getText());
        libro.setPortada(imagenLibro);

        libroDAO.agregarLibro(libro);
        int libroId = libroDAO.encontrarIdLibro(campoIsbn.getText());
        ejemplar.setDisponibles(Integer.parseInt(campoEjemplares.getText()));
        ejemplar.setCopiaId(libroId);
        ejemplarDAO.agregarEjemplar(ejemplar);

        Stage stage = (Stage) campoIsbn.getScene().getWindow();
        stage.close();

        if (onLibroChangeListener != null) {
            onLibroChangeListener.run();
        }
    }

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        if (onLibroChangeListener != null) {
            onLibroChangeListener.run();
        }
    }

    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                imagenLibro = Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(new FileInputStream(selectedFile));
                imagenLibroView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        stackPaneImagenLibro.getStyleClass().add("border-default");

        for (MenuItem menuItem : generoMenuButton.getItems()) {
            menuItem.setOnAction(event -> {
                generoMenuButton.setText(menuItem.getText());
            });
        }
    }

    private boolean validarCampos() {
        if (campoIsbn.getText().isEmpty() || campoTitulo.getText().isEmpty() || campoAutor.getText().isEmpty() || campoAnoPublicacion.getText().isEmpty() || campoEjemplares.getText().isEmpty()) {
            return false;
        }

        try {
            Integer.parseInt(campoAnoPublicacion.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        try {
            Integer.parseInt(campoEjemplares.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        if (generoMenuButton.getText().equals("Seleccionar Genero")) {
            return false;
        }

        return true;
    }

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, introduzca los campos correctamente.");

        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

        alert.showAndWait();
    }
}