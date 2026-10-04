package org.example.my_heading_project_01.repository;

import org.example.my_heading_project_01.domain.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieRepository {
    Movie save(Movie movie);
    List<Movie> findAll();
    Optional<Movie> findById(int id);
    Movie update(Movie movie);
    void deleteById(int id);
}
