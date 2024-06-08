package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import atlantafx.base.theme.*;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros.ControladorAgregarLibro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControladorSocio {
    @FXML
    private CheckBox checkDireccion;

    @FXML
    private CheckBox checkEmail;

    @FXML
    private CheckBox checkId;

    @FXML
    private CheckBox checkNombre;

    @FXML
    private CheckBox checkTelefono;

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
    private StackPane stackPaneImagenSocio;


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
    void onClickRefrescar(ActionEvent event) {
        mostrarSocios();
        campoBusqueda.clear();
        checkId.setSelected(false);
        checkNombre.setSelected(false);
        checkDireccion.setSelected(false);
        checkTelefono.setSelected(false);
        checkEmail.setSelected(false);
    }

    @FXML
    void onKeyReleasedBuscar(KeyEvent event) {
        ProgressIndicator cargando = new ProgressIndicator();
        tablaSocios.setPlaceholder(cargando);
        String criterioBusqueda = campoBusqueda.getText();

        boolean buscarPorId = checkId.isSelected();
        boolean buscarPorNombre = checkNombre.isSelected();
        boolean buscarPorDireccion = checkDireccion.isSelected();
        boolean buscarPorTelefono = checkTelefono.isSelected();
        boolean buscarPorEmail = checkEmail.isSelected();

        Task<List<Socio>> task = new Task<List<Socio>>() {
            @Override
            protected List<Socio> call() throws Exception {
                SocioDAO socioDAO = new SocioDAO();
                if (buscarPorId || buscarPorNombre || buscarPorDireccion) {
                    return socioDAO.buscarSocioCheck(criterioBusqueda, buscarPorId, buscarPorNombre, buscarPorDireccion, buscarPorTelefono, buscarPorEmail);
                } else {
                    return socioDAO.buscarSocios(criterioBusqueda);
                }
            }
        };

        task.setOnSucceeded(e -> {
            ObservableList<Socio> observableList = FXCollections.observableArrayList(task.getValue());
            tablaSocios.setItems(observableList);
            tablaSocios.setPlaceholder(new Label("No se encontro nada en la busqueda."));
        });
        new Thread(task).start();
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
                mostrarSocios();
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
        stackPaneImagenSocio.getStyleClass().add("border-default");

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

    private void mostrarSocios() {
        ProgressIndicator progressIndicator = new ProgressIndicator();
        tablaSocios.setPlaceholder(progressIndicator);

        Task<List<Socio>> task = new Task<List<Socio>>() {
            @Override
            protected List<Socio> call() throws Exception {
                SocioDAO socioDAO = new SocioDAO();
                return socioDAO.obtenerSocios();
            }
        };

        task.setOnSucceeded(e -> {
            List<Socio> socios = task.getValue();
            ObservableList<Socio> observableList = FXCollections.observableArrayList(socios);
            tablaSocios.setItems(observableList);
            tablaSocios.setPlaceholder(new Label("No hay datos disponibles"));
        });
        new Thread(task).start();
    }

    @FXML
    void onClickInicio(ActionEvent event) {
        cargarVista("inicio-view.fxml");
    }

    public void exportarJson(List<Socio> socios, String pathingArchivo) {
        try (Writer writer = new FileWriter(pathingArchivo)) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                    .create();
            gson.toJson(socios, writer);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación");
            alert.setHeaderText(null);
            alert.setContentText("La exportación a JSON ha sido completada exitosamente.");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarCSV(List<Socio> socios, String pathingArchivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathingArchivo))) {
            String[] cabecera = { "ID", "Nombre", "Direccion", "Telefono", "Email"};
            writer.writeNext(cabecera);

            for (Socio socio : socios) {
                String[] datos = { String.valueOf(socio.getSocioId()), socio.getNombre(), socio.getDireccion(), socio.getTelefono(), socio.getEmail()};
                writer.writeNext(datos);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportación");
            alert.setHeaderText(null);
            alert.setContentText("La exportación a CSV ha sido completada exitosamente.");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickCSV(ActionEvent event) {
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socios = socioDAO.obtenerSocios();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_socios_csv_" + fecha + ".csv";
        exportarCSV(socios, pathingArchivo);
    }

    @FXML
    void onClickJson(ActionEvent event) {
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socios = socioDAO.obtenerSocios();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_socios_json_" + fecha + ".json";
        exportarJson(socios, pathingArchivo);
    }
}

