package com.aluracursos.challenge_literalura_g9;

import com.aluracursos.challenge_literalura_g9.repository.AutorRepository;
import com.aluracursos.challenge_literalura_g9.repository.LiterAluraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChallengeLiteraluraG9Application implements CommandLineRunner {
    @Autowired
    private LiterAluraRepository repositorioLibros;
    @Autowired
    private AutorRepository repositorioAutores;


	public static void main(String[] args) {
		SpringApplication.run(ChallengeLiteraluraG9Application.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        LiterAlura bookSearch = new LiterAlura(repositorioLibros, repositorioAutores);
        bookSearch.mostrarMenu();
    }
}
