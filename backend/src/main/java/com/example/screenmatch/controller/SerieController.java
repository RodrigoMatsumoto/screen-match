package com.example.screenmatch.controller;

import com.example.screenmatch.dto.SerieDTO;
import com.example.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

  @Autowired
  private SerieRepository serieRepository;

  @GetMapping("/series")
  public List<SerieDTO> obterSeries() {
    return serieRepository.findAll()
        .stream()
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
