package com.example.screenmatch.service;

import com.example.screenmatch.dto.EpisodioDTO;
import com.example.screenmatch.dto.SerieDTO;
import com.example.screenmatch.model.Categoria;
import com.example.screenmatch.model.Episodio;
import com.example.screenmatch.model.Serie;
import com.example.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

  @Autowired
  private SerieRepository serieRepository;

  public List<SerieDTO> obterTodasAsSeries() {
    return converterDadosSerie(serieRepository.findAll());
  }

  public List<SerieDTO> obterTop5Series() {
    return converterDadosSerie(serieRepository.findTop5ByOrderByAvaliacaoDesc());
  }

  public List<SerieDTO> obterLancamentos() {
    return converterDadosSerie(serieRepository.encontrarEpisodiosMaisRecentes());
  }

  public SerieDTO obterPorId(Long id) {
    Optional<Serie> serie = serieRepository.findById(id);

    if (serie.isPresent()) {
      Serie s = serie.get();
      return new SerieDTO(
          s.getId(),
          s.getTitulo(),
          s.getTotalTemporadas(),
          s.getAvaliacao(),
          s.getGenero(),
          s.getAtores(),
          s.getPoster(),
          s.getSinopse()
      );
    }
    return null;
  }

  public List<EpisodioDTO> obterTodasTemporadas(Long id) {
    Optional<Serie> serie = serieRepository.findById(id);

    if (serie.isPresent()) {
      Serie s = serie.get();
      return converterDadosEpisodio(s.getEpisodios());
    }

    return null;
  }

  public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
    return converterDadosEpisodio(serieRepository.obterEpisodioPorTemporada(id, numero));
  }

  public List<SerieDTO> obterSeriesPorCategoria(String nomeGenero) {
    Categoria categoria = Categoria.fromPortugues(nomeGenero);

    return converterDadosSerie(serieRepository.findByGenero(categoria));
  }

  public List<EpisodioDTO> obterTop5Episodios(Long id) {
    Optional<Serie> serie = serieRepository.findById(id);

    return serie.map(value -> converterDadosEpisodio(serieRepository.topEpisodiosPorSerie(value))).orElse(null);
  }

  private List<SerieDTO> converterDadosSerie(List<Serie> series) {
    return series.stream()
        .map(serie -> new SerieDTO(
             serie.getId(),
             serie.getTitulo(),
             serie.getTotalTemporadas(),
             serie.getAvaliacao(),
             serie.getGenero(),
             serie.getAtores(),
             serie.getPoster(),
             serie.getSinopse()
            )
        ).collect(Collectors.toList());
  }

  private List<EpisodioDTO> converterDadosEpisodio(List<Episodio> episodios) {
    return episodios.stream()
        .map(episodio -> new EpisodioDTO(
             episodio.getTemporada(),
             episodio.getNumeroEpisodio(),
             episodio.getTitulo()
        )).collect(Collectors.toList());
  }
}