![Alura + One](./img/alura.png)

# Challenge ONE
## LiterAlura - Challenge Java - G9

Se usa API [Gutendex](https://gutendex.com/) para buscar libros y guardarlos en la BBDD local, para consultas posteriores. Project Gutenberg (biblioteca en línea y gratuita) tiene un catálogo de información de más de 70.000 libros.

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

## Crear variable de entorno en Windows (interfaz gráfica)

1. Abrir el **Panel de control**.  
2. Ir a **Sistema y seguridad**.  
3. Seleccionar **Sistema**.  
4. Hacer clic en **Configuración avanzada del sistema**.  
5. Presionar **Variables de entorno…**.  
6. En la sección correspondiente (usuario o sistema), hacer clic en **Nueva…** e ingresar:
   - **Nombre de la variable**: `DB_HOST`  
   - **Valor de la variable**: `localhost`

---

## Crear variable de entorno en macOS (terminal)

En una terminal:

`echo 'export DB_HOST=localhost' >> ~/.zshrc`


`source ~/.zshrc`

---

## Crear variable de entorno en GitHub (Codespaces Secret)

1. Ir al repositorio en GitHub.  
2. Abrir **Settings**.  
3. Ir a **Secrets and variables** → **Codespaces**.  
4. Hacer clic en **New repository secret**.  
5. Definir:
   - **Name**: `DB_HOST`  
   - **Value**: `localhost`

---

## Verificar que la variable existe

- En **Windows** (PowerShell o CMD):

`echo %DB_HOST%`

- En **macOS / Linux** (terminal):

`echo $DB_HOST`

---

## Compilar y ejecutar (Maven)

`./mvnw spring-boot:run`

O con Maven global

`mvn spring-boot:run`

---

## Funcionalidades

Al ejecutar la aplicación se mostrará el siguiente menú en consola:


Bienvenido a LiterAlura, tu buscador de libros favorito.

Elige una opción para comenzar:

1 - Listar libros

2 - Buscar libro por título

3 - Mostrar autores de libros buscados

4 - Mostrar autores de libros buscados vivos en determinado año

5 - Mostrar cantidad de libros buscados en un determinado idioma

\---------------------------

0 - Salir
