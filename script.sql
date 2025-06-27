create database if not exists alumnos;

use alumnos;


CREATE TABLE alumnos (
    dni VARCHAR(20) PRIMARY KEY,
    apellido VARCHAR(50),
    nombre VARCHAR(50),
    eliminado BOOLEAN DEFAULT false,
    email VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    fechaIngreso DATE,
    promedio DECIMAL(4, 2)
);

