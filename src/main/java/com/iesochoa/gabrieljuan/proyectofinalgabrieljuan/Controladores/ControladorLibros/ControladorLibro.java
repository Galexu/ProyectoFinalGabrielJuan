package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros;

import atlantafx.base.theme.*;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
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
     * Este método se ejecuta cuando se libera una tecla en el campo de búsqueda.
     * Usa asincronia para que no se ralentice la aplicacion
     * Crea una tarea en segundo plano para buscar libros en la base de datos según el criterio de búsqueda y los filtros seleccionados.
     * Muestra un indicador de progreso mientras se realiza la búsqueda.
     * Cuando la búsqueda se completa, actualiza la tabla de libros con los resultados de la búsqueda.
     *
     * @param event el evento de liberación de tecla
     */
    @FXML
    void onKeyReleasedBuscar(KeyEvent event) {
        // Crea un indicador de progreso y lo establece como el marcador de posición de la tabla de libros
        ProgressIndicator cargando = new ProgressIndicator();
        tablaLibros.setPlaceholder(cargando);

        // Obtiene el criterio de búsqueda del campo de búsqueda
        String criterioBusqueda = campoBusqueda.getText();

        // Obtiene los filtros seleccionados
        boolean buscarPorIsbn = checkIsbn.isSelected();
        boolean buscarPorTitulo = checkTitulo.isSelected();
        boolean buscarPorAutor = checkAutor.isSelected();
        boolean buscarPorAno = checkAno.isSelected();
        boolean buscarPorGenero = checkGenero.isSelected();

        // Crea una tarea en segundo plano para buscar libros
        Task<List<Libro>> task = new Task<List<Libro>>() {
            @Override
            protected List<Libro> call() throws Exception {
                LibroDAO libroDAO = new LibroDAO();

                // Si se seleccionó al menos un filtro, busca libros con el criterio de búsqueda y los filtros seleccionados
                if (buscarPorIsbn || buscarPorTitulo || buscarPorAutor || buscarPorAno || buscarPorGenero) {
                    return libroDAO.buscarLibroCheck(criterioBusqueda, buscarPorIsbn, buscarPorTitulo, buscarPorAutor, buscarPorAno, buscarPorGenero);
                } else {

                    // Si no se seleccionó ningún filtro, busca libros solo con el criterio de búsqueda
                    return libroDAO.buscarLibro(criterioBusqueda);
                }
            }
        };

        // Cuando la búsqueda se completa, actualiza la tabla de libros con los resultados de la búsqueda
        task.setOnSucceeded(e -> {
            ObservableList<Libro> observableList = FXCollections.observableArrayList(task.getValue());
            tablaLibros.setItems(observableList);
            tablaLibros.setPlaceholder(new Label("No se encontro nada en la busqueda."));
        });

        // Inicia la tarea en segundo plano
        new Thread(task).start();
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de refrescar.
     * Llama al método para mostrar los libros, limpia el campo de búsqueda y desmarca todas las casillas de verificación.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickRefrescar(ActionEvent event) {
        // Muestra los libros
        mostrarLibros();
        // Limpia el campo de búsqueda
        campoBusqueda.clear();
        // Desmarca todas las casillas de verificación
        checkAno.setSelected(false);
        checkAutor.setSelected(false);
        checkGenero.setSelected(false);
        checkIsbn.setSelected(false);
        checkTitulo.setSelected(false);
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de agregar libro.
     * Carga la vista de agregar libro en una nueva ventana.
     * Configura un listener para actualizar la lista de libros cuando se agrega un nuevo libro.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickAgregarLibro(ActionEvent event) {
        try {
            // Carga la vista de agregar libro
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-libro-view.fxml"));
            Parent root = loader.load();

            // Obtiene el controlador de la vista de agregar libro
            ControladorAgregarLibro controlador = loader.getController();
            // Configura un listener para actualizar la lista de libros cuando se agrega un nuevo libro
            controlador.setOnLibroChangeListener(this::mostrarLibros);

            // Crea una nueva ventana y muestra la vista de agregar libro
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de borrar libro.
     * Primero, verifica si hay un libro seleccionado en la tabla de libros.
     * Si hay un libro seleccionado, verifica si existe un préstamo para ese libro.
     * Si existe un préstamo, muestra un mensaje de error y termina la ejecución del método.
     * Si no existe un préstamo, muestra un mensaje de confirmación para eliminar el libro.
     * Si el usuario confirma la eliminación, elimina el libro y todos sus ejemplares de la base de datos.
     * Finalmente, actualiza la tabla de libros para reflejar la eliminación del libro.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickBorrarLibro(ActionEvent event) {
        // Obtiene el libro seleccionado en la tabla de libros
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        // Si hay un libro seleccionado
        if (libroSeleccionado != null) {
            PrestamoDAO prestamoDAO = new PrestamoDAO();

            // Si existe un préstamo para el libro seleccionado
            if (prestamoDAO.existePrestamoParaLibro(libroSeleccionado.getLibroId())) {
                // Muestra un mensaje de error y termina la ejecución del método
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se puede borrar el libro porque existe un préstamo para este libro, primero elimine el prestamo correspondiente si asi lo desea.");
                return;
            }

            // Muestra un mensaje de confirmación para eliminar el libro
            Optional<ButtonType> resultado = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación", "¿Estás seguro de que deseas eliminar el libro seleccionado?");
            // Si el usuario confirma la eliminación
            if (resultado.get() == ButtonType.OK) {
                EjemplarDAO ejemplarDAO = new EjemplarDAO();
                // Elimina todos los ejemplares del libro seleccionado
                ejemplarDAO.eliminarEjemplar(libroSeleccionado.getLibroId());

                LibroDAO libroDAO = new LibroDAO();
                // Elimina el libro seleccionado
                libroDAO.eliminarLibro(String.valueOf(libroSeleccionado.getIsbn()));

                // Actualiza la tabla de libros para reflejar la eliminación del libro
                mostrarLibros();
            }
        }
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de modificar libro.
     * Primero, verifica si hay un libro seleccionado en la tabla de libros.
     * Si hay un libro seleccionado, carga la vista de modificar libro en una nueva ventana.
     * Configura un listener para actualizar la lista de libros cuando se modifica un libro.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickModificarLibro(ActionEvent event) {
        // Obtiene el libro seleccionado en la tabla de libros
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        // Si hay un libro seleccionado
        if (libroSeleccionado != null) {
            try {
                // Carga la vista de modificar libro
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-libro-view.fxml"));
                Parent root = loader.load();

                // Obtiene el controlador de la vista de modificar libro
                ControladorModificarLibro controlador = loader.getController();
                // Posiciona el libro seleccionado en el controlador
                controlador.posicionarLibro(libroSeleccionado);
                // Configura un listener para actualizar la lista de libros cuando se modifica un libro
                controlador.setOnLibroChangeListener(this::mostrarLibros);

                // Crea una nueva ventana y muestra la vista de modificar libro
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de exportar a CSV.
     * Primero, obtiene todos los libros de la base de datos.
     * Luego, genera un nombre de archivo con la fecha y hora actual.
     * Finalmente, llama al método para exportar los libros a un archivo CSV con el nombre de archivo generado.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickCSV(ActionEvent event) {
        // Obtiene todos los libros de la base de datos
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        // Genera un nombre de archivo con la fecha y hora actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_libros_csv_" + fecha + ".csv";

        // Llama al método para exportar los libros a un archivo CSV con el nombre de archivo generado
        exportarCSV(libros, pathingArchivo);
    }

    /**
     * Este método se ejecuta cuando se hace clic en el botón de exportar a JSON.
     * Primero, obtiene todos los libros de la base de datos.
     * Luego, genera un nombre de archivo con la fecha y hora actual.
     * Finalmente, llama al método para exportar los libros a un archivo JSON con el nombre de archivo generado.
     *
     * @param event el evento de clic
     */
    @FXML
    void onClickJson(ActionEvent event) {
        // Obtiene todos los libros de la base de datos
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibros();

        // Genera un nombre de archivo con la fecha y hora actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        String pathingArchivo = "src/main/resources/exportaciones/datos_libros_json_" + fecha + ".json";

        // Llama al método para exportar los libros a un archivo JSON con el nombre de archivo generado
        exportarJson(libros, pathingArchivo);
    }

    /**
     * Este método se ejecuta durante la inicialización del controlador.
     * Configura la apariencia de la tabla de libros y sus columnas.
     * Configura un listener para la selección de libros en la tabla de libros.
     * Cuando se selecciona un libro, actualiza la etiqueta del título del libro y la imagen del libro.
     * Finalmente, muestra los libros en la tabla de libros.
     */
    @FXML
    void initialize() {
        // Añade la clase de estilo "border-default" al StackPane de la imagen del libro
        stackPaneImagenLibro.getStyleClass().add("border-default");

        // Aplica las clases de estilo "bordered" y "striped" a la tabla de libros
        Styles.toggleStyleClass(tablaLibros, Styles.BORDERED);
        Styles.toggleStyleClass(tablaLibros, Styles.STRIPED);

        // Configura las columnas de la tabla de libros
        idColumn.setCellValueFactory(new PropertyValueFactory<>("libroId"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        anoPublicacionColumn.setCellValueFactory(new PropertyValueFactory<>("anoPublicacion"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));
        ejemplaresColumn.setCellValueFactory(new PropertyValueFactory<>("disponibles"));

        // Configura un listener para la selección de libros en la tabla de libros
        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Si se selecciona un libro
            if (newSelection != null) {
                // Obtiene el libro seleccionado
                Libro libroSeleccionado = newSelection;
                // Actualiza la etiqueta del título del libro con el título del libro seleccionado
                labelTituloLibro.setText(libroSeleccionado.getTitulo());
                // Obtiene la portada del libro seleccionado
                byte[] portadaBytes = libroSeleccionado.getPortada();
                // Si la portada del libro seleccionado no es nula
                if (portadaBytes != null) {
                    // Crea una imagen con la portada del libro seleccionado y la establece en el ImageView de la imagen del libro
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    // Si la portada del libro seleccionado es nula, establece la imagen del ImageView de la imagen del libro a nula
                    imagenLibroView.setImage(null);
                }
            }
        });

        // Muestra los libros en la tabla de libros
        mostrarLibros();
    }

    /**
     * Este método se utiliza para cargar una nueva vista en el mainPane.
     * Crea un nuevo FXMLLoader con el nombre de vista especificado, carga la vista y la establece como el único hijo del mainPane.
     *
     * @param viewName el nombre del archivo FXML a cargar. Debe estar ubicado en el directorio "/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/".
     */
    private void cargarVista(String viewName) {
        try {
            // Crea un nuevo FXMLLoader con el nombre de vista especificado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/" + viewName));
            // Carga la vista
            Node view = loader.load();
            // Establece la vista cargada como el único hijo del mainPane
            mainPane.getChildren().setAll(view);
        } catch (IOException e) {
            // Imprime la traza de la pila si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para mostrar los libros en la tabla de libros.
     * Primero, crea un indicador de progreso y lo establece como el marcador de posición de la tabla de libros.
     * Luego, crea una tarea en segundo plano para obtener todos los libros de la base de datos.
     * Cuando la tarea se completa, actualiza la tabla de libros con los libros obtenidos.
     * Si no hay libros disponibles, muestra un mensaje en la tabla de libros.
     */
    private void mostrarLibros() {
        // Crea un indicador de progreso y lo establece como el marcador de posición de la tabla de libros
        ProgressIndicator cargando = new ProgressIndicator();
        tablaLibros.setPlaceholder(cargando);

        // Crea una tarea en segundo plano para obtener todos los libros de la base de datos
        Task<List<Libro>> task = new Task<List<Libro>>() {
            @Override
            protected List<Libro> call() throws Exception {
                LibroDAO libroDAO = new LibroDAO();
                return libroDAO.obtenerLibros();
            }
        };

        // Cuando la tarea se completa, actualiza la tabla de libros con los libros obtenidos
        task.setOnSucceeded(e -> {
            List<Libro> libros = task.getValue();
            ObservableList<Libro> observableList = FXCollections.observableArrayList(libros);
            tablaLibros.setItems(observableList);
            // Si no hay libros disponibles, muestra un mensaje en la tabla de libros
            tablaLibros.setPlaceholder(new Label("No hay datos disponibles"));
        });

        // Inicia la tarea en segundo plano
        new Thread(task).start();
    }

    /**
     * Este método se utiliza para exportar una lista de libros a un archivo JSON.
     * Primero, crea un nuevo escritor de archivos con el nombre de archivo especificado.
     * Luego, crea un nuevo objeto Gson con un GsonBuilder que excluye los campos con modificadores transitorios.
     * Después, convierte la lista de libros a JSON y la escribe en el archivo.
     * Finalmente, muestra una alerta para informar al usuario que la exportación ha sido completada exitosamente.
     *
     * @param libros la lista de libros a exportar
     * @param pathingArchivo el nombre del archivo al que se exportarán los libros
     */
    public void exportarJson(List<Libro> libros, String pathingArchivo) {
        try (Writer writer = new FileWriter(pathingArchivo)) {
            // Crea un nuevo objeto Gson con un GsonBuilder que excluye los campos con modificadores transitorios
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                    .create();
            // Convierte la lista de libros a JSON y la escribe en el archivo
            gson.toJson(libros, writer);

            // Muestra una alerta para informar al usuario que la exportación ha sido completada exitosamente
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación", "La exportación a JSON ha sido completada exitosamente.");
        } catch (IOException e) {
            // Imprime la traza de la pila si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para exportar una lista de libros a un archivo CSV.
     * Primero, crea un nuevo escritor CSV con el nombre de archivo especificado.
     * Luego, escribe la cabecera en el archivo CSV.
     * Después, itera sobre la lista de libros, convierte cada libro a un array de strings, y escribe el array en el archivo CSV.
     * Finalmente, muestra una alerta para informar al usuario que la exportación ha sido completada exitosamente.
     *
     * @param libros la lista de libros a exportar
     * @param pathingArchivo el nombre del archivo al que se exportarán los libros
     */
    public void exportarCSV(List<Libro> libros, String pathingArchivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathingArchivo))) {
            // Define la cabecera
            String[] cabecera = { "LibroId", "Isbn", "Titulo", "Autor", "AnoPublicacion", "Genero", "Disponibles" };
            // Escribe la cabecera en el archivo CSV
            writer.writeNext(cabecera);

            // Itera sobre la lista de libros
            for (Libro libro : libros) {
                // Convierte cada libro a un array de strings
                String[] datos = { String.valueOf(libro.getLibroId()), libro.getIsbn(), libro.getTitulo(), libro.getAutor(), String.valueOf(libro.getAnoPublicacion()), libro.getGenero(), String.valueOf(libro.getDisponibles()) };
                // Escribe el array en el archivo CSV
                writer.writeNext(datos);
            }

            // Muestra una alerta para informar al usuario que la exportación ha sido completada exitosamente
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación", "La exportación a CSV ha sido completada exitosamente.");
        } catch (IOException e) {
            // Imprime la traza de la pila si se produce una IOException
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

