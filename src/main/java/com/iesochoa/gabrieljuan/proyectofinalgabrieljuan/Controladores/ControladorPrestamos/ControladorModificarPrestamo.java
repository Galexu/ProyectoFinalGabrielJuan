package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Ejemplar;
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

import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ControladorModificarPrestamo implements ControladorPrestamoSeleccionarLibro.libroSelectedListener, ControladorPrestamoSeleccionarSocio.socioSelectedListener {

    @FXML
    private Label labelLibro;

    @FXML
    private Label labelLibroId;

    @FXML
    private Label labelSocio;

    @FXML
    private Label labelSocioId;

    @FXML
    private MenuButton estadoMenuButton;

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
    private Prestamo prestamo;
    private Libro libro;
    private Socio socio;
    private Ejemplar ejemplar;
    private Runnable onPrestamoChangeListener;

    /**
     * Este método se invoca cuando se selecciona un libro.
     * Primero, actualiza el label del libro con el título y el ID del libro seleccionado.
     * Luego, actualiza la imagen del libro con la portada del libro seleccionado.
     *
     * @param libroSeleccionado el libro que ha sido seleccionado
     */
    @Override
    public void libroSeleccionado(Libro libroSeleccionado) {
        // Actualizar el label del libro con el título y el ID del libro seleccionado
        libroLabel(libroSeleccionado.getTitulo(), libroSeleccionado.getLibroId());
        // Actualizar la imagen del libro con la portada del libro seleccionado
        libroImagen(libroSeleccionado.getPortada());
    }

    /**
     * Este método se invoca cuando se selecciona un socio.
     * Primero, actualiza el label del socio con el nombre y el ID del socio seleccionado.
     * Luego, actualiza la imagen del socio con la foto del socio seleccionado.
     *
     * @param socioSeleccionado el socio que ha sido seleccionado
     */
    @Override
    public void socioSeleccionado(Socio socioSeleccionado) {
        // Actualizar el label del socio con el nombre y el ID del socio seleccionado
        socioLabel(socioSeleccionado.getNombre(), socioSeleccionado.getSocioId());
        // Actualizar la imagen del socio con la foto del socio seleccionado
        socioImagen(socioSeleccionado.getSocioFoto());
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Seleccionar Libro".
     * Primero, crea un FXMLLoader y establece su ubicación a la ruta de la vista de selección de libros.
     * Luego, carga la vista y obtiene el controlador de la vista.
     * Después, establece el listener de selección de libros del controlador a este controlador.
     * Finalmente, crea un nuevo Stage, establece su escena a la vista cargada y lo muestra.
     * Si se produce una IOException durante la carga de la vista, imprime la traza de la excepción.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickSeleccionarLibro(ActionEvent event) {
        try {
            // Crear un FXMLLoader y establecer su ubicación a la ruta de la vista de selección de libros
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-libro-view.fxml"));
            // Cargar la vista
            Parent root = loader.load();

            // Obtener el controlador de la vista
            ControladorPrestamoSeleccionarLibro controladorSeleccionarLibro = loader.getController();
            // Establecer el listener de selección de libros del controlador a este controlador
            controladorSeleccionarLibro.onLibroSelectedListener(this);

            // Crear un nuevo Stage, establecer su escena a la vista cargada y mostrarlo
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Seleccionar Socio".
     * Primero, crea un FXMLLoader y establece su ubicación a la ruta de la vista de selección de socios.
     * Luego, carga la vista y obtiene el controlador de la vista.
     * Después, establece el listener de selección de socios del controlador a este controlador.
     * Finalmente, crea un nuevo Stage, establece su escena a la vista cargada y lo muestra.
     * Si se produce una IOException durante la carga de la vista, imprime la traza de la excepción.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickSeleccionarSocio(ActionEvent event) {
        try {
            // Crear un FXMLLoader y establecer su ubicación a la ruta de la vista de selección de socios
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-socio-view.fxml"));
            // Cargar la vista
            Parent root = loader.load();

            // Obtener el controlador de la vista
            ControladorPrestamoSeleccionarSocio controladorSeleccionarSocio = loader.getController();
            // Establecer el listener de selección de socios del controlador a este controlador
            controladorSeleccionarSocio.onSocioSelectedListener(this);

            // Crear un nuevo Stage, establecer su escena a la vista cargada y mostrarlo
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Cancelar".
     * Primero, obtiene el Stage del evento y lo cierra.
     * Luego, si el listener de cambio de préstamo no es null, ejecuta el método run del listener.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickCancelar(ActionEvent event) {
        // Obtener el Stage del evento y cerrarlo
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        // Si el listener de cambio de préstamo no es null, ejecutar el método run del listener
        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Modificar".
     * Primero, valida los campos. Si no son válidos, muestra una alerta de error y retorna.
     * Luego, obtiene el ID del ejemplar a través del EjemplarDAO y establece los valores del préstamo.
     * Después, formatea las fechas de los campos de fecha y las convierte a objetos Date.
     * Establece las fechas y el estado del préstamo.
     * A continuación, crea un PrestamoDAO y actualiza el préstamo en la base de datos.
     * Finalmente, cierra la ventana y, si el listener de cambio de préstamo no es null, ejecuta el método run del listener.
     *
     * @param event el evento de clic del botón
     * @throws ParseException si ocurre un error al analizar las fechas
     */
    @FXML
    void onClickModificar(ActionEvent event) throws ParseException {
        // Validar los campos. Si no son válidos, mostrar una alerta de error y retornar
        if (!validarCampos()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Por favor, rellene todos los campos correctamente.");
            return;
        }

        // Obtener el ID del ejemplar a través del EjemplarDAO y establecer los valores del préstamo
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        int ejemplarId = ejemplarDAO.encontrarIdEjemplar(labelLibroId.getText());
        prestamo.setPrestamoId(prestamo.getPrestamoId());
        prestamo.setCopiaId(ejemplarId);
        prestamo.setSocioId(Integer.parseInt(labelSocioId.getText()));

        // Formatear las fechas de los campos de fecha y convertirlas a objetos Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaPrestamo = campoFechaPrestamo.getValue().format(formatter);
        String fechaDevolucion = campoFechaDevolucion.getValue().format(formatter);
        String fechaLimite = campoFechaLimite.getValue().format(formatter);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date datePrestamo = format.parse(fechaPrestamo);
        prestamo.setFechaPrestamo(datePrestamo);
        Date dateDevolucion = format.parse(fechaDevolucion);
        prestamo.setFechaDevolucion(dateDevolucion);
        Date dateLimite = format.parse(fechaLimite);
        prestamo.setFechaLimite(dateLimite);

        // Establecer las fechas y el estado del préstamo
        prestamo.setFechaPrestamo(datePrestamo);
        prestamo.setFechaDevolucion(dateDevolucion);
        prestamo.setFechaLimite(dateLimite);
        prestamo.setEstado(estadoMenuButton.getText());

        // Crear un PrestamoDAO y actualizar el préstamo en la base de datos
        PrestamoDAO prestamoDAO = new PrestamoDAO();
        prestamoDAO.actualizarPrestamo(prestamo);

        // Cerrar la ventana y, si el listener de cambio de préstamo no es null, ejecutar el método run del listener
        Stage stage = (Stage) labelLibroId.getScene().getWindow();
        stage.close();

        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    /**
     * Este método se invoca después de que todos los valores @FXML hayan sido inyectados.
     * Primero, añade la clase de estilo "border-default" a los StackPanes de las imágenes del libro y del socio.
     * Luego, para cada MenuItem en el MenuButton de estado, establece su acción de clic para actualizar el texto del MenuButton al texto del MenuItem.
     */
    @FXML
    void initialize() {
        // Añadir la clase de estilo "border-default" a los StackPanes de las imágenes del libro y del socio
        stackPaneImagenLibro.getStyleClass().add("border-default");
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Para cada MenuItem en el MenuButton de estado
        for (MenuItem menuItem : estadoMenuButton.getItems()) {
            // Establecer su acción de clic para actualizar el texto del MenuButton al texto del MenuItem
            menuItem.setOnAction(event -> {
                estadoMenuButton.setText(menuItem.getText());
            });
        }
    }

    /**
     * Este método se utiliza para establecer el listener de cambio de préstamo.
     * El listener de cambio de préstamo es un Runnable que se ejecuta cuando se produce un cambio en el préstamo.
     *
     * @param onLibroChangeListener el listener de cambio de préstamo
     */
    public void setOnPrestamoChangeListener(Runnable onLibroChangeListener) {
        // Establecer el listener de cambio de préstamo
        this.onPrestamoChangeListener = onLibroChangeListener;
    }

    /**
     * Este método se utiliza para posicionar un préstamo.
     * Primero, asigna los valores de libro, socio, préstamo y ejemplar.
     * Luego, utiliza DAOs para obtener el libro y el socio asociados al préstamo.
     * Si el libro no es null, establece la imagen del libro y actualiza los labels del libro.
     * Si el socio no es null, establece la imagen del socio y actualiza los labels del socio.
     * Si el préstamo no es null, recorre los items del MenuButton de estado y dispara el evento del item que coincide con el estado del préstamo.
     *
     * @param prestamo el préstamo a posicionar
     */
    public void posicionarPrestamo(Prestamo prestamo) {
        // Asignar los valores de libro, socio, préstamo y ejemplar
        this.libro = libro;
        this.socio = socio;
        this.prestamo = prestamo;
        this.ejemplar = ejemplar;

        // Utilizar DAOs para obtener el libro y el socio asociados al préstamo
        LibroDAO libroDAO = new LibroDAO();
        SocioDAO socioDAO = new SocioDAO();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();

        libro = ejemplarDAO.obtenerLibroPorCopiaId(prestamo.getCopiaId());
        socio = socioDAO.obtenerSocioPorId(prestamo.getSocioId());

        // Si el libro no es null, establecer la imagen del libro y actualizar los labels del libro
        if (libro != null) {
            imagenLibro = libro.getPortada();

            byte[] portadaBytes = libro.getPortada();

            if (portadaBytes != null) {
                Image image = new Image(new ByteArrayInputStream(portadaBytes));
                imagenLibroView.setImage(image);
            }
            labelLibroId.setText(String.valueOf(libro.getLibroId()));
            labelLibro.setText(libro.getTitulo());
        }

        // Si el socio no es null, establecer la imagen del socio y actualizar los labels del socio
        if (socio != null) {
            imagenSocio = socio.getSocioFoto();

            byte[] socioBytes = socio.getSocioFoto();

            if (socioBytes != null) {
                Image image = new Image(new ByteArrayInputStream(socioBytes));
                imagenSocioView.setImage(image);
            }
            labelSocioId.setText(String.valueOf(socio.getSocioId()));
            labelSocio.setText(socio.getNombre());
        }

        // Si el préstamo no es null, recorrer los items del MenuButton de estado y disparar el evento del item que coincide con el estado del préstamo
        if (prestamo != null) {

            for (MenuItem menuItem : estadoMenuButton.getItems()) {
                if (menuItem.getText().equals(prestamo.getEstado())) {
                    menuItem.fire();
                    break;
                }
            }

        }
    }

    /**
     * Este método se utiliza para establecer la imagen del libro.
     * Si la imagen del libro no es null, crea una nueva imagen a partir de los bytes de la imagen y la establece en la vista de la imagen del libro.
     * Si la imagen del libro es null, establece la vista de la imagen del libro en null.
     *
     * @param imagenLibro los bytes de la imagen del libro
     */
    public void libroImagen(byte[] imagenLibro) {
        if (imagenLibro != null) {
            Image image = new Image(new ByteArrayInputStream(imagenLibro));
            imagenLibroView.setImage(image);
        } else {
            imagenLibroView.setImage(null);
        }
    }

    /**
     * Este método se utiliza para establecer la imagen del socio.
     * Si la imagen del socio no es null, crea una nueva imagen a partir de los bytes de la imagen y la establece en la vista de la imagen del socio.
     * Si la imagen del socio es null, establece la vista de la imagen del socio en null.
     *
     * @param imagenSocio los bytes de la imagen del socio
     */
    public void socioImagen(byte[] imagenSocio) {
        if (imagenSocio != null) {
            Image image = new Image(new ByteArrayInputStream(imagenSocio));
            imagenSocioView.setImage(image);
        } else {
            imagenSocioView.setImage(null);
        }
    }

    /**
     * Este método se utiliza para establecer el título y el ID del libro en los labels correspondientes.
     *
     * @param tituloLibro el título del libro
     * @param labelLibroId el ID del libro
     */
    public void libroLabel(String tituloLibro, int labelLibroId) {
        this.labelLibro.setText(tituloLibro);
        this.labelLibroId.setText(Integer.toString(labelLibroId));
    }

    /**
     * Este método se utiliza para establecer el nombre y el ID del socio en los labels correspondientes.
     *
     * @param nombreSocio el nombre del socio
     * @param labelSocioId el ID del socio
     */
    public void socioLabel(String nombreSocio, int labelSocioId) {
        this.labelSocio.setText(nombreSocio);
        this.labelSocioId.setText(Integer.toString(labelSocioId));
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