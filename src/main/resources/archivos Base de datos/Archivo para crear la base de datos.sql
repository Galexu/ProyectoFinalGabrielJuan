CREATE DATABASE ProyectoFinalGabrielJuan;
USE ProyectoFinalGabrielJuan;

CREATE TABLE libros (
    isbn VARCHAR(255) PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    autor VARCHAR(255) NOT NULL,
    ano_publicacion INT,
    genero VARCHAR(50),
    portada BLOB
);

CREATE TABLE ejemplares (
    copia_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(255),
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (isbn) REFERENCES libros(isbn)
);

CREATE TABLE socios (
    socio_id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(15),
    email VARCHAR(255),
    socio_foto BLOB
);

CREATE TABLE prestamos (
    prestamo_id INT AUTO_INCREMENT PRIMARY KEY,
    copia_id INT,
    socio_id INT,
    fecha_prestamo DATE,
    fecha_devolucion DATE,
    fecha_limite DATE,
    estado VARCHAR(50),
    FOREIGN KEY (copia_id) REFERENCES ejemplares(copia_id),
    FOREIGN KEY (socio_id) REFERENCES socios(socio_id)
);