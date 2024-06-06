package com.example.screenmatch.principal;

import com.example.screenmatch.model.DadosSerie;
import com.example.screenmatch.model.DadosTemporada;
import com.example.screenmatch.service.ConsumirApi;
import com.example.screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

  private final Scanner scanner = new Scanner(System.in);
  private final ConsumirApi consumirApi = new ConsumirApi();
  private final ConverterDados converterDados = new ConverterDados();
  private final String ENDERECO = "https://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=6585022c";

  public void exibirMenu() {
    var menu = """
                1 - Buscar séries
                2 - Buscar episódios
                3 - 
                0 - Sair
                """;

    System.out.println(menu);
    var opcao = scanner.nextInt();
    scanner.nextLine();

    switch (opcao) {
      case 1:
        buscarSerieWeb();
        break;
      case 2:
        buscarEpisodioPorSerie();
        break;
      case 0:
        System.out.println("Saindo...");
        break;
      default:
        System.out.println("Opção inválida");
    }
  }

  private void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
    System.out.println(dados);
  }

  private DadosSerie getDadosSerie() {
    System.out.println("Digite o nome da série para busca");
    var nomeSerie = scanner.nextLine();
    var json = consumirApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    DadosSerie dadosSerie = converterDados.obterDados(json, DadosSerie.class);
    return dadosSerie;
  }

  private void buscarEpisodioPorSerie(){
    DadosSerie dadosSerie = getDadosSerie();
    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
      var json = consumirApi.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
      DadosTemporada dadosTemporada = converterDados.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }
    temporadas.forEach(System.out::println);
  }
}