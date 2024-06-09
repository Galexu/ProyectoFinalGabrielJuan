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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.List;

public class ControladorPrestamoSeleccionarLibro {

    @FXML
    private TableColumn<?, ?> EjemplaresColumn;

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

    @FXML
    private StackPane stackPaneImagenLibro;

    private ControladorPrestamoSeleccionarLibro.libroSelectedListener libroSelectedListener;

    /**
     * Este método se invoca cuando se hace clic en el botón "Cancelar".
     * Obtiene el Stage del evento y cierra la ventana.
     *
     * @param event el evento de acción que ocurrió
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Obtener el Stage del evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Cerrar la ventana
        stage.close();
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Seleccionar".
     * Obtiene el libro seleccionado de la tabla de libros.
     * Si el libro seleccionado y el listener de libro seleccionado no son null, invoca el método libroSeleccionado del listener con el libro seleccionado y cierra la ventana.
     *
     * @param event el evento de acción que ocurrió
     */
    @FXML
    void onClickSeleccionar(ActionEvent event) {
        // Obtener el libro seleccionado de la tabla de libros
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();
        // Si el libro seleccionado y el listener de libro seleccionado no son null
        if (libroSeleccionado != null && libroSelectedListener != null) {
            // Invocar el método libroSeleccionado del listener con el libro seleccionado
            libroSelectedListener.libroSeleccionado(libroSeleccionado);
            // Obtener el Stage del evento y cerrar la ventana
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Este método se invoca automáticamente después de que se ha cargado el archivo FXML.
     * Añade la clase de estilo "border-default" al StackPane de la imagen del libro.
     * Cambia la clase de estilo de la tabla de libros a "bordered" y "striped".
     * Establece las fábricas de celdas de valor para las columnas de la tabla de libros.
     * Añade un listener a la propiedad selectedItem de la selección de la tabla de libros que actualiza la imagen del libro cuando se selecciona un libro.
     * Llama al método muestraLibros para llenar la tabla de libros con los libros disponibles para el préstamo.
     */
    @FXML
    void initialize() {
        // Añadir la clase de estilo "border-default" al StackPane de la imagen del libro
        stackPaneImagenLibro.getStyleClass().add("border-default");

        // Cambiar la clase de estilo de la tabla de libros a "bordered" y "striped"
        Styles.toggleStyleClass(tablaLibros, Styles.BORDERED);
        Styles.toggleStyleClass(tablaLibros, Styles.STRIPED);

        // Establecer las fábricas de celdas de valor para las columnas de la tabla de libros
        idColumn.setCellValueFactory(new PropertyValueFactory<>("libroId"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorColumn.setCellValueFactory(new PropertyValueFactory<>("autor"));
        anoPublicacionColumn.setCellValueFactory(new PropertyValueFactory<>("anoPublicacion"));
        generoColumn.setCellValueFactory(new PropertyValueFactory<>("genero"));

        // Añadir un listener a la propiedad selectedItem de la selección de la tabla de libros
        tablaLibros.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Si se selecciona un libro
            if (newSelection != null) {
                // Obtener el libro seleccionado
                Libro libroSeleccionado = newSelection;
                // Obtener la portada del libro seleccionado
                byte[] portadaBytes = libroSeleccionado.getPortada();
                // Si la portada del libro seleccionado no es null
                if (portadaBytes != null) {
                    // Crear una nueva imagen con la portada del libro seleccionado y establecerla en la vista de la imagen del libro
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenLibroView.setImage(image);
                } else {
                    // Si la portada del libro seleccionado es null, establecer la vista de la imagen del libro en null
                    imagenLibroView.setImage(null);
                }
            }
        });

        // Llenar la tabla de libros con los libros disponibles para el préstamo
        muestraLibros();
    }

    /**
     * Esta interfaz define un listener que se invoca cuando se selecciona un libro.
     */
    public interface libroSelectedListener {
        /**
         * Este método se invoca cuando se selecciona un libro.
         *
         * @param libroSeleccionado el libro que se ha seleccionado
         */
        void libroSeleccionado(Libro libroSeleccionado);
    }

    /**
     * Este método se utiliza para establecer el listener que se invoca cuando se selecciona un libro.
     *
     * @param listener el listener que se invoca cuando se selecciona un libro
     */
    public void onLibroSelectedListener(ControladorPrestamoSeleccionarLibro.libroSelectedListener listener) {
        // Establecer el listener que se invoca cuando se selecciona un libro
        this.libroSelectedListener = listener;
    }

    /**
     * Este método se utiliza para mostrar los libros disponibles para el préstamo en la tabla de libros.
     * Primero, crea un LibroDAO y obtiene los libros disponibles para el préstamo.
     * Luego, crea una ObservableList con los libros y establece los items de la tabla de libros con la ObservableList.
     */
    void muestraLibros() {
        // Crear un LibroDAO y obtener los libros disponibles para el préstamo
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.obtenerLibrosPrestamo();

        // Crear una ObservableList con los libros
        ObservableList<Libro> observableList = FXCollections.observableArrayList(libros);
        // Establecer los items de la tabla de libros con la ObservableList
        tablaLibros.setItems(observableList);
    }
}
