package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;

public class ControladorAgregarSocio {
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

    private byte[] imagenSocio;

    private Runnable onSocioChangeListener;

    @FXML
    private StackPane stackPaneImagenSocio;

    /**
     * Este método se activa al hacer clic en el botón "Agregar".
     * Primero, valida los campos de entrada. Si no son válidos, muestra una alerta y termina la ejecución del método.
     * Luego, crea un nuevo objeto Socio y establece sus propiedades con los valores de los campos de entrada.
     * Después, crea un nuevo objeto SocioDAO y utiliza su método agregarSocio para agregar el nuevo Socio a la base de datos.
     * A continuación, obtiene el Stage de la ventana actual y lo cierra.
     * Finalmente, si el oyente onSocioChangeListener no es nulo, ejecuta su método run.
     *
     * @param event el evento de acción que ocurrió. En este caso, un clic en el botón "Agregar".
     */
    @FXML
    void onClickAgregar(ActionEvent event) {
        // Validar los campos de entrada
        if (!validarCampos()) {
            // Mostrar una alerta si los campos de entrada no son válidos
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, rellene todos los campos correctamente.");
            return;
        }

        // Crear un nuevo objeto Socio y establecer sus propiedades con los valores de los campos de entrada
        Socio socio = new Socio();
        socio.setNombre(campoNombre.getText());
        socio.setDireccion(campoDireccion.getText());
        socio.setTelefono(campoTelefono.getText());
        socio.setEmail(campoEmail.getText());
        socio.setSocioFoto(imagenSocio);

        // Crear un nuevo objeto SocioDAO y utilizar su método agregarSocio para agregar el nuevo Socio a la base de datos
        SocioDAO socioDAO = new SocioDAO();
        socioDAO.agregarSocio(socio);

        // Obtener el Stage de la ventana actual y cerrarlo
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();

        // Si el oyente onSocioChangeListener no es nulo, ejecutar su método run
        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    /**
     * Este método se activa al hacer clic en el botón "Cancelar".
     * Primero, obtiene el Stage de la ventana actual y lo cierra.
     * Luego, si el oyente onSocioChangeListener no es nulo, ejecuta su método run.
     *
     * @param event el evento de acción que ocurrió. En este caso, un clic en el botón "Cancelar".
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Obtener el Stage de la ventana actual y cerrarlo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Si el oyente onSocioChangeListener no es nulo, ejecutar su método run
        if (onSocioChangeListener != null) {
            onSocioChangeListener.run();
        }
    }

    /**
     * Este método se activa al hacer clic en el botón de selección de imagen.
     * Primero, crea un objeto FileChooser y añade un filtro de extensión para permitir solo archivos de imagen.
     * Luego, abre el diálogo de selección de archivos y guarda el archivo seleccionado.
     * Si se selecciona un archivo, intenta leer todos los bytes del archivo y los guarda en la variable imagenSocio.
     * Después, crea un objeto Image a partir del archivo seleccionado y establece esta imagen en el ImageView imagenSocioView.
     * Si ocurre una excepción de tipo IOException durante la lectura del archivo, imprime la traza de la pila de la excepción.
     *
     * @param event el evento de acción que ocurrió. En este caso, un clic en el botón de selección de imagen.
     */
    @FXML
    private void seleccionarImagen(ActionEvent event) {
        // Crear un objeto FileChooser y añadir un filtro de extensión para permitir solo archivos de imagen
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg"));

        // Abrir el diálogo de selección de archivos y guardar el archivo seleccionado
        File selectedFile = fileChooser.showOpenDialog(null);

        // Si se selecciona un archivo
        if (selectedFile != null) {
            try {
                // Leer todos los bytes del archivo y guardarlos en la variable imagenSocio
                imagenSocio = Files.readAllBytes(selectedFile.toPath());

                // Crear un objeto Image a partir del archivo seleccionado
                Image image = new Image(new FileInputStream(selectedFile));

                // Establecer la imagen en el ImageView imagenSocioView
                imagenSocioView.setImage(image);
            } catch (IOException e) {
                // Imprimir la traza de la pila de la excepción si ocurre una IOException
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método se llama automáticamente después de que se ha cargado el archivo FXML.
     * Se utiliza para añadir una clase CSS ("border-default") al StackPane llamado "stackPaneImagenSocio".
     */
    @FXML
    void initialize() {
        // Obtener la lista de clases de estilo de stackPaneImagenSocio y añadir la clase "border-default"
        stackPaneImagenSocio.getStyleClass().add("border-default");
    }

    /**
     * Este método se utiliza para asignar un objeto Runnable al campo onSocioChangeListener.
     * El objeto Runnable representa un comando que puede ser ejecutado, y en este contexto, parece ser utilizado como una función de callback que se ejecuta cuando ocurre un cambio en un objeto "Socio".
     *
     * @param onLibroChangeListener el objeto Runnable que se va a asignar al campo onSocioChangeListener.
     */
    public void setOnSocioChangeListener(Runnable onLibroChangeListener) {
        this.onSocioChangeListener = onLibroChangeListener;
    }

    /**
     * Este método se utiliza para validar los campos de entrada del formulario.
     * Comprueba si los campos campoNombre, campoDireccion, campoTelefono y campoEmail no están vacíos.
     * También comprueba si la entrada en campoTelefono coincide con el patrón de un número de 9 dígitos y si la entrada en campoEmail coincide con el patrón de una dirección de correo electrónico válida.
     *
     * @return true si todos los campos son válidos, false en caso contrario.
     */
    private boolean validarCampos() {
        // Comprobar si los campos campoNombre, campoDireccion, campoTelefono y campoEmail no están vacíos
        if (campoNombre.getText().isEmpty() || campoDireccion.getText().isEmpty() || campoTelefono.getText().isEmpty() || campoEmail.getText().isEmpty()) {
            return false;
        }

        // Comprobar si la entrada en campoTelefono coincide con el patrón de un número de 9 dígitos
        if (!campoTelefono.getText().matches("^\\d{9}$")) {
            return false;
        }

        // Comprobar si la entrada en campoEmail coincide con el patrón de una dirección de correo electrónico válida
        String regexEmail = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        if (!campoEmail.getText().matches(regexEmail)) {
            return false;
        }

        // Si todos los campos son válidos, devolver true
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