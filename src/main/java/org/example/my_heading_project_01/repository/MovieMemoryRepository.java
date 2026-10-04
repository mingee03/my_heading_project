package org.example.my_heading_project_01.repository;

import org.example.my_heading_project_01.domain.Movie;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository // 스프링 빈으로 등록
public class MovieMemoryRepository implements MovieRepository {
    private final Map<Long, Movie> store = new LinkedHashMap<>();
    private long sequence = 0L;

    @Override
    public Movie save(Movie movie) {
        if (movie.getId() == null)movie.setId(++sequence);
        store.put(movie.getId(), movie);
        return movie;
    }

    @Override
    public List<Movie> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Movie> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Movie update(Movie movie) {
        store.put(movie.getId(), movie);
        return movie;
    }

    @Override
    public void deleteById(Long id) {
        store.remove(id);
    }
}
