package com.aluracursos.challenge_literalura_g9.repository;

import com.aluracursos.challenge_literalura_g9.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LiterAluraRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTituloAndAutorId(String titulo, Long autorId);

    long countByIdioma(String idioma);

    //Optional<Book> findByTituloContainsIgnoreCase(String nombreLibro);

    /*@Query(value = "select e from Serie s JOIN s.episodios e " +
            "where lower(e.titulo) like lower(concat('%', :nombreEpisodio, '%'))")
    //List<Episodio> episodioPorNombre(String nombreEpisodio);*/
}