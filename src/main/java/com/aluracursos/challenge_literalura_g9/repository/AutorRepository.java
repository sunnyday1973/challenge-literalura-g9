package com.aluracursos.challenge_literalura_g9.repository;

import com.aluracursos.challenge_literalura_g9.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String name);
    List<Person> findAllByOrderByNameAsc();

    List<Person> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(int anioNacimiento, int anioFallecimiento);
}
