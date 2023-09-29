package br.com.codercast.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTemporada(@JsonAlias("Season") Integer temporadas,
                             @JsonAlias("Episodes") List<DadosEpisodio> episodios) {
}
