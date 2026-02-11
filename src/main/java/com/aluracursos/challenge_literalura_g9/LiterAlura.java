package com.aluracursos.challenge_literalura_g9;

import com.aluracursos.challenge_literalura_g9.dto.BookDTO;
import com.aluracursos.challenge_literalura_g9.dto.GutendexDTO;
import com.aluracursos.challenge_literalura_g9.dto.PersonDTO;
import com.aluracursos.challenge_literalura_g9.model.Book;
import com.aluracursos.challenge_literalura_g9.model.Person;
import com.aluracursos.challenge_literalura_g9.repository.AutorRepository;
import com.aluracursos.challenge_literalura_g9.repository.LiterAluraRepository;
import com.aluracursos.challenge_literalura_g9.service.ConsumeApi;
import com.aluracursos.challenge_literalura_g9.service.DataConverter;
import com.aluracursos.challenge_literalura_g9.validation.ApiResponseValidator;
import com.aluracursos.challenge_literalura_g9.validation.GutendexException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;
import java.util.stream.Collectors;

public class LiterAlura {
    private Scanner teclado = new Scanner(System.in);
    private ConsumeApi consumoAPI = new ConsumeApi();
    private ObjectMapper objectMapper = new ObjectMapper();
    private DataConverter conversor = new DataConverter();
    private final String URL_BASE = "https://gutendex.com/books?";
    private final LiterAluraRepository repositorioLibros;
    private final AutorRepository repositorioAutores;

    public LiterAlura(LiterAluraRepository repositorioLibros, AutorRepository repositorioAutores) {
        this.repositorioLibros = repositorioLibros;
        this.repositorioAutores = repositorioAutores;
    }

    public void mostrarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    
                    Bienvenido a LiterAlura, tu buscador de libros favorito.
                    
                    Elige una opción para comenzar:
                    1 - Listar libros
                    2 - Buscar libro por título
                    3 - Mostrar autores de libros buscados
                    4 - Mostrar autores de libros buscados vivos en determinado año
                    5 - Mostrar cantidad de libros buscados en un determinado idioma
                    ---------------------------
                    0 - Salir
                    
                    Su opción es: 
                    """;
            System.out.println(menu);

            try {
                opcion = teclado.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Opción inválida");
                continue;
            } finally {
                teclado.nextLine();
            }

            try {
                switch (opcion) {
                    case 1 -> listarLibros();
                    case 2 -> buscarLibro();
                    case 3 -> mostrarAutoresDeLibrosBuscados();
                    case 4 -> mostrarAutoresDeLibrosBuscadosVivos();
                    case 5 -> mostrarCantidadDeLibrosBuscadosEnIdioma();
                    case 0 -> System.out.println("Cerrando la aplicación...");
                    default -> System.out.println("Opción inválidaa");
                }
            } catch (GutendexException e) {
                System.out.println("Error al consultar la API: " + e.getMessage());
            } finally {

            }

        }
    }

    private void listarLibros() {
        System.out.println("Ingrese numero de página que desea listar (1, 30, 100):");
        var input = teclado.nextLine();
        var url = URL_BASE;

        if (!input.isBlank()) {
            try {
                int pagina = Integer.parseInt(input);
                if (pagina > 1) {
                    url += "page=" + pagina;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida, mostrando página 1 por defecto.");
            }
        }

        var json = consumoAPI.consumirAPI(url);

        mostrarLibros(json, "No se encontraron libros.");
    }

    private void buscarLibro() {
        System.out.println("Escribe el nombre del libro que deseas buscar:");
        var nombreLibro = teclado.nextLine();
        var url = URL_BASE + "search=" + nombreLibro.replace(" ", "+");
        var json = consumoAPI.consumirAPI(url);

        mostrarLibros(json, "No se encontraron libros con ese título.");
    }

    public void mostrarLibros(String json, String mensajeNoEncontrado) {
        var respuesta = conversor.convertir(json, GutendexDTO.class);

        try {
            // validar si Gutendex mandó error
            var resultado = ApiResponseValidator.validarRespuesta(respuesta);

            Optional<List<BookDTO>> librosOpt = Optional.ofNullable(resultado.resultado())
                    .filter(lista -> !lista.isEmpty());

            librosOpt.ifPresentOrElse(libros -> {
                libros.forEach(libroDTO -> {
                    // convertir DTO a entidad Book JPA SIN asignar autor todavía
                    Book libroEntity = dtoToEntity(libroDTO);

                    // manejar los duplicados del Autor
                    if (libroDTO.autores() != null && !libroDTO.autores().isEmpty()) {
                        PersonDTO autorDTO = libroDTO.autores().get(0); // tomar el primero

                        // buscar si el autor ya existe en la BD
                        Optional<Person> autorExistente = repositorioAutores.findByName(autorDTO.name());
                        Person autorFinal;

                        // El autor existe
                        if (autorExistente.isPresent()) {
                            autorFinal = autorExistente.get();

                            // si el autor ya existe (tiene ID), verificamos
                            // si el LIBRO ya existe para no duplicarlo
                            Optional<Book> libroExistente = repositorioLibros.findByTituloAndAutorId(
                                    libroEntity.getTitulo(),
                                    autorFinal.getId()
                            );

                            if (libroExistente.isPresent()) {
                                System.out.println("El libro '" + libroEntity.getTitulo() + "' ya existe en la BD.");
                                return; // no guardar nada
                            }
                        } else {
                            // crear el autor y guardarlo en la BD
                            Person nuevoAutor = new Person();
                            nuevoAutor.setName(autorDTO.name());
                            nuevoAutor.setBirthYear(autorDTO.birth_year());
                            nuevoAutor.setDeathYear(autorDTO.death_year());
                            autorFinal = repositorioAutores.save(nuevoAutor);
                        }

                        libroEntity.setAutor(autorFinal);
                    }

                    // Guardar el libro
                    System.out.println("\n**************************************************");
                    try {
                        repositorioLibros.save(libroEntity);
                        System.out.println("Libro guardado exitosamente");
                    } catch (Exception e) {
                        System.out.println("Error al guardar libro: " + e.getMessage());
                    }

                    // Mostrar información en consola
                    System.out.println("Libro: " + libroDTO.titulo());
                    System.out.println("**************************************************");
                    if (!libroDTO.sumarios().isEmpty()) {
                        System.out.println("Sumario: " + libroDTO.sumarios().get(0));
                    }

                    String idiomas = String.join(", ", libroDTO.idiomas());
                    System.out.println("Idioma(s): " + idiomas);

                    String autores = libroDTO.autores().stream()
                            .map(PersonDTO::name)
                            .collect(Collectors.joining(", "));
                    System.out.println("Autor(es): " + autores);

                    String formatos = libroDTO.formatos().entrySet().stream()
                            .map(entry -> entry.getKey() + "=" + entry.getValue())
                            .collect(Collectors.joining(", "));
                    System.out.println("Formatos: " + formatos);

                    System.out.println("Número de Descargas: " + libroDTO.cantidadDescargas());
                    System.out.println("**************************************************\n");
                });
            }, () -> System.out.println(mensajeNoEncontrado));
        } catch (RuntimeException e) {
            System.out.println("Ups! " + e.getMessage());
        }
    }

    private Book dtoToEntity(BookDTO dto) {
        var entity = new Book();
        entity.setTitulo(dto.titulo());
        entity.setSubjects(dto.subjects());
        entity.setSumarios(dto.sumarios());

        // Traductores
        if (dto.traductores() != null) {
            List<String> nombresTrad = dto.traductores().stream()
                    .map(PersonDTO::name)
                    .toList();
            entity.setTraductoresNombres(nombresTrad);
        }

        // considera solo el primer idioma de la lista (langugages) relacionado con el libro
        if (dto.idiomas() != null && !dto.idiomas().isEmpty()) {
            // tomar el primer código (ej: "en", "es")
            String codigoIdioma = dto.idiomas().get(0);
            try {
                // buscar el Enum correcto
                entity.setIdioma(codigoIdioma);
            } catch (IllegalArgumentException e) {
                // si llega un idioma raro que no esta en Enum
                System.out.println("Idioma no reconocido: " + codigoIdioma);
                // asignar null por defecto
                entity.setIdioma(null);
            }
        }

        entity.setCopyright(dto.copyright());
        entity.setMediaType(dto.mediaType());
        entity.setFormatos(dto.formatos());
        entity.setCantidadDescargas(dto.cantidadDescargas());

        return entity;
    }

    private void mostrarAutoresDeLibrosBuscados() {
        // obtener todos los autores de la Base de Datos de libros buscados
        List<Person> autores = repositorioAutores.findAllByOrderByNameAsc();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        System.out.println("\n**************************************************");
        System.out.println("          AUTORES REGISTRADOS");
        System.out.println("**************************************************");
        autores.forEach(autor -> {
            System.out.println((autor.getName() != null ? autor.getName() : "Desconocido"));

        });
    }

    private void mostrarAutoresDeLibrosBuscadosVivos() {
        System.out.println("Escribe el año para filtrar autores vivos en ese año:");
        var input = teclado.nextLine();

        if (!input.isBlank()) {
            try {
                int anio = Integer.parseInt(input);

                // obtener todos los autores de la Base de Datos de libros buscados
                List<Person> autoresVivos = repositorioAutores.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(anio, anio);

                if (autoresVivos.isEmpty()) {
                    System.out.println("No hay autores vivos en el año " + anio);
                    return;
                }

                System.out.println("\n**************************************************");
                System.out.println("          AUTORES VIVOS EN " + anio);
                System.out.println("**************************************************");
                autoresVivos.forEach(autor -> {
                    System.out.println((autor.getName() != null ? autor.getName() : "Desconocido"));
                });
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida, mostrando página 1 por defecto.");
            }
        }
    }

    private void mostrarCantidadDeLibrosBuscadosEnIdioma() {
        System.out.println("""
        Escribe el código del idioma para filtrar libros por idioma.
        (Ej.: en, es, fr, de, es, it, pt, ru, zh, ja, ar):
        """);
        var idioma = teclado.nextLine();

        if (!idioma.isBlank()) {
            try {
                long cantidad = repositorioLibros.countByIdioma(idioma);
                System.out.println("Cantidad de libros encontrados en idioma '" + idioma + "': " + cantidad);
            } catch (IllegalArgumentException e) {
                System.out.println("Código de idioma no reconocido: " + idioma);
            }
        }
    }
}
