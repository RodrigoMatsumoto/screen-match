package com.example.screenmatch.controller;

import com.example.screenmatch.dto.SerieDTO;
import com.example.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieController {

  @Autowired
  private SerieService serieService;

  @GetMapping
  public List<SerieDTO> obterSeries() {
    return serieService.obterTodasAsSeries();
  }

  @GetMapping("/top5")
  public List<SerieDTO> obterTop5Series() {
    return serieService.obterTop5Series();
  }
}
