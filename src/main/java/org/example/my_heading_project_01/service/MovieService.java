package org.example.my_heading_project_01.service;

import org.example.my_heading_project_01.domain.Movie;
import org.example.my_heading_project_01.dto.*;
import org.example.my_heading_project_01.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }
    public MovieResponse create(MovieRequest r) {
        return toResponse(repository.save(new Movie(null, r.title(), r.director(), r.genre(), r.view(), r.year())));
    }
    public List<MovieResponse> findAll(){
        return  repository.findAll().stream().map(this::toResponse).toList();
    }
    public MovieResponse findById(Long id) {
        return toResponse(findMovie(id));
    }
    public MovieResponse update(Long id, MovieRequest r) {
        Movie m = findMovie(id);
        m.setTitle(r.title());
        m.setDirector(r.director());
        m.setGenre(r.genre());
        m.setYear(r.year());
        return toResponse(repository.save(m));
    }
    public void delete(Long id) {
        findMovie(id);
        repository.deleteById(id);
    }
    private Movie findMovie(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found: " + id));
    }

    private MovieResponse toResponse(Movie m) {
        return new MovieResponse(m.getId(), m.getTitle(), m.getDirector(), m.getGenre(), m.getYear(), m.getView());
    }
}
