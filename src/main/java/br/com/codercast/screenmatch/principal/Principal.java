package br.com.codercast.screenmatch.principal;

import br.com.codercast.screenmatch.model.DadosEpisodio;
import br.com.codercast.screenmatch.model.DadosSerie;
import br.com.codercast.screenmatch.model.DadosTemporada;
import br.com.codercast.screenmatch.model.Episodio;
import br.com.codercast.screenmatch.service.ConsumirApi;
import br.com.codercast.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String APY_KEY = "&apikey=6585022c";
    //https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c
    private ConsumirApi consumoAPI = new ConsumirApi();
    private ConverteDados conversor = new ConverteDados();
    private Scanner lerTexto = new Scanner(System.in);

    public void exibeMenu() {
        System.out.println("Digite o nome da Serie: ");

        var nomeSerie = lerTexto.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + APY_KEY);

        DadosSerie dados = conversor.ObterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.numeroTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&Season=" + i + APY_KEY);
            DadosTemporada dadosTemporada = conversor.ObterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());
        dadosEpisodios.add(new DadosEpisodio("teste", 9, "2023-05-18", "9.0"));
        dadosEpisodios.forEach(System.out::println);

        System.out.println("\nTop 3 melhores: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(3)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporadas(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);
    }
}
