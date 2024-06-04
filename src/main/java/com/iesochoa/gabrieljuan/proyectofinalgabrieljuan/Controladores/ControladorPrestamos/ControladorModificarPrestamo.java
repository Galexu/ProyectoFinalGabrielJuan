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
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ControladorModificarPrestamo implements ControladorPrestamoSeleccionarLibro.OnLibroSelectedListener, ControladorPrestamoSeleccionarSocio.OnSocioSelectedListener {

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

    private byte[] imagenLibro;

    private byte[] imagenSocio;

    private Prestamo prestamo;
    private Libro libro;
    private Socio socio;
    private Ejemplar ejemplar;

    private Runnable onPrestamoChangeListener;

    public void setOnPrestamoChangeListener(Runnable onLibroChangeListener) {
        this.onPrestamoChangeListener = onLibroChangeListener;
    }


    @Override
    public void onLibroSelected(Libro libroSeleccionado) {
        setLibroLabel(libroSeleccionado.getTitulo(), libroSeleccionado.getLibroId());
        setLibroImage(libroSeleccionado.getPortada());
    }

    @Override
    public void onSocioSelected(Socio socioSeleccionado) {
        setSocioLabel(socioSeleccionado.getNombre(), socioSeleccionado.getSocioId());
        setSocioImage(socioSeleccionado.getSocioFoto());
    }

    @FXML
    void onClickSeleccionarLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-libro-view.fxml"));
            Parent root = loader.load();

            ControladorPrestamoSeleccionarLibro controladorSeleccionarLibro = loader.getController();
            controladorSeleccionarLibro.setOnLibroSelectedListener(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickSeleccionarSocio(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-socio-view.fxml"));
            Parent root = loader.load();

            ControladorPrestamoSeleccionarSocio controladorSeleccionarSocio = loader.getController();
            controladorSeleccionarSocio.setOnSocioSelectedListener(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onClickCancelar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();

        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    @FXML
    void onClickModificar(ActionEvent event) throws ParseException {
        if (!validarCampos()) {
            mostrarAlerta();
            return;
        }

        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        int ejemplarId = ejemplarDAO.encontrarIdEjemplar(labelLibroId.getText());
        prestamo.setPrestamoId(prestamo.getPrestamoId());
        prestamo.setCopiaId(ejemplarId);
        prestamo.setSocioId(Integer.parseInt(labelSocioId.getText()));

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

        prestamo.setFechaPrestamo(datePrestamo);
        prestamo.setFechaDevolucion(dateDevolucion);
        prestamo.setFechaLimite(dateLimite);
        prestamo.setEstado(estadoMenuButton.getText());

        PrestamoDAO prestamoDAO = new PrestamoDAO();
        prestamoDAO.actualizarPrestamo(prestamo);

        Stage stage = (Stage) labelLibroId.getScene().getWindow();
        stage.close();

        if (onPrestamoChangeListener != null) {
            onPrestamoChangeListener.run();
        }
    }

    public void posicionarPrestamo(Prestamo prestamo) {
        this.libro = libro;
        this.socio = socio;
        this.prestamo = prestamo;
        this.ejemplar = ejemplar;

        LibroDAO libroDAO = new LibroDAO();
        SocioDAO socioDAO = new SocioDAO();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();

        libro = ejemplarDAO.obtenerLibroPorCopiaId(prestamo.getCopiaId());
        socio = socioDAO.obtenerSocioPorId(prestamo.getSocioId());

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

        if (prestamo != null) {

            for (MenuItem menuItem : estadoMenuButton.getItems()) {
                if (menuItem.getText().equals(prestamo.getEstado())) {
                    menuItem.fire();
                    break;
                }
            }

        }
    }

    @FXML
    void initialize() {
        for (MenuItem menuItem : estadoMenuButton.getItems()) {
            menuItem.setOnAction(event -> {
                estadoMenuButton.setText(menuItem.getText());
            });
        }
    }

    public void setLibroImage(byte[] imagenLibro) {
        if (imagenLibro != null) {
            Image image = new Image(new ByteArrayInputStream(imagenLibro));
            imagenLibroView.setImage(image);
        } else {
            imagenLibroView.setImage(null);
        }
    }

    public void setSocioImage(byte[] imagenSocio) {
        if (imagenSocio != null) {
            Image image = new Image(new ByteArrayInputStream(imagenSocio));
            imagenSocioView.setImage(image);
        } else {
            imagenSocioView.setImage(null);
        }
    }

    public void setLibroLabel(String tituloLibro, int labelLibroId) {
        this.labelLibro.setText(tituloLibro);
        this.labelLibroId.setText(Integer.toString(labelLibroId));

    }

    public void setSocioLabel(String nombreSocio, int labelSocioId) {
        this.labelSocio.setText(nombreSocio);
        this.labelSocioId.setText(Integer.toString(labelSocioId));
    }

    private boolean validarCampos() {
        if (campoFechaPrestamo.getValue() == null || campoFechaDevolucion.getValue() == null || campoFechaLimite.getValue() == null) {
            return false;
        }

        if (labelLibroId.getText().isEmpty()) {
            return false;
        }

        if (labelSocioId.getText().isEmpty()) {
            return false;
        }

        if (estadoMenuButton.getText().equals("Seleccionar Estado")) {
            return false;
        }

        if (campoFechaDevolucion.getValue().isBefore(campoFechaPrestamo.getValue()) || campoFechaLimite.getValue().isBefore(campoFechaPrestamo.getValue())) {
            return false;
        }

        return true;
    }

    private void mostrarAlerta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Por favor, introduzca los campos correctos.");

        alert.showAndWait();
    }

}