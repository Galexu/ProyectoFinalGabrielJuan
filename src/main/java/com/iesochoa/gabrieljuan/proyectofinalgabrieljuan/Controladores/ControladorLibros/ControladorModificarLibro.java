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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class ControladorModificarLibro {

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
    private ImageView imagenLibroView;

    @FXML
    private MenuButton generoMenuButton;

    private byte[] imagenLibro;

    private Libro libro;

    private EjemplarDAO ejemplarDAO = new EjemplarDAO();

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
     * Maneja el evento de clic en el botón "Modificar".
     * Si los campos no son válidos, muestra una alerta.
     * Si los campos son válidos, modifica el libro y el ejemplar correspondiente en la base de datos y cierra la ventana.
     *
     * @param event el evento de acción
     */
    @FXML
    void onClickModificar(ActionEvent event) {
        // Si los campos no son válidos, muestra una alerta y termina la ejecución del método
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

        // Establece los atributos del libro con los valores de los campos
        libro.setIsbn(campoIsbn.getText());
        libro.setTitulo(campoTitulo.getText());
        libro.setAutor(campoAutor.getText());
        libro.setAnoPublicacion(Integer.parseInt(campoAnoPublicacion.getText()));
        libro.setGenero(generoMenuButton.getText());
        libro.setPortada(imagenLibro);

        // Crea un nuevo DAO para libros
        LibroDAO libroDAO = new LibroDAO();
        // Actualiza el libro en la base de datos
        libroDAO.actualizarLibro(libro);

        // Crea un nuevo ejemplar
        Ejemplar ejemplar = new Ejemplar();
        // Crea un nuevo DAO para ejemplares
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        // Encuentra el ID del libro que se acaba de actualizar
        int libroId = libroDAO.buscarLibroIdPorIsbn(campoIsbn.getText());
        // Establece los atributos del ejemplar
        ejemplar.setDisponibles(Integer.parseInt(campoEjemplares.getText()));
        ejemplar.setCopiaId(libroId);
        // Actualiza el ejemplar en la base de datos
        ejemplarDAO.actualizarEjemplar(ejemplar);

        // Cierra la ventana
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
     * Posiciona el libro en el formulario.
     * Rellena los campos del formulario con los datos del libro.
     *
     * @param libro el libro a posicionar
     */
    public void posicionarLibro(Libro libro) {
        this.libro = libro;

        // Si el libro no es null
        if (libro != null) {
            // Establece la imagen del libro
            imagenLibro = libro.getPortada();

            // Si la portada del libro no es null
            byte[] portadaBytes = libro.getPortada();
            if (portadaBytes != null) {
                // Crea una nueva imagen a partir de los bytes de la portada y la establece como la imagen del ImageView
                Image image = new Image(new ByteArrayInputStream(portadaBytes));
                imagenLibroView.setImage(image);
            }
            // Rellena los campos del formulario con los datos del libro
            campoIsbn.setText(libro.getIsbn());
            campoTitulo.setText(libro.getTitulo());
            campoAutor.setText(libro.getAutor());
            campoAnoPublicacion.setText(String.valueOf(libro.getAnoPublicacion()));
            generoMenuButton.setText(libro.getGenero());

            // Selecciona el género del libro en el menú de género
            for (MenuItem menuItem : generoMenuButton.getItems()) {
                if (menuItem.getText().equals(libro.getGenero())) {
                    menuItem.fire();
                    break;
                }
            }

            // Obtiene el número de ejemplares disponibles del libro y lo establece en el campo correspondiente
            int disponibles = ejemplarDAO.obtenerDisponibles(libro.getIsbn());
            campoEjemplares.setText(String.valueOf(disponibles));
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
     * Si el libro no es null, rellena los campos del formulario con los datos del libro.
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

        // Si el libro no es null, rellena los campos del formulario con los datos del libro
        if (libro != null) {
            campoIsbn.setText(libro.getIsbn());
            campoTitulo.setText(libro.getTitulo());
            campoAutor.setText(libro.getAutor());
            campoAnoPublicacion.setText(String.valueOf(libro.getAnoPublicacion()));
            generoMenuButton.setText(libro.getGenero());
            campoEjemplares.setText(String.valueOf(libro.getDisponibles()));
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

        // Muestra la alerta y espera a que el usuario la cierre
        alert.showAndWait();
    }
}