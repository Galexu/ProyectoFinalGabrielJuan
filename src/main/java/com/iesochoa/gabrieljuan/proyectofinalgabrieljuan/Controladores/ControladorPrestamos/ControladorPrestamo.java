package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import atlantafx.base.theme.*;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros.ControladorAgregarLibro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros.ControladorModificarLibro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios.ControladorAgregarSocio;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Prestamo;
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
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ControladorPrestamo {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private TableColumn<Prestamo, String> estadoColumn;

    @FXML
    private TableColumn<Prestamo, Date> fechaDevolucionColumn;

    @FXML
    private TableColumn<Prestamo, String> libroColunn;

    @FXML
    private TableColumn<Prestamo, Date> socioColumn;

    @FXML
    private TableColumn<Prestamo, Date> fechaLimiteColumn;

    @FXML
    private TableColumn<Prestamo, Date> fechaPrestamoColumn;

    @FXML
    private TableColumn<Prestamo, Integer> idCopiaColumn;

    @FXML
    private TableColumn<Prestamo, Integer> idPrestamoColumn;

    @FXML
    private TableColumn<Prestamo, Integer> idSocioColumn;

    @FXML
    private TableView<Prestamo> tablaPrestamos;
    @FXML
    private ImageView imagenLibroView;

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
    void onClickAgregarPrestamo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-view.fxml"));
            Parent root = loader.load();

            ControladorAgregarPrestamo controlador = loader.getController();
            controlador.setOnPrestamoChangeListener(this::mostrarPrestamos);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickBorrarPrestamo(ActionEvent event) {
        Prestamo prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        if (prestamoSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de borrado");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que quieres borrar el prestamo seleccionado?");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                prestamoDAO.eliminarPrestamo(prestamoSeleccionado.getPrestamoId());
                onClickMostrarPrestamos(event);
            }
        }

        mostrarPrestamos();
    }

    @FXML
    void onClickBuscar(ActionEvent event) {
        String criterioBusqueda = campoBusqueda.getText();

        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.buscarPrestamos(criterioBusqueda);

        ObservableList<Prestamo> observableList = FXCollections.observableArrayList(prestamos);
        tablaPrestamos.setItems(observableList);
    }

    @FXML
    void onClickModificarPrestamo(ActionEvent event) {
        Prestamo prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        if (prestamoSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-prestamo-view.fxml"));
                Parent root = loader.load();

                ControladorModificarPrestamo controlador = loader.getController();
                controlador.posicionarPrestamo(prestamoSeleccionado);
                controlador.setOnPrestamoChangeListener(this::mostrarPrestamos);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClickMostrarPrestamos(ActionEvent event) {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();

        ObservableList<Prestamo> observableList = FXCollections.observableArrayList(prestamos);
        tablaPrestamos.setItems(observableList);
    }

    @FXML
    void initialize() {
        Styles.toggleStyleClass(tablaPrestamos, Styles.BORDERED);
        Styles.toggleStyleClass(tablaPrestamos, Styles.STRIPED);
        idPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("prestamoId"));
        libroColunn.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));
        socioColumn.setCellValueFactory(new PropertyValueFactory<>("nombreSocio"));
        idCopiaColumn.setCellValueFactory(new PropertyValueFactory<>("copiaId"));
        idSocioColumn.setCellValueFactory(new PropertyValueFactory<>("socioId"));
        fechaPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        fechaLimiteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Prestamo prestamoSeleccionado = newSelection;
                EjemplarDAO ejemplarDAO = new EjemplarDAO();
                SocioDAO socioDAO = new SocioDAO();

                Libro libroSeleccionado = ejemplarDAO.obtenerLibroPorCopiaId(prestamoSeleccionado.getCopiaId());
                Socio socioSeleccionado = socioDAO.obtenerSocioPorId(prestamoSeleccionado.getSocioId());

                byte[] portadaBytes = libroSeleccionado.getPortada();
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    imagenLibroView.setImage(null);
                }

                byte[] socioBytes = socioSeleccionado.getSocioFoto();
                if (socioBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(socioBytes));
                    imagenSocioView.setImage(image);
                } else {
                    imagenSocioView.setImage(null);
                }
            }
        });

        mostrarPrestamos();
    }


    void mostrarPrestamos() {
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();

        ObservableList<Prestamo> observableList = FXCollections.observableArrayList(prestamos);
        tablaPrestamos.setItems(observableList);
    }

}

