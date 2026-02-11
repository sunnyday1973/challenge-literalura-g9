package com.aluracursos.challenge_literalura_g9.model;

import com.aluracursos.challenge_literalura_g9.dto.PersonDTO;
import com.aluracursos.challenge_literalura_g9.service.ListStringConverter;
import com.aluracursos.challenge_literalura_g9.service.MapStringConverter;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "books")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(unique = true)
    @Column(unique = true, length = 500) // Aumenta el título por si acaso
    private String titulo;

    @Convert(converter = ListStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> subjects;

    //@ElementCollection
    //private List<Person> autores;
    @ManyToOne // muchos libros pueden ser de una persona
    @JoinColumn(name = "autor_id") // eso crea la columna FK en la tabla books
    private Person autor;

    @Convert(converter = ListStringConverter.class)
     @Column(columnDefinition = "TEXT")
    private List<String> sumarios;

    @Convert(converter = ListStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private List<String> traductoresNombres;
    //private List<Person> traductores;

    //@Enumerated(EnumType.STRING)
    private String idioma;

    private Boolean copyright;

    private String mediaType;

    @Convert(converter = MapStringConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, String> formatos;

    private Long cantidadDescargas;

    public Book() {
    }
}