package org.example.my_heading_project_01.domain;

public class Movie {
    private Long id;
    private String title;
    private String director;
    private String genre;

    public Movie() {
        this.id = null;
        this.title = null;
        this.director = null;
        this.genre = null;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
