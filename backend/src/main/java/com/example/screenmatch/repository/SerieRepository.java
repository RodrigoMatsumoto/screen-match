package com.example.screenmatch.repository;

import com.example.screenmatch.model.Categoria;
import com.example.screenmatch.model.Episodio;
import com.example.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

  Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

  List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

  List<Serie> findTop5ByOrderByAvaliacaoDesc();

  List<Serie> findByGenero(Categoria categoria);

  @Query("""
    SELECT s
    FROM Serie s
    WHERE s.totalTemporadas <= :totalTemporadas
    AND s.avaliacao >= :avaliacao
  """)
  List<Serie> seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);

  @Query("""
    SELECT e
    FROM Serie s
    JOIN s.episodios e
    WHERE e.titulo
    ILIKE %:trechoEpisodio%
  """)
  List<Episodio> espisodiosPorTrecho(String trechoEpisodio);

  @Query("""
    SELECT e
    FROM Serie s
    JOIN s.episodios e
    WHERE s = :serie
    ORDER BY e.avaliacao
    DESC LIMIT 5
  """)
  List<Episodio> topEpisodiosPorSerie(Serie serie);

  @Query("""
    SELECT e
    FROM Serie s
    JOIN s.episodios e
    WHERE s = :serie
    AND YEAR(e.dataLancamento) >= :anoLancamento
  """)
  List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

  @Query("""
    SELECT s
    FROM Serie s
    JOIN s.episodios e
    GROUP BY s
    ORDER BY MAX(e.dataLancamento) DESC LIMIT 5
  """)
  List<Serie> encontrarEpisodiosMaisRecentes();
}