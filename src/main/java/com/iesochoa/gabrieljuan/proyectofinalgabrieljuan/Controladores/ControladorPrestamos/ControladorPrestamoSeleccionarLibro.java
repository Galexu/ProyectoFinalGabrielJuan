package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import atlantafx.base.theme.Styles;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ControladorPrestamoSeleccionarLibro {

    @FXML
    private TableColumn<?, ?> anoPublicacionColumn;

    @FXML
    private TableColumn<?, ?> autorColumn;

    @FXML
    private TableColumn<?, ?> generoColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> isbnColumn;

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<?, ?> tituloColumn;

    @FXML
    private ImageView imagenLibroView;

    private OnLibroSelectedListener libroSelectedListener;

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickSeleccionar(ActionEvent event) {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        if (libroSeleccionado != null && libroSelectedListener != null) {
            libroSelectedListener.onLibroSelected(libroSeleccionado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void onClickMostrarLibros(ActionEvent event) {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        ObservableList<Libro> observableList = FXCollections.observableArrayList(libros);
        tablaLibros.setItems(observableList);
    }

    @FXML
    void initialize() {
        Styles.toggleStyleClass(tablaLibros, Styles.BORDERED);
        Styles.toggleStyleClass(tablaLibros, Styles.STRIPED);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("libroId"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        anoPublicacionColumn.setCellValueFactory(new PropertyValueFactory<>("anoPublicacion"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));

        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Libro libroSeleccionado = newSelection;
                byte[] portadaBytes = libroSeleccionado.getPortada();
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    imagenLibroView.setImage(null);
                }
            }
        });
    }

    public interface OnLibroSelectedListener {
        void onLibroSelected(Libro libroSeleccionado);
    }

    public void setOnLibroSelectedListener(OnLibroSelectedListener listener) {
        this.libroSelectedListener = listener;
    }
}
