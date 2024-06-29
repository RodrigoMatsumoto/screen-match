package com.example.screenmatch.dto;

import com.example.screenmatch.model.Categoria;

public record SerieDTO(
    Long id,
    String titulo,
    Integer totalTemporadas,
    Double avaliacao,
    Categoria genero,
    String atores,
    String poster,
    String sinopse
    ) { }