package org.example.my_heading_project_01.domain;

public class Movie {

    private Long id;
    private String title;
    private String director;
    private String genre;
    private String year;
    private int view;

    public Movie(Long id, String title, String director, String genre, int view, String year) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.genre = genre;
        this.view = view;
        this.year = year;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
    public int getView() { return view; }
    public void setView(int view) { this.view = view; }
}
