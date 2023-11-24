package br.com.codercast.screenmatch.principal;

import br.com.codercast.screenmatch.model.DadosEpisodio;
import br.com.codercast.screenmatch.model.DadosSerie;
import br.com.codercast.screenmatch.model.DadosTemporada;
import br.com.codercast.screenmatch.model.Episodio;
import br.com.codercast.screenmatch.service.ConsumirApi;
import br.com.codercast.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

//        System.out.println("\nTop 3 melhores: ");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(3)
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporadas(), d)))
                .collect(Collectors.toList());
        episodios.forEach(System.out::println);

        System.out.println("Digite um trecho do titulo do episodio");
        var trechoTitulo = lerTexto.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else{
            System.out.println("Episodio não encontrado");
        }

//        System.out.println("A partir de qual ano você quer ver os episódios? ");
//        var ano = lerTexto.nextInt();
//        lerTexto.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatadorDataBR = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data lançamento: " + e.getDataLancamento().format(formatadorDataBR)
//                ));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        // Usando a classe DoubleSummaryStatistics e o metodo summarizingDouble
        DoubleSummaryStatistics estatisticas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println(estatisticas);
        System.out.println("Média: " + estatisticas.getAverage());
        System.out.println("Pior episódio: " + estatisticas.getMin());
        System.out.println("Melhor episódio: " + estatisticas.getMax());
        System.out.println("Quantidade de episódios: " + estatisticas.getCount());
    }
}