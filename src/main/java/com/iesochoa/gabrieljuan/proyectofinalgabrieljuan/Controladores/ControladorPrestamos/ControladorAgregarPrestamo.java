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
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ControladorAgregarPrestamo implements ControladorPrestamoSeleccionarLibro.OnLibroSelectedListener, ControladorPrestamoSeleccionarSocio.OnSocioSelectedListener {

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

    private byte[] imagenLibro;

    private byte[] imagenSocio;

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
    }

    @FXML
    void onClickAgregar(ActionEvent event) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String fechaPrestamo = campoFechaPrestamo.getValue().format(formatter);
        String fechaDevolucion = campoFechaDevolucion.getValue().format(formatter);
        String fechaLimite = campoFechaLimite.getValue().format(formatter);

        Prestamo prestamo = new Prestamo();
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        int ejemplarId = ejemplarDAO.encontrarIdEjemplar(labelLibroId.getText());
        prestamo.setCopiaId((ejemplarId));
        prestamo.setSocioId(Integer.parseInt(labelSocioId.getText()));
        prestamo.setEstado(estadoMenuButton.getText());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date datePrestamo = format.parse(fechaPrestamo);
        prestamo.setFechaPrestamo(datePrestamo);
        Date dateDevolucion = format.parse(fechaDevolucion);
        prestamo.setFechaDevolucion(dateDevolucion);
        Date dateLimite = format.parse(fechaLimite);
        prestamo.setFechaLimite(dateLimite);

        PrestamoDAO prestamoDAO = new PrestamoDAO();
        prestamoDAO.agregarPrestamo(prestamo);

        Stage stage = (Stage) labelLibroId.getScene().getWindow();
        stage.close();
    }

    @FXML
    void initialize() {
        for (MenuItem menuItem : estadoMenuButton.getItems()) {
            menuItem.setOnAction(event -> {
                estadoMenuButton.setText(menuItem.getText());
            });
        }
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

    public void setLibroLabel(String tituloLibro, int labelLibroId) {
        this.labelLibro.setText(tituloLibro);
        this.labelLibroId.setText(Integer.toString(labelLibroId));

    }

    public void setSocioLabel(String nombreSocio, int labelSocioId) {
        this.labelSocio.setText(nombreSocio);
        this.labelSocioId.setText(Integer.toString(labelSocioId));
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
}