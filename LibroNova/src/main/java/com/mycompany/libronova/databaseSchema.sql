/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Coder
 * Created: 14/10/2025
 */

DROP DATABASE IF EXISTS libroNova;
CREATE DATABASE libroNova;
USE libroNova;

CREATE TABLE usuarios (
    id_usuario INT PRIMARY KEY AUTO_INCREMENT,
    nombre_usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    rol ENUM('ADMIN', 'USUARIO') NOT NULL DEFAULT 'USUARIO',
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE libros (
    id_libro INT PRIMARY KEY AUTO_INCREMENT,
    isbn VARCHAR(20) UNIQUE NOT NULL,
    titulo VARCHAR(200) NOT NULL,
    autor VARCHAR(100) NOT NULL,
    stock INT NOT NULL DEFAULT 1,
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE prestamos (
    id_prestamo INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario INT NOT NULL,
    id_libro INT NOT NULL,
    fecha_prestamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion DATE NOT NULL,
    devuelto BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_devolucion_real TIMESTAMP NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_libro) REFERENCES libros(id_libro) ON DELETE CASCADE
);

-- Usuario admin por defecto (contraseña: admin123)
INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, rol) 
VALUES ('admin', 'admin123', 'Administrador', 'ADMIN');

-- Datos de ejemplo - Libros
INSERT INTO libros (isbn, titulo, autor, stock, disponible) VALUES
('978-0-13-468599-1', 'Clean Code', 'Robert C. Martin', 3, TRUE),
('978-0-13-235088-4', 'Clean Architecture', 'Robert C. Martin', 2, TRUE),
('978-0-201-63361-0', 'Design Patterns', 'Gang of Four', 1, TRUE),
('978-0-13-110362-7', 'The Pragmatic Programmer', 'Andrew Hunt', 2, TRUE),
('978-0-13-475759-9', 'Refactoring', 'Martin Fowler', 1, TRUE);

-- Usuarios de ejemplo (contraseña: password123 para todos)
INSERT INTO usuarios (nombre_usuario, contrasena, nombre_completo, rol) VALUES
('juan.perez', 'password123', 'Juan Pérez', 'USUARIO'),
('maria.gomez', 'password123', 'María Gómez', 'USUARIO'),
('carlos.ruiz', 'password123', 'Carlos Ruiz', 'USUARIO');

-- Préstamos activos (no devueltos)
INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion, devuelto) VALUES
-- Juan tiene 2 libros prestados
(2, 1, '2025-10-01 10:30:00', '2025-10-15', FALSE),
(2, 4, '2025-10-05 14:20:00', '2025-10-19', FALSE),
-- María tiene 1 libro prestado
(3, 2, '2025-10-08 09:15:00', '2025-10-22', FALSE);

-- Préstamos ya devueltos (historial)
INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion, devuelto, fecha_devolucion_real) VALUES
-- Juan devolvió estos libros
(2, 3, '2025-09-10 11:00:00', '2025-09-24', TRUE, '2025-09-23 16:45:00'),
(2, 5, '2025-09-15 13:30:00', '2025-09-29', TRUE, '2025-10-01 10:20:00'),
-- María devolvió este libro
(3, 1, '2025-09-20 10:00:00', '2025-10-04', TRUE, '2025-10-03 15:30:00'),
-- Carlos devolvió este libro tarde
(4, 2, '2025-09-05 12:00:00', '2025-09-19', TRUE, '2025-09-25 11:00:00');