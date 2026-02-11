package com.aluracursos.challenge_literalura_g9.service;

public interface IDataConverter {
    <T> T convertir(String json, Class<T> clase);
}
