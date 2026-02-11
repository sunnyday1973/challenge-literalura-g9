package com.aluracursos.challenge_literalura_g9.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumeApi {

    public String consumirAPI(String url) {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        //System.out.println("url: " + url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Validar que no sea un error 404 o 500
        if (response.statusCode() != 200) {
            System.out.println("Error API: " + response.statusCode());
            return ""; // O lanzar excepción
        }

        //System.out.println("json: " + response.body());
        return response.body();
    }
}
