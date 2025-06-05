create database if not exists alumnos;

use alumnos;


CREATE TABLE alumnos (
    dni VARCHAR(20) PRIMARY KEY,
    apellido VARCHAR(50),
    nombre VARCHAR(50),
    eliminado BOOLEAN DEFAULT false
);

