package br.com.codercast.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ItfcConverteDados {
    <T> T ObterDados(String json, Class<T> classe) throws JsonProcessingException;
}
