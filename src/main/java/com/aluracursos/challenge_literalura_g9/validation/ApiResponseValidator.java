package com.aluracursos.challenge_literalura_g9.validation;

public class ApiResponseValidator {
    public static <T extends IApiResultado> T validarRespuesta(T datos) {
        // verificar si la API explícitamente reportó error ("detail": "...")
        if (datos.error()) {
            String msg = switch (datos.mensajeError()) {
                case "Invalid page." -> "El número de página solicitado no existe.";
                case "Not found." -> "Recurso no encontrado (ID inválido).";
                default -> "Error de Gutendex: " + datos.mensajeError();
            };
            throw new GutendexException(msg);
        }

        return datos;
    }
}