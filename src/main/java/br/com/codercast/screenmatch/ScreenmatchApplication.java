package br.com.codercast.screenmatch;

import br.com.codercast.screenmatch.model.DadosEpisodio;
import br.com.codercast.screenmatch.model.DadosSerie;
import br.com.codercast.screenmatch.model.DadosTemporada;
import br.com.codercast.screenmatch.service.ConsumirApi;
import br.com.codercast.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var consumoApi = new ConsumirApi();
        var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=6585022c");
        System.out.println(json);

//		json =  consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
        ConverteDados conversor = new ConverteDados();
        DadosSerie dados = conversor.ObterDados(json, DadosSerie.class);
        System.out.println(dados);

        json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=6585022c");
        DadosEpisodio episodio = conversor.ObterDados(json, DadosEpisodio.class);
        System.out.println(episodio);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.numeroTemporadas(); i++) {
            json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=6585022c");
            DadosTemporada dadosTemporada = conversor.ObterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
    }
}
