package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import atlantafx.base.theme.Styles;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
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

public class ControladorPrestamoSeleccionarSocio {
    @FXML
    private TableColumn<?, ?> direccionColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nombreColumn;

    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<?, ?> telefonoColumn;

    @FXML
    private ImageView imagenSocioView;

    private OnSocioSelectedListener socioSelectedListener;

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void onClickSeleccionar(ActionEvent event) {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        if (socioSeleccionado != null && socioSelectedListener != null) {
            socioSelectedListener.onSocioSelected(socioSeleccionado);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void onClickMostrarSocios(ActionEvent event) {
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socio = socioDAO.obtenerSocios();

        ObservableList<Socio> observableList = FXCollections.observableArrayList(socio);
        tablaSocios.setItems(observableList);
    }

    @FXML
    void initialize() {
        Styles.toggleStyleClass(tablaSocios, Styles.BORDERED);
        Styles.toggleStyleClass(tablaSocios, Styles.STRIPED);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("socioId"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        tablaSocios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Socio socioSeleccionado = newSelection;
                byte[] portadaBytes = socioSeleccionado.getSocioFoto();
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenSocioView.setImage(image);
                } else {
                    imagenSocioView.setImage(null);
                }
            }
        });
    }

    public interface OnSocioSelectedListener {
        void onSocioSelected(Socio socioSeleccionado);
    }

    public void setOnSocioSelectedListener(OnSocioSelectedListener listener) {
        this.socioSelectedListener = listener;
    }
}
