# LibroNova - Library Management System

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blue.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![Swing](https://img.shields.io/badge/GUI-Java%20Swing-green.svg)](https://docs.oracle.com/javase/tutorial/uiswing/)

LibroNova is a comprehensive library management system built with Java that provides an intuitive desktop interface for managing books, users, and loans in a library setting. The system features role-based access control with separate dashboards for administrators and regular users.

## 🚀 Features

### For Administrators
- **User Management**: Create, edit, and delete user accounts
- **Book Management**: Add, update, and remove books from the library catalog
- **Loan Management**: Monitor all loans, process returns, and track overdue items
- **Dashboard Analytics**: View system statistics and reports
- **Role Management**: Assign roles (Admin/User) to different users
- **Import Data from csv**: All data visible on each panel can be imported from CSV.
- **Export Data from csv**: All data visible on each panel can be exported from CSV.

### For Regular Users
- **Book Search**: Browse and search for books by title, author, or ISBN
- **Loan History**: View personal loan history and current loans
- **Book Availability**: Check real-time availability of books

### System Features
- **Authentication**: Secure login system with role-based access
- **Database Integration**: MySQL database for persistent data storage
- **Intuitive GUI**: User-friendly Swing-based graphical interface
- **Data Validation**: Comprehensive input validation and error handling
- **Dependency Injection**: Clean architecture with ApplicationContext pattern

## 🏗️ Architecture

The project follows a layered architecture pattern:

```
src/main/java/com/mycompany/libronova/
├── domain/                 # Entity classes (User, Book, Loan)
├── repository/            # Data access layer
│   └── impl/             # Repository implementations
├── service/              # Business logic layer
│   └── impl/            # Service implementations
├── ui/                   # User interface components
├── dbconnection/         # Database connection management
├── infra/config/         # Configuration and dependency injection
└── exception/            # Custom exception classes
```

## 📋 Prerequisites

Before running LibroNova, ensure you have the following installed:

- **Java Development Kit (JDK) 17** or higher
- **Apache Maven 3.x**
- **MySQL Server 8.0** or higher
- **MySQL Connector/J 8.0.33** (included as dependency)

## 🛠️ Installation

### 1. Database Setup

First, create the MySQL database and tables:

```sql
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
```

### 2. Database Configuration

Update the database configuration in `src/main/resources/application.properties`:

```properties
db.vendor=mysql
db.name=libroNova
db.user=your_username
db.password=your_password
db.port=3306
db.host=localhost
db.url=jdbc:mysql://localhost:3306/libroNova?useSSL=false&serverTimezone=UTC
```

**⚠️ Important**: Replace `your_username` and `your_password` with your actual MySQL credentials.

### 3. Project Compilation

Navigate to the project directory and compile using Maven:

```bash
cd /path/to/NetBeansProjects/LibroNova
mvn clean compile
```

## 🚀 Running the Application

### Method 1: Using Maven

```bash
mvn exec:java -Dexec.mainClass="com.mycompany.libronova.LibroNova"
```

### Method 2: Using Maven with exec plugin

```bash
mvn exec:exec
```

### Method 3: Building and running JAR

```bash
# Build the project
mvn clean package

# Run the JAR file
java -jar target/LibroNova-1.0-SNAPSHOT.jar
```

### Method 4: From IDE

Open the project in NetBeans IDE and run the main class:
- Main Class: `com.mycompany.libronova.LibroNova`
- Right-click on the project → Run

## 👤 Default User Accounts

The system comes with pre-configured user accounts for testing:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| admin | admin123 | ADMIN | Full system access |
| juan.perez | password123 | USUARIO | Regular user access |

## 📚 Usage Guide

### Administrator Workflow

1. **Login**: Use admin credentials to access the system
2. **User Management**: 
   - Add new users with appropriate roles
   - Edit existing user information
   - Remove users when necessary
3. **Book Management**:
   - Add new books to the catalog
   - Update book information and stock levels
   - Remove books from the system
4. **Loan Management**:
   - Process loan requests
   - Handle book returns
   - Monitor overdue loans

### User Workflow

1. **Login**: Use your credentials to access the system
2. **Browse Books**: Search and view available books
3. **Request Loans**: Request books for borrowing
4. **View History**: Check your loan history and current loans
5. **Return Books**: Process returns through the admin

## 🏗️ Project Structure

```
LibroNova/
├── pom.xml                              # Maven configuration
├── src/main/
│   ├── java/com/mycompany/libronova/
│   │   ├── LibroNova.java              # Main application class
│   │   ├── domain/                      # Entity models
│   │   │   ├── User.java               # User entity
│   │   │   ├── Book.java               # Book entity
│   │   │   └── Loan.java               # Loan entity
│   │   ├── repository/                  # Data access layer
│   │   │   ├── Repository.java         # Base repository interface
│   │   │   ├── UserRepository.java     # User repository interface
│   │   │   ├── BookRepository.java     # Book repository interface
│   │   │   ├── LoanRepository.java     # Loan repository interface
│   │   │   └── impl/                   # Repository implementations
│   │   ├── service/                     # Business logic layer
│   │   │   ├── UserService.java        # User service interface
│   │   │   ├── BookService.java        # Book service interface
│   │   │   ├── LoanService.java        # Loan service interface
│   │   │   └── impl/                   # Service implementations
│   │   ├── ui/                         # User interface components
│   │   │   ├── Login.java              # Login window
│   │   │   ├── DashboardAdmin.java     # Admin dashboard
│   │   │   └── DashboardUser.java      # User dashboard
│   │   ├── dbconnection/               # Database connection
│   │   │   └── DbConnection.java       # Connection manager
│   │   ├── infra/config/               # Configuration
│   │   │   ├── AppConfig.java          # App configuration
│   │   │   └── ApplicationContext.java # Dependency injection
│   │   └── exception/                   # Custom exceptions
│   │       ├── AuthenticationException.java
│   │       └── EntityNotFoundException.java
│   └── resources/
│       └── application.properties       # Configuration file
└── target/                             # Compiled classes and JAR
```

## 🔧 Technologies Used

- **Java 17**: Core programming language
- **Maven**: Build automation and dependency management
- **MySQL 8.0**: Database management system
- **JDBC**: Database connectivity
- **Java Swing**: GUI framework
- **Nimbus Look and Feel**: Modern UI appearance

### Dependencies

- **MySQL Connector/J 8.0.33**: MySQL database driver

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify MySQL server is running
   - Check database credentials in `application.properties`
   - Ensure the database `libroNova` exists

2. **ClassNotFoundException**
   - Ensure MySQL Connector/J is in the classpath
   - Try rebuilding the project: `mvn clean compile`

3. **Authentication Failed**
   - Use default credentials: admin/admin123 or juan.perez/password123
   - Check if users table has been populated

4. **GUI Not Appearing**
   - Ensure you're running in a graphical environment
   - Try running with different look and feel settings

### Debug Mode

Run with debug information:
```bash
mvn exec:java -Dexec.mainClass="com.mycompany.libronova.LibroNova" -X
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is developed for educational purposes. See the license file for details.

## 📞 Support

For support and questions:
- Check the troubleshooting section above
- Review the project documentation
- Contact the development team

## 🔄 Version History

- **v1.0-SNAPSHOT**: Initial release
  - Basic user and book management
  - Role-based authentication
  - Loan tracking system
  - Swing-based GUI

## 🎯 Future Enhancements

- [ ] Web-based interface
- [ ] Advanced reporting features
- [ ] Email notifications for overdue books
- [ ] Book reservation system
- [ ] Integration with external library databases
- [ ] Mobile application support
- [ ] Advanced search and filtering options
- [ ] User preference management

---

**LibroNova** - Simplifying library management through technology. 📚✨