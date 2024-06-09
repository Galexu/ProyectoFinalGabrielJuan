package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

public class ControladorModificarSocio {

    @FXML
    private TextField campoDireccion;

    @FXML
    private TextField campoEmail;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoTelefono;

    @FXML
    private ImageView imagenSocioView;

    @FXML
    private StackPane stackPaneImagenSocio;

    private byte[] imagenSocio;

    private Socio socio;

    private Runnable onSocioChangeListener;

    /**
     * Este método se activa cuando se hace clic en el botón "Modificar".
     * Primero, valida los campos de entrada. Si no son válidos, muestra una alerta y termina la ejecución del método.
     * Si los campos son válidos, establece los valores de los campos de entrada en el objeto Socio correspondiente.
     * Luego, crea un nuevo objeto SocioDAO y utiliza su método actualizarSocio para actualizar el Socio en la base de datos.
     * Después de eso, cierra la ventana actual.
     * Finalmente, si se ha establecido un oyente de cambios en Socio, lo ejecuta.
     *
     * @param event el evento de clic en el botón que activó este método.
     */
    @FXML
    void onClickModificar(ActionEvent event) {
        // Validar los campos de entrada
        if (!validarCampos()) {
            // Si los campos no son válidos, mostrar una alerta y terminar la ejecución del método
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, rellene todos los campos correctamente.");
            return;
        }

        // Establecer los valores de los campos de entrada en el objeto Socio correspondiente
        socio.setNombre(campoNombre.getText());
        socio.setDireccion(campoDireccion.getText());
        socio.setTelefono(campoTelefono.getText());
        socio.setEmail(campoEmail.getText());
        socio.setSocioFoto(imagenSocio);

        // Crear un nuevo objeto SocioDAO y utilizar su método actualizarSocio para actualizar el Socio en la base de datos
        SocioDAO socioDAO = new SocioDAO();
        socioDAO.actualizarSocio(socio);

        // Cerrar la ventana actual
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Si se ha establecido un oyente de cambios en Socio, ejecutarlo
        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    /**
     * Este método se activa cuando se hace clic en el botón "Cancelar".
     * Primero, obtiene la ventana actual a partir del evento y la cierra.
     * Luego, si se ha establecido un oyente de cambios en Socio, lo ejecuta.
     *
     * @param event el evento de clic en el botón que activó este método.
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Obtener la ventana actual a partir del evento
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Cerrar la ventana
        stage.close();

        // Si se ha establecido un oyente de cambios en Socio, ejecutarlo
        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    /**
     * Este método se activa cuando se selecciona una imagen.
     * Primero, crea un FileChooser y añade un filtro de extensión para los archivos de imagen.
     * Luego, muestra el diálogo de selección de archivos y obtiene el archivo seleccionado.
     * Si se ha seleccionado un archivo, intenta leer todos los bytes del archivo y establecerlos en la variable imagenSocio.
     * También crea una nueva imagen a partir del archivo seleccionado y la establece en imagenSocioView.
     * Si ocurre una excepción de E/S durante este proceso, imprime la traza de la pila de la excepción.
     *
     * @param event el evento de selección de imagen que activó este método.
     */
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                imagenSocio = Files.readAllBytes(selectedFile.toPath());
                Image image = new Image(new FileInputStream(selectedFile));
                imagenSocioView.setImage(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método se inicializa cuando se crea la instancia de la clase.
     * Primero, añade la clase "border-default" al StackPane de la imagen del socio.
     * Luego, si el socio no es nulo, establece los campos de texto con los valores correspondientes del socio.
     */
    @FXML
    void initialize() {
        // Añadir la clase "border-default" al StackPane de la imagen del socio
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Si el socio no es nulo, establecer los campos de texto con los valores correspondientes del socio
        if (socio != null) {
            campoNombre.setText(socio.getNombre());
            campoDireccion.setText(socio.getDireccion());
            campoTelefono.setText(socio.getTelefono());
            campoEmail.setText(socio.getEmail());
        }
    }

    /**
     * Este método se utiliza para establecer el oyente de cambios en Socio.
     * El oyente de cambios en Socio es una instancia de Runnable que se ejecuta cuando se produce un cambio en Socio.
     *
     * @param onLibroChangeListener el oyente de cambios en Socio que se va a establecer.
     */
    public void setOnSocioChangeListener(Runnable onLibroChangeListener) {
        this.onSocioChangeListener = onLibroChangeListener;
    }

    /**
     * Este método se utiliza para posicionar un socio en la interfaz de usuario.
     * Primero, establece el socio proporcionado como el socio actual.
     * Luego, si el socio no es nulo, realiza las siguientes operaciones:
     * - Establece la imagen del socio en la interfaz de usuario a partir de los bytes de la foto del socio.
     * - Establece los campos de texto de la interfaz de usuario con los valores correspondientes del socio.
     *
     * @param socio el socio que se va a posicionar en la interfaz de usuario.
     */
    public void posicionarSocio(Socio socio) {
        // Establecer el socio proporcionado como el socio actual
        this.socio = socio;

        // Si el socio no es nulo, realizar las siguientes operaciones
        if (socio != null) {
            // Establecer la imagen del socio en la interfaz de usuario a partir de los bytes de la foto del socio
            imagenSocio = socio.getSocioFoto();

            byte[] portadaBytes = socio.getSocioFoto();
            if (portadaBytes != null) {
                Image image = new Image(new ByteArrayInputStream(portadaBytes));
                // Establecer la imagen en la vista de la imagen del socio
                imagenSocioView.setImage(image);
            }

            // Establecer los campos de texto de la interfaz de usuario con los valores correspondientes del socio
            campoNombre.setText(socio.getNombre());
            campoDireccion.setText(socio.getDireccion());
            campoTelefono.setText(socio.getTelefono());
            campoEmail.setText(socio.getEmail());
        }
    }

    /**
     * Este método se utiliza para validar los campos de entrada.
     * Primero, comprueba si los campos de texto están vacíos. Si alguno de ellos está vacío, devuelve false.
     * Luego, comprueba si el texto del campo de teléfono coincide con el patrón de un número de teléfono válido. Si no coincide, devuelve false.
     * Después, define una expresión regular para un correo electrónico válido y comprueba si el texto del campo de correo electrónico coincide con ella. Si no coincide, devuelve false.
     * Si todas las comprobaciones son correctas, devuelve true.
     *
     * @return un booleano que indica si los campos de entrada son válidos.
     */
    private boolean validarCampos() {
        // Comprobar si los campos de texto están vacíos
        if (campoNombre.getText().isEmpty() || campoDireccion.getText().isEmpty() || campoTelefono.getText().isEmpty() || campoEmail.getText().isEmpty()) {
            return false;
        }

        // Comprobar si el texto del campo de teléfono coincide con el patrón de un número de teléfono válido
        if (!campoTelefono.getText().matches("^\\d{9}$")) {
            return false;
        }

        // Definir una expresión regular para un correo electrónico válido
        String regexEmail = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        // Comprobar si el texto del campo de correo electrónico coincide con la expresión regular
        if (!campoEmail.getText().matches(regexEmail)) {
            return false;
        }

        // Si todas las comprobaciones son correctas, devolver true
        return true;
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