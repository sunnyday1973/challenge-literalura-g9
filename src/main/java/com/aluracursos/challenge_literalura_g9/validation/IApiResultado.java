package com.aluracursos.challenge_literalura_g9.validation;

public interface IApiResultado {
    boolean error(); // Gutendex no dice "success", así que mejor preguntamos si falló
    String mensajeError(); // Para devolver el "detail"
}
