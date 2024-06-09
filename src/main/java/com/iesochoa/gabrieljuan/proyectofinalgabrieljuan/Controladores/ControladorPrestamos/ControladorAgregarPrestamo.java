package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Prestamo;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

public class ControladorAgregarPrestamo implements ControladorPrestamoSeleccionarLibro.libroSelectedListener, ControladorPrestamoSeleccionarSocio.socioSelectedListener {

    @FXML
    private MenuButton estadoMenuButton;

    @FXML
    private Label labelLibro;

    @FXML
    private Label labelLibroId;

    @FXML
    private Label labelSocio;

    @FXML
    private Label labelSocioId;

    @FXML
    private DatePicker campoFechaDevolucion;

    @FXML
    private DatePicker campoFechaLimite;

    @FXML
    private DatePicker campoFechaPrestamo;

    @FXML
    private ImageView imagenLibroView;

    @FXML
    private ImageView imagenSocioView;

    @FXML
    private StackPane stackPaneImagenSocio;

    @FXML
    private StackPane stackPaneImagenLibro;

    private byte[] imagenLibro;

    private byte[] imagenSocio;

    private Runnable onPrestamoChangeListener;

    /**
     * Este método se invoca cuando se hace clic en el botón "Seleccionar Libro".
     * Primero, carga el archivo FXML de la vista de selección de libros y obtiene el controlador asociado.
     * Luego, establece este controlador como el listener de la selección de libros.
     * Finalmente, crea una nueva ventana (Stage), establece la vista de selección de libros como la escena de la ventana y muestra la ventana.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickSeleccionarLibro(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de selección de libros y obtener el controlador asociado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-libro-view.fxml"));
            Parent root = loader.load();

            ControladorPrestamoSeleccionarLibro controladorSeleccionarLibro = loader.getController();

            // Establecer este controlador como el listener de la selección de libros
            controladorSeleccionarLibro.onLibroSelectedListener(this);

            // Crear una nueva ventana (Stage), establecer la vista de selección de libros como la escena de la ventana y mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si ocurre un error al cargar la vista de selección de libros
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Seleccionar Socio".
     * Primero, carga el archivo FXML de la vista de selección de socios y obtiene el controlador asociado.
     * Luego, establece este controlador como el listener de la selección de socios.
     * Finalmente, crea una nueva ventana (Stage), establece la vista de selección de socios como la escena de la ventana y muestra la ventana.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickSeleccionarSocio(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la vista de selección de socios y obtener el controlador asociado
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-socio-view.fxml"));
            Parent root = loader.load();

            ControladorPrestamoSeleccionarSocio controladorSeleccionarSocio = loader.getController();

            // Establecer este controlador como el listener de la selección de socios
            controladorSeleccionarSocio.onSocioSelectedListener(this);

            // Crear una nueva ventana (Stage), establecer la vista de selección de socios como la escena de la ventana y mostrar la ventana
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si ocurre un error al cargar la vista de selección de socios
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Cancelar".
     * Primero, obtiene la ventana (Stage) del evento y la cierra.
     * Luego, si el listener de cambio de préstamo no es null, lo ejecuta.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Obtener la ventana (Stage) del evento y cerrarla
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Si el listener de cambio de préstamo no es null, ejecutarlo
        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Agregar".
     * Primero, valida los campos de la interfaz de usuario. Si no son válidos, muestra una alerta y retorna.
     * Luego, formatea las fechas de préstamo, devolución y límite a una cadena de texto con el formato "yyyy-MM-dd".
     * Después, crea un nuevo objeto Prestamo y establece sus propiedades con los valores de los campos de la interfaz de usuario.
     * A continuación, crea un nuevo objeto EjemplarDAO y utiliza su método encontrarIdEjemplar para obtener el ID del ejemplar del libro seleccionado.
     * Luego, crea un nuevo objeto PrestamoDAO y utiliza su método agregarPrestamo para agregar el préstamo a la base de datos.
     * Después, utiliza el método reducirEjemplar de EjemplarDAO para reducir la cantidad de ejemplares disponibles del libro seleccionado.
     * Finalmente, cierra la ventana y, si el listener de cambio de préstamo no es null, lo ejecuta.
     *
     * @param event el evento de clic del botón
     * @throws ParseException si ocurre un error al parsear las fechas
     */
    @FXML
    void onClickAgregar(ActionEvent event) throws ParseException {
        // Validar los campos de la interfaz de usuario. Si no son válidos, mostrar una alerta y retornar
        if (!validarCampos()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, rellene todos los campos correctamente.");
            return;
        }

        // Formatear las fechas de préstamo, devolución y límite a una cadena de texto con el formato "yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaPrestamo = campoFechaPrestamo.getValue().format(formatter);
        String fechaDevolucion = campoFechaDevolucion.getValue().format(formatter);
        String fechaLimite = campoFechaLimite.getValue().format(formatter);

        // Crear un nuevo objeto Prestamo y establecer sus propiedades con los valores de los campos de la interfaz de usuario
        Prestamo prestamo = new Prestamo();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        int ejemplarId = ejemplarDAO.encontrarIdEjemplar(labelLibroId.getText());
        prestamo.setCopiaId((ejemplarId));
        prestamo.setSocioId(Integer.parseInt(labelSocioId.getText()));
        prestamo.setEstado(estadoMenuButton.getText());

        // Parsear las fechas de préstamo, devolución y límite a objetos Date y establecerlas en el objeto Prestamo
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date datePrestamo = format.parse(fechaPrestamo);
        prestamo.setFechaPrestamo(datePrestamo);
        Date dateDevolucion = format.parse(fechaDevolucion);
        prestamo.setFechaDevolucion(dateDevolucion);
        Date dateLimite = format.parse(fechaLimite);
        prestamo.setFechaLimite(dateLimite);

        // Crear un nuevo objeto PrestamoDAO y utilizar su método agregarPrestamo para agregar el préstamo a la base de datos
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        prestamoDAO.agregarPrestamo(prestamo);
        // Utilizar el método reducirEjemplar de EjemplarDAO para reducir la cantidad de ejemplares disponibles del libro seleccionado
        ejemplarDAO.reducirEjemplar(labelLibroId.getText());

        // Cerrar la ventana
        Stage stage = (Stage) labelLibroId.getScene().getWindow();
        stage.close();

        // Si el listener de cambio de préstamo no es null, ejecutarlo
        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    /**
     * Este método se invoca cuando se selecciona un libro.
     * Actualiza la etiqueta del libro y la imagen del libro en la interfaz de usuario con los detalles del libro seleccionado.
     *
     * @param libroSeleccionado el libro que se ha seleccionado
     */
    @Override
    public void libroSeleccionado(Libro libroSeleccionado) {
        // Actualizar la etiqueta del libro con el título del libro seleccionado
        libroLabel(libroSeleccionado.getTitulo(), libroSeleccionado.getLibroId());
        // Actualizar la imagen del libro con la portada del libro seleccionado
        libroImagen(libroSeleccionado.getPortada());
    }

    /**
     * Este método se invoca cuando se selecciona un socio.
     * Actualiza la etiqueta del socio y la imagen del socio en la interfaz de usuario con los detalles del socio seleccionado.
     *
     * @param socioSeleccionado el socio que se ha seleccionado
     */
    @Override
    public void socioSeleccionado(Socio socioSeleccionado) {
        // Actualizar la etiqueta del socio con el nombre del socio seleccionado
        socioLabel(socioSeleccionado.getNombre(), socioSeleccionado.getSocioId());
        // Actualizar la imagen del socio con la foto del socio seleccionado
        socioImagen(socioSeleccionado.getSocioFoto());
    }

    /**
     * Este método se invoca automáticamente después de que se ha cargado el archivo FXML.
     * Primero, añade la clase CSS "border-default" a los StackPanes de las imágenes del libro y del socio.
     * Luego, para cada MenuItem del MenuButton de estado, establece un manejador de eventos de acción que cambia el texto del MenuButton al texto del MenuItem cuando se hace clic en él.
     */
    @FXML
    void initialize() {
        // Añadir la clase CSS "border-default" a los StackPanes de las imágenes del libro y del socio
        stackPaneImagenLibro.getStyleClass().add("border-default");
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Para cada MenuItem del MenuButton de estado, establecer un manejador de eventos de acción que cambia el texto del MenuButton al texto del MenuItem cuando se hace clic en él
        for (MenuItem menuItem : estadoMenuButton.getItems()) {
            menuItem.setOnAction(event -> {
                estadoMenuButton.setText(menuItem.getText());
            });
        }
    }

    /**
     * Este método se utiliza para establecer el listener de cambio de préstamo.
     * El listener se invocará cuando se realice un cambio en el préstamo.
     *
     * @param onLibroChangeListener el listener de cambio de préstamo
     */
    public void prestamoCambiaListener(Runnable onLibroChangeListener) {
        this.onPrestamoChangeListener = onLibroChangeListener;
    }

    /**
     * Este método se utiliza para actualizar la etiqueta del libro en la interfaz de usuario.
     * Establece el texto de la etiqueta del libro con el título del libro y el ID del libro.
     *
     * @param tituloLibro el título del libro
     * @param labelLibroId el ID del libro
     */
    public void libroLabel(String tituloLibro, int labelLibroId) {
        // Establecer el texto de la etiqueta del libro con el título del libro
        this.labelLibro.setText(tituloLibro);
        // Establecer el texto de la etiqueta del ID del libro con el ID del libro
        this.labelLibroId.setText(Integer.toString(labelLibroId));
    }

    /**
     * Este método se utiliza para actualizar la etiqueta del socio en la interfaz de usuario.
     * Establece el texto de la etiqueta del socio con el nombre del socio y el ID del socio.
     *
     * @param nombreSocio el nombre del socio
     * @param labelSocioId el ID del socio
     */
    public void socioLabel(String nombreSocio, int labelSocioId) {
        // Establecer el texto de la etiqueta del socio con el nombre del socio
        this.labelSocio.setText(nombreSocio);
        // Establecer el texto de la etiqueta del ID del socio con el ID del socio
        this.labelSocioId.setText(Integer.toString(labelSocioId));
    }

    /**
     * Este método se utiliza para actualizar la imagen del libro en la interfaz de usuario.
     * Si la imagen del libro no es null, crea una nueva imagen con la imagen del libro y la establece en la vista de la imagen del libro.
     * Si la imagen del libro es null, establece la vista de la imagen del libro en null.
     *
     * @param imagenLibro la imagen del libro en formato de array de bytes
     */
    public void libroImagen(byte[] imagenLibro) {
        // Si la imagen del libro no es null, crear una nueva imagen con la imagen del libro y establecerla en la vista de la imagen del libro
        if (imagenLibro != null) {
            Image image = new Image(new ByteArrayInputStream(imagenLibro));
            imagenLibroView.setImage(image);
        } else {
            // Si la imagen del libro es null, establecer la vista de la imagen del libro en null
            imagenLibroView.setImage(null);
        }
    }

    /**
     * Este método se utiliza para actualizar la imagen del socio en la interfaz de usuario.
     * Si la imagen del socio no es null, crea una nueva imagen con la imagen del socio y la establece en la vista de la imagen del socio.
     * Si la imagen del socio es null, establece la vista de la imagen del socio en null.
     *
     * @param imagenSocio la imagen del socio en formato de array de bytes
     */
    public void socioImagen(byte[] imagenSocio) {
        // Si la imagen del socio no es null, crear una nueva imagen con la imagen del socio y establecerla en la vista de la imagen del socio
        if (imagenSocio != null) {
            Image image = new Image(new ByteArrayInputStream(imagenSocio));
            imagenSocioView.setImage(image);
        } else {
            // Si la imagen del socio es null, establecer la vista de la imagen del socio en null
            imagenSocioView.setImage(null);
        }
    }

    /**
     * Este método se utiliza para validar los campos de la interfaz de usuario.
     * Comprueba si las fechas de préstamo, devolución y límite están vacías, si los textos de los labels de ID de libro y socio están vacíos,
     * si el texto del botón de menú de estado es "Seleccionar Estado", y si las fechas de devolución y límite son anteriores a la fecha de préstamo.
     * Si alguna de estas condiciones se cumple, retorna false. De lo contrario, retorna true.
     *
     * @return true si todos los campos son válidos, false si alguno de los campos no es válido
     */
    private boolean validarCampos() {
        // Comprobar si las fechas de préstamo, devolución y límite están vacías
        if (campoFechaPrestamo.getValue() == null || campoFechaDevolucion.getValue() == null || campoFechaLimite.getValue() == null) {
            return false;
        }

        // Comprobar si los textos de los labels de ID de libro y socio están vacíos
        if (labelLibroId.getText().isEmpty()) {
            return false;
        }

        if (labelSocioId.getText().isEmpty()) {
            return false;
        }

        // Comprobar si el texto del botón de menú de estado es "Seleccionar Estado"
        if (estadoMenuButton.getText().equals("Seleccionar Estado")) {
            return false;
        }

        // Comprobar si las fechas de devolución y límite son anteriores a la fecha de préstamo
        if (campoFechaDevolucion.getValue().isBefore(campoFechaPrestamo.getValue()) || campoFechaLimite.getValue().isBefore(campoFechaPrestamo.getValue())) {
            return false;
        }

        // Si ninguna de las condiciones anteriores se cumple, retornar true
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