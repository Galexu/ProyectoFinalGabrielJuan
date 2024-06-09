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

    /**
     * Establece el listener que se ejecutará cuando cambie el libro.
     *
     * @param onLibroChangeListener el listener a ejecutar
     */
    public void setOnLibroChangeListener(Runnable onLibroChangeListener) {
        this.onLibroChangeListener = onLibroChangeListener;
    }

    /**
     * Maneja el evento de clic en el botón "Agregar".
     * Si los campos no son válidos, muestra una alerta.
     * Si los campos son válidos, crea un nuevo libro y un nuevo ejemplar, los agrega a la base de datos y cierra la ventana.
     *
     * @param event el evento de acción
     */
    @FXML
    void onClickAgregar(ActionEvent event) {
        // Si los campos no son válidos, muestra una alerta y termina la ejecución del método
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

        // Crea un nuevo libro y un nuevo ejemplar
        Libro libro = new Libro();
        Ejemplar ejemplar = new Ejemplar();
        // Crea un nuevo DAO para libros y ejemplares
        LibroDAO libroDAO = new LibroDAO();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();

        // Establece los atributos del libro con los valores de los campos
        libro.setIsbn(campoIsbn.getText());
        libro.setTitulo(campoTitulo.getText());
        libro.setAutor(campoAutor.getText());
        libro.setAnoPublicacion(Integer.parseInt(campoAnoPublicacion.getText()));
        libro.setGenero(generoMenuButton.getText());
        libro.setPortada(imagenLibro);

        // Agrega el libro a la base de datos
        libroDAO.agregarLibro(libro);
        // Encuentra el ID del libro que se acaba de agregar
        int libroId = libroDAO.buscarLibroIdPorIsbn(campoIsbn.getText());
        // Establece los atributos del ejemplar
        ejemplar.setDisponibles(Integer.parseInt(campoEjemplares.getText()));
        ejemplar.setCopiaId(libroId);
        // Agrega el ejemplar a la base de datos
        ejemplarDAO.agregarEjemplar(ejemplar);

        // Cierra la ventana
        Stage stage = (Stage) campoIsbn.getScene().getWindow();
        stage.close();

        // Si se ha establecido un listener de cambio de libro, lo ejecuta
        if (onLibroChangeListener != null) {
            onLibroChangeListener.run();
        }
    }

    /**
     * Maneja el evento de clic en el botón "Cancelar".
     * Cierra la ventana y ejecuta el listener de cambio de libro si está establecido.
     *
     * @param event el evento de acción
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Cierra la ventana
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Si se ha establecido un listener de cambio de libro, lo ejecuta
        if (onLibroChangeListener != null) {
            onLibroChangeListener.run();
        }
    }

    /**
     * Maneja el evento de selección de imagen.
     * Abre un selector de archivos para seleccionar una imagen y la establece como la imagen del libro.
     *
     * @param event el evento de acción
     */
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        // Crea un nuevo selector de archivos
        FileChooser fileChooser = new FileChooser();
        // Añade un filtro para seleccionar solo archivos de imagen
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        // Muestra el selector de archivos y obtiene el archivo seleccionado
        File selectedFile = fileChooser.showOpenDialog(null);

        // Si se ha seleccionado un archivo
        if (selectedFile != null) {
            try {
                // Lee los bytes del archivo y los almacena en la variable imagenLibro
                imagenLibro = Files.readAllBytes(selectedFile.toPath());
                // Crea una nueva imagen a partir del archivo y la establece como la imagen del ImageView
                Image image = new Image(new FileInputStream(selectedFile));
                imagenLibroView.setImage(image);
            } catch (IOException e) {
                // Imprime la traza de la pila si se produce una IOException
                e.printStackTrace();
            }
        }
    }

    /**
     * Inicializa el controlador.
     * Añade un borde por defecto al StackPane de la imagen del libro y establece los eventos de acción de los items del menú de género.
     */
    @FXML
    void initialize() {
        // Añade un borde por defecto al StackPane de la imagen del libro
        stackPaneImagenLibro.getStyleClass().add("border-default");

        // Establece los eventos de acción de los items del menú de género
        for (MenuItem menuItem : generoMenuButton.getItems()) {
            menuItem.setOnAction(event -> {
                // Establece el texto del MenuButton con el texto del MenuItem seleccionado
                generoMenuButton.setText(menuItem.getText());
            });
        }
    }

    /**
     * Valida los campos del formulario.
     * Comprueba que todos los campos estén llenos y que los campos de año de publicación y ejemplares sean números enteros.
     * También comprueba que se haya seleccionado un género.
     *
     * @return true si los campos son válidos, false en caso contrario
     */
    private boolean validarCampos() {
        // Comprueba que todos los campos estén llenos
        if (campoIsbn.getText().isEmpty() || campoTitulo.getText().isEmpty() || campoAutor.getText().isEmpty() || campoAnoPublicacion.getText().isEmpty() || campoEjemplares.getText().isEmpty()) {
            return false;
        }

        // Comprueba que el campo de año de publicación sea un número entero
        try {
            Integer.parseInt(campoAnoPublicacion.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        // Comprueba que el campo de ejemplares sea un número entero
        try {
            Integer.parseInt(campoEjemplares.getText());
        } catch (NumberFormatException e) {
            return false;
        }

        // Comprueba que se haya seleccionado un género
        if (generoMenuButton.getText().equals("Seleccionar Genero")) {
            return false;
        }

        // Si todas las comprobaciones han pasado, devuelve true
        return true;
    }

    /**
     * Muestra una alerta de error indicando que los campos no se han introducido correctamente.
     */
    private void mostrarAlerta() {
        // Crea una nueva alerta de error
        Alert alert = new Alert(Alert.AlertType.ERROR);
        // Establece el título de la alerta
        alert.setTitle("Error");
        // Establece el texto de la cabecera de la alerta en null
        alert.setHeaderText(null);
        // Establece el texto de contenido de la alerta
        alert.setContentText("Por favor, introduzca los campos correctamente.");

        // Obtiene el Stage de la alerta y le añade un icono
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/favicon.png")));

        // Muestra la alerta y espera a que el usuario la cierre
        alert.showAndWait();
    }
}