package com.aluracursos.challenge_literalura_g9.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookDTO(
            Long id,
            @JsonAlias("title") String titulo,
            @JsonAlias("subjects") List<String> subjects,
            @JsonAlias("authors") List<PersonDTO> autores,
            @JsonAlias("summaries") List<String> sumarios,
            @JsonAlias("translators") List<PersonDTO> traductores,
            @JsonAlias("languages") List<String> idiomas,
            @JsonAlias("copyright") Boolean copyright,
            @JsonAlias("media_type") String mediaType,
            @JsonAlias("formats") Map<String, String> formatos,
            @JsonAlias("download_count") Long cantidadDescargas
    ) {
}