# challenge-literalura-g9
LiterAlura - Challenge Java - G9

La conexion a la BBDD se configura en archivo *application.properties*. 
```
server.port=8080
spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASS}

```

En su entorno de ejecución o en la IDE se deben confiugar las siguentes varibales:
- DB_HOST
- DB_PORT
- DB_NAME
- DB_USER
- DB_PASS

Es necesario crear una BBDD antes de ejecutar la aplicacion.
```

-- DROP DATABASE IF EXISTS literalura;

CREATE DATABASE literalura
    WITH
    OWNER = postgres
    ENCODING = 'UTF8';

COMMENT ON DATABASE literalura
    IS 'Practicando Spring Boot: Challenge Literalura';
```
