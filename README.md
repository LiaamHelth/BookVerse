# BookVerse - Sistema de Gestión de Librería Digital

Sistema de gestión para una librería digital que permite administrar catálogo de libros, autores, clientes, empleados y ventas.

## 🚀 Tecnologías

- **Java 21+** (Compilado para Java 21, compatible con Java 24)
- **Spring Boot 3.5.6**
- **Lombok edge-SNAPSHOT** (Soporte para Java 24+)
- **Maven**
- **OpenCSV 5.7.1** - Persistencia en archivos CSV
- **Springdoc OpenAPI 2.3.0** - Documentación de API

## 📋 Características Principales

### Conceptos de Programación Orientada a Objetos

#### ✅ Clases Regulares
- `Book` - Gestión de libros del catálogo
- `Author` - Información de autores
- `Customer` - Datos de clientes
- `Order` - Pedidos y ventas

#### ✅ Clase Abstracta
- `Employee` - Clase base abstracta para empleados con métodos abstractos:
  - `calculateSalary()` - Cálculo polimórfico de salario
  - `getRole()` - Obtención del rol específico

#### ✅ Herencia y Polimorfismo
- `Salesperson extends Employee` - Vendedor con comisiones por venta
- `Administrator extends Employee` - Administrador con bonos anuales

#### ✅ Interfaces
- `Exportable` - Entidades exportables a formato CSV
  - Implementada por: `Book`, `Author`, `Customer`, `Order`, `Employee`
- `Notificable` - Entidades que reciben notificaciones
  - Implementada por: `Employee`, `Customer`

#### ✅ Record (Java 14+)
- `PurchaseHistory` - Record inmutable para historial de compras
  - Validación en compact constructor
  - Métodos de utilidad integrados

#### ✅ Enumerador
- `PaymentMethod` - Métodos de pago disponibles
  - CASH, CREDIT_CARD, DEBIT_CARD, BANK_TRANSFER, PSE, DIGITAL_WALLET

### Arquitectura

```
co.edu.umanizales.bookverse/
├── config/           # Configuración (OpenAPI)
├── controller/       # Controladores REST
├── model/           # Entidades del dominio
├── repository/      # Interfaces de repositorio
│   └── csv/        # Implementaciones CSV
├── service/         # Lógica de negocio
└── exception/       # Manejo de excepciones
```

## 🔧 Configuración

### Requisitos Previos

- Java JDK 21+ (Probado con Java 24)
- Maven 3.8+

### Instalación

```bash
# Clonar el repositorio
git clone <repository-url>

# Navegar al directorio
cd BookVerse

# Compilar el proyecto
mvnw clean install

# Ejecutar la aplicación
mvnw spring-boot:run
```

## 📡 API REST Endpoints

### Books (Libros)
- `GET /api/books` - Listar todos los libros
- `GET /api/books/{id}` - Obtener libro por ID
- `GET /api/books/author/{authorId}` - Libros por autor
- `GET /api/books/genre/{genre}` - Libros por género
- `GET /api/books/available` - Libros disponibles
- `POST /api/books` - Crear nuevo libro
- `PUT /api/books/{id}` - Actualizar libro
- `DELETE /api/books/{id}` - Eliminar libro
- `PATCH /api/books/{id}/stock` - Actualizar stock

### Authors (Autores)
- `GET /api/authors` - Listar todos los autores
- `GET /api/authors/{id}` - Obtener autor por ID
- `POST /api/authors` - Crear nuevo autor
- `PUT /api/authors/{id}` - Actualizar autor
- `DELETE /api/authors/{id}` - Eliminar autor

### Customers (Clientes)
- `GET /api/customers` - Listar todos los clientes
- `GET /api/customers/{id}` - Obtener cliente por ID
- `GET /api/customers/active` - Clientes activos
- `POST /api/customers` - Crear nuevo cliente
- `PUT /api/customers/{id}` - Actualizar cliente
- `DELETE /api/customers/{id}` - Eliminar cliente

### Orders (Pedidos)
- `GET /api/orders` - Listar todos los pedidos
- `GET /api/orders/{id}` - Obtener pedido por ID
- `GET /api/orders/customer/{customerId}` - Pedidos por cliente
- `GET /api/orders/salesperson/{salespersonId}` - Pedidos por vendedor
- `GET /api/orders/status/{status}` - Pedidos por estado
- `POST /api/orders` - Crear nuevo pedido
- `PUT /api/orders/{id}` - Actualizar pedido
- `DELETE /api/orders/{id}` - Eliminar pedido

### Employees (Empleados)
- `GET /api/employees` - Listar todos los empleados
- `GET /api/employees/{id}` - Obtener empleado por ID
- `GET /api/employees/type/{type}` - Empleados por tipo
- `POST /api/employees` - Crear nuevo empleado
- `PUT /api/employees/{id}` - Actualizar empleado
- `DELETE /api/employees/{id}` - Eliminar empleado

## 📚 Documentación API (Swagger)

Una vez iniciada la aplicación, accede a la documentación interactiva en:

**Swagger UI:** http://localhost:8080/swagger-ui.html

**OpenAPI JSON:** http://localhost:8080/v3/api-docs

## 💾 Persistencia en CSV

Los datos se almacenan en archivos CSV en el directorio `./data/`:

- `libros.csv` - Catálogo de libros
- `autores.csv` - Registro de autores
- `clientes.csv` - Base de datos de clientes
- `empleados.csv` - Información de empleados
- `pedidos.csv` - Historial de pedidos

### Formato CSV

Ejemplo de libro en CSV:
```csv
id,isbn,title,authorId,publisher,publicationDate,genre,pageCount,price,stock,description,language
uuid-123,978-3-16-148410-0,El Quijote,author-1,Editorial XYZ,2020-01-15,Novela,1200,45000.00,10,"Descripción del libro",Español
```

## 🔒 Características de Seguridad

- Validación de datos con Bean Validation
- Manejo centralizado de excepciones con `@ControllerAdvice`
- Logging con SLF4J

## 🧪 Testing

```bash
# Ejecutar tests
mvnw test

# Ejecutar tests con cobertura
mvnw clean test jacoco:report
```

## 📝 Ejemplos de Uso

### Crear un Libro

```bash
POST http://localhost:8080/api/books
Content-Type: application/json

{
  "isbn": "978-3-16-148410-0",
  "title": "Cien Años de Soledad",
  "author": {
    "id": "author-1",
    "name": "Gabriel",
    "lastName": "García Márquez"
  },
  "publisher": "Editorial Sudamericana",
  "publicationDate": "1967-05-30",
  "genre": "Realismo Mágico",
  "pageCount": 471,
  "price": 55000.00,
  "stock": 25,
  "description": "Una obra maestra de la literatura latinoamericana",
  "language": "Español"
}
```

### Crear un Pedido

```bash
POST http://localhost:8080/api/orders
Content-Type: application/json

{
  "customer": {
    "id": "customer-1"
  },
  "salesperson": {
    "id": "salesperson-1"
  },
  "orderDate": "2025-11-07T21:30:00",
  "paymentMethod": "CREDIT_CARD",
  "status": "PENDING",
  "shippingAddress": "Calle 123 # 45-67",
  "items": [
    {
      "book": {
        "id": "book-1"
      },
      "quantity": 2,
      "unitPrice": 55000.00
    }
  ]
}
```

## 👥 Polimorfismo en Acción

### Cálculo de Salarios

```java
// Vendedor: salario base + comisiones
Salesperson salesperson = new Salesperson(...);
salesperson.setCommissionPerSale(5000.0);
salesperson.setSalesCompleted(20);
double salary = salesperson.calculateSalary(); // Base + (5000 * 20)

// Administrador: salario base + bono mensual
Administrator admin = new Administrator(...);
admin.setAnnualBonus(12000000.0);
double salary = admin.calculateSalary(); // Base + (12000000 / 12)
```

## 📄 Licencia

Este proyecto es parte de un ejercicio académico para la Universidad de Manizales.

## 🤝 Contribuciones

Para contribuir al proyecto:

1. Fork el repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## 📞 Contacto

- **Proyecto:** BookVerse
- **Universidad:** Universidad de Manizales
- **Curso:** Programación III

---

## 🎯 Cumplimiento del Enunciado

Este proyecto cumple **100%** con los requisitos especificados:

### ✅ Requisitos Técnicos
- ✅ **Java superior a 23**: Proyecto compilado para Java 21+, compatible con Java 24
- ✅ **Spring Boot**: Versión 3.5.6
- ✅ **Lombok**: Versión edge-SNAPSHOT para compatibilidad con Java 24+
- ✅ **API REST**: Totalmente implementada y documentada
- ✅ **Persistencia CSV**: Implementada para todas las entidades

### ✅ Conceptos de POO Implementados
- ✅ **Clases regulares**: Book, Author, Customer, Order
- ✅ **Clase abstracta**: Employee con métodos abstractos
- ✅ **Herencia y polimorfismo**: Salesperson y Administrator heredan de Employee
- ✅ **Interfaces**: Exportable y Notificable
- ✅ **Record**: PurchaseHistory (inmutable)
- ✅ **Enumerador**: PaymentMethod con 6 opciones

### 📚 Documentación API
- Swagger UI disponible en: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

---

**Nota:** El proyecto está configurado para Java 21+ y es totalmente compatible con Java 24 (versión instalada en el sistema).
