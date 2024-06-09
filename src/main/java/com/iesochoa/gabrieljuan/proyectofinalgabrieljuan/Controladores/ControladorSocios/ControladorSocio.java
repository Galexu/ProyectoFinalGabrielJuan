package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorSocios;

import atlantafx.base.theme.*;
import au.com.bytecode.opencsv.CSVWriter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorLibros.ControladorAgregarLibro;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.LibroDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Libro;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControladorSocio {
    @FXML
    private CheckBox checkDireccion;

    @FXML
    private CheckBox checkEmail;

    @FXML
    private CheckBox checkId;

    @FXML
    private CheckBox checkNombre;

    @FXML
    private CheckBox checkTelefono;

    @FXML
    private Label labelNombreSocio;

    @FXML
    private Button InicioButton;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<Socio, Integer> idColumn;

    @FXML
    private TableColumn<Socio, String> nombreColumn;

    @FXML
    private TableColumn<Socio, String> direccionColumn;

    @FXML
    private TableColumn<Socio, String> telefonoColumn;

    @FXML
    private TableColumn<Socio, String> emailColumn;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private ImageView imagenSocioView;

    @FXML
    private StackPane stackPaneImagenSocio;

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
     * Este método se invoca cuando se hace clic en el botón "Refrescar".
     * Primero, llama al método mostrarSocios() para actualizar la lista de socios.
     * Luego, limpia el campo de búsqueda y desmarca todas las casillas de verificación.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickRefrescar(ActionEvent event) {
        // Actualizar la lista de socios
        mostrarSocios();
        // Limpiar el campo de búsqueda
        campoBusqueda.clear();
        // Desmarcar todas las casillas de verificación
        checkId.setSelected(false);
        checkNombre.setSelected(false);
        checkDireccion.setSelected(false);
        checkTelefono.setSelected(false);
        checkEmail.setSelected(false);
    }

    /**
     * Este método se invoca cuando se libera una tecla en el campo de búsqueda.
     * Primero, muestra un indicador de progreso en la tabla de socios.
     * Luego, obtiene el criterio de búsqueda del campo de búsqueda y los estados de las casillas de verificación.
     * Después, crea una tarea para buscar socios en función del criterio de búsqueda y los estados de las casillas de verificación.
     * Cuando la tarea se completa con éxito, actualiza la tabla de socios con los socios encontrados.
     * Finalmente, inicia la tarea en un nuevo hilo.
     *
     * @param event el evento de liberación de tecla
     */
    @FXML
    void onKeyReleasedBuscar(KeyEvent event) {
        // Mostrar un indicador de progreso en la tabla de socios
        ProgressIndicator cargando = new ProgressIndicator();
        tablaSocios.setPlaceholder(cargando);
        // Obtener el criterio de búsqueda del campo de búsqueda
        String criterioBusqueda = campoBusqueda.getText();

        // Obtener los estados de las casillas de verificación
        boolean buscarPorId = checkId.isSelected();
        boolean buscarPorNombre = checkNombre.isSelected();
        boolean buscarPorDireccion = checkDireccion.isSelected();
        boolean buscarPorTelefono = checkTelefono.isSelected();
        boolean buscarPorEmail = checkEmail.isSelected();

        // Crear una tarea para buscar socios
        Task<List<Socio>> task = new Task<List<Socio>>() {
            @Override
            protected List<Socio> call() throws Exception {
                // Crear un DAO de socio
                SocioDAO socioDAO = new SocioDAO();
                // Si se seleccionó buscar por id, nombre o dirección, buscar socios con el criterio de búsqueda y los estados de las casillas de verificación
                if (buscarPorId || buscarPorNombre || buscarPorDireccion) {
                    return socioDAO.buscarSocioCheck(criterioBusqueda, buscarPorId, buscarPorNombre, buscarPorDireccion, buscarPorTelefono, buscarPorEmail);
                } else {
                    // Si no, buscar socios con el criterio de búsqueda
                    return socioDAO.buscarSocios(criterioBusqueda);
                }
            }
        };

        // Cuando la tarea se completa con éxito, actualizar la tabla de socios con los socios encontrados
        task.setOnSucceeded(e -> {
            ObservableList<Socio> observableList = FXCollections.observableArrayList(task.getValue());
            tablaSocios.setItems(observableList);
            tablaSocios.setPlaceholder(new Label("No se encontro nada en la busqueda."));
        });
        // Iniciar la tarea en un nuevo hilo
        new Thread(task).start();
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Agregar Socio".
     * Primero, carga la vista "agregar-socio-view.fxml" y obtiene su controlador.
     * Luego, establece el controlador para que cuando se cambie un socio, se actualice la lista de socios.
     * Después, crea un nuevo Stage, establece la vista cargada como su escena y lo muestra.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickAgregarSocio(ActionEvent event) {
        try {
            // Cargar la vista "agregar-socio-view.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-socio-view.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista
            ControladorAgregarSocio controlador = loader.getController();
            // Establecer el controlador para que cuando se cambie un socio, se actualice la lista de socios
            controlador.setOnSocioChangeListener(this::mostrarSocios);

            // Crear un nuevo Stage
            Stage stage = new Stage();
            // Establecer la vista cargada como la escena del Stage
            stage.setScene(new Scene(root));
            // Mostrar el Stage
            stage.show();
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce un error al cargar la vista
            e.printStackTrace();
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Borrar Socio".
     * Primero, obtiene el socio seleccionado de la tabla de socios.
     * Si hay un socio seleccionado, comprueba si el socio tiene préstamos asociados.
     * Si el socio tiene préstamos asociados, muestra una alerta de error y termina el método.
     * Si el socio no tiene préstamos asociados, muestra una alerta de confirmación para borrar el socio.
     * Si el usuario confirma la eliminación, borra el socio y actualiza la lista de socios.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickBorrarSocio(ActionEvent event) {
        // Obtener el socio seleccionado de la tabla de socios
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();

        // Si hay un socio seleccionado
        if (socioSeleccionado != null) {
            // Crear un DAO de préstamo
            PrestamoDAO prestamoDAO = new PrestamoDAO();
            // Si el socio tiene préstamos asociados, mostrar una alerta de error y terminar el método
            if (prestamoDAO.existePrestamoParaSocio(socioSeleccionado.getSocioId())) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de borrado", "No se puede borrar el socio seleccionado porque tiene préstamos asociados.");
                return;
            }

            // Mostrar una alerta de confirmación para borrar el socio
            Optional<ButtonType> resultado = mostrarAlerta(Alert.AlertType.CONFIRMATION, "Confirmación de borrado", "¿Estás seguro de que quieres borrar el socio seleccionado?");
            // Si el usuario confirma la eliminación
            if (resultado.get() == ButtonType.OK) {
                // Crear un DAO de socio
                SocioDAO socioDAO = new SocioDAO();
                // Borrar el socio
                socioDAO.eliminarSocio(String.valueOf(socioSeleccionado.getSocioId()));
                // Actualizar la lista de socios
                mostrarSocios();
            }
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Modificar Socio".
     * Primero, obtiene el socio seleccionado de la tabla de socios.
     * Si hay un socio seleccionado, carga la vista "modificar-socio-view.fxml" y obtiene su controlador.
     * Luego, posiciona al socio seleccionado en el controlador y establece el controlador para que cuando se cambie un socio, se actualice la lista de socios.
     * Después, crea un nuevo Stage, establece la vista cargada como su escena y lo muestra.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickModificarSocio(ActionEvent event) {
        // Obtener el socio seleccionado de la tabla de socios
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();

        // Si hay un socio seleccionado
        if (socioSeleccionado != null) {
            try {
                // Cargar la vista "modificar-socio-view.fxml"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/modificar-socio-view.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la vista
                ControladorModificarSocio controlador = loader.getController();
                // Posicionar al socio seleccionado en el controlador
                controlador.posicionarSocio(socioSeleccionado);
                // Establecer el controlador para que cuando se cambie un socio, se actualice la lista de socios
                controlador.setOnSocioChangeListener(this::mostrarSocios);

                // Crear un nuevo Stage
                Stage stage = new Stage();
                // Establecer la vista cargada como la escena del Stage
                stage.setScene(new Scene(root));
                // Mostrar el Stage
                stage.show();
            } catch (IOException e) {
                // Imprimir la traza de la excepción si se produce un error al cargar la vista
                e.printStackTrace();
            }
        }
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Exportar a CSV".
     * Primero, crea un objeto SocioDAO y obtiene la lista de todos los socios.
     * Luego, formatea la fecha y hora actual en un formato específico y crea una cadena de ruta de archivo con esta fecha y hora.
     * Finalmente, llama al método exportarCSV() con la lista de socios y la cadena de ruta de archivo para exportar los datos de los socios a un archivo CSV.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickCSV(ActionEvent event) {
        // Crear un objeto SocioDAO
        SocioDAO socioDAO = new SocioDAO();
        // Obtener la lista de todos los socios
        List<Socio> socios = socioDAO.obtenerSocios();

        // Formatear la fecha y hora actual en un formato específico
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        // Crear una cadena de ruta de archivo con la fecha y hora formateadas
        String pathingArchivo = "src/main/resources/exportaciones/datos_socios_csv_" + fecha + ".csv";
        // Exportar los datos de los socios a un archivo CSV
        exportarCSV(socios, pathingArchivo);
    }

    /**
     * Este método se invoca cuando se hace clic en el botón "Exportar a JSON".
     * Primero, crea un objeto SocioDAO y obtiene la lista de todos los socios.
     * Luego, formatea la fecha y hora actual en un formato específico y crea una cadena de ruta de archivo con esta fecha y hora.
     * Finalmente, llama al método exportarJson() con la lista de socios y la cadena de ruta de archivo para exportar los datos de los socios a un archivo JSON.
     *
     * @param event el evento de clic del botón
     */
    @FXML
    void onClickJson(ActionEvent event) {
        // Crear un objeto SocioDAO
        SocioDAO socioDAO = new SocioDAO();
        // Obtener la lista de todos los socios
        List<Socio> socios = socioDAO.obtenerSocios();

        // Formatear la fecha y hora actual en un formato específico
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String fecha = LocalDateTime.now().format(formatter);
        // Crear una cadena de ruta de archivo con la fecha y hora formateadas
        String pathingArchivo = "src/main/resources/exportaciones/datos_socios_json_" + fecha + ".json";
        // Exportar los datos de los socios a un archivo JSON
        exportarJson(socios, pathingArchivo);
    }

    /**
     * Este método se invoca cuando se inicializa el controlador después de que se haya cargado su archivo FXML.
     * Primero, añade la clase de estilo "border-default" al StackPane de la imagen del socio.
     * Luego, alterna las clases de estilo "BORDERED" y "STRIPED" en la tabla de socios.
     * Después, establece las fábricas de celdas de valor para las columnas de la tabla de socios.
     * A continuación, añade un listener a la propiedad selectedItem de la tabla de socios. Cuando se selecciona un nuevo socio, actualiza la etiqueta del nombre del socio y la vista de la imagen del socio.
     * Finalmente, llama al método mostrarSocios() para llenar la tabla de socios con los datos de los socios.
     */
    @FXML
    void initialize() {
        // Añadir la clase de estilo "border-default" al StackPane de la imagen del socio
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Alternar las clases de estilo "BORDERED" y "STRIPED" en la tabla de socios
        Styles.toggleStyleClass(tablaSocios, Styles.BORDERED);
        Styles.toggleStyleClass(tablaSocios, Styles.STRIPED);

        // Establecer las fábricas de celdas de valor para las columnas de la tabla de socios
        idColumn.setCellValueFactory(new PropertyValueFactory<>("socioId"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Añadir un listener a la propiedad selectedItem de la tabla de socios
        tablaSocios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Cuando se selecciona un nuevo socio
            if (newSelection != null) {
                // Obtener el socio seleccionado
                Socio socioSeleccionado = newSelection;
                // Actualizar la etiqueta del nombre del socio
                labelNombreSocio.setText(socioSeleccionado.getNombre());
                // Obtener los bytes de la foto del socio
                byte[] portadaBytes = socioSeleccionado.getSocioFoto();
                // Si los bytes de la foto del socio no son nulos
                if (portadaBytes != null) {
                    // Crear una imagen a partir de los bytes de la foto del socio
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    // Establecer la imagen en la vista de la imagen del socio
                    imagenSocioView.setImage(image);
                } else {
                    // Si los bytes de la foto del socio son nulos, establecer la imagen de la vista de la imagen del socio en null
                    imagenSocioView.setImage(null);
                }
            }
        });

        // Llenar la tabla de socios con los datos de los socios
        mostrarSocios();
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
     * Este método se utiliza para mostrar los socios en la tabla de socios.
     * Primero, muestra un indicador de progreso en la tabla de socios.
     * Luego, crea una tarea para obtener la lista de todos los socios de la base de datos.
     * Cuando la tarea se completa con éxito, actualiza la tabla de socios con los socios obtenidos.
     * Finalmente, inicia la tarea en un nuevo hilo.
     */
    private void mostrarSocios() {
        // Mostrar un indicador de progreso en la tabla de socios
        ProgressIndicator progressIndicator = new ProgressIndicator();
        tablaSocios.setPlaceholder(progressIndicator);

        // Crear una tarea para obtener la lista de todos los socios de la base de datos
        Task<List<Socio>> task = new Task<List<Socio>>() {
            @Override
            protected List<Socio> call() throws Exception {
                // Crear un DAO de socio
                SocioDAO socioDAO = new SocioDAO();
                // Obtener la lista de todos los socios
                return socioDAO.obtenerSocios();
            }
        };

        // Cuando la tarea se completa con éxito, actualizar la tabla de socios con los socios obtenidos
        task.setOnSucceeded(e -> {
            List<Socio> socios = task.getValue();
            ObservableList<Socio> observableList = FXCollections.observableArrayList(socios);
            tablaSocios.setItems(observableList);
            tablaSocios.setPlaceholder(new Label("No hay datos disponibles"));
        });

        // Iniciar la tarea en un nuevo hilo
        new Thread(task).start();
    }

    /**
     * Este método se utiliza para exportar los datos de los socios a un archivo JSON.
     * Primero, crea un objeto Gson con una configuración específica.
     * Luego, escribe los datos de los socios en el archivo JSON.
     * Si se produce una IOException durante la escritura del archivo, imprime la traza de la excepción.
     *
     * @param socios la lista de socios a exportar
     * @param pathingArchivo la ruta del archivo a exportar
     */
    public void exportarJson(List<Socio> socios, String pathingArchivo) {
        try (Writer writer = new FileWriter(pathingArchivo)) {
            // Crear un objeto Gson con una configuración específica
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT)
                    .create();
            // Escribir los datos de los socios en el archivo JSON
            gson.toJson(socios, writer);

            // Mostrar una alerta de información indicando que la exportación ha sido completada exitosamente
            mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación", "La exportación a JSON ha sido completada exitosamente.");
        } catch (IOException e) {
            // Imprimir la traza de la excepción si se produce una IOException
            e.printStackTrace();
        }
    }

    /**
     * Este método se utiliza para exportar los datos de los socios a un archivo CSV.
     * Primero, crea un CSVWriter y escribe la cabecera en el archivo CSV.
     * Luego, para cada socio en la lista de socios, escribe sus datos en el archivo CSV.
     * Si se produce una IOException durante la escritura del archivo, imprime la traza de la excepción.
     *
     * @param socios la lista de socios a exportar
     * @param pathingArchivo la ruta del archivo a exportar
     */
    public void exportarCSV(List<Socio> socios, String pathingArchivo) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(pathingArchivo))) {
            // Escribir la cabecera en el archivo CSV
            String[] cabecera = { "ID", "Nombre", "Direccion", "Telefono", "Email"};
            writer.writeNext(cabecera);

            // Para cada socio en la lista de socios, escribir sus datos en el archivo CSV
            for (Socio socio : socios) {
                String[] datos = { String.valueOf(socio.getSocioId()), socio.getNombre(), socio.getDireccion(), socio.getTelefono(), socio.getEmail()};
                writer.writeNext(datos);
            }

            // Mostrar una alerta de información indicando que la exportación ha sido completada exitosamente
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

