package com.aluracursos.challenge_literalura_g9.dto;

import com.aluracursos.challenge_literalura_g9.validation.IApiResultado;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GutendexDTO(
                @JsonAlias("count") Integer cantidad,
                @JsonAlias("next") String urlPaginaAnterior,
                @JsonAlias("previous") String urlPaginaSiguiente,
                @JsonAlias("results") List<BookDTO> resultado,
                // campo especial para capturar errores de Gutendex
                @JsonAlias("detail") String detalleError
) implements IApiResultado {

    @Override
    public boolean error() {
        // Si "detail" tiene texto, es que hubo error
        return detalleError != null && !detalleError.isBlank();
    }

    @Override
    public String mensajeError() {
        return detalleError;
    }

    // Método helper para acceder a los resultados seguros
    public List<BookDTO> resultado() {
        return resultado != null ? resultado : List.of();
    }
}