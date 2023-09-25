package br.com.codercast.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String Titulo, @JsonAlias("Episode") Integer NumEpisodio,
                            @JsonAlias("Released") String DataLancamento, @JsonAlias("imdbRating") String Avaliacao) {
}
