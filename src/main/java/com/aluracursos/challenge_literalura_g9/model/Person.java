package com.aluracursos.challenge_literalura_g9.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import com.aluracursos.challenge_literalura_g9.model.Book;

import java.util.List;

@Entity
@Table(name = "autors")
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int birthYear;
    private int deathYear;
    private String name;
    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> libros;

    public Person() {}
}
