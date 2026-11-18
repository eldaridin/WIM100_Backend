# WIM100 Backend

API REST desarrollada con Java y Spring Boot para gestionar usuarios y el proceso de evaluación de postulantes de la iniciativa WIM100.

El proyecto utiliza una arquitectura monolítica organizada por capas: controladores, servicios, repositorios, modelos y DTO. La persistencia se realiza con Spring Data JPA sobre MySQL.

## Tecnologías

- Java 21
- Spring Boot 4.1.2
- Spring Web MVC
- Spring Data JPA
- Hibernate
- Bean Validation
- Spring Security Crypto con BCrypt
- MySQL 8.0
- Maven Wrapper
- Docker Compose
- Lombok

## Funcionalidades

- Inicio de sesión para usuarios registrados.
- Administración de usuarios con perfiles `EVALUADORA` y `DIRECCION`.
- Registro y consulta de postulantes.
- Actualización de datos y eliminación de candidaturas.
- Cambio del estado de una candidatura.
- Validación de datos de entrada.
- Manejo centralizado de errores HTTP.
- Contraseñas almacenadas mediante hashing BCrypt.
- Datos demostrativos cargados automáticamente al iniciar la aplicación.

## Roles

| Rol | Descripción |
|---|---|
| `EVALUADORA` | Usuario con perfil de evaluación de postulaciones. |
| `DIRECCION` | Usuario con perfil de dirección y supervisión. |

## Estados de una candidatura

- `POSTULADA`
- `EN_EVALUACION`
- `EVALUADA`
- `SELECCIONADA`
- `NO_SELECCIONADA`

## Estructura del proyecto

```text
wim100-backend/
├── src/main/java/cl/duoc/wim100_backend/
│   ├── config/          # Configuración y carga de datos iniciales
│   ├── controller/      # Endpoints REST
│   ├── dto/             # Objetos de entrada y salida de la API
│   ├── exception/       # Excepciones y manejador global
│   ├── model/           # Entidades y enumeraciones
│   ├── repository/      # Acceso a datos con Spring Data JPA
│   └── service/         # Reglas de negocio
├── src/main/resources/
│   └── application.properties
├── src/test/java/
├── docker-compose.yml
├── pom.xml
├── mvnw
└── mvnw.cmd
```

## Requisitos

- Docker Desktop o Docker Engine con Docker Compose
- Java 21
- Maven 3.9 o superior, opcional si se utiliza el Maven Wrapper

## Ejecución con Docker Compose

Docker Compose levanta una instancia de MySQL con la base de datos `wim100`.

Desde la raíz del proyecto:

```bash
cd wim100-backend
docker compose up -d
./mvnw spring-boot:run
```

En Windows:

```powershell
cd wim100-backend
docker compose up -d
mvnw.cmd spring-boot:run
```

Una vez iniciada la aplicación:

- API: `http://localhost:8080`
- Base de datos: `localhost:3306`
- Base de datos creada: `wim100`

Para detener MySQL:

```bash
docker compose down
```

El volumen `wim100-mysql-data` conserva los datos entre reinicios. Para eliminarlos completamente:

```bash
docker compose down -v
```

## Ejecución local

Si ya tienes MySQL ejecutándose, asegúrate de que coincida con la configuración de `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/wim100?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Santiago
spring.datasource.username=root
spring.datasource.password=root
```

Luego ejecuta:

```bash
cd wim100-backend
./mvnw spring-boot:run
```

También puedes construir el archivo ejecutable:

```bash
./mvnw clean package
java -jar target/wim100-backend-0.0.1-SNAPSHOT.jar
```

## Datos de demostración

Al iniciar la aplicación, `DataLoader` crea datos iniciales solo si las tablas correspondientes están vacías.

Usuarios disponibles:

| Usuario | Contraseña | Nombre | Rol |
|---|---|---|---|
| `evaluadora1` | `wim2026` | Camila Rojas Demo | `EVALUADORA` |
| `evaluadora2` | `wim2026` | Valentina Muñoz Demo | `EVALUADORA` |
| `direccion` | `wim2026` | Dirección WIM Demo | `DIRECCION` |

También se crean tres postulaciones ficticias para facilitar las pruebas del backend.

> Las credenciales anteriores son datos de demostración. Antes de llevar el proyecto a producción deben modificarse y gestionarse mediante variables de entorno o un sistema seguro de configuración.

## Endpoints

### Autenticación

#### Iniciar sesión

```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "usuario": "evaluadora1",
  "password": "wim2026"
}
```

Respuesta exitosa: `200 OK`

```json
{
  "id": 1,
  "usuario": "evaluadora1",
  "nombre": "Camila Rojas Demo",
  "rol": "EVALUADORA"
}
```

### Usuarios

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/usuarios` | Lista todos los usuarios. |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario por ID. |
| `POST` | `/api/usuarios` | Crea un usuario. |

Ejemplo de creación:

```json
{
  "usuario": "evaluadora3",
  "password": "contraseñaSegura",
  "nombre": "Nombre Apellido",
  "rol": "EVALUADORA"
}
```

### Postulantes

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/postulantes` | Lista las postulaciones, comenzando por las más recientes. |
| `GET` | `/api/postulantes/{id}` | Obtiene una candidatura por ID. |
| `POST` | `/api/postulantes` | Registra una nueva candidatura. |
| `PUT` | `/api/postulantes/{id}` | Actualiza los datos de una candidatura. |
| `PATCH` | `/api/postulantes/{id}/estado` | Cambia el estado de una candidatura. |
| `DELETE` | `/api/postulantes/{id}` | Elimina una candidatura. |

Ejemplo de creación:

```json
{
  "nombre": "Persona Ejemplo",
  "correo": "persona@ejemplo.cl",
  "telefono": "+56 9 1234 5678",
  "area": "Seguridad de la información",
  "aniosExperiencia": 5,
  "resumenExperiencia": "Profesional con experiencia en gestión de riesgos y seguridad informática.",
  "linkedinUrl": "https://www.linkedin.com/in/persona-ejemplo",
  "cvNombre": "CV_persona.pdf",
  "registradaPor": "evaluadora1"
}
```

Ejemplo de cambio de estado:

```json
{
  "estado": "EN_EVALUACION"
}
```

## Respuestas de error

Los errores se devuelven en formato JSON a través de `GlobalExceptionHandler`:

```json
{
  "status": 404,
  "mensaje": "No existe el postulante 10",
  "errores": {}
}
```

Códigos utilizados por la API:

| Código | Situación |
|---|---|
| `400` | Datos de entrada inválidos. |
| `401` | Credenciales incorrectas. |
| `404` | Recurso no encontrado. |
| `409` | Usuario o correo ya registrado. |

## Ejecución de pruebas

```bash
cd wim100-backend
./mvnw test
```

La suite actual contiene una prueba de carga del contexto de Spring Boot.

## Consideraciones antes de producción

- La autenticación responde con datos de usuario, pero el proyecto no implementa tokens JWT ni sesiones protegidas.
- Los endpoints no cuentan actualmente con autorización por rol.
- Las credenciales de base de datos se encuentran en archivos de configuración y Compose; conviene usar variables de entorno o secretos.
- `spring.jpa.show-sql=true` es útil en desarrollo, pero debe deshabilitarse en producción.
- Para producción se recomienda migrar `ddl-auto=update` a herramientas de versionado de esquemas como Flyway o Liquibase.

## Licencia

Actualmente el repositorio no incluye una licencia. Antes de distribuir o reutilizar el proyecto, conviene definir una licencia mediante el archivo `LICENSE`.

## Autor

Desarrollado por [eldaridin](https://github.com/eldaridin).
