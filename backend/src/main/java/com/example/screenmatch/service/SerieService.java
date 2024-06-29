package com.example.screenmatch.service;

import com.example.screenmatch.dto.SerieDTO;
import com.example.screenmatch.model.Serie;
import com.example.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerieService {

  @Autowired
  private SerieRepository serieRepository;

  public List<SerieDTO> obterTodasAsSeries() {
    return converterDados(serieRepository.findAll());
  }

  public List<SerieDTO> obterTop5Series() {
    return converterDados(serieRepository.findTop5ByOrderByAvaliacaoDesc());
  }

  public List<SerieDTO> obterLancamentos() {
    return converterDados(serieRepository.findTop5ByOrderByEpisodiosDataLancamentoDesc());
  }

  private List<SerieDTO> converterDados(List<Serie> series) {
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
}