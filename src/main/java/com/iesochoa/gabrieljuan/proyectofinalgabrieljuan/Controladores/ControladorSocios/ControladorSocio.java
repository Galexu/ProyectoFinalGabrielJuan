package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import atlantafx.base.theme.*;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros.ControladorAgregarLibro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ControladorSocio {

    @FXML
    private Label labelNombreSocio;

    @FXML
    private Button InicioButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<Socio, Integer> idColumn;

    @FXML
    private TableColumn<Socio, String> nombreColumn;

    @FXML
    private TableColumn<Socio, String> direccionColumn;

    @FXML
    private TableColumn<Socio, String> telefonoColumn;

    @FXML
    private TableColumn<Socio, String> emailColumn;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private ImageView imagenSocioView;

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
        cargarVista("libro-view.fxml");
    }

    @FXML
    void onClickPrestamo(ActionEvent event) {
        cargarVista("prestamo-view.fxml");
    }

    @FXML
    void onClickSocio(ActionEvent event) {
        cargarVista("socio-view.fxml");
    }

    @FXML
    void onClickMostrarSocios(ActionEvent event) {
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socios = socioDAO.obtenerSocios();

        ObservableList<Socio> observableList = FXCollections.observableArrayList(socios);
        tablaSocios.setItems(observableList);
    }

    @FXML
    void onClickAgregarSocio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-socio-view.fxml"));
            Parent root = loader.load();

            ControladorAgregarSocio controlador = loader.getController();
            controlador.setOnSocioChangeListener(this::mostrarSocios);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickBorrarSocio(ActionEvent event) {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();

        if (socioSeleccionado != null) {

            PrestamoDAO prestamoDAO = new PrestamoDAO();
            if (prestamoDAO.existePrestamoParaSocio(socioSeleccionado.getSocioId())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se puede borrar el socio porque existe un préstamo para este socio, primero elimine el prestamo correspondiente si asi lo desea.");

                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de borrado");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que quieres borrar el socio seleccionado?");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

            Optional<ButtonType> result = alert.showAndWait();


            if (result.get() == ButtonType.OK) {
                SocioDAO socioDAO = new SocioDAO();
                socioDAO.eliminarSocio(String.valueOf(socioSeleccionado.getSocioId()));
                onClickMostrarSocios(event);
            }
        }
    }

    @FXML
    void onClickModificarSocio(ActionEvent event) {
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();

        if (socioSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-socio-view.fxml"));
                Parent root = loader.load();

                ControladorModificarSocio controlador = loader.getController();
                controlador.posicionarSocio(socioSeleccionado);
                controlador.setOnSocioChangeListener(this::mostrarSocios);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClickBuscar(ActionEvent event) {
        String criterioBusqueda = campoBusqueda.getText();

        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socios = socioDAO.buscarSocios(criterioBusqueda);

        ObservableList<Socio> observableList = FXCollections.observableArrayList(socios);
        tablaSocios.setItems(observableList);
    }

    private void cargarVista(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/" + viewName));
            Node view = loader.load();
            mainPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                labelNombreSocio.setText(socioSeleccionado.getNombre());
                byte[] portadaBytes = socioSeleccionado.getSocioFoto();
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenSocioView.setImage(image);
                } else {
                    imagenSocioView.setImage(null);
                }
            }
        });

        mostrarSocios();
    }

    void mostrarSocios() {
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socios = socioDAO.obtenerSocios();

        ObservableList<Socio> observableList = FXCollections.observableArrayList(socios);
        tablaSocios.setItems(observableList);
    }

    @FXML
    void onClickInicio(ActionEvent event) {
        cargarVista("inicio-view.fxml");
    }
}

