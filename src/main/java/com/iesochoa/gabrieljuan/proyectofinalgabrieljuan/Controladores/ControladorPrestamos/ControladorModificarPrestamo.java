package com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.Controladores.ControladorPrestamos;

import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.EjemplarDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.PrestamoDAO;
import com.iesochoa.gabrieljuan.proyectofinalgabrieljuan.DAO.SocioDAO;
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
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ControladorModificarPrestamo {

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

    private Prestamo prestamo;

    @FXML
    void onClickSeleccionarLibro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/iesochoa/gabrieljuan/proyectofinalgabrieljuan/agregar-prestamo-seleccion-libro-view.fxml"));
            Parent root = loader.load();

//            ControladorPrestamoSeleccionarLibro controladorSeleccionarLibro = loader.getController();
//
//            controladorSeleccionarLibro.setOnLibroSelectedListener(this);

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

//            ControladorPrestamoSeleccionarSocio controladorSeleccionarSocio = loader.getController();
//
//            controladorSeleccionarSocio.setOnSocioSelectedListener(this);

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
    void onClickModificar(ActionEvent event) throws ParseException {
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
    }

    public void posicionarPrestamo(Prestamo prestamo) {
    }

    @FXML
    void initialize() {
    }
}