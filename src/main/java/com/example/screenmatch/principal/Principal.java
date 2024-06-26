package com.example.screenmatch.principal;

import com.example.screenmatch.model.DadosSerie;
import com.example.screenmatch.model.DadosTemporada;
import com.example.screenmatch.model.Episodio;
import com.example.screenmatch.model.Serie;
import com.example.screenmatch.repository.SerieRepository;
import com.example.screenmatch.service.ConsumirApi;
import com.example.screenmatch.service.ConverterDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

  private final Scanner scanner = new Scanner(System.in);
  private final ConsumirApi consumirApi = new ConsumirApi();
  private final ConverterDados converterDados = new ConverterDados();
  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=6585022c";
  private final SerieRepository serieRepository;
  private List<Serie> series = new ArrayList<>();

  public Principal(SerieRepository serieRepository) {
    this.serieRepository = serieRepository;
  }

  public void exibirMenu() {

    var opcao = -1;

    while (opcao != 0) {
      var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - Listar séries buscadas
                4 - Buscar série por título
                5 - Buscar séries por ator
                6 - Top 5 séries
                
                0 - Sair
                """;

      System.out.println(menu);
      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          buscarSerieWeb();
          break;
        case 2:
          buscarEpisodioPorSerie();
          break;
        case 3:
          listarSeriesBuscadas();
          break;
        case 4:
          buscarSeriePorTitulo();
          break;
        case 5:
          buscarSeriesPorAtor();
          break;
        case 6:
          buscarTop5Series();
          break;
        case 0:
          System.out.println("Saindo...");
          break;
        default:
          System.out.println("Opção inválida");
      }
    }
  }


  private DadosSerie getDadosSerie() {
    System.out.println("Digite o nome da série para busca");
    var nomeSerie = scanner.nextLine();
    var json = consumirApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

    return converterDados.obterDados(json, DadosSerie.class);
  }

  private void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
    Serie serie = new Serie(dados);

    serieRepository.save(serie);
    System.out.println(dados);
  }

  private void buscarEpisodioPorSerie(){
    listarSeriesBuscadas();
    System.out.println("Escolha uma série pelo nome:");
    var nomeSerie = scanner.nextLine();

    Optional<Serie> serie = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

    if (serie.isPresent()) {
      var serieEncontrada = serie.get();
      List<DadosTemporada> temporadas = new ArrayList<>();

      for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
        var json = consumirApi.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(
            " ", "+") + "&season=" + i + API_KEY);

        DadosTemporada dadosTemporada = converterDados.obterDados(json, DadosTemporada.class);
        temporadas.add(dadosTemporada);
      }
      temporadas.forEach(System.out::println);

      List<Episodio> episodios = temporadas.stream().flatMap(d -> d.episodios().stream()
              .map(e -> new Episodio(d.numero(), e) {}))
          .collect(Collectors.toList());

      serieEncontrada.setEpisodios(episodios);
      serieRepository.save(serieEncontrada);
    } else {
      System.out.println("Série não encontrada");
    }
  }

  private void listarSeriesBuscadas() {
    series = serieRepository.findAll();

    series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
  }

  private void buscarSeriePorTitulo() {
    System.out.println("Escolha uma série pelo nome:");
    var nomeSerie = scanner.nextLine();

    Optional<Serie> serieBuscada = serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

    if (serieBuscada.isPresent()) {
      System.out.println("Dados da série:" + serieBuscada.get());
    } else {
      System.out.println("Série não encontrada");
    }
  }

  private void buscarSeriesPorAtor() {
    System.out.println("Qual o nome do ator?");
    var nomeAtor = scanner.nextLine();

    System.out.println("Avaliações a partir de que valor? ");
    var avaliacao = scanner.nextDouble();

    List<Serie> seriesEncontradas =
        serieRepository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
    System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
    seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
  }

  private void buscarTop5Series() {
    List<Serie> seriesTop = serieRepository.findTop5ByOrderByAvaliacaoDesc();
    seriesTop.forEach(s -> System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
  }
}
