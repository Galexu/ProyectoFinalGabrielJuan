package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import atlantafx.base.theme.Styles;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Modelo.Socio;
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

public class ControladorPrestamoSeleccionarSocio {
    @FXML
    private TableColumn<?, ?> direccionColumn;

    @FXML
    private TableColumn<?, ?> emailColumn;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> nombreColumn;

    @FXML
    private TableView<Socio> tablaSocios;

    @FXML
    private TableColumn<?, ?> telefonoColumn;

    @FXML
    private ImageView imagenSocioView;

    @FXML
    private StackPane stackPaneImagenSocio;

    private ControladorPrestamoSeleccionarSocio.socioSelectedListener socioSelectedListener;

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
     * Obtiene el socio seleccionado de la tabla de socios.
     * Si el socio seleccionado y el listener de socio seleccionado no son null, invoca el método socioSeleccionado del listener con el socio seleccionado y cierra la ventana.
     *
     * @param event el evento de acción que ocurrió
     */
    @FXML
    void onClickSeleccionar(ActionEvent event) {
        // Obtener el socio seleccionado de la tabla de socios
        Socio socioSeleccionado = tablaSocios.getSelectionModel().getSelectedItem();
        // Si el socio seleccionado y el listener de socio seleccionado no son null
        if (socioSeleccionado != null && socioSelectedListener != null) {
            // Invocar el método socioSeleccionado del listener con el socio seleccionado
            socioSelectedListener.socioSeleccionado(socioSeleccionado);
            // Obtener el Stage del evento y cerrar la ventana
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Este método se invoca automáticamente después de que se ha cargado el archivo FXML.
     * Añade la clase de estilo "border-default" al StackPane de la imagen del socio.
     * Cambia la clase de estilo de la tabla de socios a "bordered" y "striped".
     * Establece las fábricas de celdas de valor para las columnas de la tabla de socios.
     * Añade un listener a la propiedad selectedItem de la selección de la tabla de socios que actualiza la imagen del socio cuando se selecciona un socio.
     * Llama al método mostraSocios para llenar la tabla de socios con los socios disponibles.
     */
    @FXML
    void initialize() {
        // Añadir la clase de estilo "border-default" al StackPane de la imagen del socio
        stackPaneImagenSocio.getStyleClass().add("border-default");

        // Cambiar la clase de estilo de la tabla de socios a "bordered" y "striped"
        Styles.toggleStyleClass(tablaSocios, Styles.BORDERED);
        Styles.toggleStyleClass(tablaSocios, Styles.STRIPED);

        // Establecer las fábricas de celdas de valor para las columnas de la tabla de socios
        idColumn.setCellValueFactory(new PropertyValueFactory<>("socioId"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        direccionColumn.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        telefonoColumn.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Añadir un listener a la propiedad selectedItem de la selección de la tabla de socios
        tablaSocios.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            // Si se selecciona un socio
            if (newSelection != null) {
                // Obtener el socio seleccionado
                Socio socioSeleccionado = newSelection;
                // Obtener la foto del socio seleccionado
                byte[] portadaBytes = socioSeleccionado.getSocioFoto();
                // Si la foto del socio seleccionado no es null
                if (portadaBytes != null) {
                    // Crear una nueva imagen con la foto del socio seleccionado y establecerla en la vista de la imagen del socio
                    Image image = new Image(new ByteArrayInputStream(portadaBytes));
                    imagenSocioView.setImage(image);
                } else {
                    // Si la foto del socio seleccionado es null, establecer la vista de la imagen del socio en null
                    imagenSocioView.setImage(null);
                }
            }
        });

        // Llenar la tabla de socios con los socios disponibles
        mostraSocios();
    }

    /**
     * Esta interfaz define un listener que se invoca cuando se selecciona un socio.
     */
    public interface socioSelectedListener {
        /**
         * Este método se invoca cuando se selecciona un socio.
         *
         * @param socioSeleccionado el socio que se ha seleccionado
         */
        void socioSeleccionado(Socio socioSeleccionado);
    }

    /**
     * Este método se utiliza para establecer el listener que se invoca cuando se selecciona un socio.
     *
     * @param listener el listener que se invoca cuando se selecciona un socio
     */
    public void onSocioSelectedListener(ControladorPrestamoSeleccionarSocio.socioSelectedListener listener) {
        // Establecer el listener que se invoca cuando se selecciona un socio
        this.socioSelectedListener = listener;
    }

    /**
     * Este método se utiliza para mostrar los socios disponibles en la tabla de socios.
     * Primero, crea un SocioDAO y obtiene los socios.
     * Luego, crea una ObservableList con los socios y establece los items de la tabla de socios con la ObservableList.
     */
    void mostraSocios() {
        // Crear un SocioDAO y obtener los socios
        SocioDAO socioDAO = new SocioDAO();
        List<Socio> socio = socioDAO.obtenerSocios();

        // Crear una ObservableList con los socios
        ObservableList<Socio> observableList = FXCollections.observableArrayList(socio);
        // Establecer los items de la tabla de socios con la ObservableList
        tablaSocios.setItems(observableList);
    }
}
