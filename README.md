# TODO List API - Test Práctico de Conocimientos

## Descripción

Este proyecto es una API Restful que permite la gestión de una lista de tareas (TODO List) asociadas a usuarios registrados. El sistema ofrece las siguientes funcionalidades:

- Registro de usuarios
- Login de usuarios
- Obtención de datos del usuario a partir de la sesión
- Gestión de tareas (Crear, Obtener, Marcar como resueltas, Eliminar) para cada usuario

### Tecnologías utilizadas:

- **Lenguaje de programación**: Java
- **Framework**: Spring-boot
- **Seguridad**: Autenticación mediante JWT
- **Pruebas**: Test unitarios con Junit 5

## Requisitos

Para ejecutar el proyecto necesitarás tener instalado:

- [Java](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)
- [Git](https://git-scm.com/)
- [PostgresSQL](https://www.postgresql.org/)

## Instrucciones de instalación

1. Clona este repositorio en tu máquina local:

   ```bash
   git clone <URL_del_repositorio>
   
2. Accede al directorio del proyecto:
    ```bash
   cd TODO-list API
   
3. Instala las dependencias de Maven:
    ```bash
   mvn clean install
   
4. Agregar las variables de entorno:

   - POS_HOSTNAME: Hostname de la DB
   - POS_PORT: Puerto de la DB
   - POS_DB_NAME: Name de la DB
   - POS_USERNAME: Username de la DB
   - POS_PASSWORD: Password de la DB
   - PORT: Puerto donde se alojará la API

5. Luego entrar a Swagger:

   [Swagger](http://localhost:8080/api/swagger-ui/index.html#/)


6. Para correr los test:

    ```bash
   mvn run test


## Author ✒️

* **Eduard José Pichardo Rochet** - [Eduard177](https://github.com/Eduard177)