package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros;

import atlantafx.base.theme.*;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
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
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControladorLibro {
    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, Integer> idColumn;

    @FXML
    private TableColumn<Libro, String> isbnColumn;

    @FXML
    private TableColumn<Libro, String> tituloColumn;

    @FXML
    private TableColumn<Libro, String> autorColumn;

    @FXML
    private TableColumn<Libro, Integer> anoPublicacionColumn;

    @FXML
    private TableColumn<Libro, String> generoColumn;

    @FXML
    private TableColumn<Libro, String> ejemplaresColumn;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private ImageView imagenLibroView;

    @FXML
    private CheckBox checkAutor;

    @FXML
    private CheckBox checkIsbn;

    @FXML
    private CheckBox checkTitulo;

    @FXML
    private Label labelTituloLibro;

    @FXML
    private CheckBox checkAno;

    @FXML
    private CheckBox checkGenero;

    @FXML
    private StackPane stackPaneImagenLibro;

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
    void onClickInicio(ActionEvent event) {
        cargarVista("inicio-view.fxml");
    }

    @FXML
    void onKeyReleasedBuscar(KeyEvent event) {
        ProgressIndicator cargando = new ProgressIndicator();
        tablaLibros.setPlaceholder(cargando);
        String criterioBusqueda = campoBusqueda.getText();

        boolean buscarPorIsbn = checkIsbn.isSelected();
        boolean buscarPorTitulo = checkTitulo.isSelected();
        boolean buscarPorAutor = checkAutor.isSelected();
        boolean buscarPorAno = checkAno.isSelected();
        boolean buscarPorGenero = checkGenero.isSelected();

        Task<List<Libro>> task = new Task<List<Libro>>() {
            @Override
            protected List<Libro> call() throws Exception {
                LibroDAO libroDAO = new LibroDAO();
                if (buscarPorIsbn || buscarPorTitulo || buscarPorAutor || buscarPorAno || buscarPorGenero) {
                    return libroDAO.buscarLibroCheck(criterioBusqueda, buscarPorIsbn, buscarPorTitulo, buscarPorAutor, buscarPorAno, buscarPorGenero);
                } else {
                    return libroDAO.buscarLibro(criterioBusqueda);
                }
            }
        };

        task.setOnSucceeded(e -> {
            ObservableList<Libro> observableList = FXCollections.observableArrayList(task.getValue());
            tablaLibros.setItems(observableList);
            tablaLibros.setPlaceholder(new Label("No se encontro nada en la busqueda."));
        });
        new Thread(task).start();
    }

    @FXML
    void onClickRefrescar(ActionEvent event) {
        mostrarLibros();
        campoBusqueda.clear();
        checkAno.setSelected(false);
        checkAutor.setSelected(false);
        checkGenero.setSelected(false);
        checkIsbn.setSelected(false);
        checkTitulo.setSelected(false);
    }

    @FXML
    void onClickAgregarLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-libro-view.fxml"));
            Parent root = loader.load();

            ControladorAgregarLibro controlador = loader.getController();
            controlador.setOnLibroChangeListener(this::mostrarLibros);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickBorrarLibro(ActionEvent event) {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado != null) {
            PrestamoDAO prestamoDAO = new PrestamoDAO();

            if (prestamoDAO.existePrestamoParaLibro(libroSeleccionado.getLibroId())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No se puede borrar el libro porque existe un préstamo para este libro, primero elimine el prestamo correspondiente si asi lo desea.");

                Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

                alert.showAndWait();
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de borrado");
            alert.setHeaderText(null);
            alert.setContentText("¿Estás seguro de que quieres borrar el libro seleccionado?");

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                EjemplarDAO ejemplarDAO = new EjemplarDAO();
                ejemplarDAO.eliminarEjemplar(libroSeleccionado.getLibroId());

                LibroDAO libroDAO = new LibroDAO();
                libroDAO.eliminarLibro(String.valueOf(libroSeleccionado.getIsbn()));

                mostrarLibros();
            }
        }
    }

    @FXML
    void onClickModificarLibro(ActionEvent event) {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-libro-view.fxml"));
                Parent root = loader.load();

                ControladorModificarLibro controlador = loader.getController();
                controlador.posicionarLibro(libroSeleccionado);
                controlador.setOnLibroChangeListener(this::mostrarLibros);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void onClickCSV(ActionEvent event) {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_libros_csv_" + fecha + ".csv";
        exportarCSV(libros, pathingArchivo);
    }

    @FXML
    void onClickJson(ActionEvent event) {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_libros_json_" + fecha + ".json";
        exportarJson(libros, pathingArchivo);
    }

    @FXML
    void initialize() {
        stackPaneImagenLibro.getStyleClass().add("border-default");

        Styles.toggleStyleClass(tablaLibros, Styles.BORDERED);
        Styles.toggleStyleClass(tablaLibros, Styles.STRIPED);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("libroId"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        anoPublicacionColumn.setCellValueFactory(new PropertyValueFactory<>("anoPublicacion"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));
        ejemplaresColumn.setCellValueFactory(new PropertyValueFactory<>("disponibles"));

        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                Libro libroSeleccionado = newSelection;
                labelTituloLibro.setText(libroSeleccionado.getTitulo());
                byte[] portadaBytes = libroSeleccionado.getPortada();
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    imagenLibroView.setImage(null);
                }
            }
        });
        mostrarLibros();
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

    private void mostrarLibros() {
        ProgressIndicator cargando = new ProgressIndicator();
        tablaLibros.setPlaceholder(cargando);

        Task<List<Libro>> task = new Task<List<Libro>>() {
            @Override
            protected List<Libro> call() throws Exception {
                LibroDAO libroDAO = new LibroDAO();
                return libroDAO.obtenerLibros();
            }
        };

        task.setOnSucceeded(e -> {
            List<Libro> libros = task.getValue();
            ObservableList<Libro> observableList = FXCollections.observableArrayList(libros);
            tablaLibros.setItems(observableList);
            tablaLibros.setPlaceholder(new Label("No hay datos disponibles"));
        });
        new Thread(task).start();
    }

    public void exportarJson(List<Libro> libros, String pathingArchivo) {
        try (Writer writer = new FileWriter(pathingArchivo)) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                    .create();
            gson.toJson(libros, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarCSV(List<Libro> libros, String pathingArchivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathingArchivo))) {
            String[] cabecera = { "LibroId", "Isbn", "Titulo", "Autor", "AnoPublicacion", "Genero", "Disponibles" };
            writer.writeNext(cabecera);

            for (Libro libro : libros) {
                String[] datos = { String.valueOf(libro.getLibroId()), libro.getIsbn(), libro.getTitulo(), libro.getAutor(), String.valueOf(libro.getAnoPublicacion()), libro.getGenero(), String.valueOf(libro.getDisponibles()) };
                writer.writeNext(datos);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

