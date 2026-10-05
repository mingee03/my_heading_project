package org.example.my_heading_project_01.service;

import org.example.my_heading_project_01.domain.Movie;
import org.example.my_heading_project_01.dto.*;
import org.example.my_heading_project_01.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }
    public MovieResponse create(MovieRequest r) {
        return toResponse(repository.save(new Movie(null, r.title(), r.director(), r.genre(), r.view(), r.year())));
    }
    
    // 장르 필터링 추가 (B 기능)
    public List<MovieResponse> findAll(String genre) {
        List<Movie> movies = repository.findAll();
        if (genre != null && !genre.isEmpty()) {
            movies = movies.stream()
                    .filter(m -> m.getGenre().equalsIgnoreCase(genre))
                    .collect(Collectors.toList());
        }
        return movies.stream().map(this::toResponse).toList();
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
        m.setView(r.view());
        return toResponse(repository.update(m)); // 버그 수정: save -> update
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
