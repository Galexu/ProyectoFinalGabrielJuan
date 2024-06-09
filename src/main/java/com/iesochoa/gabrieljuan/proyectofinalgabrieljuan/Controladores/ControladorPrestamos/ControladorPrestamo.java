package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import atlantafx.base.theme.*;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Prestamo;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private CheckBox checkDevuelto;

    @FXML
    private CheckBox checkId;

    @FXML
    private CheckBox checkLibro;

    @FXML
    private CheckBox checkLimite;

    @FXML
    private CheckBox checkPrestamo;

    @FXML
    private CheckBox checkSocio;

    @FXML
    private CheckBox checkEstado;

    @FXML
    private Label labelNombreSocio;

    @FXML
    private Label labelTituloLibro;

    @FXML
    private StackPane stackPaneImagenSocio;

    @FXML
    private StackPane stackPaneImagenLibro;

    /**
     * Cambia el tema de la aplicación a Light Prime.
     * @param event
     */
    @FXML
    void onClickLightPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Dark Prime.
     * @param event
     */
    @FXML
    void onClickDarkPrime(ActionEvent event) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Dracula.
     * @param event
     */
    @FXML
    void onClickDracula(ActionEvent event) {
        Application.setUserAgentStylesheet(new Dracula().getUserAgentStylesheet());
    }

    /**
     * Cambia el tema de la aplicación a Nord Dark.
     * @param event
     */
    @FXML
    void onClickNordDark(ActionEvent event) {
        Application.setUserAgentStylesheet(new NordDark().getUserAgentStylesheet());
    }

    /**
     * Carga la vista de libro.
     * @param event
     */
    @FXML
    void onClickLibro(ActionEvent event) {
        cargarVista("libro-view.fxml");
    }

    /**
     * Carga la vista de prestamo.
     * @param event
     */
    @FXML
    void onClickPrestamo(ActionEvent event) {
        cargarVista("prestamo-view.fxml");
    }

    /**
     * Carga la vista de socio.
     * @param event
     */
    @FXML
    void onClickSocio(ActionEvent event) {
        cargarVista("socio-view.fxml");
    }

    /**
     * Carga la vista de inicio.
     * @param event
     */
    @FXML
    void onClickInicio(ActionEvent event) {
        cargarVista("inicio-view.fxml");
    }

    /**
     * Este método se invoca cuando se libera una tecla en el campo de búsqueda.
     * Primero, crea un indicador de progreso y lo establece como el marcador de posición de la tabla de préstamos.
     * Luego, obtiene el texto del campo de búsqueda y el estado de los checkboxes.
     * Después, crea una tarea que busca préstamos basado en los criterios de búsqueda y los checkboxes seleccionados.
     * Cuando la tarea se completa con éxito, actualiza los elementos de la tabla de préstamos con los resultados de la búsqueda.
     * Finalmente, inicia la tarea en un nuevo hilo.
     *
     * @param event el evento de liberación de tecla
     */
    @FXML
    void onKeyReleasedBuscar(KeyEvent event) {
        // Crear un indicador de progreso y establecerlo como el marcador de posición de la tabla de préstamos
        ProgressIndicator cargando = new ProgressIndicator();
        tablaPrestamos.setPlaceholder(cargando);

        // Obtener el texto del campo de búsqueda y el estado de los checkboxes
        String criterioBusqueda = campoBusqueda.getText();
        boolean buscarPorId = checkId.isSelected();
        boolean buscarPorLibro = checkLibro.isSelected();
        boolean buscarPorSocio = checkSocio.isSelected();
        boolean buscarPorPrestamo = checkPrestamo.isSelected();
        boolean buscarPorDevuelto = checkDevuelto.isSelected();
        boolean buscarPorLimite = checkLimite.isSelected();
        boolean buscarPorEstado = checkEstado.isSelected();

        // Crear una tarea que busca préstamos basado en los criterios de búsqueda y los checkboxes seleccionados
        Task<List<Prestamo>> task = new Task<List<Prestamo>>() {
            @Override
            protected List<Prestamo> call() throws Exception {
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                if (buscarPorId || buscarPorLibro || buscarPorSocio || buscarPorPrestamo || buscarPorDevuelto || buscarPorLimite || buscarPorEstado) {
                    return prestamoDAO.buscarPrestamoCheck(criterioBusqueda, buscarPorId, buscarPorLibro, buscarPorSocio, buscarPorPrestamo, buscarPorDevuelto, buscarPorLimite, buscarPorEstado);
                } else {
                    return prestamoDAO.buscarPrestamos(criterioBusqueda);
                }
            }
        };

        // Cuando la tarea se completa con éxito, actualizar los elementos de la tabla de préstamos con los resultados de la búsqueda
        task.setOnSucceeded(e -> {
            ObservableList<Prestamo> observableList = FXCollections.observableArrayList(task.getValue());
            tablaPrestamos.setItems(observableList);
            tablaPrestamos.setPlaceholder(new Label("No se encontro nada en la busqueda."));
        });

        // Iniciar la tarea en un nuevo hilo
        new Thread(task).start();
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Refrescar".
     * Primero, llama al método mostrarPrestamos() para actualizar la lista de préstamos.
     * Luego, limpia el campo de búsqueda y desmarca todos los checkboxes.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickRefrescar(ActionEvent event) {
        // Actualizar la lista de préstamos
        mostrarPrestamos();
        // Limpiar el campo de búsqueda
        campoBusqueda.clear();
        // Desmarcar todos los checkboxes
        checkId.setSelected(false);
        checkLibro.setSelected(false);
        checkSocio.setSelected(false);
        checkPrestamo.setSelected(false);
        checkDevuelto.setSelected(false);
        checkLimite.setSelected(false);
        checkEstado.setSelected(false);
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Agregar Préstamo".
     * Primero, crea un FXMLLoader y establece su ubicación al archivo FXML de la vista "agregar-prestamo-view.fxml".
     * Luego, carga la vista y obtiene el controlador de la vista.
     * Después, establece el listener del controlador para que cuando cambie el préstamo, se muestren los préstamos.
     * Finalmente, crea un nuevo Stage, establece la escena con la vista cargada y muestra el Stage.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickAgregarPrestamo(ActionEvent event) {
        try {
            // Crear un FXMLLoader y establecer su ubicación al archivo FXML de la vista "agregar-prestamo-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-view.fxml"));
            // Cargar la vista
            Parent root = loader.load();

            // Obtener el controlador de la vista
            ControladorAgregarPrestamo controlador = loader.getController();
            // Establecer el listener del controlador para que cuando cambie el préstamo, se muestren los préstamos
            controlador.prestamoCambiaListener(this::mostrarPrestamos);

            // Crear un nuevo Stage, establecer la escena con la vista cargada y mostrar el Stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Borrar Préstamo".
     * Primero, obtiene el préstamo seleccionado en la tabla de préstamos.
     * Si hay un préstamo seleccionado, muestra un cuadro de diálogo de confirmación para asegurarse de que el usuario quiere eliminar el préstamo.
     * Si el usuario confirma la eliminación, crea un PrestamoDAO, elimina el préstamo seleccionado y actualiza la lista de préstamos.
     * Independientemente de si el usuario confirma la eliminación o no, actualiza la lista de préstamos.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickBorrarPrestamo(ActionEvent event) {
        // Obtener el préstamo seleccionado en la tabla de préstamos
        Prestamo prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        // Si hay un préstamo seleccionado
        if (prestamoSeleccionado != null) {
            // Mostrar un cuadro de diálogo de confirmación para asegurarse de que el usuario quiere eliminar el préstamo
            Optional<ButtonType> resultado = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Eliminar Prestamo", "¿Estás seguro de que quieres eliminar este prestamo?");
            // Si el usuario confirma la eliminación
            if (resultado.get() == ButtonType.OK) {
                // Crear un PrestamoDAO, eliminar el préstamo seleccionado y actualizar la lista de préstamos
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                prestamoDAO.eliminarPrestamo(prestamoSeleccionado.getPrestamoId());
                mostrarPrestamos();
            }
        }

        // Actualizar la lista de préstamos
        mostrarPrestamos();
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Modificar Préstamo".
     * Primero, obtiene el préstamo seleccionado en la tabla de préstamos.
     * Si hay un préstamo seleccionado, crea un FXMLLoader y establece su ubicación al archivo FXML de la vista "modificar-prestamo-view.fxml".
     * Luego, carga la vista y obtiene el controlador de la vista.
     * Después, posiciona el préstamo seleccionado en el controlador y establece el listener del controlador para que cuando cambie el préstamo, se muestren los préstamos.
     * Finalmente, crea un nuevo Stage, establece la escena con la vista cargada y muestra el Stage.
     * Si se produce una IOException durante la carga de la vista, imprime la traza de la excepción.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickModificarPrestamo(ActionEvent event) {
        // Obtener el préstamo seleccionado en la tabla de préstamos
        Prestamo prestamoSeleccionado = tablaPrestamos.getSelectionModel().getSelectedItem();

        // Si hay un préstamo seleccionado
        if (prestamoSeleccionado != null) {
            try {
                // Crear un FXMLLoader y establecer su ubicación al archivo FXML de la vista "modificar-prestamo-view.fxml"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-prestamo-view.fxml"));
                // Cargar la vista
                Parent root = loader.load();

                // Obtener el controlador de la vista
                ControladorModificarPrestamo controlador = loader.getController();
                // Posicionar el préstamo seleccionado en el controlador
                controlador.posicionarPrestamo(prestamoSeleccionado);
                // Establecer el listener del controlador para que cuando cambie el préstamo, se muestren los préstamos
                controlador.setOnPrestamoChangeListener(this::mostrarPrestamos);

                // Crear un nuevo Stage, establecer la escena con la vista cargada y mostrar el Stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                // Imprimir la traza de la excepción si se produce una IOException
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "CSV".
     * Primero, crea un PrestamoDAO y obtiene la lista de préstamos.
     * Luego, formatea la fecha y hora actual en un formato específico y genera la ruta del archivo CSV.
     * Finalmente, llama al método exportarCSV para exportar los datos de los préstamos a un archivo CSV.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickCSV(ActionEvent event) {
        // Crear un PrestamoDAO y obtener la lista de préstamos
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();

        // Formatear la fecha y hora actual en un formato específico y generar la ruta del archivo CSV
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_prestamos_csv_" + fecha + ".csv";

        // Llamar al método exportarCSV para exportar los datos de los préstamos a un archivo CSV
        exportarCSV(prestamos, pathingArchivo);
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Json".
     * Primero, crea un PrestamoDAO y obtiene la lista de préstamos.
     * Luego, formatea la fecha y hora actual en un formato específico y genera la ruta del archivo JSON.
     * Finalmente, llama al método exportarJson para exportar los datos de los préstamos a un archivo JSON.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickJson(ActionEvent event) {
        // Crear un PrestamoDAO y obtener la lista de préstamos
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        List<Prestamo> prestamos = prestamoDAO.obtenerPrestamos();

        // Formatear la fecha y hora actual en un formato específico y generar la ruta del archivo JSON
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_prestamos_json_" + fecha + ".json";

        // Llamar al método exportarJson para exportar los datos de los préstamos a un archivo JSON
        exportarJson(prestamos, pathingArchivo);
    }

    /**
     * Este método se invoca cuando se inicializa el controlador después de que se haya cargado su archivo FXML.
     * Primero, añade la clase de estilo "border-default" a los StackPanes de las imágenes del libro y del socio.
     * Luego, alterna las clases de estilo "BORDERED" y "STRIPED" en la tabla de préstamos.
     * Después, establece las fábricas de celdas de valor para las columnas de la tabla de préstamos.
     * A continuación, añade un listener a la propiedad selectedItem de la tabla de préstamos.
     * Cuando se selecciona un nuevo préstamo en la tabla, obtiene el libro y el socio asociados al préstamo seleccionado,
     * actualiza los labels con el nombre del socio y el título del libro, y actualiza las vistas de imagen con las imágenes del libro y del socio.
     * Si el libro o el socio no tienen una imagen, establece la imagen de la vista de imagen correspondiente en null.
     * Finalmente, llama al método mostrarPrestamos() para cargar los préstamos en la tabla.
     */
    @FXML
    void initialize() {
        // Añadir la clase de estilo "border-default" a los StackPanes de las imágenes del libro y del socio
        stackPaneImagenLibro.getStyleClass().add("border-default");
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Alternar las clases de estilo "BORDERED" y "STRIPED" en la tabla de préstamos
        Styles.toggleStyleClass(tablaPrestamos, Styles.BORDERED);
        Styles.toggleStyleClass(tablaPrestamos, Styles.STRIPED);

        // Establecer las fábricas de celdas de valor para las columnas de la tabla de préstamos
        idPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("prestamoId"));
        libroColunn.setCellValueFactory(new PropertyValueFactory<>("tituloLibro"));
        socioColumn.setCellValueFactory(new PropertyValueFactory<>("nombreSocio"));
        idCopiaColumn.setCellValueFactory(new PropertyValueFactory<>("copiaId"));
        idSocioColumn.setCellValueFactory(new PropertyValueFactory<>("socioId"));
        fechaPrestamoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaPrestamo"));
        fechaDevolucionColumn.setCellValueFactory(new PropertyValueFactory<>("fechaDevolucion"));
        fechaLimiteColumn.setCellValueFactory(new PropertyValueFactory<>("fechaLimite"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // Añadir un listener a la propiedad selectedItem de la tabla de préstamos
        tablaPrestamos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Cuando se selecciona un nuevo préstamo en la tabla
            if (newSelection != null) {
                // Obtener el préstamo seleccionado
                Prestamo prestamoSeleccionado = newSelection;
                // Crear un EjemplarDAO y un SocioDAO
                EjemplarDAO ejemplarDAO = new EjemplarDAO();
                SocioDAO socioDAO = new SocioDAO();

                // Obtener el libro y el socio asociados al préstamo seleccionado
                Libro libroSeleccionado = ejemplarDAO.obtenerLibroPorCopiaId(prestamoSeleccionado.getCopiaId());
                Socio socioSeleccionado = socioDAO.obtenerSocioPorId(prestamoSeleccionado.getSocioId());

                // Actualizar los labels con el nombre del socio y el título del libro
                labelNombreSocio.setText(socioSeleccionado.getNombre());
                labelTituloLibro.setText(libroSeleccionado.getTitulo());

                // Obtener las imágenes del libro y del socio
                byte[] portadaBytes = libroSeleccionado.getPortada();
                byte[] socioBytes = socioSeleccionado.getSocioFoto();

                // Si el libro tiene una imagen, actualizar la vista de imagen del libro con la imagen del libro
                if (portadaBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    // Si el libro no tiene una imagen, establecer la imagen de la vista de imagen del libro en null
                    imagenLibroView.setImage(null);
                }

                // Si el socio tiene una imagen, actualizar la vista de imagen del socio con la imagen del socio
                if (socioBytes != null) {
                    Image image = new Image(new ByteArrayInputStream(socioBytes));
                    imagenSocioView.setImage(image);
                } else {
                    // Si el socio no tiene una imagen, establecer la imagen de la vista de imagen del socio en null
                    imagenSocioView.setImage(null);
                }
            }
        });

        // Cargar los préstamos en la tabla
        mostrarPrestamos();
    }

    /**
     * Este método se utiliza para cargar una vista en el panel principal.
     * Primero, crea un FXMLLoader y establece su ubicación a la ruta de la vista especificada.
     * Luego, carga la vista y la establece como el único hijo del panel principal.
     * Si se produce una IOException durante la carga de la vista, imprime la traza de la excepción.
     *
     * @param viewName el nombre de la vista a cargar. Debe ser el nombre de un archivo .fxml en el paquete /com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/.
     */
    private void cargarVista(String viewName) {
        try {
            // Crear un FXMLLoader y establecer su ubicación a la ruta de la vista especificada
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/" + viewName));
            // Cargar la vista
            Node view = loader.load();
            // Establecer la vista como el único hijo del panel principal
            mainPane.getChildren().setAll(view);
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para mostrar los préstamos en la tabla de préstamos.
     * Primero, crea un indicador de progreso y lo establece como el marcador de posición de la tabla de préstamos.
     * Luego, crea una tarea que obtiene la lista de préstamos de la base de datos.
     * Cuando la tarea se completa con éxito, actualiza los elementos de la tabla de préstamos con los resultados de la tarea.
     * Finalmente, inicia la tarea en un nuevo hilo.
     */
    private void mostrarPrestamos() {
        // Crear un indicador de progreso y establecerlo como el marcador de posición de la tabla de préstamos
        ProgressIndicator progressIndicator = new ProgressIndicator();
        tablaPrestamos.setPlaceholder(progressIndicator);

        // Crear una tarea que obtiene la lista de préstamos de la base de datos
        Task<List<Prestamo>> task = new Task<List<Prestamo>>() {
            @Override
            protected List<Prestamo> call() throws Exception {
                // Crear un PrestamoDAO y obtener la lista de préstamos
                PrestamoDAO prestamoDAO = new PrestamoDAO();
                return prestamoDAO.obtenerPrestamos();
            }
        };

        // Cuando la tarea se completa con éxito, actualizar los elementos de la tabla de préstamos con los resultados de la tarea
        task.setOnSucceeded(e -> {
            // Obtener la lista de préstamos de la tarea
            List<Prestamo> prestamos = task.getValue();
            // Crear una lista observable a partir de la lista de préstamos
            ObservableList<Prestamo> observableList = FXCollections.observableArrayList(prestamos);
            // Establecer los elementos de la tabla de préstamos a la lista observable
            tablaPrestamos.setItems(observableList);
            // Establecer el marcador de posición de la tabla de préstamos a un nuevo Label
            tablaPrestamos.setPlaceholder(new Label("No hay datos disponibles"));
        });

        // Iniciar la tarea en un nuevo hilo
        new Thread(task).start();
    }

    /**
     * Este método se utiliza para exportar una lista de préstamos a un archivo JSON.
     * Primero, crea un escritor de archivos y un objeto Gson con un GsonBuilder que excluye los campos con el modificador TRANSIENT.
     * Luego, convierte la lista de préstamos a JSON y la escribe en el archivo.
     * Después, muestra una alerta de información indicando que la exportación a JSON se ha completado con éxito.
     * Si se produce una IOException durante la escritura del archivo, imprime la traza de la excepción.
     *
     * @param prestamos la lista de préstamos a exportar
     * @param pathingArchivo la ruta del archivo al que se exportarán los préstamos
     */
    public void exportarJson(List<Prestamo> prestamos, String pathingArchivo) {
        try (Writer writer = new FileWriter(pathingArchivo)) {
            // Crear un objeto Gson con un GsonBuilder que excluye los campos con el modificador TRANSIENT
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                    .create();
            // Convertir la lista de préstamos a JSON y escribirlo en el archivo
            gson.toJson(prestamos, writer);

            // Mostrar una alerta de información indicando que la exportación a JSON se ha completado con éxito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación", "La exportación a JSON ha sido completada exitosamente.");
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para exportar una lista de préstamos a un archivo CSV.
     * Primero, crea un escritor de CSV y un array de strings para la cabecera del CSV.
     * Luego, escribe la cabecera en el CSV.
     * Después, para cada préstamo en la lista de préstamos, crea un array de strings con los datos del préstamo y los escribe en el CSV.
     * Finalmente, muestra una alerta de información indicando que la exportación a CSV se ha completado con éxito.
     * Si se produce una IOException durante la escritura del archivo, imprime la traza de la excepción.
     *
     * @param prestamos la lista de préstamos a exportar
     * @param pathingArchivo la ruta del archivo al que se exportarán los préstamos
     */
    public void exportarCSV(List<Prestamo> prestamos, String pathingArchivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathingArchivo))) {
            // Crear un array de strings para la cabecera del CSV
            String[] cabecera = {"ID Prestamo", "Nombre Socio", "Titulo Libro", "ID Copia", "ID Socio", "Fecha Prestamo", "Fecha Devolucion", "Fecha Limite", "Estado"};
            // Escribir la cabecera en el CSV
            writer.writeNext(cabecera);

            // Para cada préstamo en la lista de préstamos
            for (Prestamo prestamo : prestamos) {
                // Crear un array de strings con los datos del préstamo
                String[] datos = {String.valueOf(prestamo.getPrestamoId()), prestamo.getNombreSocio(), prestamo.getTituloLibro(), String.valueOf(prestamo.getCopiaId()), String.valueOf(prestamo.getSocioId()), String.valueOf(prestamo.getFechaPrestamo()), String.valueOf(prestamo.getFechaDevolucion()), String.valueOf(prestamo.getFechaLimite()), prestamo.getEstado()};
                // Escribir los datos del préstamo en el CSV
                writer.writeNext(datos);
            }

            // Mostrar una alerta de información indicando que la exportación a CSV se ha completado con éxito
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación", "La exportación a CSV ha sido completada exitosamente.");
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para mostrar un cuadro de diálogo de alerta con el tipo, título y mensaje especificados.
     * Primero, crea una Alerta con el tipo especificado.
     * Luego, establece el título y el texto de contenido de la Alerta con el título y mensaje especificados.
     * También establece el texto de la cabecera de la Alerta en null.
     * Después de eso, obtiene el Stage del cuadro de diálogo de la Alerta y le añade un icono.
     * Finalmente, muestra el cuadro de diálogo de la Alerta y espera a que el usuario lo cierre.
     *
     * @param tipo el tipo del cuadro de diálogo de la Alerta. Determina el icono que se muestra en el cuadro de diálogo de la Alerta.
     * @param titulo el título del cuadro de diálogo de la Alerta. Se muestra en la barra de título del cuadro de diálogo de la Alerta.
     * @param mensaje el mensaje del cuadro de diálogo de la Alerta. Se muestra como el texto de contenido del cuadro de diálogo de la Alerta.
     * @return un Optional<ButtonType> que contiene el tipo del botón que el usuario ha pulsado para cerrar el cuadro de diálogo de la Alerta.
     */
    private Optional<ButtonType> mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        // Crea una Alerta con el tipo especificado
        Alert alert = new Alert(tipo);
        // Establece el título y el texto de contenido de la Alerta
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        // Obtiene el Stage del cuadro de diálogo de la Alerta y le añade un icono
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

        // Muestra el cuadro de diálogo de la Alerta y espera a que el usuario lo cierre
        return alert.showAndWait();
    }
}

